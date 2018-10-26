package com.example.loska.seatfinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.Calendar;

public class ScanFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2,
        View.OnClickListener {
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
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
    }

    private SeatScanListener listener;

    public ScanFragment() {
        // Required empty public constructor
    }

    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestCameraPermission();
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        book = (Button)getView().findViewById(R.id.book);
        book.setOnClickListener(this);
        tv = (TextView)getView().findViewById(R.id.tv);
        imView = (ImageView)getView().findViewById(R.id.imView);
        cvView = (CameraBridgeViewBase)getView().findViewById(R.id.cvView);
        cvView.setCvCameraViewListener(this);

        seat = -1;
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

            final String finalText = SeatFinderUtils.genSeatFloor(getContext(), seat);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText(finalText);
                    book.setEnabled(true);
                }
            });
        }
        getActivity().runOnUiThread(new Runnable() {
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
        BarcodeDetector detector = new BarcodeDetector.Builder(getContext())
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
    private Bitmap mapFromMat(Mat mat) {
        Bitmap map = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, map);
        return map;
    }
    private String genSeatFloor(int seat) {
        return getString(R.string.seat) + " " + (seat%1000) + " " +
                getString(R.string.floor) + " " + (seat/1000);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SeatScanListener) {
            listener = (SeatScanListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        String TAG = new StringBuilder(_TAG).append("onResume").toString();
        if (!OpenCVLoader.initDebug()) {
            Log.i(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initiation");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, getActivity(), loaderCallback);
        } else {
            Log.i(TAG, "OpenCV library found inside package. Using it");
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }

    @Override
    public void onPause() {
        String TAG = new StringBuilder(_TAG).append("onPause").toString();
        Log.i(TAG, "Disabling a camera view");
        if (cvView != null) {
            cvView.disableView();
        }
        super.onPause();
    }

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(getActivity()) {
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
    public void onDestroy() {
        String TAG = new StringBuilder(_TAG).append("onDestroy").toString();
        Log.i(TAG, "Disabling a camera view");
        if (cvView != null) {
            cvView.disableView();
        }
        super.onDestroy();
    }

    public void onClick(View view) {
        listener.onBook(seat);
    }

    public interface SeatScanListener {
        void onBook(int seat);
    }
}
