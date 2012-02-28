package com.owleyes.moustache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

    private Point coords = new Point();

    private boolean _selected;

    private boolean _delayedSelected;

    private static int num = 0;

    /**
     * A new CustomImageView with context CONTEXT.
     * 
     */
    public CustomImageView(Context context) {
        super(context);
        num++;
        _selected = false;
        _delayedSelected = false;
        this.setScaleType(ScaleType.CENTER_CROP);
        this.setAdjustViewBounds(true);
    }

    public CustomImageView(Context context, boolean selected) {
        super(context);
        num++;
        _selected = selected;
        _delayedSelected = selected;
        this.setAdjustViewBounds(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this._delayedSelected) {
            Paint p = new Paint();
            p.setARGB(255, 255, 255, 255);
            p.setPathEffect(new DashPathEffect(new float[] { 5, 5 }, 1));
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, p);
        }
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
                break;
            case MotionEvent.ACTION_UP:
                if (this.getParent() instanceof CustomRelativeLayout) {
                    if (((CustomRelativeLayout) this.getParent()).outOfBounds(coords)) {
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
        this.removeSelected();
        Matrix mat = new Matrix();
        float amount2 = ((float) amount) / 10;
        coords.x += this.getWidth() * amount2 / 2;
        coords.y += this.getHeight() * amount2 / 2;
        mat.postScale((float) (1.5f + amount2), (float) (1.5f + amount2));
        this.setDrawingCacheEnabled(true);
        Bitmap newBm = Bitmap.createBitmap(this.getDrawingCache(), 0, 0, this.getWidth(), this.getHeight(), mat, true);
        this.setDrawingCacheEnabled(false);
        Drawable newDraw = new BitmapDrawable(newBm);
        this.setImageDrawable(newDraw);

        this._delayedSelected = true;
        this._selected = true;
        ((View) this.getParent()).invalidate();
    }

    /**
     * Rotates me by AMOUNT * 10 degrees clockwise.
     * 
     * @param amount
     */
    public void rotateX(int amount) {
        this.removeSelected();
        this.setDrawingCacheEnabled(true);
        Matrix mat = new Matrix();
        amount *= 5;
        amount = amount % 360;
        if (amount < 0) {
            amount += 360;
        }
        mat.postRotate(amount, getWidth() / 2, getHeight() / 2);

        double w = getWidth();
        double h = getHeight();
        double sinTheta = Math.abs(Math.sin(amount * Math.PI / 180));
        double cosTheta = Math.abs(Math.cos(amount * Math.PI / 180));
        double widthcrop = -(w * Math.pow(sinTheta, 2) - h * cosTheta * sinTheta + h * (cosTheta - 1) * sinTheta - cosTheta * w * (cosTheta - 1))
                / (2 * (Math.pow(cosTheta, 2) - Math.pow(sinTheta, 2)));
        double heightcrop = -(-h * Math.pow(sinTheta, 2) + w * cosTheta * sinTheta - w * (cosTheta - 1) * sinTheta + cosTheta * h * (cosTheta - 1))
                / (2d * (Math.pow(sinTheta, 2) - Math.pow(cosTheta, 2)));

        Bitmap cropped = Bitmap.createBitmap(getDrawingCache(), (int) widthcrop, (int) heightcrop, (int) (w - 2d * widthcrop), (int) (h - 2d * heightcrop));
        Bitmap newBm = Bitmap.createBitmap(cropped, 0, 0, (int) (w - 2d * widthcrop), (int) (h - 2d * heightcrop), mat, true);
        Bitmap padded = newBm;
        if (w - newBm.getWidth() > 0 && h - newBm.getHeight() > 0) {
            padded = padBitmap(newBm, (int) (w - newBm.getWidth()), (int) (h - newBm.getHeight()));
        }
        this.setDrawingCacheEnabled(false);
        this.setImageBitmap(padded);
        this._delayedSelected = true;
        this._selected = true;
        ((View) this.getParent()).invalidate();
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
}
