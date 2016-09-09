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

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import io.valuesfeng.picker.MimeType;
import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.utils.ParcelUtils;

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
    private long mMaxPixels;        //最大size
    private boolean mEnableCamera;//是否可用相机
    private boolean mStartWithCamera;
    private LoadEngine engine;      //图片加载器 glide  imageloder picasso
    private Set<MimeType> mMimeTypeSet;

    public SelectionSpec() {
        mMinSelectable = 0;
        mMaxSelectable = 1;
        mMinPixels = 0L;
        mMaxPixels = Long.MAX_VALUE;
        mEnableCamera = false;
        mStartWithCamera = false;
    }

    SelectionSpec(Parcel source) {
        mMinSelectable = source.readInt();
        mMaxSelectable = source.readInt();
        mMinPixels = source.readLong();
        mMaxPixels = source.readLong();
        mEnableCamera = ParcelUtils.readBoolean(source);
        mStartWithCamera = ParcelUtils.readBoolean(source);
        this.engine = source.readParcelable(LoadEngine.class.getClassLoader());
        List<MimeType> list = new ArrayList<>();
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
        ParcelUtils.writeBoolean(dest, mEnableCamera);
        ParcelUtils.writeBoolean(dest, mStartWithCamera);
        dest.writeParcelable(this.engine, 0);
        dest.writeList(new ArrayList<>(mMimeTypeSet));
    }

    public boolean ismEnableCamera() {
        return mEnableCamera;
    }

    public boolean willStartCamera() { return mStartWithCamera; }

    public void setmEnableCamera(boolean mEnableCamera) {
        this.mEnableCamera = mEnableCamera;
    }

    public void startWithCamera(boolean mStartWithCamera) { this.mStartWithCamera = mStartWithCamera; }

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

    public LoadEngine getEngine() {
        return engine;
    }

    public void setEngine(LoadEngine engine) {
        this.engine = engine;
    }

    public boolean isSingleChoose() {
        if (mMinSelectable == 0 && mMaxSelectable == 1) {
            return true;
        } else {
            return false;
        }
    }

    public long getMaxPixels() {
        return mMaxPixels;
    }

    public Set<MimeType> getMimeTypeSet() {
        return mMimeTypeSet;
    }
}
