package com.planuri.rootonixsmartmirror.util.file;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.TextView;

import com.planuri.rootonixsmartmirror.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 텍스트 파일 읽기와 읽은 내용을 TextView 에 비동기적으로 업데이트를 수행하기 위해 생성
 * 2023-06-27. Planuri.Ryu
 * **/

public class TextFileLoadTask extends AsyncTask<Pair<TextView, Integer>, Void, Pair<TextView, String>[]> {

    private WeakReference<Context> contextRef;

    public TextFileLoadTask (Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    /**
     * 백그라운드에서 매개변수로 받은 resourceIds 들 만큼
     * resource id 에 해당하는 text 파일들을 읽어서 results 배열에 저장 **/
    @SafeVarargs
    @Override
    protected final Pair<TextView, String>[] doInBackground(Pair<TextView, Integer>... resourcePairs) {

        Context safeContextRef = contextRef.get();
        if(safeContextRef == null) return null;

        Pair<TextView, String>[] results = new Pair[resourcePairs.length];
        for (int i = 0; i < resourcePairs.length; i++) {
            String text = loadTextFromRawResource(safeContextRef, resourcePairs[i].second);
            results[i] = new Pair<>(resourcePairs[i].first, text);
        }
        return results;
    }

    @Override
    protected void onPostExecute(Pair<TextView, String>[] results) {
        if (results != null) {
            for (Pair<TextView, String> result : results) {
                TextView textView = result.first;
                String text = result.second;
                if (textView != null && text != null) {
                    textView.setText(text);
                }
            }
        }
    }

    private String loadTextFromRawResource(Context context, int resourceId) {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                result.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
}
