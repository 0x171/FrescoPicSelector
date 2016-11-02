package com.xperfect.tt;

import android.app.Application;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.xperfect.tt.lib.config.FrescoDefaultConfig;

import java.io.File;

/**
 * Created by Kyle on 2016/11/1.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //配置fresco
        FrescoDefaultConfig.defaultConfig( this );
    }

}