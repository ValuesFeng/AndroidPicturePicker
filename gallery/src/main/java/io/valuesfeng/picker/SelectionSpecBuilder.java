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
import android.support.v4.app.Fragment;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.valuesfeng.picker.model.SelectionSpec;

/**
 */
@SuppressWarnings("unused") // public APIs
public final class SelectionSpecBuilder {
    public static final String TAG = SelectionSpecBuilder.class.getSimpleName();
    private final ImageBuilder mImageBuilder;
    private final Set<MimeType> mMimeType;
    private final SelectionSpec mSelectionSpec;
    private boolean mEnableCapture;
    private boolean mEnableSelectedView;
    private int mActivityOrientation;
    private List<Uri> mResumeList;

    /**
     */
    /* package */ SelectionSpecBuilder(ImageBuilder imageBuilder, Set<MimeType> mimeType) {
        mImageBuilder = imageBuilder;
        mMimeType = mimeType;
        mSelectionSpec = new SelectionSpec();
        mResumeList = new ArrayList<Uri>();
        mActivityOrientation = -1;
    }

    /**
     */
    /* package */ SelectionSpecBuilder(ImageBuilder imageBuilder) {
        mImageBuilder = imageBuilder;
        mMimeType = MimeType.allOf() ;
        mSelectionSpec = new SelectionSpec();
        mResumeList = new ArrayList<Uri>();
        mActivityOrientation = -1;
    }


    /**
     * Sets the limitation of a selectable count within the specified range.
     * @param min minimum value to select.
     * @param max maximum value to select.
     * @return the specification builder context.
     */
    public SelectionSpecBuilder count(int min, int max) {
        mSelectionSpec.setMinSelectable(min);
        mSelectionSpec.setMaxSelectable(max);
        return this;
    }


    /**
     * Sets the flag to determine whether the list of which image has been selected should be shown or not.
     * The flag is set as false by default.
     * @param enableSelectedView the flag of visibility.
     * @return the specification builder context.
     */
    public SelectionSpecBuilder enableSelectedView(boolean enableSelectedView) {
        mEnableSelectedView = enableSelectedView;
        return this;
    }


    /**
     * Sets the limitation of a selectable image quality by pixel count within the specified range.
     * @param minPixel minimum value to select.
     * @param maxPixel maximum value to select.
     * @return the specification builder context.
     */
    public SelectionSpecBuilder quality(int minPixel, int maxPixel) {
        mSelectionSpec.setMinPixels(minPixel);
        mSelectionSpec.setMaxPixels(maxPixel);
        return this;
    }

    /**
     * Sets the default selection to resume photo picking activity.
     * @param uriList to set selected as default.
     * @return the specification builder context.
     */
    public SelectionSpecBuilder resume(List<Uri> uriList) {
        if (uriList == null) { // nothing to do.
            return this;
        }
        mResumeList.addAll(uriList);
        return this;
    }

    /**
     * Determines whether the photo capturing is enabled or not on the camera photo grid view.
     * This flag is false by default.
     * @param enable whether to enable capturing or not.
     * @return the specification builder context.
     */
    public SelectionSpecBuilder capture(boolean enable) {
        mEnableCapture = enable;
        mSelectionSpec.setmEnableCapture(enable);
        return this;
    }



    public SelectionSpecBuilder isWideScreen(boolean isWideScreen) {
        mSelectionSpec.setIsWideScreen(isWideScreen);
        return this;
    }

    public SelectionSpecBuilder restrictOrientation(int activityOrientation) {
        mActivityOrientation = activityOrientation;
        return this;
    }

    /**
     * Start to select photo.
     * @param requestCode identity of the requester activity.
     */
    public void forResult(int requestCode) {
        Activity activity = mImageBuilder.getActivity();
        if (activity == null) {
            return; // cannot continue;
        }
        mSelectionSpec.setMimeTypeSet(mMimeType);
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.EXTRA_SELECTION_SPEC, mSelectionSpec);
        intent.putParcelableArrayListExtra(ImageSelectActivity.EXTRA_RESUME_LIST, (ArrayList<? extends android.os.Parcelable>) mResumeList);

        Fragment fragment = mImageBuilder.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }
}
