package com.wh.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CropPicturePicker extends ActivityResultContract<Uri, Boolean> {


    @CallSuper
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, @NonNull Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");//设置要裁剪的图片Uri和类型
        intent.putExtra("aspectX", 768);//宽度比
        intent.putExtra("aspectY", 1024);//高度比
        intent.putExtra("outputX", 768);//输出图片的宽度
        intent.putExtra("outputY", 1024);//输出图片的高度
        intent.putExtra("scale", true);//缩放
        intent.putExtra("return-data", false);//当为true的时候就返回缩略图，false就不返回、需要通过Uri
        intent.putExtra("noFaceDetection", false);//前置摄像头

        Uri mCaptureUri = pickerUtils.insertUri(context, pickerUtils.CROP);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri);
        return intent;
    }

    @Nullable
    @Override
    public final SynchronousResult<Boolean> getSynchronousResult(@NonNull Context context, @NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public final Boolean parseResult(int resultCode, @Nullable Intent intent) {
        return resultCode == Activity.RESULT_OK;
    }
}
