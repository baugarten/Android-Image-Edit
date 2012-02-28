package com.owleyes.moustache;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {
    private int mDragging;
    private ImageView mEditable;

    private ArrayList<CustomImageView> mImages;

    public CustomRelativeLayout(Context context) {
        super(context);
        mImages = new ArrayList<CustomImageView>();
        mDragging = -1;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < mImages.size(); i++) {
            mImages.get(i).reLayout();
        }
    }

    /**
     * Sets the image that the user is currently dragging/scaling/etc to CIV.
     */
    public void setDragging(CustomImageView civ) {
        if (civ == null) {
            mDragging = -1;
        } else {
            mDragging = mImages.size();
            mImages.add(civ);
        }
    }

    /** Sets the image being edited and viewed (the main one) to CIV. */
    public void setEditable(ImageView civ) {
        mEditable = civ;

    }

    /** RETURNS the image currently being edited. */
    public ImageView getEditableImage() {
        return mEditable;
    }

    /** RETURNS the image currently being dragging and placed. */
    public View getSelectedImage() {
        if (mDragging == -1) {
            return null;
        }
        return mImages.get(mDragging);
    }

    private void changeImageSize(int size) {
        if (mImages.size() <= mDragging || mDragging == -1) {
            return;
        }
        mImages.get(mDragging).scaleX(size);
    }

    private void rotateImageClockwise(int degree) {
        if (mImages.size() <= mDragging || mDragging == -1) {
            return;
        }
        mImages.get(mDragging).rotateX(degree);

    }

    public void handleEvent(int state, int amount) {
        switch (state) {
            case Viewer.ROTATE:
                rotateImageClockwise(amount);
                break;
            case Viewer.SCALE:
                changeImageSize(amount);
                break;
        }
    }

    public void removeAllSelected() {
        for (CustomImageView v : mImages) {
            v.removeSelected();
        }
    }

    public boolean outOfBounds(Point coords) {
        return mEditable.getBottom() < coords.y || mEditable.getTop() > coords.y || mEditable.getRight() < coords.x || mEditable.getLeft() > coords.x;
    }
}
