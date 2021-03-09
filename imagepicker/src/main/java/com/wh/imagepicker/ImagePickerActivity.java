package com.wh.imagepicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;


/**
 * 2021/3/9
 * wh
 * desc：拍照，相册选择 兼容Android 11
 */
public class ImagePickerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mAlbumImagge;
    private String mPermission;

    //权限
    private final ActivityResultLauncher<String> mRequestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    if (mPermission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        capture();
                    } else if (mPermission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        album();
                    }
                } else {
                    Toast.makeText(ImagePickerActivity.this, "申请权限失败", Toast.LENGTH_LONG).show();
                }
            });

    //拍照
    private final ActivityResultLauncher<Uri> mTakePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            result -> {
            }
    );
    //相册
    private final ActivityResultLauncher<Void> mAlbumPictureLauncher = registerForActivityResult(new AlbumPicturePick(),
            result -> {
                ContentResolver resolver = getApplicationContext().getContentResolver();
                try (InputStream stream = resolver.openInputStream(result)) {
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    mAlbumImagge.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        mAlbumImagge = findViewById(R.id.album_img);
        Button capture = findViewById(R.id.capture);
        Button album = findViewById(R.id.album);
        capture.setOnClickListener(this);
        album.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.capture) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                capture();
            } else {
                checkPermissionFun(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        } else if (v.getId() == R.id.album) {
            checkPermissionFun(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    //拍照
    private void capture() {
        ContentResolver resolver = getApplicationContext().getContentResolver();
        Uri audioCollection;
        // On API <= 28, use VOLUME_EXTERNAL instead.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            audioCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            audioCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }
        ContentValues newSongDetails = new ContentValues();
        newSongDetails.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        Uri myFavoriteSongUri = resolver.insert(audioCollection, newSongDetails);
        mTakePictureLauncher.launch(myFavoriteSongUri);
    }


    //相册
    private void album() {
        mAlbumPictureLauncher.launch(null);
    }


    //申请权限
    private void checkPermissionFun(String permission) {

        mPermission = permission;
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            if (mPermission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                capture();
            } else if (mPermission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                album();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permission)) {
                Toast.makeText(this, "已拒绝此权限，展示该权限的使用说明", Toast.LENGTH_LONG).show();
            } else {
                mRequestPermissionLauncher.launch(permission);
            }
        } else {
            mRequestPermissionLauncher.launch(permission);
        }
    }

}