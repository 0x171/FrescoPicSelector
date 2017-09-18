package com.xperfect.tt;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xperfect.tt.lib.activity.PictureSelectorActivity_;
import com.xperfect.tt.lib.adapter.ImageSelectAdapter;
import com.xperfect.tt.lib.permition.MPermission;
import com.xperfect.tt.lib.permition.annotation.OnMPermissionDenied;
import com.xperfect.tt.lib.permition.annotation.OnMPermissionGranted;
import com.xperfect.tt.lib.permition.annotation.OnMPermissionNeverAskAgain;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.a_main)
public class MainActivity extends AppCompatActivity {

    private final int BASIC_PERMISSION_REQUEST_CODE = 100;

    @ViewById(R.id.btn_pic_select)
    Button btnPicSelect;

    @AfterViews
    public void afterViews() {
        requestBasicPermission();
    }

    @Click(R.id.btn_pic_select)
    public void clickEvents(View view) {
        switch (view.getId()) {
            case R.id.btn_pic_select:
                startActivityForResult(
                        new Intent(MainActivity.this, PictureSelectorActivity_.class),
                        ImageSelectAdapter.SELECT_PHOTO_REQUEST_CODE
                );
                break;
        }
    }

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }


}