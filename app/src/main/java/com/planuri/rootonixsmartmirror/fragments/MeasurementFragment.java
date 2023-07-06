package com.planuri.rootonixsmartmirror.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.dialog.DialogCompletFragment;
import com.planuri.rootonixsmartmirror.dialog.DialogFailFragment;
import com.planuri.rootonixsmartmirror.dialog.DlgAnalysisAnimation;
import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.dto.ReqSaveAnalysisDto;
import com.planuri.rootonixsmartmirror.dto.ResScalpAnalysisDto;
import com.planuri.rootonixsmartmirror.model.kioskuser.AnalysisImgInfo;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.Utils;
import com.planuri.rootonixsmartmirror.util.retrofit.ScalpAnalysisRetrofit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeasurementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeasurementFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static String TAG = "MeasurementFragment";
     ImageView imgTakePicture;
    /**
     * 권한 관련
     */
    private static int PERMISSIONS_REQ_CODE = 100;  //권한 요청 코드
    //note: 요청 및 확인할 권한 배열
    private String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    /**
     * 카메라 관련
     */
    private String mSelectedCameraId = null; //선택된 카메라 아이디(0=후면, 1=정면, 2=기타). 루토닉스는 0으로 잡힘
    private CameraManager mCameraManager;   //시스템 서비스의 CameraManager
    private CameraDevice mCameraDevice; //Preview, take picture 할 카메라 디바이스. mCameraManager.openCamera 에서 설정한 카메라 디바이스의 stateCallback 함수에서 반환 됨
    private CameraCaptureSession mSession;  //여러 카메라 스트림(미리보기, 캡처 등)을 사용할 카메라 세션

    /**
     * 카메라 화면(Preview) SurfaceView
     */
    private Handler mHandler;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceViewHolder;

    /** 저장 이미지 관련 */
    private Size mImageSize;
    private ImageReader mImageReader;
    private CaptureRequest.Builder mPreviewBuilder;
    private int mDeviceRotation;    //카메라 회전 정보
    private String capturedBitmap;

    public void onkeyevent(KeyEvent event) {

        imgTakePicture.performClick();
    }

    /** 두피 사진 위치 타입 */
    /**private enum PicturePosType {
        fr_1, fr_2, th_1, th_2, lh_1, lh_2, rh_1, rh_2,
        bh_1, bh_2
    }**/
    /** 2023-04-13 수정. 두피사진 위치가 정면(앞머리), 정수리로 각 3장씩 총 6장 **/
    private enum PicturePosType {
        fr_1, fr_2, fr_3,
        th_1, th_2, th_3
    }

    private static final int MAX_SCALP_IMG_COUNT = 6;  //2023-04-13 수정. 전체 10장에서 6장으로 변경
    private String ScalpImgPath[];  //두피 이미지 경로 배열
    private ImageView mNextImageView = null;

    private int mNextTakePos = 0;     //사진 찍을 위치(0 ~ MAX_SCALP_IMG_COUNT, -1이면 모두 찍어서 위치 없음)

    /** 파일 저장 위치 */
    private static String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String SAVE_FOLDER = "/TempScalpImage/";
    private static String SAVE_PATH = EXTERNAL_STORAGE + SAVE_FOLDER;

    ImageView mBtnAnalysis; //분석 버튼

    /** 이미지 회전 */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private ProgressBar mProgressBar;
    private DialogFragment mVideoDlg;
    private Activity activity;

    public MeasurementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeasurementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeasurementFragment newInstance(String param1, String param2) {
        MeasurementFragment fragment = new MeasurementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measurement, container, false);

        /**
         * 사용자 정보 수정 화면 진입
         */
        ImageView measurement_changing_info = view.findViewById(R.id.measurement_changing_info);
        measurement_changing_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(16);
            }
        });

        /**
         * 로그아웃 버튼
         */
        ImageView logout_ms = view.findViewById(R.id.logout_ms);
        logout_ms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(3);
                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * UserService 화면 이동
         */
        ImageView user_service_layout = view.findViewById(R.id.user_service_layout);

        user_service_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "서비스 선택 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).fragmentView(4);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(REQUIRED_PERMISSIONS)) {
                requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQ_CODE);
            }
        }

        mVideoDlg = new DlgAnalysisAnimation(); // 분석 중 동영상 Dialog

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mBtnAnalysis = (ImageView) view.findViewById(R.id.btnProgressAnalysis);
        mBtnAnalysis.setEnabled(false);
        /** 분석요청 클릭 */
        mBtnAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mVideoDlg.show(getActivity().getSupportFragmentManager(), "DlgAnalysisAnimation");

                /** 카메라 없을 때 폴더 이미지로 테스트하기 위한 구문 **/
                /*File targetFolder = new File(SAVE_PATH);
                try {
                    if(!targetFolder.exists()) {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int i = 0;
                        File[] files = targetFolder.listFiles();
                        //파일을 순서대로 보내라고 했으므로 userIdx_fr1 ~ fr3, userIdx_th1 ~ th3 으로 총 6장을 순서대로
                        //보내기 위해 오름차순으로 정렬
                        Arrays.sort(files);

                        for(File file : files) {
                            if(!file.isDirectory()) {
                                ScalpImgPath[i] = file.getName();
                                i++;
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "이미지 파일 삭제 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }*/

                ArrayList<MultipartBody.Part> files = new ArrayList<>();

                for(int i = 0; i < MAX_SCALP_IMG_COUNT; i++) {
                    File imgFile = new File(SAVE_PATH + ScalpImgPath[i]);
                    RequestBody reqFileBody = RequestBody.create(MediaType.parse("image/jpeg"), imgFile);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", ScalpImgPath[i], reqFileBody);
                    files.add(filePart);
                }

                ScalpAnalysisRetrofit.getInstance().getAISvrInterface().reqScalpAnalysis(files).enqueue(new Callback<ResScalpAnalysisDto>() {
                    @Override
                    public void onResponse(Call<ResScalpAnalysisDto> call, Response<ResScalpAnalysisDto> response) {

                        ResScalpAnalysisDto resData;
                        if(response.isSuccessful() && response.body() != null) {    //HTTP 200 ~300 HTTP OK
                            resData = response.body();
                            Log.d(TAG, resData.msg);

                            if (resData == null) {
                                ShowFailedDlg("서버에서 분석결과를 받아오지 못했습니다.");
                            }
                            else if(!Objects.equals(resData.msg, "성공")) {   // 응답 msg 가 "성공"이 아닌 경우
                                ShowFailedDlg(resData.msg);
                            }
                            else {  /** 분석 성공 **/
                                // LoginKioskUser 에 분석 정보 저장
                                LoginKioskUser.getInstance().setAnalysisData(resData.result);
                                LoginKioskUser.getInstance().getAnalysisData().total.analyzedDate = Utils.getCurrentDateTime();

                                /** 분석 성공이므로 API 서버에 분석 데이터 저장 요청 */
                                ReqSaveAnalysisDto reqSaveAnalysisDto = new ReqSaveAnalysisDto();
                                reqSaveAnalysisDto.deadSkinPt = resData.result.total.deadskin;
                                reqSaveAnalysisDto.hairNumPt = resData.result.total.num_hairs;
                                reqSaveAnalysisDto.hairThickPt = resData.result.total.thickness;
                                reqSaveAnalysisDto.sebumPt = resData.result.total.sebum;
                                reqSaveAnalysisDto.sensitivityPt = resData.result.total.sensitive;
                                reqSaveAnalysisDto.diagnosisName = resData.result.total.title;
                                reqSaveAnalysisDto.diagnosisDesc = resData.result.total.short_explain;
                                reqSaveAnalysisDto.totalDesc = resData.result.total.total_explain;
                                reqSaveAnalysisDto.expertSuggest = resData.result.total.expert_suggest;
                                reqSaveAnalysisDto.expertSuggestItem1 = resData.result.total.expert_list.get(0);
                                reqSaveAnalysisDto.expertSuggestItem2 = resData.result.total.expert_list.get(1);

                                for(int i = 0; i < resData.result.total.images_origin.size(); i++) {    //원본

                                    AnalysisImgInfo imgInfo = new AnalysisImgInfo();
                                    /** 이미지 URL 은 순서대로 보낸다고 정의 했으므로 순서대로 받는 것이므로
                                     두피 촬영 위치(1=정면, 2=정수리, 3=좌측두부, 4=우측두부, 5=후두부) 가 각 2장씩(전체 10장) 순서대로 전달됨
                                     <2023-04-13 수정>
                                     두피 촬영 위치(1=정면, 2=정수리)가 각 3장씩(전체 6장) 순서대로 전달로 변경
                                     **/                                     

                                    imgInfo.imgUrl = resData.result.total.images_origin.get(i);
                                    imgInfo.headPosition = (i / 3) + 1; //두피 촬영 위치
                                    imgInfo.imgSource = 1; // 1 = 원본 | 2 = 분석본
                                    imgInfo.imgNum = (i % 3) + 1;   //headPosition 별 이미지 순서

                                    reqSaveAnalysisDto.analysisImgInfo.add(imgInfo);
                                }

                                for(int i = 0; i < resData.result.total.images_result.size(); i++) {    //분석본

                                    AnalysisImgInfo imgInfo = new AnalysisImgInfo();
                                    /** 이미지 URL 은 순서대로 보낸다고 정의 했으므로 순서대로 받는 것으로... */
                                    imgInfo.imgUrl = resData.result.total.images_result.get(i);
                                    imgInfo.headPosition = (i / 3) + 1;
                                    imgInfo.imgSource = 2;
                                    imgInfo.imgNum = (i % 3) + 1;

                                    reqSaveAnalysisDto.analysisImgInfo.add(imgInfo);
                                }

                                /** API 서버에 분석 데이터 저장 요청 */
                                CustomRestClient.getInstance().getApiInterface().saveAnalysisData(LoginKioskUser.getInstance().getKioskUser().kuserIdx,
                                    reqSaveAnalysisDto).enqueue(new Callback<CommonResponse>() {
                                    @Override
                                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                        /** API 서버에 저장 성공 (모든 분석 요청 과정 성공 )*/
                                        if(response.isSuccessful() && response.body() != null) {

                                            /** 분석 요청했던 이미지 폴더 내 모든 파일(이미지) 삭제 */
                                            //폴더 내 전체 파일 삭제
                                            File targetFolder = new File(SAVE_PATH);
                                            try {
                                                File[] files = targetFolder.listFiles();
                                                for(File file : files) {
                                                    if(!file.isDirectory()) {
                                                        file.delete();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), "분석 완료된 이미지 파일 삭제 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                            }

                                            mVideoDlg.dismiss(); //분석 중 동영상 내리기

                                            LoginKioskUser.getInstance().isHistoryCall = false;

                                            DialogFragment dlg = new DialogCompletFragment();
                                            dlg.show(getActivity().getSupportFragmentManager(), "DialogCompletFragment");
                                        }
                                        else {  //HTTP 오류(500)
                                            ShowFailedDlg("분석 결과를 클라우드에 저장 중 오류가 발생했습니다.");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                                        ShowFailedDlg("분석 결과를 클라우드에 저장 중 통신 오류가 발생했습니다.");
                                    }
                                });
                                /** API 서버에 분석 데이터 저장 요청 끝 */

                            }
                            /** 분석 성공 응답 받은 후 루틴 끝 */
                        }
                        else {  // AI SERVER 에 분석 요청 후 HTTP 오류(500)
                            ShowFailedDlg("분석 요청 중 프로토콜 오류가 발생했습니다.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResScalpAnalysisDto> call, Throwable t) {
                        ShowFailedDlg("분석 요청 중 통신 오류가 발생했습니다.");
                    }
                });

                Toast.makeText(getActivity(), "분석요청", Toast.LENGTH_SHORT).show();
            } /** 분석요청 클릭 이벤트 끝 */
        });

        //두피 이미지 배열 초기화
        ScalpImgPath = new String[MAX_SCALP_IMG_COUNT];
        mNextTakePos = getNextPos();    //찍을 이미지 위치
        mNextImageView = (ImageView)view.findViewById(R.id.fr_img_1); //찍을 이미지뷰
        mNextImageView.setImageResource(R.drawable.left_camera);


        HandlerThread handlerThread = new HandlerThread("UsbCamera");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());

        mCameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE); //CameraManager 객체 생성

        mSurfaceView = view.findViewById(R.id.cameraSurfaceView);    //Preview 용 SurfaceView
        initSurfaceView();  // SurfaceView 초기화. 뷰가 생성되면 surfaceCreated 콜백 함수에서 카메라 초기화 및 Preview 생성

        /** 사진 찍기 이미지뷰 클릭 리스너 등록 */
         imgTakePicture = view.findViewById(R.id.take_picture);
        imgTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mNextTakePos == -1) return;  //모두 찍은 상태에서 사진 찍기 클릭. 리턴
                if(mCameraManager == null || mCameraDevice == null) {    //카메라 초기화 안 된 상태
                    Toast.makeText(getActivity(), "카메라 초기화가 안 된 상태에서 촬영을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //클릭 disable, 프로그래스바 보이기, 윈도우 터치 비활성
                setFragmentVisible(false, false, View.VISIBLE);

                takePicture();  // 두피 사진 찍기
            }
        });

        /** 다시찍기 이미지뷰 클릭 리스너 (전체 삭제)*/
        final ImageView imgRetakePicture = view.findViewById(R.id.btn_retake);
        imgRetakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmptyTakenPicture()) { //촬영한 사진 없음
                    Toast.makeText(activity, "촬영한 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //다시찍기 클릭 disable, 프로그래스바 보이기, 윈도우 터치 비활성
                imgRetakePicture.setEnabled(false); // 다시찍기 버튼
                ((ProgressBar)view.findViewById(R.id.retakeProgressBar)).setVisibility(View.VISIBLE); //프로그래스바 활성
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,   //윈도우 터치 막음
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // 촬영한 이미지 모두 초기화
                ImageView tempView;
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);
                for(PicturePosType pos : PicturePosType.values()) {

                    tempView = getImageViewByPos(fragment, pos, false);
                    deleteTakenPicture(tempView, pos.ordinal());

                    tempView = getImageViewByPos(fragment, pos, true);
                    deleteTakenPicture(tempView, pos.ordinal());
                }
                // 두피 이미지 배열 및 위치 초기화
                mNextTakePos = getNextPos();    //찍을 이미지 위치
                mNextImageView = (ImageView)view.findViewById(R.id.fr_img_1); //찍을 이미지뷰
                mNextImageView.setImageResource(R.drawable.left_camera);

                //폴더 내 전체 파일 삭제
                File targetFolder = new File(SAVE_PATH);
                try {
                    if(!targetFolder.exists()) {
                        Toast.makeText(getActivity(), "경로가 존재하지 않는 폴더 내 파일 삭제를 시도했습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        File[] files = targetFolder.listFiles();
                        for(File file : files) {
                            if(!file.isDirectory()) {
                                file.delete();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "이미지 파일 삭제 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                } finally {
                    imgRetakePicture.setEnabled(true); // 다시찍기 버튼 enable
                    ((ProgressBar)view.findViewById(R.id.retakeProgressBar)).setVisibility(View.GONE); //프로그래스바 사라짐
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);    // 윈도우터치 enable
                }

                Toast.makeText(getActivity(), "모든 촬영이미지를 초기화 했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        /** 다시찍기 이미지뷰 클릭 리스너 (전체 삭제) 끝 */

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if(ScalpImgPath != null) {
            ScalpImgPath = null;
        }
        mNextTakePos = 0;
    }

    private boolean isEmptyTakenPicture() {

        for(int i = 0; i < MAX_SCALP_IMG_COUNT; i++) {
            if(ScalpImgPath[i] != null) return false;
        }
        return true;

    }

    /********************************************** 카메라 관련 ***********/

    /**
     * 연결된 카메라 중 첫번째 것으로 초기화
     * Return: 연결된 카메라 ID
     * Params: CameraManager
     */
    public String initFirstCamera(CameraManager cameraManager) {

        String selectedCameraId = null; // return 할 카메라 ID
        Handler mainHandler = new Handler(activity.getMainLooper()); // ImageReader 리스너 핸들러

        try {
            String cameras[] = cameraManager.getCameraIdList(); //연결된 카메라 리스트(0=후면, 1=정면 카메라). 루토닉스 카메라는 후면(0)으로 잡힘
            if (cameras.length > 0) {
                for (String cameraId : cameras) {
                    selectedCameraId = cameraId;
                    CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);   //카메라 정보가 담긴 CameraCharacteristics

                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP); //카메라의 각종 지원 정보

                    Size[] previewSizes = map.getOutputSizes(SurfaceTexture.class); //카메라 제공 미리보기 사이즈 배열
                    Size[] outSizes = map.getOutputSizes(ImageFormat.JPEG);    //카메라 제공 JPG 포맷 Output 이미지 사이즈 배열

                    for(Size size : previewSizes) {
                        Log.i(TAG, "PREVIEW SIZE: [" + size.getWidth() + "X" + size.getHeight() + "]");
                    }
                    for (Size size : outSizes) {
                        Log.i(TAG, "OUTPUT SIZE: " + size.getWidth() + " " + size.getHeight());
                    }
                    /** 루토닉스에서 제공한 카메라 프리뷰 사이즈는 배열 순서대로 1920 1080, 1280 720, 800 480, 640 480, 640 320*/
                    Size previewSize = previewSizes[2];
                    Log.i("SelectedSize", previewSize.getWidth() + " " + previewSize.getHeight());
                    mSurfaceViewHolder.setFixedSize(1280, 720);

                    /** 루토닉스에서 제공한 카메라 OUTPUT SIZE 는 배열 순서대로 1280 720, 1920 1080, 800 480, 640 480, *640 320*/
                    //이미지 사이즈
                    Size outSize = new Size(1280, 720); //outSizes[2];
                    mImageReader = ImageReader.newInstance(outSize.getWidth(), outSize.getHeight(), ImageFormat.JPEG, 5);
                    mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mainHandler); //새 이미지를 사용할 수 있을 때 호출되는 리스너 등록

                    //카메라 Permission 이 없으면 Open 을 못 하므로 확인 후 다시 요청
                    if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        //카메라 오픈
                        mCameraManager.openCamera(selectedCameraId, deviceStateCallback, null);
                    } else {
                        //권한 다시 요청
                        requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQ_CODE);
                    }

                    break;  // 루토닉스는 1개의 카메라만 사용하므로 카메라가 잡히면 해당 카메라 사용하므로 break
                }
                //Toast.makeText(this, "카메라 찾음", Toast.LENGTH_SHORT).show();
            } else {
                selectedCameraId = null;

                Toast.makeText(activity, "연결된 카메라를 찾지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (CameraAccessException e) {
            selectedCameraId = null;
            Toast.makeText(activity, "카메라 초기화에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }

        return selectedCameraId;
    }

    /**
     * 카메라 Preview 생성
     */
    public void createCameraPreview() throws CameraAccessException {
        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mPreviewBuilder.addTarget(mSurfaceViewHolder.getSurface());
        mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceViewHolder.getSurface(), mImageReader.getSurface()), mSessionPreviewStateCallback, mHandler);
    }

    //동작하는 카메라 쓰레드에서 이미지를 Callback 으로 받아올 Session
    private CameraCaptureSession.StateCallback mSessionPreviewStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {   //카메라가 실행 준비되면 실행됨
            mSession = session;

            try {
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Toast.makeText(activity, "카메라 구성 실패", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 카메라 Preview 용 SurfaceView 초기화
     */
    public void initSurfaceView() {
        //DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mSurfaceViewHolder = mSurfaceView.getHolder();
        mSurfaceViewHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (mCameraManager == null) {
                    Toast.makeText(activity, "Camera Manager NULL 오류", Toast.LENGTH_SHORT).show();
                    return;
                }

                mSelectedCameraId = initFirstCamera(mCameraManager); //첫번째 잡히는 카메라로 초기화
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCameraDevice != null) {
                    mCameraDevice.close();
                    mCameraDevice = null;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
    }

    /******** 카메라 디바이스 콜백 함수들 *******/
    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) { // 카메라 오픈 성공
            mCameraDevice = camera;
            try {
                createCameraPreview();  // 카메라 보기 생성
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Toast.makeText(activity, "카메라 상태 처리 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    /********************************* 카메라 사진 찍기 관련 *********************************/

    /**
     * 이미지리더 리스너. (takePicture 로) 새 이미지가 들어오면 호출됨
     */
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireLatestImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            //이미지 저장
            new SaveImageTask().execute(bitmap);
            image.close();
        }
    };

    public void takePicture() {
        try {
            if (mCameraDevice != null) {
                // 새 캡처 요청을 위한 빌더 생성. TEMPLATE_STILL_CAPTURE = 프레임 속도보다 이미지 품질을 우선시 하여 이미지 캡처에 적합
                CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

                captureRequestBuilder.addTarget(mImageReader.getSurface()); // 캡처 대상 Surface
                captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);   //캡처 요청 필드

                //mDeviceRotation = getResources().getConfiguration().orientation;
                mDeviceRotation = ORIENTATIONS.get(0);
                //mDeviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                Log.d("@@@", "rotation" + mDeviceRotation + "");

                captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mDeviceRotation);
                CaptureRequest mCaptureRequest = captureRequestBuilder.build();
                mSession.capture(mCaptureRequest, mSessionCaptureCallback, mHandler);
            } else {

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            mSession = session;
//            unlockFocus();
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            mSession = session;
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            /** 카메라 capture 실패 */
            //사진 촬영 버튼 enable, 윈도우 클릭 enable, progressbar 사라짐
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    setFragmentVisible(true, true, View.GONE);
                    Toast.makeText(getActivity(), failure.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    /******************************* 사진 찍은 후 이미지 저장 관련 *********************************/

    private class SaveImageTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Bitmap... data) {
            Matrix matrix = new Matrix();
            Bitmap bitmap = null;
            Bitmap rotated_bitmap = null;

            //matrix.preRotate(-90, 0, 0);
            matrix.preScale(1.0f, -1.0f);
            try {
                bitmap = getRotatedBitmap(data[0], mDeviceRotation);
                rotated_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                //Const.capture_bitmap = rotated_bitmap.toString();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                capturedBitmap = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }


            /** 파일형식은(useridx_[촬영위치]_[촬영위치별번호]_yyyymmddhhmmss.jpg
             *  ex) 3_fr_1_20221123123217.jpg*/
            String filenameHead = "";
            // userIdx
            if(LoginKioskUser.getInstance().isLogin()) {    //로그인 한 유저면 userIdx 값, 로그인 하지 않으면 0
                filenameHead = String.valueOf(LoginKioskUser.getInstance().getKioskUser().kuserIdx);
            }
            else {
                filenameHead = "0";
            }

            // 촬영위치 및 위치별번호
            PicturePosType [] posTypes =  PicturePosType.values();
            filenameHead += "_" + posTypes[mNextTakePos].name();

            // 촬영날짜시간
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");

            String filename = filenameHead + "_" + simpleDate.format(date);
            String strSaveFilePath = insertImage(activity.getContentResolver(), rotated_bitmap, filename);

            if(strSaveFilePath != null) {
                insImageByCurrentPos(rotated_bitmap, strSaveFilePath, posTypes);
            }

            return null;
        }
    }

    /** 다음 두피 사진 찍을 위치 -1이면 모두 찍어서 찍을 위치가 없음 */
    private int getNextPos() {

        int currentPos = -1;
        for(int idx = 0; idx < MAX_SCALP_IMG_COUNT; idx++) {
            if(ScalpImgPath[idx] == null) {
                currentPos = idx;
                break;
            }
        }
        return currentPos;
    }

    private void insImageByCurrentPos(Bitmap bitmap, String strSaveFilePath, PicturePosType posType[]) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                //현재 프래그먼트
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);

                if(fragment.isVisible() && fragment instanceof MeasurementFragment) {

                    ImageView tempImageView = getImageViewByPos(fragment, posType[mNextTakePos], false); //사진 위치에 따른 이미지뷰
                    ImageView iconImageView = getImageViewByPos(fragment, posType[mNextTakePos], true); //x 아이콘 이미지뷰

                    //찍은 이미지 삽입
                    tempImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    tempImageView.setImageBitmap(bitmap);
                    iconImageView.setImageResource(R.drawable.icon_photo_delete_n1); //x 아이콘 이미지

                    ScalpImgPath[mNextTakePos] = strSaveFilePath;

                    /** 아이콘 이미지뷰에 클릭(재촬영/삭제) 리스너 설정 */
                    final int listenViewPos = mNextTakePos;
                    iconImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // 선택한 이미지 파일 삭제
                            String filePath = SAVE_PATH + ScalpImgPath[listenViewPos];
                            File delFile;
                            try {
                                delFile = new File(filePath);
                                if(delFile.exists()) {
                                    delFile.delete();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "이미지 파일 삭제 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                            }

                            // 선택한 이미지 다시찍기(삭제), X아이콘 삭제
                            deleteTakenPicture(tempImageView, listenViewPos);
                            deleteTakenPicture(iconImageView, listenViewPos);

                            // 현재 찍으려는 카메라 이미지 삭제
                            if(mNextImageView != null) {    //전체 찍은 상태에서 이미지 다시 찍는 경우에는 현재 찍고 있는 이미지는 null 이다.
                                deleteTakenPicture(mNextImageView, mNextTakePos);
                            }
                            else {  // null 이므로 전체 찍은 상태에서 다시찍기 한 경우이다. 분석하기 버튼 비활성
                                mBtnAnalysis.setImageResource(R.drawable.analysis_btn);
                                mBtnAnalysis.setEnabled(false);
                            }

                            // 다음 찍을 위치 조정
                            mNextTakePos = getNextPos();
                            mNextImageView = getImageViewByPos(fragment, posType[mNextTakePos], false);
                            mNextImageView.setImageResource(R.drawable.left_camera);
                        }
                    });
                    /** 아이콘(X) 이미지뷰 클릭 리스너 끝 */

                    mNextTakePos = getNextPos();    //다음 찍을 위치 선택
                    if(mNextTakePos == -1) { //모두 찍었음
                        mNextImageView = null;
                        //분석버튼 활성
                        mBtnAnalysis.setImageResource(R.drawable.analysis_btn_on);
                        mBtnAnalysis.setEnabled(true);
                    }
                    else {
                        //다음 이미지뷰에 카메라 아이콘 삽입
                        ImageView nextImageView = getImageViewByPos(fragment, posType[mNextTakePos], false);
                        nextImageView.setImageResource(R.drawable.left_camera);
                        mNextImageView = nextImageView;
                    }

                    //사진 촬영 버튼 enable, 윈도우 클릭 enable, progressbar 사라짐
                    setFragmentVisible(true, true, View.GONE);

                    //Toast.makeText(getActivity(), strSaveFilePath + " 에 저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setFragmentVisible(boolean isTakeBtnClickable, boolean isWindowTouchable, int progressBarVisible) {

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);

        if(fragment.isVisible() && fragment instanceof MeasurementFragment) {
            /** 사진찍기 클릭, 프로그래스바,윈도우 터치 활성 / 비활성 */
            (fragment.getView().findViewById(R.id.take_picture)).setEnabled(isTakeBtnClickable); //사진찍기 버튼
            mProgressBar.setVisibility(progressBarVisible); //프로그래스바
            if(isWindowTouchable) { //윈도우 터치
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            else {
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }

    /** (두피 이미지 위치)PicturePosType 에 따른 ImageView 반환 및 이미지뷰 클릭 리스너 처리
     * PARAM isIcon 은 두피 사진 우하향 'X' 아이콘(재촬영) 이미지 뷰인지 여부*/
    private ImageView getImageViewByPos(Fragment fragment, PicturePosType posType, boolean isIcon) {

        ImageView tempView = null;

        switch (posType) {
            case fr_1: {    //앞머리1
                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_1);
                else tempView = fragment.getView().findViewById(R.id.fr_img_1);
                break;
            }
            case fr_2: {    //앞머리2
                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_2);
                else tempView = fragment.getView().findViewById(R.id.fr_img_2);
                break;
            }
            case fr_3: {    //앞머리3
                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_3);
                else tempView = fragment.getView().findViewById(R.id.fr_img_3);
                break;
            }
            case th_1: {    //정수리1
                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_4);
                else tempView = fragment.getView().findViewById(R.id.th_img_1);
                break;
            }
            case th_2: {    //정수리2
                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_5);
                else tempView = fragment.getView().findViewById(R.id.th_img_2);
                break;
            }
            case th_3: {    //정수리3
                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_6);
                else tempView = fragment.getView().findViewById(R.id.th_img_3);
                break;
            }
/*            case lh_2: {
                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_6);
                else tempView = fragment.getView().findViewById(R.id.lh_img_2);
                break;
            }
//            case rh_1: {
//                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_7);
//                else tempView = fragment.getView().findViewById(R.id.rh_img_1);
//                break;
//            }
//            case rh_2: {
//                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_8);
//                else tempView = fragment.getView().findViewById(R.id.rh_img_2);
//                break;
//            }
//            case bh_1: {
//                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_9);
//                else tempView = fragment.getView().findViewById(R.id.bh_img_1);
//                break;
//            }
//            case bh_2: {
//                if(isIcon) tempView = fragment.getView().findViewById(R.id.retake_img_10);
//                else tempView = fragment.getView().findViewById(R.id.bh_img_2);
//                break;
//            }*/
        }
        return tempView;
    }

    /** 찍은 사진 삭제 */
    private void deleteTakenPicture(ImageView delView, int pos) {
        if(pos < 0 && pos >= MAX_SCALP_IMG_COUNT) return;

        delView.setImageResource(0); //이미지뷰 X 이미지 삭제
        delView.setBackgroundColor(Color.parseColor("#1A1A1A"));
        ScalpImgPath[pos] = null;;    //이미지 경로 배열에서 해당 위치 삭제
        delView.setOnClickListener(null);    //클릭 리스너 삭제
    }

    public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception {
        if (bitmap == null) return null;
        if (degrees == 0) return bitmap;

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    public static final String insertImage(ContentResolver cr,
                                           Bitmap source,
                                           String title) {

        String filename = title + ".jpg";
        String stringUrl = null;    /* value to be returned */

        File file_path;
        try {
            file_path = new File(SAVE_PATH);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }

            if (source != null) {
                FileOutputStream imageOut = new FileOutputStream(SAVE_PATH + filename);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 80, imageOut);
                } finally {
                    imageOut.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return filename;
    }

    /********************************* 분석 요청 관련 ***********************************/

    private void ShowFailedDlg(String cause) {

        Bundle args = new Bundle();
        args.putString("cause", cause);

        DialogFragment dialogFragment = new DialogFailFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "DialogFailFragment");
        mVideoDlg.dismiss();

    }

    /********************************* Permission(권한) 관련 ***********************************/
    private boolean hasPermissions(String[] permissions) {
        int result;

        for (String perms : permissions) {
            result = ContextCompat.checkSelfPermission(activity, perms);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 거부: grantResults[i] 거부 = -1, 허용: grantResults[i] = 0 (PackageManager.PERMISSION_GRANTED)
        if (requestCode == PERMISSIONS_REQ_CODE && grantResults.length > 0) {
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writeExStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean readExStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            //권한별 확인 후 메시지 알림은 나중에...
        }
    }
}