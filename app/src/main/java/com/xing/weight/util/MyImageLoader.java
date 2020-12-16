package com.xing.weight.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.xing.weight.CPApp;
import com.xing.weight.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author 星哥的
 */
public class MyImageLoader {

    public static void loadHead(Context c, String url, ImageView imageView) {
        Glide.with(c).load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
    }

    /**
     * @param c         skipMemoryCache(true) , recyclerView 局部刷新时会闪一下
     * @param fullPath
     * @param imageView
     */
    public static void loadImageNoMemoryCache(Context c, String fullPath, ImageView imageView) {
        Glide.with(c).load(fullPath).error(R.mipmap.ic_launcher).dontAnimate().skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    public static void loadImageNoCache(Context c, String fullPath, ImageView imageView) {
        Glide.with(c).load(fullPath).error(R.mipmap.ic_launcher).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    public static void loadImageNoCache(Context c, String fullPath, ImageView imageView, String sign) {
        Glide.with(c).load(fullPath).error(R.mipmap.ic_launcher).signature(new ObjectKey(sign)).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    public static void loadImage(Context c, String fullPath, ImageView imageView, int id) {
        Glide.with(c).load(fullPath).placeholder(id).error(id).dontAnimate().into(imageView);
    }

    public static void loadImage(Context c, Object path, ImageView imageView, int id) {
        Glide.with(c).load(path).placeholder(id).error(id).dontAnimate().into(imageView);
    }


    public static void clearMemory(Context c) {
        Glide.get(c).clearMemory();
    }

    public static void loadCusRoundImage(Context c, String fullPath, ImageView imageView, int id) {
        Glide.with(c).load(fullPath).transform(new RoundedCorners(Tools.dip2px(c,10))).placeholder(id).error(id).into(imageView);
    }

    public static void loadCusRoundImage(Context c, String fullPath, ImageView imageView, int id, int round) {
        Glide.with(c).load(fullPath).transform(new RoundedCorners(Tools.dip2px(c,round))).placeholder(id).error(id).into(imageView);
    }

    public static void downloadImg(Context context,String url){
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
            }
        });
    }

    public static boolean clearCacheDiskSelf() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(CPApp.getInstance()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(CPApp.getInstance()).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
