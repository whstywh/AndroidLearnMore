package com.wh.imagepicker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

public class pickerUtils {

    public static final String CAPTURE = "capture";
    public static final String CROP = "crop";

    public static Uri insertUri(Context context, String picName) {
        ContentResolver resolver = context.getApplicationContext().getContentResolver();
        Uri sUri;
        // On API <= 28, use VOLUME_EXTERNAL instead.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            sUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            sUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }
        ContentValues desUri = new ContentValues();
        desUri.put(MediaStore.Images.Media.DISPLAY_NAME, picName + ".jpg");
        return resolver.insert(sUri, desUri);
    }

}
