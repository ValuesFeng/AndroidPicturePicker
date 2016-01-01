package io.valuesfeng.picker.engine.imageloader;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import io.valuesfeng.picker.R;
import io.valuesfeng.picker.engine.LoadEngine;

/**
 * Author:    valuesfeng
 * Version    V1.0
 * Date:      16/1/1
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 16/1/1          valuesfeng              1.0                    1.0
 * Why & What is modified:
 */
public class ImageLoaderEngine implements LoadEngine {

    private int img_loading;
    private int camera_loading;
    private DisplayImageOptions displayOptions;
    private DisplayImageOptions cameraOptions;


    public ImageLoaderEngine() {
        this(0, 0);
    }

    public ImageLoaderEngine(int img_loading, int camera_loading) {
        if (ImageLoader.getInstance() == null) {
            throw new ExceptionInInitializerError(INITIALIZE_ENGINE_ERROR);
        }
        if (img_loading == 0)
            this.img_loading = R.drawable.image_not_exist;
        else
            this.img_loading = img_loading;
        if (camera_loading == 0)
            this.camera_loading = R.drawable.ic_camera;
        else
            this.camera_loading = camera_loading;
    }

    @Override
    public void displayImage(String path, ImageView imageView) {
        ImageLoader.getInstance().displayImage(path, imageView, getPathImageOptions());
    }

    @Override
    public void displayImage(int res, ImageView imageView) {
        ImageLoader.getInstance().displayImage("drawable://" + res, imageView, getCameraOptions());
    }

    @Override
    public void pauseOnScroll(GridView view) {
        view.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
    }

    private DisplayImageOptions getPathImageOptions() {

        if (displayOptions == null)
            displayOptions = new DisplayImageOptions
                    .Builder()
                    .showImageOnLoading(img_loading)
                    .showImageForEmptyUri(img_loading)
                    .showImageOnFail(img_loading)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();

        return displayOptions;
    }

    private DisplayImageOptions getCameraOptions() {

        if (cameraOptions == null)
            cameraOptions = new DisplayImageOptions
                    .Builder()
                    .showImageOnLoading(camera_loading)
                    .showImageForEmptyUri(camera_loading)
                    .showImageOnFail(camera_loading)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();
        return cameraOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.img_loading);
        dest.writeInt(this.camera_loading);
    }

    protected ImageLoaderEngine(Parcel in) {
        this.img_loading = in.readInt();
        this.camera_loading = in.readInt();
    }

    public static final Creator<ImageLoaderEngine> CREATOR = new Creator<ImageLoaderEngine>() {
        public ImageLoaderEngine createFromParcel(Parcel source) {
            return new ImageLoaderEngine(source);
        }

        public ImageLoaderEngine[] newArray(int size) {
            return new ImageLoaderEngine[size];
        }
    };
}
