package com.xperfect.tt.lib.item;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.xperfect.tt.lib.R;
import com.xperfect.tt.lib.model.ImageBean;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kyle on 2016/10/13.
 */
@EViewGroup(resName = "i_album")
public class ImageItem extends RelativeLayout implements View.OnClickListener {

    @ViewById(resName = "fl_i_album_image")
    FrameLayout fl_i_album_image;
    @ViewById(resName = "sdv_i_album_image")
    public SimpleDraweeView sdv_i_album_image;
    @ViewById(resName = "v_i_album_mask")
    public View v_i_album_mask;
    @ViewById(resName = "iv_i_album_text_bg")
    public ImageView iv_i_album_text_bg;
    @ViewById(resName = "tv_i_album_text")
    public TextView tv_i_album_text;

    private boolean isSelected = false;
    private int position = 0;

    OnItemSelectListener onItemSelectListener;

    public OnItemSelectListener getOnItemSelectListener() {
        return onItemSelectListener;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public ImageItem(Context context) {
        super(context);
    }

    public ImageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(ImageBean imageBean, int position, ArrayList<ImageBean> selectedList ){
        if( imageBean.imagePath.endsWith(".gif") || imageBean.imagePath.endsWith(".GIF") ){
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setOldController(sdv_i_album_image.getController())
//                    //允许播放GIF
//                    .setAutoPlayAnimations(true)
//                    .build();
//            sdv_i_album_image.setController(controller);
            //
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource( Uri.fromFile( new File( imageBean.imagePath ) ) )
                    .setResizeOptions( new ResizeOptions( 300, 300) )
                    .setAutoRotateEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(sdv_i_album_image.getController())
                    //允许播放GIF
                    .setAutoPlayAnimations(true)
                    .build();
            sdv_i_album_image.setController(controller);
            sdv_i_album_image.setAspectRatio(1.0f);
        } else {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource( Uri.fromFile( new File( imageBean.imagePath ) ) )
                    .setResizeOptions( new ResizeOptions( 300, 300) )
                    .setAutoRotateEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(sdv_i_album_image.getController())
                    //允许播放GIF
                    .setAutoPlayAnimations(true)
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            sdv_i_album_image.setController(controller);
            sdv_i_album_image.setAspectRatio(1.0f);
        }
        isSelected = imageBean.isSelected;
        this.position = position;
        if( isSelected ){
            v_i_album_mask.setVisibility(VISIBLE);
            tv_i_album_text.setVisibility(VISIBLE);
            if( selectedList.contains( imageBean ) ){
                tv_i_album_text.setText( ( selectedList.indexOf( imageBean ) + 1 ) +  "" );
            }
            iv_i_album_text_bg.setBackgroundResource( R.drawable.img_select );
        } else {
            v_i_album_mask.setVisibility(GONE);
            tv_i_album_text.setVisibility(GONE);
            iv_i_album_text_bg.setBackgroundResource( R.drawable.img_unselect );
        }
        fl_i_album_image.setOnClickListener( this );
        sdv_i_album_image.setOnClickListener( this );
        v_i_album_mask.setOnClickListener( this );
        iv_i_album_text_bg.setOnClickListener( this );
        tv_i_album_text.setOnClickListener( this );

    }

    @Override
    public void onClick(View view) {
        if(
                view.getId() == R.id.fl_i_album_image
                        ||
                        view.getId() == R.id.sdv_i_album_image
                        ||
                        view.getId() == R.id.v_i_album_mask
                        ||
                        view.getId() == R.id.iv_i_album_text_bg
                        ||
                        view.getId() == R.id.tv_i_album_text
                ){
            isSelected = !isSelected;
            if( onItemSelectListener != null ){
                onItemSelectListener.itemSelect( isSelected, position );
            }
        }
    }

    public interface OnItemSelectListener {
        void itemSelect(boolean isSelected, int position);
    }

}