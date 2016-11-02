package com.xperfect.tt.lib.config;

import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

/**
 * Created by Kyle on 2016/11/1.
 */

public class FrescoDefaultConfig {

    public static void defaultConfig( final Context context ){
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                30 * ByteConstants.MB,// 内存缓存中总图片的最大大小,以字节为单位。
                30,// 内存缓存中图片的最大数量。
                10 * ByteConstants.MB,// 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,// 内存缓存中准备清除的总图片的最大数量。
                5 * ByteConstants.MB);// 内存缓存中单个图片的最大大小。
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };
        DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPathSupplier(
                        new Supplier<File>() {
                            @Override
                            public File get() {
                                return context.getCacheDir();
                            }
                        })
                .setBaseDirectoryName("image_cache")
                .setMaxCacheSize(40 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(2 * ByteConstants.MB)
                .build();
        //fresco初始化
        ImagePipelineConfig config =
                ImagePipelineConfig
                        .newBuilder(context)
                        .setBitmapMemoryCacheParamsSupplier( mSupplierMemoryCacheParams )
                        .setDownsampleEnabled(true)
                        .build();
        Fresco.initialize(context,config);
    }

}