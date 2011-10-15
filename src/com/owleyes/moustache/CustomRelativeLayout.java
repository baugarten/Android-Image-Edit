package com.owleyes.moustache;

import java.util.ArrayList;

import android.content.Context;
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
}
