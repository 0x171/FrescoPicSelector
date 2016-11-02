package com.xperfect.tt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xperfect.tt.lib.activity.PictureSelectorActivity_;
import com.xperfect.tt.lib.adapter.ImageSelectAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.a_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.btn_pic_select)
    Button btnPicSelect;

    @AfterViews
    public void afterViews(){

    }

    @Click(R.id.btn_pic_select)
    public void clickEvents( View view ){
        switch( view.getId() ){
            case R.id.btn_pic_select:
                startActivityForResult(
                        new Intent(MainActivity.this, PictureSelectorActivity_.class),
                        ImageSelectAdapter.SELECT_PHOTO_REQUEST_CODE
                        );
                break;
        }
    }


}