package com.owleyes.moustache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class CustomHorizontalScrollView extends HorizontalScrollView {
    private static final int threshold = 75;
    private boolean mScroll = false;

    private Point initial = new Point();

    private CustomImageView dragging;

    private LinearLayout ll = null;
    private CustomRelativeLayout rl = null;

    private boolean fix;

    public CustomHorizontalScrollView(Context context) {
        super(context);

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mScroll != true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ll == null) {
                    ll = (LinearLayout) this.getChildAt(0);
                }
                initial = new Point();
                initial.x = (int) ev.getX();
                initial.y = (int) ev.getY();
                for (int i = 0; i < ll.getChildCount(); i += 1) {
                    View v = ll.getChildAt(i);
                    if (initial.x > v.getLeft() - this.getScrollX() && initial.x < v.getLeft() - this.getScrollX() + v.getWidth()) {

                        dragging = (CustomImageView) v;
                    }
                }
                super.onTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mScroll) {
                    dragging.onTouchEvent(ev);

                } else if (Math.abs(ev.getY() - initial.y) > threshold) {
                    mScroll = true;

                    rl = ((CustomRelativeLayout) this.getParent());
                    CustomImageView civ = new CustomImageView(getContext(), true);

                    civ.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                    civ.setImageDrawable(dragging.getDrawable());
                    rl.removeAllSelected();
                    rl.addView(civ);
                    rl.setDragging(civ);
                    dragging = civ;

                    rl.requestLayout();
                    return true;
                } else {
                    super.onTouchEvent(ev);
                }
                return true;

            case MotionEvent.ACTION_OUTSIDE:
                mScroll = true;
                return true;

            case MotionEvent.ACTION_UP:
                ((CustomImageView) dragging).onTouchEvent(ev);
                mScroll = false;
                dragging = null;

                super.onTouchEvent(ev);

                return false;
        }
        return false;
    }

}