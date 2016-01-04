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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.lang.ref.WeakReference;

import io.valuesfeng.picker.adapter.AlbumAdapter;
import io.valuesfeng.picker.loader.AlbumLoader;
import io.valuesfeng.picker.model.Album;
import io.valuesfeng.picker.model.SelectionSpec;
import io.valuesfeng.picker.utils.BundleUtils;
import io.valuesfeng.picker.utils.HandlerUtils;

/**
 * @version 1.0.0
 * @hide
 */
public class AlbumCollection implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private static final int LOADER_ID = 1;
    private static final String STATE_CURRENT_SELECTION = BundleUtils.buildKey(AlbumCollection.class, "STATE_CURRENT_SELECTION");
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
    private OnDirectorySelectListener directorySelectListener;
    private int mCurrentSelection;
    private SelectionSpec selectionSpec;
    private AlbumAdapter albumAdapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Context context = mContext.get();
        if (context == null) {
            return null;
        }
        return AlbumLoader.newInstance(context, selectionSpec);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }
        albumAdapter.swapCursor(data);
        HandlerUtils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (data.getCount() > 0) {
                    data.moveToFirst();
                    Album currentAlbum = Album.valueOf(data);
                    if (directorySelectListener != null) {
                        directorySelectListener.onReset(currentAlbum);
                    }
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }
        albumAdapter.swapCursor(null);
    }

    public void onCreate(FragmentActivity activity, OnDirectorySelectListener directorySelectListener, SelectionSpec selectionSpec, ListView listView) {
        mContext = new WeakReference<Context>(activity);
        mLoaderManager = activity.getSupportLoaderManager();
        this.directorySelectListener = directorySelectListener;
        this.selectionSpec = selectionSpec;
        albumAdapter = new AlbumAdapter(activity, null);
        listView.setAdapter(albumAdapter);
        listView.setOnItemClickListener(this);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        mCurrentSelection = savedInstanceState.getInt(STATE_CURRENT_SELECTION);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_SELECTION, mCurrentSelection);
    }

    public void onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID);
        directorySelectListener = null;
    }

    public void loadAlbums() {
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    public void resetLoadAlbums() {
        mLoaderManager.restartLoader(LOADER_ID, null, this);
    }


    public int getCurrentSelection() {
        return mCurrentSelection;
    }

    public void setStateCurrentSelection(int currentSelection) {
        mCurrentSelection = currentSelection;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (directorySelectListener != null) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            Album album = Album.valueOf(cursor);
            directorySelectListener.onSelect(album);
        }
    }

    public interface OnDirectorySelectListener {
        void onSelect(Album album);

        void onReset(Album album);
    }


}
