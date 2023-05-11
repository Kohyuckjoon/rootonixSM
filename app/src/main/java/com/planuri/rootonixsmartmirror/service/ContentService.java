package com.planuri.rootonixsmartmirror.service;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.util.AppTrace;
import com.planuri.rootonixsmartmirror.util.Util;
import com.planuri.rootonixsmartmirror.util.gson_converter.PreferenceUtils;
import com.planuri.rootonixsmartmirror.util.log.LoggerInterface;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

public class ContentService implements LoggerInterface {

    Activity mActivity;

    private ImageView imgContent;
    private VideoView vidContent;
    private LinearLayout llWarning;
    private FrameLayout flContent;

    ArrayList<String> filenames = new ArrayList<>();
    ArrayList<String> extensions = new ArrayList<>();

    CountDownTimer imageRotation = null;


    int track = 0;
    String path;

    public ContentService(Activity activity){
//        this.imgContent = activity.findViewById(R.id.img_content);
//        this.vidContent = activity.findViewById(R.id.vid_content);
//        this.llWarning = activity.findViewById(R.id.ll_warning);
//        this.flContent = activity.findViewById(R.id.fl_content);
        this.mActivity = activity;
    }

    public void initializeContents(){
        if (!PreferenceUtils.MountPath.notNull(mActivity.getApplicationContext())){
            flContent.setVisibility(View.GONE);
            llWarning.setVisibility(View.VISIBLE);
        } else {
            flContent.setVisibility(View.VISIBLE);
            llWarning.setVisibility(View.GONE);
        }

        imageRotation = new CountDownTimer(10000, 10000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                playNextSlot();
            }
        };

        path = PreferenceUtils.MountPath.get(mActivity.getApplicationContext())+"/smartmirror/slot";
        Log.e(">>>>>", path);
        mLogger.info("mountedPath : "+path);

        try {
            URI uri = URI.create(path);
            File folder = new File(uri);

            if (folder.exists()) {
                String[] files = folder.list();
                Arrays.sort(files);

                for (int i = 0; i < files.length; i++){
                    Log.e(">>>>>files", files[i]);
                    mLogger.info("Slot1 Track : " + files[i]);
                    filenames.add(files[i]);
                    extensions.add(Util.getFielExtension(files[i]));
                }
            } else {
                //USB마운트 안될시 동작
            }

            playNextSlot();
        } catch (Exception e){
            mLogger.error("initialize Contents Fail : " + e.getMessage());
            AppTrace.e(e);
            e.printStackTrace();
        }
    }

    public void playNextSlot(){
        if (track == filenames.size()){
            track = 0;
        }

        if (extensions.get(track).equals("jpg") || extensions.get(track).equals("jpeg")
                || extensions.get(track).equals("png") || extensions.get(track).equals("bmp")){
            imgContent.setVisibility(View.VISIBLE);
            vidContent.setVisibility(View.INVISIBLE);

            //apply는 이미지의 캐시를 지우기 위함
            Glide.with(mActivity.getApplicationContext())
                    .load(path+"/"+filenames.get(track))
                    .apply(new RequestOptions()
                            .signature(new ObjectKey(System.currentTimeMillis()))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(imgContent);

            imageRotation.start();
        } else if (extensions.get(track).equals("mp4") || extensions.get(track).equals("m4a") || extensions.get(track).equals("mkv")
                || extensions.get(track).equals("webM") || extensions.get(track).equals("avi") || extensions.get(track).equals("mov")
                || extensions.get(track).equals("MP4") || extensions.get(track).equals("M4A") || extensions.get(track).equals("MKV")
                || extensions.get(track).equals("AVI") || extensions.get(track).equals("MOV")){
            imgContent.setVisibility(View.INVISIBLE);
            vidContent.setVisibility(View.VISIBLE);

            vidContent.setVideoPath(path+"/"+filenames.get(track));
            vidContent.requestFocus();
            vidContent.start();
            vidContent.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playNextSlot();
                }
            });
        }

        track+=1;
        Log.e("Playing Track", String.valueOf(track));
        Log.e("File Path", path+"/"+filenames.get(track-1));
        mLogger.info("Playing Slot Track : "+ String.valueOf(track));
        mLogger.info("Slot1 File Path : " + path+"/"+filenames.get(track-1));
    }

    public void stop(){
        imageRotation.cancel();
    }
}
