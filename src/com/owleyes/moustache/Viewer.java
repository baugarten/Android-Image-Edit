package com.owleyes.moustache;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

public class Viewer extends Activity implements OnChangeParentView {

	/** The list of drawable resource ids */
	private static final int[] imageList = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
		R.drawable.eight, R.drawable.nine, R.drawable.ten, R.drawable.eleven};
	
	/** The picture being viewed */
	private ImageView iv;
	
	/** ViewGroup for adding moustaches */
	private LinearLayout vg;
	private LinearLayout root_layout;
	private CustomHorizontalScrollView hsv;
	
	private CustomRelativeLayout rl;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		setContentView(R.layout.nothing);
		Log.e("CREATE", "Creating");
		
		root_layout = (LinearLayout) findViewById(R.id.root);
		
		rl = new CustomRelativeLayout(this);
		LayoutParams fill = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		LayoutParams wrap = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rl.setLayoutParams(fill);
		root_layout.addView(rl);
		
		iv = new ImageView(this);
		iv.setLayoutParams(wrap);
		rl.addView(iv);
//		iv = (ImageView) findViewById(R.id.image);
		vg = new LinearLayout(this); //(LinearLayout) findViewById(R.id.group);
		hsv = new CustomHorizontalScrollView(this); //(CustomHorizontalScrollView) findViewById(R.id.scroll);
//		rl = (RelativeLayout) findViewById(R.id.root_layout);
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		hsv.setBackgroundColor(Color.WHITE);
		hsv.setLayoutParams(lp);
		rl.addView(hsv);
		
		vg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		hsv.addView(vg);
		
		Intent intent = getIntent();
		Uri imageURI = (Uri) intent.getParcelableExtra("image");
		
		String[] filePathColumn = {MediaStore.Images.Media.DATA};
	    Cursor cursor = getContentResolver().query(imageURI, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        
        iv.setImageBitmap(Viewer.setUpPicture(filePath));	   
        iv.invalidate();
        
        int counter = 0;
        for (int i : imageList) {
        	CustomImageView temp = new CustomImageView(this);
        	temp.setImageResource(i);
        	temp.setId(counter);
        	counter++;
        	vg.addView(temp, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.e("Pause", "Pausing");
		iv = null;
		vg = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	/**
	 * This function takes a filepath as a parameter and returns the corresponding image, correctly
	 * scaled and rotated (if necessary)
	 * @param filePath The filepath of the image
	 * @return the scaled and rotated bitmap
	 */
	private static Bitmap setUpPicture(String filePath) {
		
		int w = 512; int h = 384; // size that does not lead to OutOfMemoryException on Nexus One
		Bitmap b = BitmapFactory.decodeFile(filePath);
		
		
		// Hack to determine whether the image is rotated
		boolean rotated = b.getWidth() > b.getHeight();
		
		Bitmap resultBmp = null;
		
		
		// If not rotated, just scale it
		int degree;
		if ((degree = degreeRotated(filePath)) == 0) {
			resultBmp = Bitmap.createScaledBitmap(b, w, h, true);
			b.recycle();
			b = null;
			Log.e("Not Rotates", "Just scale it");
				// If rotated, scale it by switching width and height and then rotated it
		} else {
			Bitmap scaledBmp = Bitmap.createScaledBitmap(b, w, h, true);
			b.recycle();
			b = null;
			Log.e("Rotated", "Switch it, Bitch");
			Matrix mat = new Matrix();
			mat.postRotate(degree);
			resultBmp = Bitmap.createBitmap(scaledBmp, 0, 0, w, h, mat, true);
			
			// Release image resources
			scaledBmp.recycle();
			scaledBmp = null;
		}
		return resultBmp;
	}

	private static int degreeRotated(String filePath) {
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(new File(filePath));
			Directory exifDirectory = metadata.getDirectory(ExifDirectory.class);
			int orientation = exifDirectory.getInt(ExifDirectory.TAG_ORIENTATION);
			Log.e("ORIENTATION", orientation + " " + (orientation == 6));
			switch (orientation) {
			case 6:
				return 90;
			case 8:
				return 270;
			default:
				return 0;
					
			}
		} catch (JpegProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public void onChangeParent(View v, int index) {
		
		
	}	

}
