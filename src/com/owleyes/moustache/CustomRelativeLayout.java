package com.owleyes.moustache;

import java.util.ArrayList;

import android.content.Context;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {
  private int dragging;

  private ArrayList<CustomImageView> _images;

  public CustomRelativeLayout(Context context) {
    super(context);
    _images = new ArrayList<CustomImageView>();
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    for (int i = 0; i < _images.size(); i++) {
      _images.get(i).reLayout();
    }
  }

  public void setDragging(CustomImageView civ) {
    dragging = _images.size();
    _images.add(civ);
  }
}
