package com.planuri.rootonixsmartmirror.dialog;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;

public class DlgAnalysisAnimation extends DialogFragment {

    private VideoView mAnalysisVideo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);   // 외부 클릭 시 dismiss 할 수 없게
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag_dlg_video, container, false);

        /** 분석 대기 동영상 설정 및 리스너 등록 */
        mAnalysisVideo = (VideoView) view.findViewById(R.id.analysisVideoView);

        // 사용법 동영상 URI
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.progress_mirror);
        mAnalysisVideo.setVideoURI(uri);

        //동영상 준비 완료 리스너
        mAnalysisVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // 준비 완료되면 비디오 재생
                mediaPlayer.start();
            }
        });

        //동영상 재상 완료 리스너
        mAnalysisVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)  {
                //동영상 완료되면 다시 처음부터 재생
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });

        return view;
    }
}
