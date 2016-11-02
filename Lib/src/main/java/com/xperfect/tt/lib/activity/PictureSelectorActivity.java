package com.xperfect.tt.lib.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xperfect.tt.lib.R;
import com.xperfect.tt.lib.adapter.FolderSelectAdapter;
import com.xperfect.tt.lib.adapter.ImageSelectAdapter;
import com.xperfect.tt.lib.item.ImageItem;
import com.xperfect.tt.lib.model.FolderBean;
import com.xperfect.tt.lib.model.ImageBean;
import com.xperfect.tt.lib.widget.FrescoRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 2016/11/1.
 */
@EActivity(resName = "a_pic_selector")
public class PictureSelectorActivity
    extends AppCompatActivity
    implements
    NavigationView.OnNavigationItemSelectedListener,
    FolderSelectAdapter.OnFolderSelectListener,
    ImageItem.OnItemSelectListener {

        // 不同loader定义
        private static final int LOADER_ALL = 0;
        private static final int LOADER_CATEGORY = 1;

        @ViewById(resName = "tv_nav_take_photo")
        TextView tv_nav_take_photo;
        @ViewById(resName = "tv_nav_gallery")
        TextView tv_nav_gallery;
        @ViewById(resName = "toolbar")
        Toolbar toolbar;
        @ViewById(resName = "drawer_layout")
        DrawerLayout drawer_layout;
        @ViewById(resName = "nav_view")
        NavigationView navigationView;
        @ViewById(resName = "rcv_img_select")
        FrescoRecyclerView rcv_img_select;
        @ViewById(resName = "rcv_img_folder_recycler")
        FrescoRecyclerView rcv_img_folder_recycler;
        @ViewById(resName = "tv_img_selected_folder")
        TextView tv_img_selected_folder;
        @ViewById(resName = "ll_img_select_folder")
        LinearLayout ll_img_select_folder;

        @Bean
        ImageSelectAdapter imageSelectAdapter;
        @Bean
        FolderSelectAdapter folderSelectAdapter;

        // 文件夹数据(图片文件夹数据)
        private ArrayList<FolderBean> resultFolderList = new ArrayList<>();

        ActionBarDrawerToggle toggle;

        private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media._ID};

            private List<String> storePath = new ArrayList<>();

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                if (id == LOADER_ALL) {
                    CursorLoader cursorLoader =
                            new CursorLoader(
                                    PictureSelectorActivity.this,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                                    null, null, IMAGE_PROJECTION[2] + " DESC"
                            );
                    return cursorLoader;
                } else if (id == LOADER_CATEGORY) {
                    CursorLoader cursorLoader =
                            new CursorLoader(
                                    PictureSelectorActivity.this,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                                    IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'",
                                    null,
                                    IMAGE_PROJECTION[2] + " DESC"
                            );
                    return cursorLoader;
                }
                return null;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if ( data != null ) {
                    int count = data.getCount();
                    if (count > 0) {
                        resultFolderList.clear();
                        //
                        data.moveToFirst();

                        File imageFile;
                        FolderBean folder;
                        File folderFile;
                        //
                        ImageBean imageBean;
                        do {
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                            long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                            // 获取文件夹名称
                            imageFile = new File(path);
                            folder = new FolderBean();
                            folderFile = imageFile.getParentFile();
                            //
                            imageBean = new ImageBean(name, path, dateTime);
                            if( folderFile != null ){
                                folder.name = folderFile.getName();
                                folder.path = folderFile.getAbsolutePath();
                                folder.cover = imageBean;
                                if( storePath.size() == 0 ){
                                    ArrayList<ImageBean> imageList = new ArrayList<>();
                                    imageList.add(imageBean);
                                    folder.images = imageList;
                                    resultFolderList.add(folder);
                                    storePath.add( folder.path );
                                } else {
                                    if ( !storePath.contains( folder.path ) ) {//新目录
                                        ArrayList<ImageBean> imageList = new ArrayList<>();
                                        imageList.add(imageBean);
                                        folder.images = imageList;
                                        resultFolderList.add(folder);
                                        storePath.add( folder.path );
                                    } else {
                                        //添加
                                        if( resultFolderList.size() > 0 && storePath.size() > 0 ){
                                            resultFolderList.get( storePath.indexOf( folder.path ) ).images.add(imageBean);
                                        }
                                    }
                                }
                            }
                        } while (data.moveToNext());
                        for( FolderBean folderBean:resultFolderList ){
                            imageSelectAdapter.setItemsAddAll( folderBean.images );
                        }
                        imageSelectAdapter.notifyDataSetChanged();
                        //
                        folderSelectAdapter.setItems( resultFolderList );
                        folderSelectAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }

        };


        @AfterViews
        public void afterViews(){
            toolbar.setTitle( getResources().getString( R.string.string_pic_select_title ) );
            setSupportActionBar(toolbar);
            toggle =
                    new ActionBarDrawerToggle(
                            this,
                            drawer_layout,
                            toolbar,
                            R.string.string_pic_select_title,
                            R.string.string_pic_select_title
                    ){
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            invalidateOptionsMenu();
                        }

                        @Override
                        public void onDrawerOpened(View drawerView) {
                            invalidateOptionsMenu();
                        }
                    };
            toggle.syncState();
            drawer_layout.setDrawerListener(toggle);
            navigationView.setNavigationItemSelectedListener(this);
            ////
            imageSelectAdapter.addHeader();
            imageSelectAdapter.setOnItemSelectListener( this );
            rcv_img_select.setAdapter( imageSelectAdapter );
            GridLayoutManager layoutManager = new GridLayoutManager( PictureSelectorActivity.this, 3 );
            rcv_img_select.setLayoutManager( layoutManager );
            ///
            rcv_img_folder_recycler.setAdapter( folderSelectAdapter );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( PictureSelectorActivity.this );
            rcv_img_folder_recycler.setLayoutManager( linearLayoutManager );
            ///
            folderSelectAdapter.setOnFolderSelectListener( this );
        }

        @Override
        public void onResume(){
            ///
            getSupportLoaderManager().initLoader(LOADER_ALL, null, cursorLoaderCallbacks);
            super.onResume();
        }


        @Click(resName = {
                "tv_nav_take_photo",
                "tv_nav_gallery",
                "ll_img_select_folder",
                "tv_img_selected_folder",
                "tv_img_selected_preview"
        })
        public void clickEvents( View view ){
            if( view.getId() == R.id.tv_nav_take_photo ){
                Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentTakePhoto.putExtra( MediaStore.EXTRA_OUTPUT, true );
                startActivityForResult(intentTakePhoto, ImageSelectAdapter.TAKE_PHOTO_REQUEST_CODE);
            } else if( view.getId() == R.id.tv_nav_gallery ){
                Intent intentGallery = new Intent(Intent.ACTION_PICK);
                intentGallery.setType("image/*");
                //存在多个相册类型的应用时,显示给用户选择的打个相册的应用界面
                startActivityForResult(Intent.createChooser(intentGallery, "请选择"), ImageSelectAdapter.SELECT_PHOTO_REQUEST_CODE);
            } else if( view.getId() == R.id.ll_img_select_folder || view.getId() == R.id.tv_img_selected_folder ){
                if( !drawer_layout.isDrawerOpen( GravityCompat.START ) ){
                    drawer_layout.openDrawer( GravityCompat.START );
                } else {
                    drawer_layout.closeDrawer( GravityCompat.START );
                }
            } else if( view.getId() == R.id.tv_img_selected_preview ){
            }
        }

        @Override
        public void onBackPressed() {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (toggle.onOptionsItemSelected(item)) {
                return true;
            }
//        if (id == R.id.action_settings) {
//            return true;
//        }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onFolderSelect(ArrayList<FolderBean> selectedFolders) {
            if( selectedFolders.size() == 0 ){
                imageSelectAdapter.getItems().clear();
                for( FolderBean folderBean:resultFolderList ){
                    imageSelectAdapter.setItemsAddAll( folderBean.images );
                }
                imageSelectAdapter.notifyDataSetChanged();
                tv_img_selected_folder.setText("全部选择");
            } else {
                imageSelectAdapter.getItems().clear();
                for( FolderBean folderBean:selectedFolders ){
                    imageSelectAdapter.setItemsAddAll( folderBean.images );
                }
                imageSelectAdapter.notifyDataSetChanged();
                tv_img_selected_folder.setText( "已选择" + selectedFolders.size() + "个文件夹" );
            }
        }

        @Override
        public void itemSelect(boolean isSelected, int position) {
            Log.e("itemSelect","itemSelect  " + position);
            if( position == 0 ){
                Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentTakePhoto.putExtra( MediaStore.EXTRA_OUTPUT, true );
                startActivityForResult(intentTakePhoto, ImageSelectAdapter.TAKE_PHOTO_REQUEST_CODE);
            }
        }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

}