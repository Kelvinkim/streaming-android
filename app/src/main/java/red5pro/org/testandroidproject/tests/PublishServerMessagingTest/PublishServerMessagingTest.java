package red5pro.org.testandroidproject.tests.PublishServerMessagingTest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.event.R5ConnectionListener;
import com.red5pro.streaming.event.R5RemoteCallContainer;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;
import com.red5pro.streaming.view.R5VideoView;

import java.util.HashMap;
import java.util.Map;

import red5pro.org.testandroidproject.R;
import red5pro.org.testandroidproject.TestDetailFragment;
import red5pro.org.testandroidproject.tests.TestContent;


public class PublishServerMessagingTest extends TestDetailFragment implements R5ConnectionListener, View.OnClickListener {

    protected R5VideoView preview;

    protected Button button1;
    protected Button button2;
    protected Button button3;
    protected Button button4;
    protected Button button5;

    protected boolean publishing;

    protected R5Stream publish;
    protected Camera cam;
    protected R5Camera camera;
    protected int camOrientation;

    public PublishServerMessagingTest(){

    }

    @Override
    public void onConnectionEvent(R5ConnectionEvent event) {
        Log.d("Publisher", ":onConnectionEvent " + event.name());

        if (event == R5ConnectionEvent.START_STREAMING) {
            publishing = true;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    enableButtons();
             }});
        }
        else if (event == R5ConnectionEvent.DISCONNECTED) {
            publishing = false;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    disableButtons();
            }});
        }
        else if (event.name() == R5ConnectionEvent.LICENSE_ERROR.name()) {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(PublishServerMessagingTest.this.getActivity()).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("License is Invalid");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",
                            new DialogInterface.OnClickListener()

                            {
                                public void onClick (DialogInterface dialog,int which){
                                    dialog.dismiss();
                                }
                            }

                    );
                    alertDialog.show();
                }
            });
        }
    }



    private void enableButtons()
    {
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        button5.setEnabled(true);
    }



    private void disableButtons()
    {
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        button5.setEnabled(false);
    }


    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.connection_messaging_test, container, false);

        preview = (R5VideoView)rootView.findViewById(R.id.videoPreview);

        button1 = (Button) rootView.findViewById(R.id.button1);
        button1.setOnClickListener(this);

        button2 = (Button) rootView.findViewById(R.id.button2);
        button2.setOnClickListener(this);

        button3 = (Button) rootView.findViewById(R.id.button3);
        button3.setOnClickListener(this);

        button4 = (Button) rootView.findViewById(R.id.button4);
        button4.setOnClickListener(this);

        button5 = (Button) rootView.findViewById(R.id.button5);
        button5.setOnClickListener(this);


        disableButtons();

        publish();

        return rootView;
    }




    @Override
    public void onClick(View view) {

        if(publishing) {
            if (view.getId() == button1.getId()) {
                clientToServer();
            } else if (view.getId() == button2.getId()) {
                callClientMethodWithoutParams();
            } else if (view.getId() == button3.getId()) {
                callClientMethodWithParams();
            } else if (view.getId() == button4.getId()) {
                callClientMethodWithParamsForResult();
            } else if (view.getId() == button5.getId()) {
                clientToServerWithParams();
            }
        }

    }



    private void clientToServer() {

        Thread myThread = new Thread(new Runnable(){

            @Override
            public void run() {

                try {
                    publish.connection.call(new R5RemoteCallContainer("clientServices.clientToServer", null, null));
                } catch (Exception e) {
                    if(e.toString().contains("InterruptedException"))
                        e.printStackTrace();
                    System.out.println("failed to call server");
                }

            }
        });

        myThread.start();
    }




    protected void clientToServerWithParams()
    {
        Thread myThread = new Thread(new Runnable(){

            @Override
            public void run() {

                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("param1", 1);
                    map.put("param2", 2);
                    publish.connection.call(new R5RemoteCallContainer("clientServices.clientToServerWithParamsForMobileSDK", null, map));
                } catch (Exception e) {
                    if(e.toString().contains("InterruptedException"))
                        e.printStackTrace();
                    System.out.println("failed to call server with params");
                }

            }
        });

        myThread.start();
    }





    private void callClientMethodWithoutParams() {

        Thread myThread = new Thread(new Runnable(){

            @Override
            public void run() {

                try {
                    publish.connection.call(new R5RemoteCallContainer("clientServices.callClientMethodWithoutParams", null, null));
                } catch (Exception e) {
                    if(e.toString().contains("InterruptedException"))
                        e.printStackTrace();
                    System.out.println("failed to send request to server");
                }

            }
        });

        myThread.start();
    }



    private void callClientMethodWithParams() {

        Thread myThread = new Thread(new Runnable(){

            @Override
            public void run() {

                try {
                    publish.connection.call(new R5RemoteCallContainer("clientServices.callClientMethodWithParams", null, null));
                } catch (Exception e) {
                    if(e.toString().contains("InterruptedException"))
                        e.printStackTrace();
                    System.out.println("failed to send request to server");
                }

            }
        });

        myThread.start();
    }



    private void callClientMethodWithParamsForResult() {

        Thread myThread = new Thread(new Runnable(){

            @Override
            public void run() {

                try {
                    publish.connection.call(new R5RemoteCallContainer("clientServices.callClientMethodWithParamsForResult", null, null));
                } catch (Exception e) {
                    if(e.toString().contains("InterruptedException"))
                        e.printStackTrace();
                    System.out.println("failed to send request to server");
                }

            }
        });

        myThread.start();
    }




    public void clientMethod1(){

        Log.d("Publisher", ":clientMethod1 called ");
    }



    public void clientMethod2(Number a, Number b){

        Log.d("Publisher", ":clientMethod2 called with params " + a + "," + b);

    }


    public Object clientMethod3(Number a, Number b){

        Log.d("Publisher", ":clientMethod3 called with params for result " + a + "," + b);

        return a.intValue() + b.intValue();
    }



    public void R5ClientToServerDone(){
        Log.d("Publisher", ":clientToServer call complete");
    }



    protected void publish() {

        String b = getActivity().getPackageName();
        //Create the configuration from the values.xml
        R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP,
                TestContent.GetPropertyString("host"),
                TestContent.GetPropertyInt("port"),
                TestContent.GetPropertyString("context"),
                TestContent.GetPropertyFloat("buffer_time"));
        config.setLicenseKey(TestContent.GetPropertyString("license_key"));
        config.setBundleID(b);

        R5Connection connection = new R5Connection(config);

        //setup a new stream using the connection
        publish = new R5Stream(connection);
        publish.setListener(this);
        publish.client = this;

        //show all logging
        publish.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

        if(TestContent.GetPropertyBool("video_on")) {
            //attach a camera video source
            cam = openFrontFacingCameraGingerbread();
            cam.setDisplayOrientation((camOrientation + 180) % 360);

            camera = new R5Camera(cam, TestContent.GetPropertyInt("camera_width"), TestContent.GetPropertyInt("camera_height"));
            camera.setBitrate(TestContent.GetPropertyInt("bitrate"));
            camera.setOrientation(camOrientation);
        }

        if(TestContent.GetPropertyBool("audio_on")) {
            //attach a microphone
            R5Microphone mic = new R5Microphone();
            publish.attachMic(mic);
        }

        preview.attachStream(publish);

        if(TestContent.GetPropertyBool("video_on"))
            publish.attachCamera(camera);

        preview.showDebugView(TestContent.GetPropertyBool("debug_view"));

        publish.publish(TestContent.GetPropertyString("stream1"), R5Stream.RecordType.Live);

        if(TestContent.GetPropertyBool("video_on"))
            cam.startPreview();

    }

    protected Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                    camOrientation = cameraInfo.orientation;
                    applyDeviceRotation();
                    break;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

        return cam;
    }

    protected void applyDeviceRotation(){
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        camOrientation += degrees;

        camOrientation = camOrientation%360;
    }

    @Override
    public void onStop() {

        if (publish != null){
            publish.stop();

            if(publish.getVideoSource() != null) {
                Camera c = ((R5Camera) publish.getVideoSource()).getCamera();
                c.stopPreview();
                c.release();
            }
            publish = null;

        }

        super.onStop();
    }


}
