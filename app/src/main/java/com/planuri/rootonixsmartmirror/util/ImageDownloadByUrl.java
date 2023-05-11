package com.planuri.rootonixsmartmirror.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageDownloadByUrl extends AsyncTask<String, String, List<Bitmap>> {
    // 다운로드 완료 후 호출 할 리스너
    public interface OnPostDownLoadListener {
        void onPost(List<Bitmap> bitmaps);
    }

    private OnPostDownLoadListener onPostDownLoad;

    // 리스너 세팅
    public ImageDownloadByUrl(OnPostDownLoadListener paramOnPostDownLad) {
        onPostDownLoad = paramOnPostDownLad;
    }

    @Override
    protected List<Bitmap> doInBackground(String... strings) {
        try {
            // 파라미터로 받은 url 로 부터 이미지 다운로드
            List<Bitmap> bitmaps = new ArrayList<>();
            for(int i = 0; i < strings.length; i++) {
                bitmaps.add(BitmapFactory.decodeStream((InputStream) new URL(strings[i]).getContent()));
            }
            return bitmaps;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmaps) {
        super.onPostExecute(bitmaps);
        // 이미지 다운로드 완료 후 리스너 호출
        if (onPostDownLoad != null)
            onPostDownLoad.onPost(bitmaps);
    }
}
