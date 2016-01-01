package io.valuesfeng.picker.engine.picasso;

import android.content.Context;
import android.os.Parcel;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
public class PicassoEngine implements LoadEngine {

    private int img_loading;
    private int camera_loading;

    public PicassoEngine() {
        this(0, 0);
    }

    public PicassoEngine(int camera_loading, int img_loading) {
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
        Picasso.with(imageView.getContext())
                .load(path)
                .placeholder(img_loading)
                .error(img_loading)
                .resize(imageView.getWidth(), imageView.getHeight())
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void displayImage(int res, ImageView imageView) {
        chargeInit(imageView.getContext());
        Picasso.with(imageView.getContext())
                .load(res)
                .placeholder(camera_loading)
                .error(camera_loading)
                .resize(imageView.getWidth(), imageView.getHeight())
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void pauseOnScroll(GridView view) {

    }


    private void chargeInit(Context context) {
        if (Picasso.with(context) == null) {
            throw new ExceptionInInitializerError(INITIALIZE_ENGINE_ERROR);
        }
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

    protected PicassoEngine(Parcel in) {
        this.img_loading = in.readInt();
        this.camera_loading = in.readInt();
    }

    public static final Creator<PicassoEngine> CREATOR = new Creator<PicassoEngine>() {
        public PicassoEngine createFromParcel(Parcel source) {
            return new PicassoEngine(source);
        }

        public PicassoEngine[] newArray(int size) {
            return new PicassoEngine[size];
        }
    };
}
