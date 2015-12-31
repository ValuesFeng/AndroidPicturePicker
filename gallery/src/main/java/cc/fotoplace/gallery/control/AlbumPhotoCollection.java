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
package cc.fotoplace.gallery.control;

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

import cc.fotoplace.gallery.adapter.AlbumPhotoAdapter;
import cc.fotoplace.gallery.loader.AlbumPhotoLoader;
import cc.fotoplace.gallery.model.Album;
import cc.fotoplace.gallery.model.SelectedUriCollection;
import cc.fotoplace.gallery.model.SelectionSpec;
import cc.fotoplace.gallery.utils.AlbumHelper;
import cc.fotoplace.gallery.utils.BundleUtils;

/**
 */
public class AlbumPhotoCollection implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 2;
    private static final String ARGS_ALBUM = BundleUtils.buildKey(AlbumPhotoCollection.class, "ARGS_ALBUM");
    private static final String ARGS_ENABLE_CAPTURE = BundleUtils.buildKey(AlbumPhotoCollection.class, "ARGS_ENABLE_CAPTURE");
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
//    private AlbumPhotoCallbacks mCallbacks;
    private GridView gridView;
    private AlbumPhotoAdapter albumPhotoAdapter;
    private SelectedUriCollection mCollection;
    private SelectionSpec selectionSpec;
    AlbumHelper albumHelper;


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

        return AlbumPhotoLoader.newInstance(context, album,selectionSpec);
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

    public void onCreate(@NonNull FragmentActivity context, @NonNull GridView gridView,SelectedUriCollection mCollection, SelectionSpec selectionSpec) {
        mContext = new WeakReference<Context>(context);
        mLoaderManager = context.getSupportLoaderManager();
        this.gridView = gridView;
        this.selectionSpec=selectionSpec;
        albumHelper=AlbumHelper.getInstance(context);
        albumPhotoAdapter=new AlbumPhotoAdapter(context,null,mCollection,albumHelper.getThumbnail());
        gridView.setAdapter(albumPhotoAdapter);
    }

    public void onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID);
    }

    public void loadAllPhoto() {
        Album album=new Album(Album.ALBUM_ID_ALL,-1,Album.ALBUM_NAME_ALL,"");
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
