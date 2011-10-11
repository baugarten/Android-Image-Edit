package com.owleyes.moustache;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {
	private CustomImageView dragging;
	public CustomRelativeLayout(Context context) {
		super(context);
	}
	public void setDragging(CustomImageView civ) {
		dragging = civ;
		this.bringChildToFront(dragging);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			int y = (int) event.getY();
			int w = ((Activity)getContext()).getWindowManager().getDefaultDisplay().getWidth() - 100;
            int h = ((Activity)getContext()).getWindowManager().getDefaultDisplay().getHeight() - 100;
            if(x > w)
                x = w;
            if(y > h)
                y = h;
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    new ViewGroup.MarginLayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
             lp.setMargins(x, y, 0, 0);
             dragging.setLayoutParams(lp);
			break;
		
		}
		return super.onTouchEvent(event);
	}

}
