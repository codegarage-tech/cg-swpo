package com.meembusoft.safewaypharmaonline.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.meembusoft.safewaypharmaonline.model.PictureData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class PictureUtils {
    private  final static String IMAGE_DIRECTORY = "/safewaypharma";
    public static PictureData getImageFromCamera(Bitmap mBitmap , Activity context){
        PictureData pictureData = null;

        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            File shohokariDirectory = new File((Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY);
            if (!shohokariDirectory.exists()) {
                shohokariDirectory.mkdirs();
            }
            try {
                File f = new File(shohokariDirectory, ((Calendar.getInstance().getTimeInMillis()) + ".jpg"));
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                MediaScannerConnection.scanFile(context,
                        new String[]{f.getPath()},
                        new String[]{"image/jpeg"}, null);
                fo.close();
                pictureData = new PictureData(f.getName(), f);
              } catch (Exception ex){

            }
        }
        return pictureData;
    }


}
