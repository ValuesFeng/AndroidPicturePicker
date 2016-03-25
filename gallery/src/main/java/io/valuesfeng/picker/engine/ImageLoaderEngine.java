package io.valuesfeng.picker.engine;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import io.valuesfeng.picker.R;

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
    private int img_camera;
    private DisplayImageOptions displayOptions;
    private DisplayImageOptions cameraOptions;


    public ImageLoaderEngine() {
        this(0, 0);
    }

    public ImageLoaderEngine(int img_loading) {
        this(img_loading,0);
    }

    public ImageLoaderEngine(int img_loading, int img_camera) {
        if (ImageLoader.getInstance() == null) {
            throw new ExceptionInInitializerError(INITIALIZE_ENGINE_ERROR);
        }
        if (img_loading == 0)
            this.img_loading = R.drawable.image_not_exist;
        else
            this.img_loading = img_loading;
        if (img_camera == 0)
            this.img_camera = R.drawable.ic_camera;
        else
            this.img_camera = img_camera;
    }

    @Override
    public void displayImage(String path, ImageView imageView) {
        ImageLoader.getInstance().displayImage(path, imageView, getPathImageOptions());
    }

    @Override
    public void displayCameraItem(ImageView imageView) {
        ImageLoader.getInstance().displayImage("drawable://" + img_camera, imageView, getCameraOptions());
    }

    @Override
    public void scrolling(GridView view) {
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
                    .showImageOnLoading(img_camera)
                    .showImageForEmptyUri(img_camera)
                    .showImageOnFail(img_camera)
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
        dest.writeInt(this.img_camera);
    }

    protected ImageLoaderEngine(Parcel in) {
        this.img_loading = in.readInt();
        this.img_camera = in.readInt();
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
