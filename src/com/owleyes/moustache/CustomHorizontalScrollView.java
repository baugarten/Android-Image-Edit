package com.owleyes.moustache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class CustomHorizontalScrollView extends HorizontalScrollView {
  private static final int threshold = 75;
  private boolean mScroll = false;

  private GestureDetector gestureDetector;

  private Point initial = new Point();
  private int[] location = new int[2];

  private CustomImageView dragging;

  private LinearLayout ll = null;
  private CustomRelativeLayout rl = null;

  private int _width;
  private int _height;
  private boolean fix;

  public CustomHorizontalScrollView(Context context) {
    super(context);

    Display display = ((Activity) context).getWindowManager()
        .getDefaultDisplay();
    _width = display.getWidth();
    _height = display.getHeight();
    gestureDetector = new GestureDetector(new YScrollDetector());
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    boolean result = super.onInterceptTouchEvent(ev);
    return mScroll != true;

  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    /**
     * TODO(baugarten): Clean up the dragging code
     */
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        Log.e("DOWN", "ACTION_DOWN");
        if (ll == null) {
          ll = (LinearLayout) this.getChildAt(0);
        }
        initial = new Point();
        initial.x = (int) ev.getX();
        initial.y = (int) ev.getY();
        for (int i = 0; i < ll.getChildCount(); i += 1) {
          View v = ll.getChildAt(i);
          if (initial.x > v.getLeft() - this.getScrollX()
              && initial.x < v.getLeft() - this.getScrollX()
                  + v.getWidth()) {

            Log.e("HEEEELLO", "Found the fucking child " + dragging);
            dragging = (CustomImageView) v;
          }
        }
        super.onTouchEvent(ev);
        return true;
      case MotionEvent.ACTION_MOVE:
        if (mScroll) {
          dragging.onTouchEvent(ev);

        } else if (Math.abs(ev.getY() - initial.y) > threshold) {
          Log.e("Adding", "Adding child to stage");
          mScroll = true;

          rl = ((CustomRelativeLayout) this.getParent());
          CustomImageView civ = new CustomImageView(getContext());

          civ.setLayoutParams(new LayoutParams(
              LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

          civ.setImageDrawable(dragging.getDrawable());
          rl.addView(civ);
          dragging = civ;
          rl.setDragging(dragging);
          for (int i = 0; i < rl.getChildCount(); i++) {
            Log.e("Child " + i, rl.getChildAt(i).getLeft() + " "
                + rl.getChildAt(i).getTop());
          }
          rl.requestLayout();
          // fix = true;
          return true;
        } else {
          super.onTouchEvent(ev);
        }
        return true;

      case MotionEvent.ACTION_OUTSIDE:
        mScroll = true;
        return true;

      case MotionEvent.ACTION_UP:
        Log.e("UP", "GET HER UP");
        ((CustomImageView) dragging).onTouchEvent(ev);
        mScroll = false;
        dragging = null;

        super.onTouchEvent(ev);

        return false;
    }
    return false;
  }

}

class YScrollDetector extends SimpleOnGestureListener {
  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2,
      float distanceX, float distanceY) {
    try {
      if (Math.abs(distanceY) > Math.abs(distanceX)) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      // nothing
    }
    return false;
  }
}
