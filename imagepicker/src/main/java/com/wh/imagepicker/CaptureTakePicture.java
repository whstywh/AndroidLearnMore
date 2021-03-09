package com.wh.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class CaptureTakePicture extends ActivityResultContract<Uri, Bitmap> {

    @CallSuper
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, @NonNull Uri input) {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, input);
    }

    @Nullable
    @Override
    public final SynchronousResult<Bitmap> getSynchronousResult(@NonNull Context context, @Nullable Uri input) {
        return null;
    }

    @Nullable
    @Override
    public final Bitmap parseResult(int resultCode, @Nullable Intent intent) {
        if (intent == null || resultCode != Activity.RESULT_OK) return null;
        return intent.getParcelableExtra("data");
    }
}
