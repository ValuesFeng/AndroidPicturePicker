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
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.GridView;

import java.lang.ref.WeakReference;

import io.valuesfeng.picker.adapter.PictureAdapter;
import io.valuesfeng.picker.loader.PictureLoader;
import io.valuesfeng.picker.model.Album;
import io.valuesfeng.picker.model.SelectionSpec;
import io.valuesfeng.picker.utils.BundleUtils;

/**
 */
public class PictureCollection implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 2;
    private static final String ARGS_ALBUM = BundleUtils.buildKey(PictureCollection.class, "ARGS_ALBUM");
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
    private PictureAdapter albumPhotoAdapter;
    private SelectionSpec selectionSpec;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Context context = mContext.get();
        if (context == null) {
            return null;
        }
        Album album = args.getParcelable(ARGS_ALBUM);
        if (album == null) {
            return null;
        }
        return PictureLoader.newInstance(context, album, selectionSpec);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }
        albumPhotoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }
        albumPhotoAdapter.swapCursor(null);
    }

    public void onCreate(@NonNull FragmentActivity context, @NonNull GridView gridView, SelectedUriCollection mCollection, SelectionSpec selectionSpec) {
        mContext = new WeakReference<Context>(context);
        mLoaderManager = context.getSupportLoaderManager();
        this.selectionSpec = selectionSpec;
        albumPhotoAdapter = new PictureAdapter(context, null, mCollection);
        mCollection.getEngine().scrolling(gridView);
        gridView.setAdapter(albumPhotoAdapter);
    }

    public void onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID);
    }

    public void loadAllPhoto() {
        Album album = new Album(Album.ALBUM_ID_ALL, -1, Album.ALBUM_NAME_ALL, "");
        load(album);
    }

    public void load(@Nullable Album target) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ALBUM, target);
        mLoaderManager.initLoader(LOADER_ID, args, this);
    }

    public void resetLoad(@Nullable Album target) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ALBUM, target);
        mLoaderManager.restartLoader(LOADER_ID, args, this);
    }

}
