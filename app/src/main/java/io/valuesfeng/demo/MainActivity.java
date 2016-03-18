package io.valuesfeng.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import io.valuesfeng.picker.MimeType;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.engine.glide.GlideEngine;
import io.valuesfeng.picker.engine.imageloader.ImageLoaderEngine;
import io.valuesfeng.picker.engine.picasso.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

public class MainActivity extends FragmentActivity {
    public static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                Log.i("picture", u.getPath());
            }
        }
    }

    //    Log.i("picture", path);
    public void onClickButton(View view) {
        Picker.from(this,MimeType.of(MimeType.JPEG,MimeType.PNG)).count(0, 3)
                .setEnableCamera(true)
                .setEngine(new PicassoEngine())
//                .setEngine(new ImageLoaderEngine())
//                .setEngine(new GlideEngine())
//                .setEngine(new CustomEngine())
                .forResult(1);
    }

    static class CustomEngine implements LoadEngine {
        @Override
        public void displayImage(String path, ImageView imageView) {
            Log.i("picture", path);
        }

        @Override
        public void displayCameraItem(ImageView imageView) {

        }

        @Override
        public void scrolling(GridView view) {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        public CustomEngine() {
        }

        protected CustomEngine(Parcel in) {
        }

        public static final Creator<CustomEngine> CREATOR = new Creator<CustomEngine>() {
            public CustomEngine createFromParcel(Parcel source) {
                return new CustomEngine(source);
            }

            public CustomEngine[] newArray(int size) {
                return new CustomEngine[size];
            }
        };
    }
}
