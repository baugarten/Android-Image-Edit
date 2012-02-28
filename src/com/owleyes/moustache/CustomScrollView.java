package com.owleyes.moustache;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
    private static final int threshold = 75;
    private boolean mScroll = false;

    private Point initial = new Point();

    private CustomImageView dragging;

    private LinearLayout ll = null;
    private CustomRelativeLayout rl = null;

    public CustomScrollView(Context context) {
        super(context);
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
                    if (initial.y < (v.getBottom() - this.getScrollY()) && initial.y > v.getTop() - this.getScrollY()) {
                        dragging = (CustomImageView) v;
                    }
                }
                super.onTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mScroll) {
                    dragging.onTouchEvent(ev);

                } else if (Math.abs(ev.getX() - initial.x) > threshold) {
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
                if (dragging != null) {
                    ((CustomImageView) dragging).onTouchEvent(ev);
                }
                mScroll = false;
                dragging = null;

                super.onTouchEvent(ev);

                return false;
        }
        return false;
    }
}
