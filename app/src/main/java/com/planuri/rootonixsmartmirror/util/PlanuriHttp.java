package com.planuri.rootonixsmartmirror.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.planuri.rootonixsmartmirror.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlanuriHttp extends AsyncTask<Void, Void, Object> {
    static final String TAG = PlanuriHttp.class.getSimpleName();
    static final Logger log = LoggerFactory.getLogger("timeBase");

    /*연결 요청 메서드(다른 값이 필요할때마다 추가 합시다)*/
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    public static final String INFO_API = "INFO_API";

    boolean bShowExcceptionDialog = true;
    boolean bShowProgressDiaog    = false;

    AlertDialog m_showingDialog = null;
    Context m_context = null;

    String m_authToken = null;
    String m_authId = null;
    String m_reqMethos = null;
    String m_addressString;
    URL m_url = null;
    HashMap<String, Object> m_params = null;
    HashMap<String, Object> m_headers = null;
    HashMap<String, Object> m_infoMap = null;
    HttpURLConnection m_connection = null;
    APIListener m_listener = null;

    public PlanuriHttp(@NonNull Context context) {
        this.m_params = new HashMap<>();
        this.m_infoMap = new HashMap<>();
        this.m_headers = new HashMap<>();
        this.m_context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(bShowProgressDiaog == true) {
            m_showingDialog = new AlertDialog.Builder(m_context)
//                            .setView(R.layout.dlg_progress)
                            .setCancelable(false).create();
            m_showingDialog.show();
            m_showingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    public PlanuriHttp setHeaderParam(String k, Object v){
        m_headers.put(k, v);
        return this;

    }

    public PlanuriHttp setHeaderAuthToken(String authToken){
        m_headers.put("auth_token", authToken);
        return this;
    }

    public PlanuriHttp setHeaderAuthId(String authId){
        m_headers.put("id", authId);
        return this;
    }


    @Override
    protected Object doInBackground(Void... voids) {
        String result = "";
        try {
            Set<Map.Entry<String, Object>> iterator = m_params.entrySet();
            boolean isFirst = true;
            String paramString="";
            for(Map.Entry item : iterator) {
                if(isFirst == true) {
                    paramString += "?";
                    isFirst = false;
                }else { paramString += "&"; }
                paramString += item.getKey() + "=" + item.getValue();
            }

            m_addressString += paramString;
            Log.d(TAG, "서버 전송요청 : " + m_addressString);

            m_url = new URL(m_addressString);
            m_connection = (HttpURLConnection)m_url.openConnection();
            for(Map.Entry<String, Object> e : m_headers.entrySet()) {
                m_connection.setRequestProperty(e.getKey(), e.getValue().toString());
            }

            if(m_connection == null) {
                log.info("Failed Connect");
            }
            m_connection.setRequestMethod(m_reqMethos);
            m_connection.setDoInput(true);
            m_connection.setConnectTimeout(10000);

            InputStream is = m_connection.getInputStream();
            BufferedInputStream stream = new BufferedInputStream(is);
            byte[] contents = new byte[32768];
            int bytesRead = 0;
            while((bytesRead = stream.read(contents)) != -1) {
                result += new String(contents,0, bytesRead, "UTF-8");
            }
            m_connection.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
            return e;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return e;
        } catch (SocketException e) {
            e.printStackTrace();
            return e;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e;
        } catch (IOException e) {
            e.printStackTrace();
            return e;
        } finally {
        }
        return result;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(m_showingDialog != null && m_showingDialog.isShowing()) {
            m_showingDialog.setCancelable(true);
            m_showingDialog.dismiss();
            m_showingDialog = null;
        }
    }

    @Override
    protected void onPostExecute(final Object s) {
        if(m_showingDialog != null && m_showingDialog.isShowing()) {
            m_showingDialog.setCancelable(true);
            m_showingDialog.dismiss();
            m_showingDialog = null;
        }

        if(s instanceof String) {
            if(m_listener != null ) {
                if(m_context instanceof Activity) {
                    ((Activity) m_context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_listener.onSuccess(s.toString());
                        }
                    });
                } else {
                    m_listener.onSuccess(s.toString());
                }
            }
        } else if(s instanceof ProtocolException){
            log.info(String.format("%s catchException : ProtocolException", m_addressString));
            if(m_listener != null){
                if(m_context instanceof Activity) {
                    ((Activity) m_context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_listener.onFailed((ProtocolException)s , m_params);
                        }
                    });
                } else {
                    m_listener.onFailed((ProtocolException)s , m_params);
                }
            }
        }else if(s instanceof SocketTimeoutException){
            log.info(String.format("%s catchException : SocketTimeoutException", m_addressString));
            if(m_listener != null){
                if(m_context instanceof Activity) {
                    ((Activity) m_context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_listener.onFailed((SocketTimeoutException)s , m_params);        }
                    });
                } else {
                    m_listener.onFailed((SocketTimeoutException)s , m_params);
                }

            }
        }else if(s instanceof SocketException){
            log.info(String.format("%s catchException : SocketException", m_addressString));
            if(m_listener != null){
                if(m_context instanceof Activity) {
                    ((Activity) m_context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_listener.onFailed((SocketException)s , m_params);
                        }
                    });
                } else {
                    m_listener.onFailed((SocketException)s , m_params);
                }
            }
        }else if(s instanceof MalformedURLException){
            log.info(String.format("%s catchException : MalformedURLException", m_addressString));
            if(m_listener != null){
                if(m_context instanceof Activity) {
                    ((Activity) m_context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_listener.onFailed((MalformedURLException)s , m_params);
                        }
                    });
                } else {
                    m_listener.onFailed((MalformedURLException)s , m_params);
                }
            }
        }else if(s instanceof IOException){
            log.info(String.format("%s catchException : IOException", m_addressString));
            if(m_listener != null){
                if(m_context instanceof Activity) {
                    ((Activity) m_context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_listener.onFailed((IOException)s , m_params);
                        }
                    });
                } else {
                    m_listener.onFailed((IOException)s , m_params);
                }
            }
        }
        super.onPostExecute(s);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
        if(m_showingDialog != null && m_showingDialog.isShowing()) {
            m_showingDialog.dismiss();
            m_showingDialog = null;
        }
    }

    /*private void popuoDialog(final String msg, final int buttonFlag) {
        if(m_context != null) {
            NoticeDialog dlg = new NoticeDialog(m_context);
            dlg.create();
            dlg .setButtonFlag(buttonFlag)
                .showInputPassword(false)
                .setContentString(msg)
                .setContentStringSize(32)
                .setListener(new NoticeDialog.OnInteractListener() {
                    @Override
                    public void onInteract(int interactedType, @Nullable String inputPasswd) {
                        switch(interactedType) {

                        }
                    }
                }).showDialog();
        } else {
            log.info(msg+" 메시지를 출력 하고자 하였으나 실패했습니다.");
        }
    }*/

    public PlanuriHttp setInfo(String k, Object v) {
        m_infoMap.put(k, v);
        return this;
    }


    public PlanuriHttp setShowExeptionDialog(boolean condition){
        bShowExcceptionDialog = condition;
        return this;
    }

    public PlanuriHttp setShowProgressDialog(boolean condition){
        bShowProgressDiaog = condition;
        return this;
    }

    public PlanuriHttp setUrl (String url) {
        m_addressString = url;
        return this;
    }

    public PlanuriHttp setListener(APIListener listener) {
        m_listener = listener;
        return this;
    }

    public PlanuriHttp setParam(String key, Object value) {
        m_params.put(key, value);
        return this;
    }

    public PlanuriHttp removeParam(String key) {
        m_params.remove(key);
        return this;
    }

    public PlanuriHttp setRequestMethod(String method) {
        m_reqMethos = method;
        return this;
    }

    public interface APIListener {
        void onSuccess(String res);
        void onFailed(Exception e, Map map);
    }


}
