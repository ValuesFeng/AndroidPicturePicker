package io.valuesfeng.picker.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import io.valuesfeng.picker.ImageSelectActivity;

/**
 * Created by zaiyong on 2015/6/6.
 */
public class PicturePickerUtils {

    private PicturePickerUtils() {
    }

    /**
     * Obtains the selection result passed to your {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param data the data.
     * @return the selected {@link Uri}s.
     */
    public static List<Uri> obtainResult(Intent data) {
        return data.getParcelableArrayListExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION);
    }

    public static List<String> obtainResult(ContentResolver resolver, Intent data) {
        List<Uri> uris = data.getParcelableArrayListExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION);
        List<String> paths = new ArrayList<>();
        for (Uri uri : uris) {
            paths.add(PhotoMetadataUtils.getPath(resolver, uri));
        }
        return paths;
    }
}
