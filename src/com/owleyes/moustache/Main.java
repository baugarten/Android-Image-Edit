package com.owleyes.moustache;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * The entry point of Android Image Edit. Basically controls the flow of data:
 * If we have a current image, it displays it, if not, we get a gallery view.
 * 
 * @author Ben Augarten
 * 
 */
public class Main extends Activity {

  /** The URI of the Image to display. */
  private Uri imageUri;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    imageUri = null;
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (imageUri != null) {
      Intent viewActivity = new Intent(this, Viewer.class);
      viewActivity.putExtra("image", imageUri);
      startActivity(viewActivity);
    } else {
      startActivityForResult(
          new Intent(
              Intent.ACTION_PICK,
              android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
          0);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    imageUri = null;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode,
      Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == Activity.RESULT_OK) {
      imageUri = data.getData();
    } else
      Log.e("result", "BAD");
  }
}