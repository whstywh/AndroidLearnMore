package com.wh.rwdata.imagePicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CropPicturePicker extends ActivityResultContract<Uri, Uri> {


    private Uri mCropDesUri;

    @CallSuper
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, @NonNull Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//临时权限
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);//宽度比
        intent.putExtra("aspectY", 1);//高度比
        intent.putExtra("outputX", 256);//裁剪区的宽度
        intent.putExtra("outputY", 256);//裁剪区的高度
        intent.putExtra("scale", true);//缩放
        intent.putExtra("return-data", false);//true：返回缩略图，false：不返回、需要通过Uri
        intent.putExtra("noFaceDetection", false);//前置摄像头
        intent.setDataAndType(uri, "image/*");//裁剪图片源Uri和类型
        mCropDesUri = PickerUtils.insertUri(context, PickerUtils.CROP);//裁剪后保存的目的uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropDesUri);
        return intent;
    }

    @Nullable
    @Override
    public final SynchronousResult<Uri> getSynchronousResult(@NonNull Context context, @NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public final Uri parseResult(int resultCode, @Nullable Intent intent) {
        if (intent == null || resultCode != Activity.RESULT_OK) return null;
        return mCropDesUri;
    }
}
