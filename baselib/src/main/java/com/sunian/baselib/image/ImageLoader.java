package com.sunian.baselib.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;


/**
 * Created by fujun on 2018/4/4.
 * 图片加载的数
 */

public class ImageLoader {
    public static void load(@NonNull Context context, @NonNull ImageView view, @NonNull String url, boolean isStore, @Nullable RounderCorners roundedCorners) {
        Glide.with(context).load(url).apply(getRequestOptions(isStore, false, roundedCorners)).into(view);
    }

    public static void load(@NonNull Context context, @NonNull ImageView view, @NonNull String url) {
        load(context, view, url, true, null);
    }

    public static void load(@NonNull Context context, @NonNull ImageView view, @NonNull String url, RounderCorners rounderCorners) {
        load(context, view, url, true, rounderCorners);
    }


    public static void load(@NonNull Context context, @NonNull ImageView view, @NonNull String url, @Nullable String thumbnail, boolean isStore, RounderCorners rounderCorners, int error, int placeholder) {
        RequestBuilder<Drawable> requestBuilder = null;
        if (thumbnail != null) {
            requestBuilder = Glide.with(context).load(thumbnail).apply(getRequestOptions(true, false, null));
        }
        RequestOptions requestOptions = getRequestOptions(isStore, false, rounderCorners);
        if (error != -1)
            requestOptions.error(error);
        if (placeholder != -1)
            requestOptions.placeholder(placeholder);
        if (requestBuilder != null)
            Glide.with(context).load(url).thumbnail(requestBuilder).apply(requestOptions).into(view);
        else
            Glide.with(context).load(url).apply(requestOptions).into(view);
    }

    public static void load(@NonNull Context context, @NonNull ImageView view, @NonNull String url, boolean isStore, RounderCorners rounderCorners, int error, int placeholder) {
        load(context, view, url, null, isStore, rounderCorners, error, placeholder);
    }

    //===============================================================
    public static void load(@NonNull Activity context, @NonNull ImageView view, @NonNull String url, boolean isStore, @Nullable RounderCorners rounderCorners) {
        Glide.with(context).load(url).apply(getRequestOptions(isStore, false, rounderCorners)).into(view);
    }

    public static void load(@NonNull Activity context, @NonNull ImageView view, @NonNull String url) {
        load(context, view, url, true, null);
    }

    public static void load(@NonNull Activity context, @NonNull ImageView view, @NonNull String url, RounderCorners rounderCorners) {
        load(context, view, url, true, rounderCorners);
    }

    //=================================================================================
    public static void load(@NonNull Fragment context, @NonNull ImageView view, @NonNull String url, boolean isStore, @Nullable RounderCorners rounderCorners) {
        Glide.with(context).load(url).apply(getRequestOptions(isStore, false, rounderCorners)).into(view);
    }

    //=================================================================================
    public static void load(@NonNull AppCompatActivity context, ImageView view, String url, boolean isStore) {
        Glide.with(context).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(view);
    }

    public static void load(@NonNull AppCompatActivity context, @NonNull ImageView view, @NonNull String url) {
        load(context, view, url, true, null);
    }

    public static void load(@NonNull AppCompatActivity context, @NonNull ImageView view, @NonNull String url, RounderCorners rounderCorners) {
        load(context, view, url, true, rounderCorners);
    }

    //=================================================================================
    public static void loadCircle(@NonNull Context context, @NonNull ImageView imageView, @NonNull String url, boolean isStore) {
        Glide.with(context).load(url).apply(getRequestOptions(isStore, true, null)).into(imageView);
    }

    public static void loadCircle(@NonNull Context context, @NonNull ImageView imageView, @NonNull String url) {
        loadCircle(context, imageView, url, true);
    }

    public static void loadCircle(@NonNull Activity context, @NonNull ImageView imageView, @NonNull String url, boolean isStore) {
        Glide.with(context).load(url).apply(getRequestOptions(isStore, true, null)).into(imageView);
    }

    public static void loadCircle(@NonNull Activity context, @NonNull ImageView imageView, @NonNull String url) {
        loadCircle(context, imageView, url, true);
    }

    public static void loadCircle(@NonNull Fragment context, @NonNull ImageView imageView, @NonNull String url, boolean isStore) {
        Glide.with(context).load(url).apply(getRequestOptions(isStore, true, null)).into(imageView);
    }

    public static void loadCircle(@NonNull Fragment context, @NonNull ImageView imageView, @NonNull String url) {
        loadCircle(context, imageView, url, true);
    }

    public static void loadCircle(@NonNull AppCompatActivity context, @NonNull ImageView imageView, @NonNull String url, boolean isStore) {
        Glide.with(context).load(url).apply(getRequestOptions(isStore, true, null)).into(imageView);
    }

    public static void loadCircle(@NonNull AppCompatActivity context, @NonNull ImageView imageView, @NonNull String url) {
        loadCircle(context, imageView, url, true);
    }


    public static void loadCircle(@NonNull Context context, @NonNull ImageView view, @NonNull String url, @Nullable String thumbnail, boolean isStore, int error) {
        RequestBuilder<Drawable> requestBuilder = null;
        if (thumbnail != null) {
            requestBuilder = Glide.with(context).load(thumbnail).apply(getRequestOptions(true, false, null));
        }
        RequestOptions requestOptions = getRequestOptions(isStore, true, null);
        if (error != -1) {
            requestOptions.error(error);
            requestOptions.placeholder(error);
        }

        if (requestBuilder != null)
            Glide.with(context).load(url).thumbnail(requestBuilder).apply(requestOptions).into(view);
        else
            Glide.with(context).load(url).apply(requestOptions).into(view);
    }

    //======================================================================================
    @SuppressLint("CheckResult")
    public static RequestOptions getRequestOptions(boolean isStore, boolean isCircle, RounderCorners rounderCorners) {
        RequestOptions requestOptions = new RequestOptions();
        if (isStore)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        else
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        if (isCircle) {
            requestOptions.transform(new CircleCrop());
        } else if (rounderCorners != null) {
            requestOptions.transform(new RoundedCornersTransformation(rounderCorners.radius, rounderCorners.margin, rounderCorners.cornerType));
        }
        return requestOptions;

    }

    public static class RounderCorners {
        public int radius;
        public int margin;
        public RoundedCornersTransformation.CornerType cornerType;

        public RounderCorners setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public RounderCorners setMargin(int margin) {
            this.margin = margin;
            return this;
        }

        public RounderCorners setCornerType(RoundedCornersTransformation.CornerType cornerType) {
            this.cornerType = cornerType;
            return this;
        }
    }

}
