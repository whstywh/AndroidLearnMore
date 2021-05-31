package com.wh.scankit;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.util.Map;
import java.util.Set;

public class ScanKitActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCAN_DEFAULT_MODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_kit);

        Button scan = findViewById(R.id.scan_btn);
        scan.setOnClickListener(v -> checkPermissionFun(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    /**
     * 华为统一扫码服务（Scan Kit）
     */
    private void toScan() {
        HmsScanAnalyzerOptions hmsScanAnalyzerOptions = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create();
        ScanUtil.startScan(this, REQUEST_CODE_SCAN_DEFAULT_MODE, hmsScanAnalyzerOptions);
    }

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
                toScan();
            });

    //申请权限
    private void checkPermissionFun(String... permission) {
        int allPermission = 0;
        for (String s : permission) {
            if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                allPermission++;
            }
        }
        if (allPermission == 0) {
            toScan();
        } else {
            mRequestPermissionLauncher.launch(permission);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN_DEFAULT_MODE && resultCode == RESULT_OK && data != null) {
            HmsScan hmsScan = data.getParcelableExtra(ScanUtil.RESULT);
            if (hmsScan != null && !TextUtils.isEmpty(hmsScan.getOriginalValue())) {
                Toast.makeText(this, hmsScan.getOriginalValue(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}