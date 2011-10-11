package com.owleyes.moustache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

public class Main extends Activity {

	private static final String LIST_ID = "moustache";
	
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
		Log.e("RESUME", "resume activity");
		if (imageUri != null) {
			Intent viewActivity = new Intent(this, Viewer.class);
    	    viewActivity.putExtra("image", imageUri);
    	      
    	    startActivity(viewActivity);
		} else {
			startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 0);
		}
	}
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		imageUri = null;
	}

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Log.e("RESULT CODE", resultCode + "");
    	if (resultCode == Activity.RESULT_OK) {
    		Log.e("result", "ok");
    	    imageUri = data.getData();
    	      
    	    
    	    Log.e("result", "Should've started that activity...");
    	} else
    		Log.e("result", "BAD");
    }
    
    
}