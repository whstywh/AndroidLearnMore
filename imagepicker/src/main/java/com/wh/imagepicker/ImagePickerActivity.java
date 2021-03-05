package com.wh.imagepicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ImagePickerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mAlbumImagge;

    //权限
    private final ActivityResultLauncher<String> mRequestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    capture();
                } else {
                    Toast.makeText(ImagePickerActivity.this, "申请权限失败", Toast.LENGTH_LONG).show();
                }
            });

    //拍照
    private final ActivityResultLauncher<Uri> mTakePicturePreviewLauncher = registerForActivityResult(new CustomTakePicture(),
            result -> {
                Drawable drawable = new BitmapDrawable(getResources(), result);
                mAlbumImagge.setImageDrawable(drawable);
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        mAlbumImagge = findViewById(R.id.album_img);
        Button capture = findViewById(R.id.capture);
        capture.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.capture) {
            checkPermissionFun();
        } else if (v.getId() == R.id.album) {
            checkPermissionFun();
        }
    }


    //拍照
    private void capture() {
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        Uri photoURI = FileProvider.getUriForFile(this, "com.wh.imagepicker.fileprovider", file);
        mTakePicturePreviewLauncher.launch(photoURI);
    }

    private void checkPermissionFun() {
        String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, readExternalStorage) == PackageManager.PERMISSION_GRANTED) {
            capture();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(readExternalStorage)) {
                Toast.makeText(this, "本应用申请该权限的原因", Toast.LENGTH_LONG).show();
            } else {
                mRequestPermissionLauncher.launch(readExternalStorage);
            }
        } else {
            mRequestPermissionLauncher.launch(readExternalStorage);
        }
    }
}