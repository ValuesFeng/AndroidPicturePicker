package cc.fotoplace.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;

import cc.fotoplace.gallery.control.AlbumPhotoCollection;
import cc.fotoplace.gallery.control.AlbumCollection;
import cc.fotoplace.gallery.model.Album;
import cc.fotoplace.gallery.model.SelectedUriCollection;
import cc.fotoplace.gallery.model.SelectionSpec;
import cc.fotoplace.gallery.utils.BundleUtils;
import cc.fotoplace.gallery.utils.MediaStoreCompat;


public class ImageSelectActivity extends FragmentActivity implements ConfirmationDialogFragment.ConfirmationSelectionListener,AlbumCollection.OnDirectorySelectListener {

    /**
     *
     */
    private static final String STATE_SELECTION = BundleUtils.buildKey(ImageSelectActivity.class, "STATE_SELECTION");
    public static final String EXTRA_RESULT_SELECTION = BundleUtils.buildKey(ImageSelectActivity.class, "EXTRA_RESULT_SELECTION");
    public static final String EXTRA_SELECTION_SPEC = BundleUtils.buildKey(ImageSelectActivity.class, "EXTRA_SELECTION_SPEC");
    public static final String EXTRA_RESUME_LIST = BundleUtils.buildKey(ImageSelectActivity.class, "EXTRA_RESUME_LIST");
    private static final String ARGS_ALBUM = BundleUtils.buildKey(ImageSelectActivity.class, "ARGS_ALBUM");
    public static final String STATE_CAPTURE_PHOTO_URI = BundleUtils.buildKey(ImageSelectActivity.class, "STATE_CAPTURE_PHOTO_URI");
    private TextView mFoldName;
    private View mListViewGroup;
    private ListView mListView;
    private GridView mGridView;
    public static final int REQUEST_CODE_CAPTURE = 3;
    private MediaStoreCompat mMediaStoreCompat;
    private Button change;
    private Button commit;
    private ImageView galleryTip;
    private SelectionSpec selectionSpec;
    private ImageView btnBack;
    private AlbumCollection albumCollection =new AlbumCollection();
    private final AlbumPhotoCollection mPhotoCollection = new AlbumPhotoCollection();
    private final SelectedUriCollection mCollection = new SelectedUriCollection(this);
    private String mCapturePhotoUriHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        mCapturePhotoUriHolder = savedInstanceState != null ? savedInstanceState.getString(STATE_CAPTURE_PHOTO_URI) : "";
        selectionSpec = getIntent().getParcelableExtra(ImageSelectActivity.EXTRA_SELECTION_SPEC);
        mMediaStoreCompat = new MediaStoreCompat(this, new Handler(Looper.getMainLooper()));
        mCollection.onCreate(savedInstanceState);
        mCollection.prepareSelectionSpec(selectionSpec);
        mCollection.setDefaultSelection(getIntent().<Uri>getParcelableArrayListExtra(EXTRA_RESUME_LIST));
        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(),true,true));
        mListView = (ListView) findViewById(R.id.listView);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        mListViewGroup = findViewById(R.id.listViewParent);
        mListViewGroup.setOnClickListener(mOnClickFoldName);
        mFoldName = (TextView) findViewById(R.id.foldName);
        galleryTip = (ImageView) findViewById(R.id.gallery_tip);
        LinearLayout selectFold = (LinearLayout) findViewById(R.id.selectFold);
        change = (Button) findViewById(R.id.change);
        commit = (Button) findViewById(R.id.commit);
        mFoldName.setText("最近图片");
        selectFold.setOnClickListener(mOnClickFoldName);
        albumCollection.onCreate(ImageSelectActivity.this,this,selectionSpec,mListView);
        albumCollection.loadAlbums();
        mPhotoCollection.onCreate(ImageSelectActivity.this,mGridView,mCollection,selectionSpec);
        mPhotoCollection.loadAllPhoto();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectionSpec.isWideScreen()) {
                    selectionSpec.setIsWideScreen(false);
                } else {
                    selectionSpec.setIsWideScreen(true);
                }
                albumCollection.loadAlbums();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCollection.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"您还未选择图片",Toast.LENGTH_LONG).show();
                }else{

                    setResult();

                }

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION,
                (ArrayList<? extends Parcelable>) mCollection.asList());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    View.OnClickListener mOnClickFoldName = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListViewGroup.getVisibility() == View.VISIBLE) {
                hideFolderList();
            } else {
                showFolderList();
            }
        }

    };


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mCollection.onSaveInstanceState(outState);
        albumCollection.onSaveInstanceState(outState);
        outState.putString(STATE_CAPTURE_PHOTO_URI, mCapturePhotoUriHolder);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Uri captured = mMediaStoreCompat.getCapturedPhotoUri(data, mCapturePhotoUriHolder);
            if (captured != null) {
                mCollection.add(captured);
                mMediaStoreCompat.cleanUp(mCapturePhotoUriHolder);
                if(mCollection.isSingleChoose()){
                   setResult();
                }
            }

        }

    }

    public void prepareCapture(String uri) {
        mCapturePhotoUriHolder = uri;
    }


    private void showFolderList() {
        galleryTip.setImageResource(R.drawable.gallery_up);
        mListViewGroup.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.listview_up);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.listview_fade_in);


        mListView.startAnimation(animation);
        mListViewGroup.startAnimation(fadeIn);
        //mListViewGroup.setVisibility(View.VISIBLE);
    }

    private void hideFolderList() {
        galleryTip.setImageResource(R.drawable.gallery_down);
        Animation animation = AnimationUtils.loadAnimation(ImageSelectActivity.this, R.anim.listview_down);
        Animation fadeOut = AnimationUtils.loadAnimation(ImageSelectActivity.this, R.anim.listview_fade_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mListViewGroup.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mListView.startAnimation(animation);
        mListViewGroup.startAnimation(fadeOut);
    }


    public MediaStoreCompat getMediaStoreCompat() {
        return mMediaStoreCompat;
    }


    @Override
    protected void onDestroy() {
        mMediaStoreCompat.destroy();
        albumCollection.onDestroy();
        mPhotoCollection.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        if (mCollection.isEmpty()) {
            setResult(Activity.RESULT_CANCELED);
            super.onBackPressed();
            return;
        }
        ConfirmationDialogFragment dialog = ConfirmationDialogFragment.newInstance(R.string.l_confirm_dialog_title, R.string.l_confirm_dialog_message);
        dialog.show(getSupportFragmentManager(), ConfirmationDialogFragment.TAG);
    }
    /**
     * 选择相机
     */
    public void showCameraAction() {
        mCapturePhotoUriHolder = mMediaStoreCompat.invokeCameraCapture(this, ImageSelectActivity.REQUEST_CODE_CAPTURE);
    }


    @Override
    public void onPositive() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onNegative() {

    }

    @Override
    public void onSelect(Album album) {

        hideFolderList();
        mFoldName.setText(album.getDisplayName(this));
        mPhotoCollection.resetLoad(album);
    }

    @Override
    public void onReset(Album album) {

//        mPhotoCollection.load(album);
    }
}
