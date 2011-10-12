package com.owleyes.moustache;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CustomImageView extends ImageView {
  private boolean pressed = false;
  private Point coords = new Point();

  private int _width;
  private int _height;

  /**
   * A new CustomImageView with context CONTEXT.
   * 
   */
  public CustomImageView(Context context) {
    super(context);
  }

  /**
   * Restores this imageView to the point (X, Y).
   * 
   */
  public void restore(int x, int y) {
    this.layout(x - this.getWidth() / 2, y
        - this.getHeight() / 2, x + this.getWidth() / 2, y
        + this.getHeight() / 2);

  }

  public void setScreenBounds(int width, int height) {
    _width = width;
    _height = height;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    /**
     * TODO(baugarten): Find a way to update the view such that
     * MotionEvent.ACTION_MOVE actually causes the image to update in real-time.
     */
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        pressed = true;
        coords.x = (int) event.getX();
        coords.y = (int) event.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        Log.e("MOVING", "IN the right class");
        coords.x = (int) event.getX();
        coords.y = _height + (int) event.getY();

        this.layout(coords.x - (this.getWidth()), coords.y
            - (this.getHeight()), coords.x, coords.y);
        break;
      case MotionEvent.ACTION_UP:
        System.out.println(this.getLeft() + " "
            + this.getRight() + " " + this.getTop() + " "
            + this.getBottom());
        break;
      case MotionEvent.ACTION_OUTSIDE:

        break;
    }
    invalidate();
    return true;
  }
}
