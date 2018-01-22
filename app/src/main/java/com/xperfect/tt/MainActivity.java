package com.xperfect.tt;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xperfect.tt.lib.activity.PictureSelectorActivity_;
import com.xperfect.tt.lib.adapter.ImageSelectAdapter;
import com.xperfect.tt.lib.permition.MPermission;
import com.xperfect.tt.lib.permition.annotation.OnMPermissionDenied;
import com.xperfect.tt.lib.permition.annotation.OnMPermissionGranted;
import com.xperfect.tt.lib.permition.annotation.OnMPermissionNeverAskAgain;
import com.xperfect.tt.lib.sim.SimCardUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.a_main)
public class MainActivity extends AppCompatActivity {

    private final int BASIC_PERMISSION_REQUEST_CODE = 100;

    @ViewById(R.id.btn_pic_select)
    Button btnPicSelect;
    @ViewById(R.id.btn_sim)
    Button btnSim;

    @AfterViews
    public void afterViews() {
        requestBasicPermission();
    }

    @Click({R.id.btn_pic_select, R.id.btn_sim})
    public void clickEvents(View view) {
        switch (view.getId()) {
            case R.id.btn_pic_select:
                startActivityForResult(
                        new Intent(MainActivity.this, PictureSelectorActivity_.class),
                        ImageSelectAdapter.SELECT_PHOTO_REQUEST_CODE
                );
                break;
            case R.id.btn_sim:
//                displaySimInfo();
                Toast.makeText(this, "你有 " + SimCardUtils.getSimCardCount(this) + " 张SIM卡", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void displaySimInfo() {
        Uri uri = Uri.parse("content://telephony/siminfo");
        Cursor cursor = null;
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        cursor = contentResolver.query(uri,
                new String[]{"_id", "sim_id", "icc_id", "display_name"}, "0=0",
                new String[]{}, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                String icc_id = cursor.getString(cursor.getColumnIndex("icc_id"));
                String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
                int sim_id = cursor.getInt(cursor.getColumnIndex("sim_id"));
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                Log.e("Q_M", "icc_id-->" + icc_id);
                Log.e("Q_M", "sim_id-->" + sim_id);
                Log.e("Q_M", "display_name-->" + display_name);
                Log.e("Q_M", "subId或者说是_id->" + _id);
                Log.e("Q_M", "---------------------------------");
            }
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