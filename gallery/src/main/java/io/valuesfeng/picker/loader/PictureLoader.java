/*
 * Copyright (C) 2014 nohana, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.valuesfeng.picker.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import io.valuesfeng.picker.model.Album;
import io.valuesfeng.picker.model.Picture;
import io.valuesfeng.picker.model.SelectionSpec;
import io.valuesfeng.picker.utils.MediaStoreCompat;


/**
 * @author KeithYokoma
 * @version 1.0.0
 * @hide
 * @since 2014/03/27
 *
 * @Modification
 *          add picture size charge
 *              by valuesFeng
 */
public class PictureLoader extends CursorLoader {
    private static final String[] PROJECTION = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME};
    private static final String ORDER_BY = MediaStore.Images.Media._ID + " DESC";
    private final boolean mEnableCapture;
    private static final String IS_LARGE_SIZE = "_size > ? or _size is null";

    public PictureLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, boolean capture) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
        mEnableCapture = capture;
    }

    public static CursorLoader newInstance(Context context, Album album, SelectionSpec selectionSpec) {
        if (album == null || album.isAll()) {
            return new PictureLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION,
                    IS_LARGE_SIZE, new String[]{selectionSpec.getMinPixels() + ""}, ORDER_BY, selectionSpec.ismEnableCamera());
        }
        return new PictureLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION,
                MediaStore.Images.Media.BUCKET_ID + " = ? and (" + IS_LARGE_SIZE + ")", new String[]{album.getId(), selectionSpec.getMinPixels() + ""}, ORDER_BY, selectionSpec.ismEnableCamera());
    }

    @Override
    public Cursor loadInBackground() {
        Cursor result = super.loadInBackground();
        if (!mEnableCapture || !MediaStoreCompat.hasCameraFeature(getContext())) {
            return result;
        }
        MatrixCursor dummy = new MatrixCursor(PROJECTION);
        dummy.addRow(new Object[]{Picture.ITEM_ID_CAPTURE, Picture.ITEM_DISPLAY_NAME_CAPTURE});
        return new MergeCursor(new Cursor[]{dummy, result});
    }
}
