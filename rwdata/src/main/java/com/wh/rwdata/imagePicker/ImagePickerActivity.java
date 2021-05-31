package com.wh.rwdata.imagePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.wh.rwdata.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;


/**
 * 2021/3/9
 * wh
 * desc：拍照,相册选择,裁剪
 * TODO:废弃图片未删除
 */
public class ImagePickerActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CAPTURE = "capture";
    public static final String CROP = "crop";

    private ImageView mAlbumImage;
    private Uri mCaptureUri;
    private String mType;

    //权限
    private final ActivityResultLauncher<String[]> mRequestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                Set<Map.Entry<String, Boolean>> entries = result.entrySet();
                for (Map.Entry<String, Boolean> entry : entries) {
                    if (!entry.getValue()) {
                        Toast.makeText(this, entry.getKey() + "权限申请失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (mType.equals("capture")) {
                    capture();
                } else if (mType.equals("album")) {
                    album();
                }
            });

    //拍照
    private final ActivityResultLauncher<Uri> mTakePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            result -> {
                if (mCaptureUri != null) {
                    crop(mCaptureUri);
                }
            }
    );

    //相册
    private final ActivityResultLauncher<Void> mAlbumPictureLauncher = registerForActivityResult(new AlbumPicturePicker(),
            this::crop
    );

    //裁剪
    private final ActivityResultLauncher<Uri> mCropPickerLauncher = registerForActivityResult(new CropPicturePicker(),
            result -> {
                if (result != null) {
                    ContentResolver resolver = getApplicationContext().getContentResolver();
                    try (InputStream stream = resolver.openInputStream(result)) {
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        mAlbumImage.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        String path = null;
//                        if (result.getScheme().equals(ContentResolver.SCHEME_FILE))
//                            path =result.getPath();
//                        else if (result.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
//                            Cursor cursor = this.getContentResolver().query(result,null, null, null, null);
//                            if (cursor.moveToFirst()) {
//                                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                            }
//                            cursor.close();
//                        }
//                        //上传图片
//                        //...
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        mAlbumImage = findViewById(R.id.album_img);
        Button capture = findViewById(R.id.capture);
        Button album = findViewById(R.id.album);
        capture.setOnClickListener(this);
        album.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.capture) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                checkPermissionFun("capture", Manifest.permission.CAMERA);
            } else {
                checkPermissionFun("capture", Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        } else if (v.getId() == R.id.album) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                checkPermissionFun("album", Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                checkPermissionFun("album", Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    //拍照
    private void capture() {
        mCaptureUri = ImagePickerActivity.insertUri(this, ImagePickerActivity.CAPTURE);
        mTakePictureLauncher.launch(mCaptureUri);
    }

    //相册
    private void album() {
        mAlbumPictureLauncher.launch(null);
    }

    //裁剪
    private void crop(Uri uri) {
        mCropPickerLauncher.launch(uri);
    }

    //申请权限
    private void checkPermissionFun(String type, String... permission) {
        mType = type;
        int allPermission = 0;
        for (String s : permission) {
            if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                allPermission++;
            }
        }
        if (allPermission == 0) {
            if (type.equals("capture")) {
                capture();
            } else if (type.equals("album")) {
                album();
            }
        } else {
            mRequestPermissionLauncher.launch(permission);
        }
    }

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