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
package cc.fotoplace.gallery.model;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import cc.fotoplace.gallery.MimeType;
import cc.fotoplace.gallery.utils.ParcelUtils;

/**
 */
public final class SelectionSpec implements Parcelable {
    public static final Creator<SelectionSpec> CREATOR = new Creator<SelectionSpec>() {
        @Override
        public SelectionSpec createFromParcel(Parcel source) {
            return new SelectionSpec(source);
        }

        @Override
        public SelectionSpec[] newArray(int size) {
            return new SelectionSpec[size];
        }
    };
    private int mMaxSelectable;   // 最大选择数量
    private int mMinSelectable;   // 最小选择数量
    private long mMinPixels;       //最小size
    private boolean mEnableCapture;//是否可用相机
    private boolean isWideScreen;//是否宽屏模式
    private Set<MimeType> mMimeTypeSet;

    private long mMaxPixels;

    public SelectionSpec() {
        mMinSelectable = 0;
        mMaxSelectable = 1;
        mMinPixels = 0L;
        mMaxPixels = Long.MAX_VALUE;
        mEnableCapture = false;
        isWideScreen = false;
    }

    /* package */ SelectionSpec(Parcel source) {
        mMinSelectable = source.readInt();
        mMaxSelectable = source.readInt();
        mMinPixels = source.readLong();
        mMaxPixels = source.readLong();
        mEnableCapture = ParcelUtils.readBoolean(source);
        isWideScreen = ParcelUtils.readBoolean(source);
        List<MimeType> list = new ArrayList<MimeType>();
        source.readList(list, MimeType.class.getClassLoader());
        mMimeTypeSet = EnumSet.copyOf(list);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMinSelectable);
        dest.writeInt(mMaxSelectable);
        dest.writeLong(mMinPixels);
        dest.writeLong(mMaxPixels);
        ParcelUtils.writeBoolean(dest, mEnableCapture);
        ParcelUtils.writeBoolean(dest, isWideScreen);
        dest.writeList(new ArrayList<MimeType>(mMimeTypeSet));
    }

    public boolean ismEnableCapture() {
        return mEnableCapture;
    }

    public void setmEnableCapture(boolean mEnableCapture) {
        this.mEnableCapture = mEnableCapture;
    }

    public void setMaxSelectable(int maxSelectable) {
        mMaxSelectable = maxSelectable;
    }

    public void setMinSelectable(int minSelectable) {
        mMinSelectable = minSelectable;
    }

    public void setMinPixels(long minPixels) {
        mMinPixels = minPixels;
    }

    public void setMaxPixels(long maxPixels) {
        mMaxPixels = maxPixels;
    }

    public void setMimeTypeSet(Set<MimeType> set) {
        mMimeTypeSet = set;
    }

    public int getMinSelectable() {
        return mMinSelectable;
    }

    public int getMaxSelectable() {
        return mMaxSelectable;
    }

    public long getMinPixels() {
        return mMinPixels;
    }
//    public String getMinPixels() {
//
//        return Long.parseLong(mMinPixels);
//    }


    public boolean isWideScreen() {
        return isWideScreen;
    }

    public boolean isSingleChoose() {

        if(mMinSelectable==0&&mMaxSelectable==1){
            return true;
        }else {
            return false;
        }
    }


    public void setIsWideScreen(boolean isWideScreen) {
        this.isWideScreen = isWideScreen;
    }

    public long getMaxPixels() {
        return mMaxPixels;
    }

    public Set<MimeType> getMimeTypeSet() {
        return mMimeTypeSet;
    }
}
