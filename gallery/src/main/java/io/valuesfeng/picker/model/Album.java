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

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import io.valuesfeng.picker.R;


/**
 * @author KeithYokoma
 * @version 1.0.0
 * @hide
 * @since 2014/03/20
 */
public class Album implements Parcelable {
    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Nullable
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
    public static final String ALBUM_ID_ALL = String.valueOf(-1);
    public static final String ALBUM_NAME_ALL = "All";
    public static final String ALBUM_NAME_CAMERA = "Camera";
    public static final String ALBUM_NAME_DOWNLOAD = "Download";
    public static final String ALBUM_NAME_SCREEN_SHOT = "Screenshots";
    private final String mId;       //BUCKET_ID
    private final long mCoverId;   //Media_ID
    private final String mDisplayName;//BUCKET_DISPLAY_NAME
    private final String mCount;        // photo count

    /* package */

    public Album(String id, long coverId, String albumName, String count) {
        mId = id;
        mCoverId = coverId;
        mDisplayName = albumName;
        mCount = count;
    }

    /* package */ Album(Parcel source) {
        mId = source.readString();
        mCoverId = source.readLong();
        mDisplayName = source.readString();
        mCount = source.readString();
    }

    /**
     * This method is not responsible for managing cursor resource, such as close, iterate, and so on.
     *
     * @param cursor to be converted.
     */
    public static Album valueOf(Cursor cursor) {
        return new Album(
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)),
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)),
                cursor.getLong(3) + ""
        );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeLong(mCoverId);
        dest.writeString(mDisplayName);
        dest.writeString(mCount);
    }

    public String getId() {
        return mId;
    }

    public long getCoverId() {
        return mCoverId;
    }

    public String getDisplayName(Context context) {
        if (isAll()) {
            return context.getString(R.string.l_album_name_all);
        }
        if (isCamera()) {
            return context.getString(R.string.l_album_name_camera);
        }
        if (ALBUM_NAME_DOWNLOAD.equals(mDisplayName)) {
            return context.getString(R.string.l_album_name_download);
        }
        if (ALBUM_NAME_SCREEN_SHOT.equals(mDisplayName)) {
            return context.getString(R.string.l_album_name_screen_shot);
        }
        return mDisplayName;
    }

    public Uri buildContentUri() {
        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mCoverId);
    }

    public boolean isAll() {
        return ALBUM_ID_ALL.equals(mId);
    }

    public boolean isCamera() {
        return ALBUM_NAME_CAMERA.equals(mDisplayName);
    }

    public String getCount() {
        return mCount;
    }
}