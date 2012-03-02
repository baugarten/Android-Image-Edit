package com.owleyes.moustache;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CustomImageView extends ImageView implements AnimationListener {

    private Point coords = new Point();

    private boolean _selected;

    private boolean _delayedSelected;

    private boolean isRotating;

    private int width;
    private int height;

    private int currentRotateEnd = 0;

    private int futureRotateEnd = 0;

    private float currentDegree = 0;

    private AnimationSet rotateAnimations;

    private static int num = 0;

    private Bitmap bitmap = null;

    private float scale = 1;

    /**
     * A new CustomImageView with context CONTEXT.
     * 
     */
    public CustomImageView(Context context) {
        super(context);

        num++;
        _selected = false;
        _delayedSelected = false;
        this.setScaleType(ScaleType.FIT_XY);
        this.setAdjustViewBounds(true);
    }

    public CustomImageView(Context context, boolean selected) {
        super(context);
        num++;
        _selected = selected;
        _delayedSelected = selected;
        this.setScaleType(ScaleType.FIT_XY);
        this.setAdjustViewBounds(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        if (this._delayedSelected) {
            Paint p = new Paint();
            p.setARGB(255, 255, 255, 255);
            p.setPathEffect(new DashPathEffect(new float[] { 5, 5 }, 1));
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, p);
        }
        canvas.save();
        canvas.rotate(this.currentDegree, this.getWidth() / 2, this.getHeight() / 2);
        canvas.scale(this.scale, this.scale);
        canvas.drawBitmap(bitmap, new Matrix(), null);
        canvas.restore();
    }

    /**
     * Deselects me as the current iamge being edited (makes me draw myself
     * without a border).
     */
    public void removeSelected() {
        this._delayedSelected = false;
        this._selected = false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _selected = true;
                ((CustomRelativeLayout) this.getParent()).removeAllSelected();
                ((CustomRelativeLayout) this.getParent()).setDragging(this);

                _delayedSelected = true;
                break;
            case MotionEvent.ACTION_MOVE:
                coords.x = (int) event.getRawX() + (this.getWidth() / 2);
                coords.y = (int) event.getRawY() - 150;
                this.layout(coords.x - (this.getWidth()), coords.y - (this.getHeight()), coords.x, coords.y);
                if (this.getParent() instanceof CustomRelativeLayout) {
                    ((CustomRelativeLayout) this.getParent()).isOverTrash(coords);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (this.getParent() instanceof CustomRelativeLayout) {
                    if (((CustomRelativeLayout) this.getParent()).outOfBounds(coords)) {
                        ((CustomRelativeLayout) this.getParent()).isOverTrash(new Point((int) event.getRawX(), (int) event.getRawY()));
                        ((CustomRelativeLayout) this.getParent()).removeView(this);

                        return true;
                    }

                }
                _selected = false;
        }
        ((View) this.getParent()).invalidate();
        return true;
    }

    /**
     * Relayout myself at the coordinates that I have saved.
     */
    public void reLayout() {
        this.layout(coords.x - this.getWidth(), coords.y - this.getHeight(), coords.x, coords.y);
    }

    /**
     * Scales the image by AMOUNT / 10 percent.
     * 
     * @param amount
     */
    public void scaleX(double amount) {
        if (amount > 0) {
            scale += .1;
            this.setLayoutParams(new RelativeLayout.LayoutParams((int) (this.getWidth() * 1.1), (int) (this.getHeight() * 1.1)));
        } else if (amount < 0) {
            scale -= .1;
            this.setLayoutParams(new RelativeLayout.LayoutParams((int) (Math.ceil(this.getWidth() * .9001)), (int) (Math.ceil(this.getHeight() * .9001))));
        }
        ((View) this.getParent()).invalidate();
    }

    /**
     * Rotates me by AMOUNT * 10 degrees clockwise.
     * 
     * @param amount
     */
    public void rotateX(int amount) {
        this.currentDegree += amount;
        currentDegree = currentDegree % 360;
        ((View) this.getParent()).invalidate();
        return;
    }

    /**
     * Pads the Bitmap NEWBM with WIDTHCROP pixels on both sides and HEIGHTCROP
     * pixels on the top and bottom with transparency. RETURNS the new Bitmap
     */
    private Bitmap padBitmap(Bitmap newBm, int widthcrop, int heightcrop) {
        int[] pixels = new int[(newBm.getWidth() + 2 * widthcrop) * (newBm.getHeight() + 2 * heightcrop)];
        int index = 0;
        for (; index < heightcrop * (widthcrop * 2 + newBm.getWidth()); index++) {
            pixels[index] = Color.TRANSPARENT;
        }
        int rowCount = 0;

        for (; rowCount < newBm.getHeight(); rowCount++) {
            int colCount = 0;
            for (; colCount < widthcrop; colCount++, index++) {
                pixels[index] = Color.TRANSPARENT;
            }
            for (; colCount < newBm.getWidth() + widthcrop; colCount++, index++) {
                pixels[index] = newBm.getPixel(colCount - widthcrop, rowCount);
            }
            for (; colCount < newBm.getWidth() + 2 * widthcrop; colCount++, index++) {
                pixels[index] = Color.TRANSPARENT;
            }
        }
        for (; index < (newBm.getHeight() + 2 * heightcrop) * (newBm.getWidth() + 2 * widthcrop); index++) {
            pixels[index] = Color.TRANSPARENT;
        }
        return Bitmap.createBitmap(pixels, newBm.getWidth() + 2 * widthcrop, newBm.getHeight() + 2 * heightcrop, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        this.isRotating = false;
        this.currentDegree = this.currentRotateEnd;

        if (this.futureRotateEnd > 0) {

            Log.e("Rotate", "Need to rotate more!");
            this.currentDegree = currentRotateEnd;
            Log.e("Current Degree", this.currentDegree + "");
            rotateX((int) (futureRotateEnd));
        } else {
            this.setDrawingCacheEnabled(true);
            Bitmap b = this.getDrawingCache();
            Bitmap newBm = b.copy(b.getConfig(), true);
            Dialog d = new Dialog(this.getContext());
            ImageView v = new ImageView(getContext());
            v.setImageBitmap(newBm);

            d.addContentView(v, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            d.show();
            this.setDrawingCacheEnabled(false);
            this.setImageBitmap(newBm);
            Log.e("Rotate", "Done rotating " + currentDegree + " " + currentRotateEnd + " " + futureRotateEnd);

            this.currentRotateEnd = 0;
            this.futureRotateEnd = 0;
            this.setAnimation(null);

            // ((View) this.getParent()).invalidate();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Nothing
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.e("Rotating", "Starting rotate");
        this.isRotating = true;

    }

    @Override
    public void setImageBitmap(Bitmap b) {
        super.setImageBitmap(b);
        this.bitmap = b;
        setDimensions();
    }

    @Override
    public void setImageResource(int res) {
        super.setImageResource(res);
        this.bitmap = BitmapFactory.decodeResource(getResources(), res);
        setDimensions();
    }

    @Override
    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d);
        this.bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        setDimensions();
    }

    private void setDimensions() {
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }
}
