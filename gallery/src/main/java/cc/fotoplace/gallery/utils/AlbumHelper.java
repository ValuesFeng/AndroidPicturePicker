package cc.fotoplace.gallery.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zaiyong on 2015/6/6.
 */
public class AlbumHelper {


    private Context context;

    private ContentResolver cr;

    private AlbumHelper(Context context) {
        init(context);
    }

    public static AlbumHelper instance;

    public static synchronized AlbumHelper getInstance(Context context) {
        if(instance == null){
            instance = new AlbumHelper(context);
        }
        return instance;
    }

    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
        }
    }


    public Map<Long,String> getThumbnail() {
        String[] projection = { MediaStore.Images.Thumbnails._ID, Thumbnails.IMAGE_ID,Thumbnails.DATA};
        Cursor cursor1 = Thumbnails.queryMiniThumbnails(cr, Thumbnails.EXTERNAL_CONTENT_URI,Thumbnails.MINI_KIND, projection);
        Map<Long,String> map = getThumbnailColumnData(cursor1);
        cursor1.close();
        return map;
    }

    private Map<Long,String> getThumbnailColumnData(Cursor cur) {
        Map<Long,String> map = new HashMap<Long,String>();
        if (cur.moveToFirst()) {
            Long image_id;
            String image_path;
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
            do {
                image_id = cur.getLong(image_idColumn);
                image_path = cur.getString(dataColumn);
                map.put(image_id, image_path);
            } while (cur.moveToNext());
        }
        return map;
    }
    public static Uri getThumUri(Long mId) {
        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mId);
    }
}
