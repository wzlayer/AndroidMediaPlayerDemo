package com.mediaplayer.demo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by ljz on 17-2-20.
 */

public class SeekToActivity extends AppCompatActivity {
    private static final String TAG = "SeekToActivity";
    private MediaPlayer mMediaPlayer;//定义音频控件
    private int mPosition;//记录音频文件播放的位置
    private TextView mTextView;
    private EditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekto);

        setTitle("音频测试");

        Intent intent = getIntent();
        String filepath = intent.getStringExtra("filepath");
        if (TextUtils.isEmpty(filepath)) {
            Toast.makeText(this, "path is null", Toast.LENGTH_SHORT).show();
            finish();
        }

        mTextView = (TextView)findViewById(R.id.text);
        mEditText = (EditText)findViewById(R.id.seek);

        mMediaPlayer = new MediaPlayer();
        File mediaFile = new File(filepath);
        Uri fileUri = Uri.fromFile(mediaFile);
        mMediaPlayer = MediaPlayer.create(this, fileUri);//将音频文件放到里面
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(SeekToActivity.this, "setOnCompletionListener:onCompletion", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "setOnCompletionListener:onCompletion");
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, "setOnCompletionListener:onError");
                return false;
            }
        });
        mMediaPlayer.setLooping(false);//不循环播放

        mPosition = mMediaPlayer.getCurrentPosition();//保存当前播放点
        //mMediaPlayer.seekTo(mPosition);
        mTextView.setText("开始处：" + mPosition +'\r'+'\n');
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);//输入类型为数字


        Button playButton = (Button)this.findViewById(R.id.play);
        Button goButton = (Button)this.findViewById(R.id.go);
        ButtonClickListener listener = new ButtonClickListener();//定义按键监听器
        playButton.setOnClickListener(listener);
        goButton.setOnClickListener(listener);
    }

    private final class ButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            Button button = (Button) v;//得到button
            try{
                switch(v.getId()){//通过传过来的button，可以判断button的类型
                    case R.id.play://播放
                        if(mMediaPlayer.isPlaying()){
                            stop();
                            mPosition = mMediaPlayer.getCurrentPosition();//保存当前播放点
                            mTextView.setText(mTextView.getText()+"段落：" + mPosition +'\r'+'\n');
                            int duration = mMediaPlayer.getDuration();
                            mTextView.setText(mTextView.getText()+"duration：" + duration +'\r'+'\n');
                        }else{
                            play();
                        }
                        break;
                    case R.id.go://进入预设的时间
                        play();//必须先用play()初始化，不然会有错误产生，而是也无法调到设定的时间
                        stop();
                        Log.i(TAG,"按下Go键");
                        Log.i(TAG,"设定的跳转位置是mPosition= "+mPosition);
                        mTextView.setText(mTextView.getText()+"设定点：" +(mEditText.getText().toString()) +'\r'+'\n');
                        mMediaPlayer.seekTo(Integer.parseInt((mEditText.getText().toString())));
                        //mMediaPlayer.seekTo(mPosition);
                        //mMediaPlayer.seekTo(30000);//调到30s
                        Log.i(TAG,"跳转到的位置是mPosition= "+mMediaPlayer.getCurrentPosition());
                        play();
                }
            }catch (Exception e){
                Log.e(TAG,e.toString());
            }
        }
    }
    private void play() throws IOException{ //此过程可能抛出异常
        // TODO Auto-generated method stub
        mMediaPlayer.start();//播放
    }
    private void stop() throws IOException { //此过程可能抛出异常
        // TODO Auto-generated method stub
        mMediaPlayer.pause();
    }

    //关于播放位置的确定
    /*
     * mPosition = mMediaPlayer.getCurrentPosition();//保存当前播放点
     * mMediaPlay.seekTo(mPosition);
     * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ///getMenuInflater().inflate(R.menu.activity_main, menu);
        ///return true;
        super.onCreateOptionsMenu(menu);
        Log.i(TAG,"创建菜单");
        menu.add(0,1,1,"播放音频");
        menu.add(0,2,2,"目录");

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        Log.i(TAG,"准备菜单");
        menu.clear();
        if(mMediaPlayer.isPlaying()){
            menu.add(0,1,1,"暂停音频");
            menu.add(0,2,2,"目录");
        }else{
            menu.add(0,1,1,"播放音频");
            menu.add(0,2,2,"目录1");
        }

        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == 1){  //选择“播放音频”
            Log.i(TAG,"菜单选择");
            try{
                //Log.i(TAG,mMediaPlayer.isPlaying()+"");
                if(mMediaPlayer.isPlaying()){
                    stop();
                    Log.i(TAG,"菜单选择");
                }else
                    play();

            }catch (Exception e){
                Log.e(TAG,e.toString());
            }

        }else if(item.getItemId() == 2){ //选择“目录”

        }
        return false;
    }

    @Override
    protected void onDestroy(){
        mMediaPlayer.release();
        super.onDestroy();
        Log.i(TAG,"OnDestroy");
    }
}
