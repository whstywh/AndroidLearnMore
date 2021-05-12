package com.wh.rwdata.imagePicker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

public class PickerUtils {

    public static final String CAPTURE = "capture";
    public static final String CROP = "crop";

    /*
    * 兼容Android 11 分区存储
    * 保存图片到系统共享存储空间，并返回uri
    * */
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
