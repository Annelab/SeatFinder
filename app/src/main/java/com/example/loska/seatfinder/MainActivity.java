package com.example.loska.seatfinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements ScanFragment.SeatScanListener,FilterFragment.OnFilterListener {

    BottomNavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestCameraPermission();
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar)findViewById(R.id.appBar);
        appBar.setLogo(getSizedLogo(R.drawable.stadsbiblioteket));
        setSupportActionBar(appBar);

        navbar = (BottomNavigationView)findViewById(R.id.navbar);
        navbar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Fragment selectedFragment = null;
                        switch (menuItem.getItemId()) {
                            case R.id.action_scan:
                                selectedFragment = ScanFragment.newInstance();
                                break;
                            case R.id.action_find:
                                selectedFragment = MapFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                }
        );

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MapFragment mapFragment = MapFragment.newInstance();
        transaction.replace(R.id.fragment, mapFragment);
        transaction.commit();
    }

    @Override
    public void onBook(int seat) {
        navbar.setSelectedItemId(R.id.action_find);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR,1);
        DateFormat format = new DateFormat();
        Toast.makeText(this, SeatFinderUtils.genSeatFloor(this, seat) +
                        " booked until 1h from now, expires " + format.format("kk:mm", cal.getTime()),
                Toast.LENGTH_LONG).show();
    }



    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appbar, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                onInfo();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private Drawable getSizedLogo(int rid) {
        return getSizedLogo(rid, 50, 50);
    }

    public Drawable getSizedLogo(int rid, int width, int height) {
        Drawable logo = getResources().getDrawable(rid);
        Bitmap bitmap = ((BitmapDrawable) logo).getBitmap();
        final float scale = getResources().getDisplayMetrics().density;
        Drawable sizedLogo = new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(bitmap, (int)(scale * width), (int)(scale * height), true));
        return sizedLogo;
    }

    public void onInfo() {
        Toast.makeText(this, "info", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFilter() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MapFragment mapFragment = MapFragment.newInstance();
        transaction.replace(R.id.fragment, mapFragment);
        transaction.commit();
    }
}
