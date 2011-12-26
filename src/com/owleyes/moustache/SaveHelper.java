package com.owleyes.moustache;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

public class SaveHelper implements MediaScannerConnection.MediaScannerConnectionClient {

    public SaveHelper(Context context) {
        context_ = context;
        fos_ = null;
        msc_ = new MediaScannerConnection(context_, this);
    }

    public void setBitmap(Bitmap b) {
        picture_ = b;
    }

    public void setOutputStream(FileOutputStream fos) {
        fos_ = fos;
    }

    public void setFilePath(String filepath) {
        filePath_ = filepath;

    }

    public boolean save() throws FileNotFoundException {
        int[] pixels = new int[picture_.getWidth() * picture_.getHeight()];
        picture_.getPixels(pixels, 0, picture_.getWidth(), 0, 0, picture_.getWidth(), picture_.getHeight());
        picture_.compress(CompressFormat.JPEG, 95, fos_);
        Log.e("FILES", Arrays.toString(context_.fileList()));
        msc_.connect();

        return true;
    }

    @Override
    public void onMediaScannerConnected() {
        Log.e("FILPATH", filePath_);
        msc_.scanFile(filePath_, "*/*"); // MediaStore.Images.Media.MIME_TYPE);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        msc_.disconnect();
    }

    private Context context_;
    private FileOutputStream fos_;
    private Bitmap picture_;
    private MediaScannerConnection msc_;
    private String filePath_;
    private URI picUri_;
}
