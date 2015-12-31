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
package io.valuesfeng.picker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

import io.valuesfeng.picker.R;
import io.valuesfeng.picker.model.Album;


/**
 */
public class AlbumAdapter extends CursorAdapter {
    LayoutInflater mInflater;
    ViewHolder viewHolder;

    public AlbumAdapter(Context context, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = mInflater.inflate(R.layout.photopick_list_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.textView =(TextView) convertView.findViewById(R.id.foldName);
        viewHolder.photoCount =(TextView) convertView.findViewById(R.id.photoCount);
        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        viewHolder = (ViewHolder) view.getTag();
        Album album = Album.valueOf(cursor);
        viewHolder.textView.setText(album.getDisplayName(context));
        viewHolder.photoCount.setText("( "+album.getCount()+" )");
    }

    static class ViewHolder{
        TextView textView;
        TextView photoCount;
    }
}