package com.owleyes.moustache;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

/**
 * The entry point of Android Image Edit. Basically controls the flow of data:
 * If we have a current image, it displays it, if not, we get a gallery view.
 * 
 * @author Ben Augarten
 * 
 */
public class Main extends Activity {

    static final String PREFS_FILE = "image_edit";

    /** The URI of the Image to display. */
    private Uri imageUri;

    private int _wait;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_screen);
        _wait = 1000;

        imageUri = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (_wait != 0) {

            new CountDownTimer(_wait, _wait) {
                @Override
                public void onFinish() {
                    if (imageUri != null) {
                        Intent viewActivity = new Intent(Main.this, Viewer.class);
                        viewActivity.putExtra("image", imageUri);
                        startActivity(viewActivity);
                    } else {
                        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 0);
                    }
                }

                @Override
                public void onTick(long millisUntilFinished) {

                }

            }.start();
            _wait = 0;
        } else {
            if (imageUri != null) {
                Intent viewActivity = new Intent(this, Viewer.class);
                viewActivity.putExtra("image", imageUri);
                startActivity(viewActivity);
            } else {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 0);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        imageUri = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
        } else {
            System.exit(0);
            Log.e("result", "BAD");
        }
    }
}