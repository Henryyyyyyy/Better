//package me.henry.versatile.fragment.live;
//
//import android.content.res.Configuration;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import com.github.faucamp.simplertmp.RtmpHandler;
//import net.ossrs.yasea.SrsCameraView;
//import net.ossrs.yasea.SrsEncodeHandler;
//import net.ossrs.yasea.SrsPublisher;
//import net.ossrs.yasea.SrsRecordHandler;
//import java.io.IOException;
//import java.net.SocketException;
//import java.util.Random;
//
//import butterknife.BindView;
//import me.henry.versatile.R;
//import me.henry.versatile.base.PermissionCheckerFragment;
//
//import static me.henry.versatile.app.Canute.getApplicationContext;
//
///**
// * Created by henry on 2017/11/30.
// */
//
//public class AnchorFragment extends PermissionCheckerFragment implements SrsEncodeHandler.SrsEncodeListener, RtmpHandler.RtmpListener, SrsRecordHandler.SrsRecordListener {
//    @BindView(R.id.publish)
//    Button btnPublish;
//    @BindView(R.id.swCam)
//    Button btnSwitchCamera;
//    @BindView(R.id.record)
//    Button btnRecord;
//    @BindView(R.id.swEnc)
//    Button btnSwitchEncoder;
//    @BindView(R.id.url)
//    EditText efu;
//    private String rtmpUrl = "rtmp://192.168.2.20:1935/zengjin/room";
//    private String recPath = Environment.getExternalStorageDirectory().getPath() + "/test.mp4";
//
//    private SrsPublisher mPublisher;
//
//    @Override
//    public Object setLayout() {
//        return R.layout.fragment_anchor;
//    }
//
//    @Override
//    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
//        _mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        efu.setText(rtmpUrl);
//        mPublisher = new SrsPublisher((SrsCameraView) mRootView.findViewById(R.id.glsurfaceview_camera));
//        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
//        mPublisher.setRtmpHandler(new RtmpHandler(this));
//        mPublisher.setRecordHandler(new SrsRecordHandler(this));
//        mPublisher.setPreviewResolution(640, 360);
//        mPublisher.setOutputResolution(360, 640);
//        mPublisher.setVideoHDMode();
//        mPublisher.startCamera();
//
//        btnPublish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (btnPublish.getText().toString().contentEquals("publish")) {
//                    rtmpUrl = efu.getText().toString();
//                    mPublisher.startPublish(rtmpUrl);
//                    mPublisher.startCamera();
//
//                    if (btnSwitchEncoder.getText().toString().contentEquals("soft encoder")) {
//                        Toast.makeText(getApplicationContext(), "Use hard encoder", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Use soft encoder", Toast.LENGTH_SHORT).show();
//                    }
//                    btnPublish.setText("stop");
//                    btnSwitchEncoder.setEnabled(false);
//                } else if (btnPublish.getText().toString().contentEquals("stop")) {
//                    mPublisher.stopPublish();
//                    mPublisher.stopRecord();
//                    btnPublish.setText("publish");
//                    btnRecord.setText("record");
//                    btnSwitchEncoder.setEnabled(true);
//                }
//            }
//        });
//
//        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
//                }
//        });
//
//        btnRecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (btnRecord.getText().toString().contentEquals("record")) {
//                    if (mPublisher.startRecord(recPath)) {
//                        btnRecord.setText("pause");
//                    }
//                } else if (btnRecord.getText().toString().contentEquals("pause")) {
//                    mPublisher.pauseRecord();
//                    btnRecord.setText("resume");
//                } else if (btnRecord.getText().toString().contentEquals("resume")) {
//                    mPublisher.resumeRecord();
//                    btnRecord.setText("pause");
//                }
//            }
//        });
//
//        btnSwitchEncoder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (btnSwitchEncoder.getText().toString().contentEquals("soft encoder")) {
//                    mPublisher.switchToSoftEncoder();
//                    btnSwitchEncoder.setText("hard encoder");
//                } else if (btnSwitchEncoder.getText().toString().contentEquals("hard encoder")) {
//                    mPublisher.switchToHardEncoder();
//                    btnSwitchEncoder.setText("soft encoder");
//                }
//            }
//        });
//    }
//
//    private static final String TAG = "zengjin";
//
//
//
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        final Button btn =  mRootView.findViewById(R.id.publish);
//        btn.setEnabled(true);
//        mPublisher.resumeRecord();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mPublisher.pauseRecord();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mPublisher.stopPublish();
//        mPublisher.stopRecord();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mPublisher.stopEncode();
//        mPublisher.stopRecord();
//        btnRecord.setText("record");
//        mPublisher.setScreenOrientation(newConfig.orientation);
//        if (btnPublish.getText().toString().contentEquals("stop")) {
//            mPublisher.startEncode();
//        }
//        mPublisher.startCamera();
//    }
//
//    private static String getRandomAlphaString(int length) {
//        String base = "abcdefghijklmnopqrstuvwxyz";
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            int number = random.nextInt(base.length());
//            sb.append(base.charAt(number));
//        }
//        return sb.toString();
//    }
//
//    private static String getRandomAlphaDigitString(int length) {
//        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            int number = random.nextInt(base.length());
//            sb.append(base.charAt(number));
//        }
//        return sb.toString();
//    }
//
//    private void handleException(Exception e) {
//        try {
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            mPublisher.stopPublish();
//            mPublisher.stopRecord();
//            btnPublish.setText("publish");
//            btnRecord.setText("record");
//            btnSwitchEncoder.setEnabled(true);
//        } catch (Exception e1) {
//            //
//        }
//    }
//
//    // Implementation of SrsRtmpListener.
//
//    @Override
//    public void onRtmpConnecting(String msg) {
//        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRtmpConnected(String msg) {
//        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRtmpVideoStreaming() {
//    }
//
//    @Override
//    public void onRtmpAudioStreaming() {
//    }
//
//    @Override
//    public void onRtmpStopped() {
//        Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRtmpDisconnected() {
//        Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRtmpVideoFpsChanged(double fps) {
//        Log.i(TAG, String.format("Output Fps: %f", fps));
//    }
//
//    @Override
//    public void onRtmpVideoBitrateChanged(double bitrate) {
//        int rate = (int) bitrate;
//        if (rate / 1000 > 0) {
//            Log.i(TAG, String.format("Video bitrate: %f kbps", bitrate / 1000));
//        } else {
//            Log.i(TAG, String.format("Video bitrate: %d bps", rate));
//        }
//    }
//
//    @Override
//    public void onRtmpAudioBitrateChanged(double bitrate) {
//        int rate = (int) bitrate;
//        if (rate / 1000 > 0) {
//            Log.i(TAG, String.format("Audio bitrate: %f kbps", bitrate / 1000));
//        } else {
//            Log.i(TAG, String.format("Audio bitrate: %d bps", rate));
//        }
//    }
//
//    @Override
//    public void onRtmpSocketException(SocketException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRtmpIOException(IOException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRtmpIllegalStateException(IllegalStateException e) {
//        handleException(e);
//    }
//
//    // Implementation of SrsRecordHandler.
//
//    @Override
//    public void onRecordPause() {
//        Toast.makeText(getApplicationContext(), "Record paused", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRecordResume() {
//        Toast.makeText(getApplicationContext(), "Record resumed", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRecordStarted(String msg) {
//        Toast.makeText(getApplicationContext(), "Recording file: " + msg, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRecordFinished(String msg) {
//        Toast.makeText(getApplicationContext(), "MP4 file saved: " + msg, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRecordIOException(IOException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
//        handleException(e);
//    }
//
//    // Implementation of SrsEncodeHandler.
//
//    @Override
//    public void onNetworkWeak() {
//        Toast.makeText(getApplicationContext(), "Network weak", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onNetworkResume() {
//        Toast.makeText(getApplicationContext(), "Network resume", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
//        handleException(e);
//    }
//
//
//
//
//}
