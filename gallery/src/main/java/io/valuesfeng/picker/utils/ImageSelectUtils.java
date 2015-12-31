package io.valuesfeng.picker.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.widget.CheckBox;
import android.widget.Toast;

import io.valuesfeng.picker.model.Item;
import io.valuesfeng.picker.model.SelectedUriCollection;

/**
 * Created by zaiyong on 2015/6/6.
 */
public class ImageSelectUtils {


    private ImageSelectUtils() {
    }


    public static void syncCheckState(Context context, SelectedUriCollection collection, Item item, CheckBox checkBox) {
        Uri uri = item.buildContentUri();
        if (collection.isSelected(uri)) {
            removeSelection(collection, uri, checkBox);
        } else {
            addSelection(context, collection, uri, checkBox);
        }
    }

    public static void removeSelection(SelectedUriCollection collection, Uri uri, CheckBox checkBox) {
        collection.remove(uri);
        checkBox.setChecked(false);
    }

    public static void addSelection(Context context, SelectedUriCollection collection, Uri uri, CheckBox checkBox) {
        collection.add(uri);
        if (collection.isCountOver()) {
            Toast.makeText(context,"图库满了,不能在选择图片了",Toast.LENGTH_LONG).show();
            collection.remove(uri);
            checkBox.setChecked(false);
            return;
        }
    }


}
