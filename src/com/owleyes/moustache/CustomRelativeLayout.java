package com.owleyes.moustache;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {
    private int mDragging;
    private ImageView mEditable;
    private ImageView trashImage;

    private static final int FORGIVENESS = 70;
    private ArrayList<CustomImageView> mImages;

    private boolean update = false;

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

    public void setTrashIcon(ImageView r) {
        this.trashImage = r;
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

        if (amount == 0)
            return;
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

    public void isOverTrash(Point coords) {
        int[] loc1 = new int[2];
        int[] loc2 = new int[2];
        trashImage.getLocationOnScreen(loc1);
        trashImage.getLocationInWindow(loc2);
        Log.e("LOCS:", loc1[0] + " " + loc1[1] + " vs " + loc2[0] + " " + loc2[1]);
        int left = loc1[0], bottom = loc1[1];
        int right = left + trashImage.getWidth(), top = bottom - trashImage.getHeight();
        boolean hit = left - FORGIVENESS < coords.x && right + FORGIVENESS > coords.x && bottom + FORGIVENESS > coords.y && top - FORGIVENESS < coords.y;
        if (coords != null && !update && hit) {
            Log.e("Trash", "Is over it");
            this.trashImage.setImageResource(R.drawable.trash2);
            this.invalidate();
            update = true;
        } else if (update && !hit) {
            this.trashImage.setImageResource(R.drawable.trash1);
            this.invalidate();
            update = false;
        }

        Log.e("Points:", left + " " + right + " " + coords.x);
        Log.e("Points:", bottom + " " + top + " " + coords.y);
        Log.e("Points:", update + "");
    }
}
