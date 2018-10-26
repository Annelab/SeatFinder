package com.example.loska.seatfinder;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.LinkedTransferQueue;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, ScanFragment.SeatScanListener {

    private GoogleMap mMap;
    private HashMap<String, Boolean> seatTaken;


    BottomNavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
                                SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                                mapFragment.getMapAsync(MapsActivity.this);
                                selectedFragment = mapFragment;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                }
        );

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(MapsActivity.this);
        transaction.replace(R.id.fragment, mapFragment);
        transaction.commit();
        seatTaken = new HashMap<String, Boolean>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        // To get coordinates on tap
        /*googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Log.d("DEBUG","Map clicked [" + point.latitude + " / " + point.longitude + "]");
                //Do your stuff with LatLng here
                //Then pass LatLng to other activity
            }
        });*/

        // Move camera to the library
        LatLng library = new LatLng(57.697844,11.9775544);
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(library, zoomLevel));

        // Place floor plan overlay on the map (orientation + position are not accurate, but
        // that is good enough, since we don't actually display a map.
        GroundOverlayOptions floorMap1 = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.floorplan_e))
                .anchor(0, 1)
                .position(library, 500, 500);
        mMap.addGroundOverlay(floorMap1);

        // Get chair coordinates
        ArrayList<LatLng> chairCoords = getChairCoords();
        //int height = 100;
        //int width = 100;

        for (int i = 0; i < chairCoords.size(); i++){
        //    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.chair_free);
         //   Bitmap b=bitmapdraw.getBitmap();
         //   Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            String title = "chair" + i;
            MarkerOptions mo = new MarkerOptions()
                    .position(chairCoords.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(title);
            //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            mMap.addMarker(mo);
            seatTaken.put(title, false);
        }
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // flip taken/not taken
        if (seatTaken.get(marker.getTitle())) {
            seatTaken.put(marker.getTitle(), false);
            marker.setIcon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            );
        }
        else {
            seatTaken.put(marker.getTitle(), true);
            marker.setIcon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            );
        }

        return true;
    }

    private ArrayList<LatLng> getChairCoords() {
        // Might eventually want to read the coordinates from an XML

        ArrayList<LatLng> coords = new ArrayList<LatLng>();
        coords.add(new LatLng(57.699883568653654, 11.981836706399918));
        coords.add(new LatLng(57.69963024095474, 11.981253661215305));
        coords.add(new LatLng(57.69946272838439, 11.981538645923136));
        coords.add(new LatLng(57.69970459120565, 11.982051953673363));
        coords.add(new LatLng(57.700887545870884, 11.98092643171549));
        coords.add(new LatLng(57.700565430587986, 11.980301141738892));

        return coords;
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
}
