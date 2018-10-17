package com.example.loska.seatfinder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.util.Calendar;

public class ScanActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {
    static {
        System.loadLibrary("opencv_java3");
    }
    private final String _TAG = "ScanActivity:";

    private CameraBridgeViewBase cvView;
    int seat;
    ImageView imView;
    Button book;
    TextView tv;

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        requestCameraPermission();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        book = (Button)findViewById(R.id.book);
        tv = (TextView)findViewById(R.id.tv);
        imView = (ImageView)findViewById(R.id.imView);
        cvView = (CameraBridgeViewBase)findViewById(R.id.cvView);
        cvView.setCvCameraViewListener(this);

        seat = -1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String TAG = new StringBuilder(_TAG).append("onResume").toString();
        if (!OpenCVLoader.initDebug()) {
            Log.i(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initiation");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, loaderCallback);
        } else {
            Log.i(TAG, "OpenCV library found inside package. Using it");
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }

    @Override
    protected void onPause() {
        String TAG = new StringBuilder(_TAG).append("onPause").toString();
        Log.i(TAG, "Disabling a camera view");
        if (cvView != null) {
            cvView.disableView();
        }
        super.onPause();
    }

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            String TAG = new StringBuilder(_TAG).append("onManagerConnected").toString();

            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV loaded successfully");
                    cvView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
            }
        }
    };

    @Override
    protected void onDestroy() {
        String TAG = new StringBuilder(_TAG).append("onDestroy").toString();
        Log.i(TAG, "Disabling a camera view");
        if (cvView != null) {
            cvView.disableView();
        }
        super.onDestroy();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat original = inputFrame.rgba();
        Mat image = new Mat();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Core.rotate(original, image, Core.ROTATE_90_CLOCKWISE);
        } else {
            image = original;
        }
        final Bitmap map = mapFromMat(image);
        if(map == null) {
            Log.d("a", "the map is NULL!!!");
        }
        String text = decodeQR(map);
        if(!text.isEmpty()) {
            try {
                seat = Integer.parseInt(text.substring(5));
            } catch(NumberFormatException e) {
                seat = -1;
            }

            final String finalText = genSeatFloor(seat);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText(finalText);
                    book.setEnabled(true);
                }
            });
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imView.setImageBitmap(map);
            }
        });
        return original;
    }


    private String decodeQR(Bitmap map) {
        Log.d("a", "Entered DECODER");
        String contents = "";
        BarcodeDetector detector = new BarcodeDetector.Builder(this)
                //.setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        if(!detector.isOperational()){
            return "Could not set up the detector!";
        }
        Frame frame = new Frame.Builder().setBitmap(map).build();
        SparseArray<Barcode> codes = detector.detect(frame);
        if(codes.size()>0) {
            Barcode code = codes.valueAt(0);
            contents = code.rawValue;
        }
        return contents;
    }

    public void onBook(View view) {
        if(seat < 0) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR,1);
        DateFormat format = new DateFormat();
        Toast.makeText(this, genSeatFloor(seat) +
                " booked until 1h from now, expires " + format.format("kk:mm", cal.getTime()),
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("seat", seat);
        startActivity(intent);
    }
    private Bitmap mapFromMat(Mat mat) {
        Bitmap map = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, map);
        return map;
    }
    private String genSeatFloor(int seat) {
        return getString(R.string.seat) + " " + (seat%1000) + " " +
                getString(R.string.floor) + " " + (seat/1000);
    }
}