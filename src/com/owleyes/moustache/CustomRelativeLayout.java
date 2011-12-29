package com.owleyes.moustache;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {
    private int dragging;
    private ImageView editable;

    private ArrayList<CustomImageView> _images;

    public CustomRelativeLayout(Context context) {
        super(context);
        _images = new ArrayList<CustomImageView>();
        dragging = -1;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < _images.size(); i++) {
            _images.get(i).reLayout();
        }
    }

    public void setDragging(CustomImageView civ) {
        if (civ == null) {
            dragging = -1;
        } else {
            dragging = _images.size();
            _images.add(civ);
        }
    }

    public void setEditable(ImageView civ) {
        editable = civ;

    }

    public ImageView getEditableImage() {
        return editable;
    }

    public View getSelectedImage() {
        if (dragging == -1) {
            return null;
        }
        return _images.get(dragging);
    }

    public void handleMinusButton(int state) {
        // TODO Auto-generated method stub

    }

    public void handlePlusButton(int state) {

    }

    private void changeImageSize(int size) {
        if (_images.size() <= dragging || dragging == -1) {
            return;
        }
        Log.e("Dragging", dragging + "");
        _images.get(dragging).scaleX(size);
    }

    private void rotateImageClockwise(int degree) {
        if (_images.size() <= dragging || dragging == -1) {
            return;
        }
        Log.e("ROTATE", "Rotating image");
        _images.get(dragging).rotateX(degree);

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
        for (CustomImageView v : _images) {
            v.removeSelected();
        }
    }
}
