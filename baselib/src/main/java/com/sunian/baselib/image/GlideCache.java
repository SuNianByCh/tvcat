package com.sunian.baselib.image;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by fujun on 2018/4/8.
 */
@GlideModule
public class GlideCache extends AppGlideModule {
    //外部路径
    private String sdRootPath = Environment.getExternalStorageDirectory().getPath();
    private String appRootPath = null;

    private String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                sdRootPath : appRootPath;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        //手机app路径
        appRootPath = context.getCacheDir().getPath();
        builder.setDiskCache(
                new DiskLruCacheFactory(getStorageDirectory() + "/GlideDisk", diskCacheSizeBytes)
        );
        super.applyOptions(context, builder);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
    }
}
