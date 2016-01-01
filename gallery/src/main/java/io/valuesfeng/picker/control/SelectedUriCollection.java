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
package io.valuesfeng.picker.control;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.model.SelectionSpec;
import io.valuesfeng.picker.utils.BundleUtils;

/**
 */
public class SelectedUriCollection {
    private static final String STATE_SELECTION = BundleUtils.buildKey(SelectedUriCollection.class, "STATE_SELECTION");
    private static final String STATE_SELECTION_POSITION = BundleUtils.buildKey(SelectedUriCollection.class, "STATE_SELECTION_POSITION");
    private final WeakReference<Context> mContext;
    private Set<Uri> mUris;
    private SelectionSpec mSpec;
    private OnSelectionChange onSelectionChange;

    public SelectedUriCollection(Context context) {
        mContext = new WeakReference<>(context);
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mUris = new LinkedHashSet<>();
        } else {
            List<Uri> saved = savedInstanceState.getParcelableArrayList(STATE_SELECTION);
            mUris = new LinkedHashSet<>(saved);
        }
    }

    public void prepareSelectionSpec(SelectionSpec spec) {
        mSpec = spec;
    }

    public void setDefaultSelection(List<Uri> uris) {
        mUris.addAll(uris);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_SELECTION, new ArrayList<>(mUris));
    }

    public boolean add(Uri uri) {
        if (onSelectionChange!=null)
            onSelectionChange.onChange(maxCount(),count()+1);
        return mUris.add(uri);
    }

    public boolean remove(Uri uri) {
        if (onSelectionChange!=null)
            onSelectionChange.onChange(maxCount(),count()-1);
        return mUris.remove(uri);
    }

    public List<Uri> asList() {
        return new ArrayList<Uri>(mUris);
    }

    public boolean isEmpty() {
        return mUris == null || mUris.isEmpty();
    }

    public boolean isSelected(Uri uri) {
        return mUris.contains(uri);
    }

    public boolean isCountInRange() {
        return mSpec.getMinSelectable() <= mUris.size() && mUris.size() <= mSpec.getMaxSelectable();
    }

    public boolean isCountOver() {
        return mUris.size() >= mSpec.getMaxSelectable();
    }

    public int count() {
        return mUris.size();
    }

    public int maxCount() {
        return mSpec.getMaxSelectable();
    }

    public boolean isSingleChoose() {
        return mSpec.isSingleChoose();
    }

    public LoadEngine getEngine(){
        return mSpec.getEngine();
    }

    public void setOnSelectionChange(OnSelectionChange onSelectionChange) {
        this.onSelectionChange = onSelectionChange;
    }

    public interface OnSelectionChange{
        void onChange(int maxCount,int selectCount);
    }
}
