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
package io.valuesfeng.picker.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

/**
 * @author KeithYokoma
 * @version 1.0.0
 * @hide
 * @since 2014/03/24
 */
public class Picture implements Parcelable {
    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        @Nullable
        public Picture createFromParcel(Parcel source) {
            return new Picture(source);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
    public static final long ITEM_ID_CAPTURE = -1;
    public static final String ITEM_DISPLAY_NAME_CAPTURE = "Capture";
    private final long mId;
    private String mDisplayName;

    /* package */ Picture(long id,String displayName) {
        mId = id;
        mDisplayName = displayName;
    }

    /* package */ Picture(Parcel source) {
        mId = source.readLong();
    }

    public static Picture valueOf(Cursor cursor) {
//        return new Picture(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        return new Picture(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)),cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
    }

    public long getId() {
        return mId;
    }

    public Uri buildContentUri() {
        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mId);
    }

    public Bitmap getThumbnail(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        ContentResolver contentResolver = context.getContentResolver();
        return MediaStore.Images.Thumbnails.getThumbnail(contentResolver, mId, MediaStore.Images.Thumbnails.MICRO_KIND, options);
    }

    public boolean isCapture() {
        return mId == ITEM_ID_CAPTURE;
    }
}