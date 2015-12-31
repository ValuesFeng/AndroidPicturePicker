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
package cc.fotoplace.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.util.Map;

import cc.fotoplace.gallery.ImageSelectActivity;
import cc.fotoplace.gallery.R;
import cc.fotoplace.gallery.model.Item;
import cc.fotoplace.gallery.model.SelectedUriCollection;
import cc.fotoplace.gallery.utils.ImageSelectUtils;

/**
 */
public class AlbumPhotoAdapter extends CursorAdapter {
    LayoutInflater mInflater;
    Context mContext;
    SelectedUriCollection mCollection;
    Map<Long, String> map;
    private DisplayImageOptions optionsImage = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.image_not_exist)
            .showImageForEmptyUri(R.drawable.image_not_exist)
            .showImageOnFail(R.drawable.image_not_exist)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();
    private DisplayImageOptions optionsCameraImage = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.ic_camera)
            .showImageForEmptyUri(R.drawable.ic_camera)
            .showImageOnFail(R.drawable.ic_camera)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

//    public AlbumPhotoAdapter(Context context, Cursor c, SelectedUriCollection mCollection, Map<Long, String> map) {


    public AlbumPhotoAdapter(Context context, Cursor c, SelectedUriCollection mCollection, Map<Long, String> map) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mCollection = mCollection;
        this.map = map;
    }

    private ViewHolder viewHolder;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = mInflater.inflate(R.layout.photopick_gridlist_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        viewHolder.check = (CheckBox) convertView.findViewById(R.id.check);
        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        viewHolder = (ViewHolder) view.getTag();
        final Item item = Item.valueOf(cursor);

//        ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//        final CheckBox check = (CheckBox) view.findViewById(R.id.check);
        viewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isCapture()) {
                    ((ImageSelectActivity) context).showCameraAction();
                } else {
                    if (mCollection.isSingleChoose()) {
                        mCollection.add(item.buildContentUri());
                        ((ImageSelectActivity) context).setResult();
                    }
                }
            }
        });

        if (mCollection.isSingleChoose()) {
            viewHolder.check.setVisibility(View.GONE);
        }else {

            viewHolder.check.setVisibility(item.isCapture() ? View.GONE : View.VISIBLE);
            viewHolder.check.setChecked(mCollection.isSelected(item.buildContentUri()));
            viewHolder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageSelectUtils.syncCheckState(mContext, mCollection, item, viewHolder.check);
                    // PhotoGridViewHelper.callCheckStateListener(mListener);
                }
            });
        }

            ImageLoader imageLoader = ImageLoader.getInstance();


            if (item.isCapture()) {
//            thumbnail.setImageResource(R.drawable.ic_camera);
                imageLoader.displayImage("drawable://" + R.drawable.ic_camera, viewHolder.thumbnail, optionsCameraImage);
            } else {
                // thumbnailuri ���Ż�, ����androidϵͳò�������Ʋ�֪��Ϊʲô��������
                //   /storage/emulated/0/Camera/.thumbnails/1427852274561.jpg

                if (map.containsKey(item.getId())) {
                    String thumbnailuri = map.get(item.getId());
                    File file = new File(thumbnailuri);
                    thumbnailuri = file.exists() && file.isFile() ? "file://" + thumbnailuri : item.buildContentUri().toString();
                    imageLoader.displayImage(thumbnailuri, viewHolder.thumbnail, optionsImage);
                } else {
                    imageLoader.displayImage(item.buildContentUri().toString(), viewHolder.thumbnail, optionsImage);
                }

        }
    }

    static class ViewHolder {
        ImageView thumbnail;
        CheckBox check;
    }


//    public void registerCheckStateListener(CheckStateListener listener) {
//        mListener = listener;
//    }
//
//    public void unregisterCheckStateListener() {
//        mListener = null;
//    }

    public static interface CheckStateListener {
        void onUpdate();
    }
}