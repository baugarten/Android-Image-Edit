package com.owleyes.moustache;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

  private Point coords = new Point();

  private boolean _selected;

  private boolean _delayedSelected;

  private static int num = 0;

  private int identifier = 0;

  /**
   * A new CustomImageView with context CONTEXT.
   * 
   */
  public CustomImageView(Context context) {
    super(context);
    identifier = num;
    num++;
    _selected = false;
    _delayedSelected = false;
  }

  public CustomImageView(Context context, boolean selected) {
    super(context);
    identifier = num;
    num++;
    _selected = selected;
    _delayedSelected = selected;
  }

  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (this._delayedSelected) {
      // ((CustomRelativeLayout)
      // this.getParent()).getEditableImage().getDrawingCache().getPixels(pixels,
      // offset, stride, x, y, width, height);
      Paint p = new Paint();
      p.setARGB(255, 255, 255, 255);
      p.setPathEffect(new DashPathEffect(new float[] { 5, 5 }, 1));
      p.setStyle(Paint.Style.STROKE);
      canvas.drawRect(0, 0, this.getWidth() - 1,
          this.getHeight() - 1, p);
      if (!this._selected) {
        this._delayedSelected = false;
      }
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        _selected = true;
        ((CustomRelativeLayout) this.getParent()).setDragging(this);
        _delayedSelected = true;
        break;
      case MotionEvent.ACTION_MOVE:
        coords.x = (int) event.getRawX() + (this.getWidth() / 2);
        coords.y = (int) event.getRawY() + 50;
        this.layout(coords.x - (this.getWidth()), coords.y
            - (this.getHeight()), coords.x, coords.y);
        break;
      case MotionEvent.ACTION_UP:
        _selected = false;
    }
    ((View) this.getParent()).invalidate();
    return true;
  }

  public void reLayout() {
    this.layout(coords.x - this.getWidth(), coords.y
        - this.getHeight(), coords.x, coords.y);
  }
}
