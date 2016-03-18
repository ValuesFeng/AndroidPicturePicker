package io.valuesfeng.picker.engine;

import android.os.Parcelable;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.Serializable;

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
public interface LoadEngine extends Parcelable {
    String INITIALIZE_ENGINE_ERROR = "initialize error,image load engine can not be null";

    void displayCameraItem(ImageView imageView);

    void displayImage(String path, ImageView imageView);

    void scrolling(GridView view);
}