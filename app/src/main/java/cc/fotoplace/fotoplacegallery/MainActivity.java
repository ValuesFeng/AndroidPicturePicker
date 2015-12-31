package cc.fotoplace.fotoplacegallery;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import io.valuesfeng.picker.ImageBuilder;


public class MainActivity extends FragmentActivity {
    public static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = ImageBuilder.obtainResult(data);
            Log.i("fotoplace", "selected: " + mSelected);
            for(Uri u:mSelected){
                Log.i("fotoplace", u.getPath());
            }
        }
    }

    public void onClickButton(View view) {

        ImageBuilder.from(this).choose().count(0, 1).quality(1000,Integer.MAX_VALUE).capture(true).forResult(REQUEST_CODE_CHOOSE);
       // startActivity(new Intent(this, ImageSelectActivity.class));
    }
}
