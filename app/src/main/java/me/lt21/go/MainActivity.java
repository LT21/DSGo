package me.lt21.go;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View.OnFocusChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends Activity {
    private final String pkgOfDSPhoto = "com.synology.dsphoto", clsOfDSPhoto = "com.synology.dsphoto.ui.welcome.WelcomeActivity";
    private final String pkgOfDSVideo = "com.synology.dsvideo", clsOfDSVideo = "com.synology.dsvideo.ui.WelcomeActivity";
    private boolean hasDSPhoto, hasDSVideo;
    private Drawable drawableOfDSPhoto, drawableOfDSVideo;
    private final ColorFilter colorFilter= new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    private OnFocusChangeListener listener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            switch (view.getId()){
                case R.id.btn_ds_photo:
                    drawableOfDSPhoto.setColorFilter(b?null:colorFilter);
                    break;
                case R.id.btn_ds_video:
                    drawableOfDSVideo.setColorFilter(b?null:colorFilter);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawable();

        updateAppsList();
    }

    private void initDrawable() {
        drawableOfDSPhoto = getDrawable(R.mipmap.card_ds_photo);
        drawableOfDSVideo = getDrawable(R.mipmap.card_ds_video);

        drawableOfDSPhoto.setColorFilter(colorFilter);
        drawableOfDSVideo.setColorFilter(colorFilter);

        ImageView DSPhoto = findViewById(R.id.btn_ds_photo);
        ImageView DSVideo = findViewById(R.id.btn_ds_video);

        DSPhoto.setImageDrawable(drawableOfDSPhoto);
        DSPhoto.setOnFocusChangeListener(listener);
        DSVideo.setImageDrawable(drawableOfDSVideo);
        DSVideo.setOnFocusChangeListener(listener);
    }

    private void updateAppsList() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);

        List<ResolveInfo> appsList = getApplication().getPackageManager().queryIntentActivities(mainIntent, 0);

        hasDSPhoto = false;
        hasDSVideo = false;
        for (ResolveInfo app : appsList) {
            if (TextUtils.equals(app.activityInfo.packageName, pkgOfDSPhoto)) {
                hasDSPhoto = true;
                if(hasDSVideo) return;
            }
            if (TextUtils.equals(app.activityInfo.packageName, pkgOfDSVideo)) {
                hasDSVideo = true;
                if(hasDSPhoto) return;
            }
        }
    }

    public void OnClick(View view) {
        switch (view.getId()){
            case R.id.btn_ds_photo:
                if (hasDSPhoto){
                    Intent intent = new Intent(pkgOfDSPhoto);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(new ComponentName(pkgOfDSPhoto, clsOfDSPhoto));
                    startActivity(intent);
                }else{
                    Toast.makeText(this, R.string.error_with_dsphoto, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_ds_video:
                if (hasDSVideo){
                    Intent intent = new Intent(pkgOfDSVideo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(new ComponentName(pkgOfDSVideo, clsOfDSVideo));
                    startActivity(intent);
                }else{
                    Toast.makeText(this, R.string.error_with_dsvideo, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
