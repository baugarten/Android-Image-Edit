package com.owleyes.moustache;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

  private Point coords = new Point();

  private boolean _selected;

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
  }

  public CustomImageView(Context context, boolean selected) {
    super(context);
    identifier = num;
    num++;
    _selected = selected;
  }

  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (this._selected) {
      // TODO(baugarten): Add a dotted outline around this imageview.
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_MOVE:
        coords.x = (int) event.getRawX() + (this.getWidth() / 2);
        coords.y = (int) event.getRawY() + 50;
        this.layout(coords.x - (this.getWidth()), coords.y
            - (this.getHeight()), coords.x, coords.y);
        break;
    }
    ((View) this.getParent()).invalidate();
    return true;
  }

  public void reLayout() {
    this.layout(coords.x - this.getWidth(), coords.y
        - this.getHeight(), coords.x, coords.y);
  }
}
