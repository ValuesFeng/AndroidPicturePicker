package cc.fotoplace.fotoplacegallery;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.utils.PicturePickerUtils;

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
            mSelected = PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                Log.i("picture", u.getPath());
            }
        }
    }

    public void onClickButton(View view) {
        Picker.from(this).count(0,1000).setEnableCamera(true).forResult(1);
    }
}
