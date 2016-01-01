package io.valuesfeng.picker.engine.glide;

import android.content.Context;
import android.os.Parcel;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
public class GlideEngine implements LoadEngine {

    private int img_loading;
    private int camera_loading;

    public GlideEngine() {
        this(0, 0);
    }

    public GlideEngine(int camera_loading, int img_loading) {
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
        chargeInit(imageView.getContext());
        Glide.with(imageView.getContext())
                .load(path)
                .centerCrop()
                .error(img_loading)
                .placeholder(img_loading)
                .crossFade()
                .into(imageView);
    }

    @Override
    public void displayImage(int res, ImageView imageView) {
        chargeInit(imageView.getContext());
        Glide.with(imageView.getContext())
                .load(res)
                .centerCrop()
                .error(camera_loading)
                .placeholder(camera_loading)
                .crossFade()
                .into(imageView);
    }

    private void chargeInit(Context context){
        if (Glide.get(context) == null) {
            throw new ExceptionInInitializerError(INITIALIZE_ENGINE_ERROR);
        }
    }
    @Override
    public void pauseOnScroll(GridView view) {

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

    protected GlideEngine(Parcel in) {
        this.img_loading = in.readInt();
        this.camera_loading = in.readInt();
    }

    public static final Creator<GlideEngine> CREATOR = new Creator<GlideEngine>() {
        public GlideEngine createFromParcel(Parcel source) {
            return new GlideEngine(source);
        }

        public GlideEngine[] newArray(int size) {
            return new GlideEngine[size];
        }
    };
}
