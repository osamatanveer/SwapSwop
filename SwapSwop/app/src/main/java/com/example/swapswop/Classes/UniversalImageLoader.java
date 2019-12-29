package com.example.swapswop.Classes;

import android.content.Context;
import android.widget.ImageView;

import com.example.swapswop.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * This class is used to upload images to the database.
 * @author Osama Tanveer
 * @version 8 May 2019
 */

public class UniversalImageLoader {
    private static final int defaultImage = R.drawable.ic_android;
    private Context mContext;

    /**
     * A constructor for the class
     * @param context the context of the activity
     */
    public UniversalImageLoader(Context context) {
        mContext = context;
    }

    /**
     * This method sets the image loader's configuration. It sets the process of selecting picture.
     * @return configuration the configuration of the image
     */
    public ImageLoaderConfiguration getConfig(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .considerExifParams(true)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        return configuration;
    }

    /**
     * This method sets the image.
     * @param imgURL the image url of the image
     * @param image the image
     */
    public static void setImage(String imgURL, ImageView image){

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imgURL, image);
    }
}

