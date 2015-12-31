//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.valuesfeng.picker.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class MediaStoreCompat {
    public static final String TAG = MediaStoreCompat.class.getSimpleName();
    private static final String MEDIA_FILE_NAME_FORMAT = "yyyyMMdd_HHmmss";
    private static final String MEDIA_FILE_EXTENSION = ".jpg";
    private static final String MEDIA_FILE_PREFIX = "IMG_";
    private final String MEDIA_FILE_DIRECTORY;
    private Context mContext;
    private ContentObserver mObserver;
    private ArrayList<MediaStoreCompat.PhotoContent> mRecentlyUpdatedPhotos;

    public MediaStoreCompat(Context context, final Handler handler) {
        this.mContext = context;
        this.MEDIA_FILE_DIRECTORY = context.getPackageName();
        this.mObserver = new ContentObserver(handler) {
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                MediaStoreCompat.this.updateLatestPhotos();
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Media.EXTERNAL_CONTENT_URI, true, this.mObserver);
    }

    public static final boolean hasCameraFeature(Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        return pm.hasSystemFeature("android.hardware.camera");
    }

    public String invokeCameraCapture(Activity activity, int requestCode) {
        if(!hasCameraFeature(this.mContext)) {
            return null;
        } else {
            File toSave = this.getOutputFileUri();
            if(toSave == null) {
                return null;
            } else {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.putExtra("output", Uri.fromFile(toSave));
                intent.putExtra("android.intent.extra.videoQuality", 1);
                activity.startActivityForResult(intent, requestCode);
                return toSave.toString();
            }
        }
    }

    public void destroy() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
    }

    public Uri getCapturedPhotoUri(Intent data, String preparedUri) {
        Uri captured = null;
        if(data != null) {
            captured = data.getData();
            if(captured == null) {
                data.getParcelableExtra("android.intent.extra.STREAM");
            }
        }

        File prepared = new File(preparedUri.toString());
        if(captured == null || captured.equals(Uri.fromFile(prepared))) {
            captured = this.findPhotoFromRecentlyTaken(prepared);
            if(captured == null) {
                captured = this.storeImage(prepared);
                prepared.delete();
            } else {
                String realPath = getPathFromUri(this.mContext.getContentResolver(), captured);
                if(realPath != null && !prepared.equals(new File(realPath))) {
                    prepared.delete();
                }
            }
        }
        return captured;
    }

    public void cleanUp(String uri) {
        File file = new File(uri.toString());
        if(file.exists()) {
            file.delete();
        }

    }

    public static String getPathFromUri(ContentResolver resolver, Uri contentUri) {
        String dataColumn = "_data";
        Cursor cursor = null;

        String var5;
        try {
            cursor = resolver.query(contentUri, new String[]{"_data"}, (String)null, (String[])null, (String)null);
            if(cursor == null || !cursor.moveToFirst()) {
                Object index1 = null;
                return (String)index1;
            }

            int index = cursor.getColumnIndex("_data");
            var5 = cursor.getString(index);
        } finally {
            if(cursor != null) {
                cursor.close();
            }

        }

        return var5;
    }

    public static long copyFileStream(FileInputStream is, FileOutputStream os) throws IOException {
        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        long length;
        try {
            srcChannel = is.getChannel();
            destChannel = os.getChannel();
            length = srcChannel.transferTo(0L, srcChannel.size(), destChannel);
        } finally {
            if(srcChannel != null) {
                srcChannel.close();
            }
            if(destChannel != null) {
                destChannel.close();
            }
        }
        return length;
    }

    private Uri findPhotoFromRecentlyTaken(File file) {
        if(this.mRecentlyUpdatedPhotos == null) {
            this.updateLatestPhotos();
        }
        long fileSize = file.length();
        long taken = ExifInterfaceCompat.getExifDateTimeInMillis(file.getAbsolutePath());
        int maxPoint = 0;
        MediaStoreCompat.PhotoContent maxItem = null;
        Iterator i$ = this.mRecentlyUpdatedPhotos.iterator();

        while(i$.hasNext()) {
            MediaStoreCompat.PhotoContent item = (MediaStoreCompat.PhotoContent)i$.next();
            int point = 0;
            if((long)item.size == fileSize) {
                ++point;
            }
            if(item.taken == taken) {
                ++point;
            }
            if(point > maxPoint) {
                maxPoint = point;
                maxItem = item;
            }
        }
        if(maxItem != null) {
            this.generateThumbnails(maxItem.id);
            return ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, maxItem.id);
        } else {
            return null;
        }
    }

    private Uri storeImage(File file) {
        try {
            ContentValues e = new ContentValues();
            e.put("title", file.getName());
            e.put("mime_type", "image/jpeg");
            e.put("description", "mixi Photo");
            e.put("orientation", Integer.valueOf(ExifInterfaceCompat.getExifOrientation(file.getAbsolutePath())));
            long date = ExifInterfaceCompat.getExifDateTimeInMillis(file.getAbsolutePath());
            if(date != -1L) {
                e.put("datetaken", Long.valueOf(date));
            }

            Uri imageUri = this.mContext.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, e);
            FileOutputStream fos = (FileOutputStream)this.mContext.getContentResolver().openOutputStream(imageUri);
            FileInputStream fis = new FileInputStream(file);
            copyFileStream(fis, fos);
            fos.close();
            fis.close();
            this.generateThumbnails(ContentUris.parseId(imageUri));
            return imageUri;
        } catch (Exception e) {
            Log.w(TAG, "cannot insert", e);
            return null;
        }
    }

    private void updateLatestPhotos() {
        Cursor c = Media.query(this.mContext.getContentResolver(), Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "datetaken", "_size"}, (String)null, (String[])null, "date_added DESC");
        if(c != null) {
            try {
                int count = 0;
                this.mRecentlyUpdatedPhotos = new ArrayList();

                while(c.moveToNext()) {
                    MediaStoreCompat.PhotoContent item = new MediaStoreCompat.PhotoContent();
                    item.id = c.getLong(0);
                    item.taken = c.getLong(1);
                    item.size = c.getInt(2);
                    this.mRecentlyUpdatedPhotos.add(item);
                    ++count;
                    if(count > 5) {
                        break;
                    }
                }
            } finally {
                c.close();
            }
        }
    }

    private void generateThumbnails(long imageId) {
        try {
            Thumbnails.getThumbnail(this.mContext.getContentResolver(), imageId, 1, (Options)null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(8)
    private File getOutputFileUri() {
        File extDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), this.MEDIA_FILE_DIRECTORY);
        if(!extDir.exists() && !extDir.mkdirs()) {
            return null;
        } else {
            String timeStamp = (new SimpleDateFormat(MEDIA_FILE_NAME_FORMAT)).format(new Date());
            return new File(extDir.getPath() + File.separator + MEDIA_FILE_PREFIX + timeStamp + MEDIA_FILE_EXTENSION);
        }
    }

    private static class PhotoContent {
        public long id;
        public long taken;
        public int size;

        private PhotoContent() {
        }
    }
}
