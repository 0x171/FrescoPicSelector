package com.xperfect.tt.lib.item;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xperfect.tt.lib.model.FolderBean;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.io.File;

/**
 * Created by Kyle on 2016/10/13.
 */
@EViewGroup(resName = "i_image_folder")
public class FolderItem extends RelativeLayout implements View.OnClickListener {

    @ViewById(resName = ".sdv_cover")
    public SimpleDraweeView sdv_cover;
    @ViewById(resName = ".ll_img_folder")
    public LinearLayout ll_img_folder;
    @ViewById(resName = ".tv_name")
    public TextView tv_name;
    @ViewById(resName = ".tv_size")
    public TextView tv_size;
    @ViewById(resName = ".iv_indicator")
    public ImageView iv_indicator;

    private boolean isSelected = false;
    private int position = 0;

    OnFolderItemClickListener onFolderItemClickListener;

    public OnFolderItemClickListener getOnFolderItemClickListener() {
        return onFolderItemClickListener;
    }

    public void setOnFolderItemClickListener(OnFolderItemClickListener onFolderItemClickListener) {
        this.onFolderItemClickListener = onFolderItemClickListener;
    }

    public FolderItem(Context context) {
        super(context);
    }

    public FolderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FolderItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(FolderBean folderBean, int position ){
        this.position = position;
        if( folderBean.cover.imagePath.endsWith(".gif") || folderBean.cover.imagePath.endsWith(".GIF") ){
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(sdv_cover.getController())
                    //允许播放GIF
                    .setAutoPlayAnimations(true)
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            sdv_cover.setController(controller);
        } else {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource( Uri.fromFile( new File( folderBean.cover.imagePath ) ) )
                    .setResizeOptions( new ResizeOptions( 300, 300) )
                    .setAutoRotateEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(sdv_cover.getController())
                    //允许播放GIF
                    .setAutoPlayAnimations(true)
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            sdv_cover.setController(controller);
        }
        tv_name.setText( folderBean.name );
        tv_size.setText( folderBean.images.size() + "" );
        isSelected = folderBean.isSelected;
        if( isSelected ){
            iv_indicator.setImageResource( R.drawable.folder_select );
        } else {
            iv_indicator.setImageResource( R.drawable.folder_unselect );
        }
        ///
        sdv_cover.setOnClickListener( this );
        ll_img_folder.setOnClickListener( this );
        tv_name.setOnClickListener( this );
        tv_size.setOnClickListener( this );
        iv_indicator.setOnClickListener( this );
    }

    @Override
    public void onClick(View view) {
        if(
                (
                        view.getId() == R.id.sdv_cover
                                ||
                                view.getId() == R.id.ll_img_folder
                                ||
                                view.getId() == R.id.tv_name
                                ||
                                view.getId() == R.id.tv_size
                                ||
                                view.getId() == R.id.iv_indicator
                )
                        &&
                        onFolderItemClickListener != null
                ){
            isSelected = !isSelected;
            onFolderItemClickListener.onItemClick( isSelected, this.position );
        }
    }

    public interface OnFolderItemClickListener {
        void onItemClick(boolean isSelected, int position);
    }

}