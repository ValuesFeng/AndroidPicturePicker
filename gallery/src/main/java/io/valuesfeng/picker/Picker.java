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
package io.valuesfeng.picker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.model.SelectionSpec;

/**
 *
 * thanks for:
 *
 * https://github.com/nohana/Laevatein
 */
public final class Picker {
    private static final String INITIALIZE_PICKER_ERROR = "Try to initialize Picker which had already been initialized before";
    private static boolean hasInitPicker;
    private final WeakReference<Activity> mContext;
    private final WeakReference<Fragment> mFragment;
    private final Set<MimeType> mMimeType;
    private final SelectionSpec mSelectionSpec;
    private LoadEngine engine;      //图片加载器 glide  imageloder picasso
    private List<Uri> mResumeList;

    Picker(Activity activity, Fragment fragment, Set<MimeType> mimeType) {
        mContext = new WeakReference<>(activity);
        if (fragment != null)
            mFragment = new WeakReference<>(fragment);
        else
            mFragment = null;
        mMimeType = mimeType;
        mSelectionSpec = new SelectionSpec();
        mResumeList = new ArrayList<>();
    }

    Picker(Activity activity, Fragment fragment) {
        mContext = new WeakReference<>(activity);
        if (fragment != null)
            mFragment = new WeakReference<>(fragment);
        else
            mFragment = null;
        mMimeType = MimeType.allOf();
        mSelectionSpec = new SelectionSpec();
        mResumeList = new ArrayList<>();
    }


    /**
     * set iamge  load engine
     *
     * @param engine
     * @return
     */
    public Picker setEngine(LoadEngine engine) {
        this.engine = engine;
        return this;
    }

    /**
     * set the first item open camera
     *
     * @param mEnableCamera
     * @return
     */
    public Picker enableCamera(boolean mEnableCamera) {
        mSelectionSpec.setmEnableCamera(mEnableCamera);
        return this;
    }

    /**
     * set if should start the camera by default
     *
     * @param mStartWithCamera
     * @return
     */
    public Picker startCamera(boolean mStartWithCamera) {
        mSelectionSpec.startWithCamera(mStartWithCamera);
        return this;
    }

    /**
     * Sets the limitation of a selectable count within the specified range.
     *
     * @param min minimum value to select.
     * @param max maximum value to select.
     * @return the specification builder context.
     */
    public Picker count(int min, int max) {
        mSelectionSpec.setMinSelectable(min);
        mSelectionSpec.setMaxSelectable(max);
        return this;
    }

    /**
     * Sets the limitation of a selectable count within the specified range.
     *
     * @param max maximum value to select.
     * @return the specification builder context.
     */
    public Picker count(int max) {
        mSelectionSpec.setMinSelectable(0);
        mSelectionSpec.setMaxSelectable(max);
        return this;
    }

    public Picker singleChoice() {
        count(0, 1);
        return this;
    }

    //TODO : Wait achieve
    /**
     * Sets the limitation of a selectable image quality by pixel count within the specified range.
     *
     * @param minPixel minimum value to select.
     * @param maxPixel maximum value to select.
     * @return the specification builder context.
     */
    public Picker quality(int minPixel, int maxPixel) {
        mSelectionSpec.setMinPixels(minPixel);
        mSelectionSpec.setMaxPixels(maxPixel);
        return this;
    }

    /**
     * Sets the default selection to resume photo picking activity.
     *
     * @param uriList to set selected as default.
     * @return the specification builder context.
     */
    public Picker resume(List<Uri> uriList) {
        if (uriList == null) { // nothing to do.
            return this;
        }
        mResumeList.addAll(uriList);
        return this;
    }

    /**
     * Start to select photo.
     *
     * @param requestCode identity of the requester activity.
     */
    public void forResult(int requestCode) {
        if (engine == null)
            throw new ExceptionInInitializerError(LoadEngine.INITIALIZE_ENGINE_ERROR);

        Activity activity = getActivity();
        if (activity == null) {
            return; // cannot continue;
        }
        mSelectionSpec.setMimeTypeSet(mMimeType);
        mSelectionSpec.setEngine(engine);
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.EXTRA_SELECTION_SPEC, mSelectionSpec);
//        intent.putExtra(ImageSelectActivity.EXTRA_ENGINE, (Serializable) engine);
        intent.putParcelableArrayListExtra(ImageSelectActivity.EXTRA_RESUME_LIST, (ArrayList<? extends android.os.Parcelable>) mResumeList);

        Fragment fragment = getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
        hasInitPicker = false;
    }

    public static Picker from(Activity activity) {
        if (hasInitPicker)
            throw new ExceptionInInitializerError(INITIALIZE_PICKER_ERROR);
        hasInitPicker = true;
        return new Picker(activity, null);
    }

    public static Picker from(Activity activity, Set<MimeType> mimeType) {
        if (hasInitPicker)
            throw new ExceptionInInitializerError(INITIALIZE_PICKER_ERROR);
        hasInitPicker = true;
        return new Picker(activity, null, mimeType);
    }

    public static Picker from(Fragment fragment) {
        if (hasInitPicker)
            throw new ExceptionInInitializerError(INITIALIZE_PICKER_ERROR);
        hasInitPicker = true;
        return new Picker(fragment.getActivity(), fragment);
    }

    public static Picker from(Fragment fragment, Set<MimeType> mimeType) {
        if (hasInitPicker)
            throw new ExceptionInInitializerError(INITIALIZE_PICKER_ERROR);
        hasInitPicker = true;
        return new Picker(fragment.getActivity(), fragment, mimeType);
    }

    /**
     * @return the actual requester context.
     */
    @Nullable
    Activity getActivity() {
        return mContext.get();
    }

    /**
     * @return the fragment that is responsible for result handling.
     */
    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }

}