package com.planuri.rootonixsmartmirror.util.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.planuri.rootonixsmartmirror.R;

public class SoundUtil {
    SoundPool spClick;
    //    SoundPool spShutter, spClick, spCount;
//    SoundPool spMan;
//    SoundPool spWoman;
//    SoundPool spAge10s;
//    SoundPool spAge20s;
//    SoundPool spAge30s;
//    SoundPool spAge40s;
//    SoundPool spAge50s;
    int clickPlay, shutterPlay, countPlay;
//    int man, woman, age10s, age20s, age30s, age40s, age50s;

    private static SoundUtil sm;

    synchronized public static SoundUtil getInstance() {
        if (sm == null) {
            sm = new SoundUtil();
        }
        return sm;
    }

    private SoundUtil() {

    }

    public void initialize(Context context) {

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        spClick = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();

        clickPlay = spClick.load(context, R.raw.click_effect, 0);
        shutterPlay = spClick.load(context, R.raw.camera_shutter, 0);
    }

    public void clickPlay(Context context) {
        spClick.play(clickPlay, 1f, 1f, 0, 0, 1f);
//        spClick = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);
//        clickPlay = spClick.load(context, R.raw.click_effect, 0);
//        spClick.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                soundPool.play(clickPlay, 1f, 1f, 0, 0, 1f);
//            }
//        });
//
//        Handler clickHandler = new Handler();
//        clickHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                spClick.release();
//            }
//        }, 500);
    }

    //only for UserInfoActivity
    public void clickPlayMan(Context context) {

        spClick.play(clickPlay, 1f, 1f, 0, 0, 1f);
//        spMan = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);
//        man = spMan.load(context, R.raw.click_effect_man, 0);
//        spMan.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                soundPool.play(man, 1f, 1f, 0, 0, 1f);
//            }
//        });
//
//        Handler handlerMan = new Handler();
//        handlerMan.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                spMan.release();
//            }
//        }, 500);
    }

    public void shutterPlay(Context context) {
        spClick.play(shutterPlay, 1f, 1f, 0, 0, 1f);
    }
}
