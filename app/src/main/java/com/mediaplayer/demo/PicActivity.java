package com.mediaplayer.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by ljz on 17-3-6.
 */

public class PicActivity extends AppCompatActivity {
    public static final String PIC_PATH = "pic_path";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        String picpath = getIntent().getStringExtra(PIC_PATH);
        if (TextUtils.isEmpty(picpath)) {
            Toast.makeText(this, "pic path is null", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView imageView = (ImageView) findViewById(R.id.pic);
        Bitmap loacalBitmap = getLoacalBitmap(picpath);
        imageView.setImageBitmap(loacalBitmap);
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
