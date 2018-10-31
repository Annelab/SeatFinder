package com.example.loska.seatfinder;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;

public class MapHandler implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private HashMap<String, Boolean> seatTakenFloorMinusOne;
    private HashMap<String, Boolean> seatTakenFloorE;
    private HashMap<String, Boolean> seatTakenFloorOne;
    private HashMap<String, Boolean> seatTakenFloorTwo;
    private int rid;
    private Floor currentFloor = Floor.FLOOR_E;

    private HashMap<String, MarkerOptions> seatsFloorMinusOne;
    private HashMap<String, MarkerOptions> seatsFloorE;
    private HashMap<String, MarkerOptions> seatsFloorOne;
    private HashMap<String, MarkerOptions> seatsFloorTwo;

    private static MapHandler mapHandler;

    public enum Floor {
        FLOOR_MINUS_ONE,
        FLOOR_E,
        FLOOR_ONE,
        FLOOR_TWO
    }

    public static MapHandler getMapHandler() {
        if (mapHandler == null)
            mapHandler = new MapHandler(R.drawable.floorplan_e);
        return mapHandler;
    }

    private MapHandler(int id) {
        seatTakenFloorMinusOne = new HashMap<String, Boolean>();
        seatTakenFloorE = new HashMap<String, Boolean>();
        seatTakenFloorOne = new HashMap<String, Boolean>();
        seatTakenFloorTwo = new HashMap<String, Boolean>();
        this.rid = id;

        seatsFloorMinusOne = new HashMap<String, MarkerOptions>();
        seatsFloorE = new HashMap<String, MarkerOptions>();
        seatsFloorOne = new HashMap<String, MarkerOptions>();
        seatsFloorTwo = new HashMap<String, MarkerOptions>();
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

        loadSeats();

        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        refreshMap();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
/*
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
        */
        return true;
    }

    private ArrayList<MarkerOptions> getChairs(Floor floor) {
        // Might eventually want to read the coordinates from an XML

        ArrayList<MarkerOptions> markers = new ArrayList<>();

        if (floor == Floor.FLOOR_E) {
            for (String seat : seatsFloorE.keySet()) {
                markers.add(seatsFloorE.get(seat));
            }
        }
        else if (floor == Floor.FLOOR_MINUS_ONE) {
            for (String seat : seatsFloorMinusOne.keySet()) {
                markers.add(seatsFloorMinusOne.get(seat));
            }
        }
        else if (floor == Floor.FLOOR_ONE) {
            for (String seat : seatsFloorOne.keySet()) {
                markers.add(seatsFloorOne.get(seat));
            }
        }
        else {
            for (String seat : seatsFloorTwo.keySet()) {
                markers.add(seatsFloorTwo.get(seat));
            }
        }

        return markers;
    }

    private void loadSeats() {
        seatsFloorE.put("1", createMarkerOptions(new LatLng(57.699883568653654, 11.981836706399918),"1"));
        seatsFloorE.put("2", createMarkerOptions(new LatLng(57.69963024095474, 11.981253661215305), "2"));
        seatsFloorE.put("3", createMarkerOptions(new LatLng(57.69946272838439, 11.981538645923136), "3"));
        seatsFloorE.put("4", createMarkerOptions(new LatLng(57.69970459120565, 11.982051953673363), "4"));
        seatsFloorE.put("5", createMarkerOptions(new LatLng(57.700887545870884, 11.98092643171549), "5"));
        seatsFloorE.put("6", createMarkerOptions(new LatLng(57.700565430587986, 11.980301141738892), "6"));
        seatTakenFloorE.put("1", false);
        seatTakenFloorE.put("2", false);
        seatTakenFloorE.put("3", false);
        seatTakenFloorE.put("4", false);
        seatTakenFloorE.put("5", false);
        seatTakenFloorE.put("6", false);

        seatsFloorOne.put("34", createMarkerOptions(new LatLng(57.99863568657654, 11.981856706399918),"34"));
        seatsFloorOne.put("35", createMarkerOptions(new LatLng(57.69964024095574, 11.981257361215305), "35"));
        seatsFloorOne.put("36", createMarkerOptions(new LatLng(57.69945272838139, 11.981536545923136), "36"));
        seatsFloorOne.put("37", createMarkerOptions(new LatLng(57.7002459120765, 11.982050963673363), "37"));
        seatsFloorOne.put("38", createMarkerOptions(new LatLng(57.700897545870484, 11.98094642171549), "38"));
        seatsFloorOne.put("39", createMarkerOptions(new LatLng(57.700575430587916, 11.980321141737892), "39"));
        seatTakenFloorOne.put("34", false);
        seatTakenFloorOne.put("35", false);
        seatTakenFloorOne.put("36", false);
        seatTakenFloorOne.put("37", false);
        seatTakenFloorOne.put("38", false);
        seatTakenFloorOne.put("39", false);

        seatsFloorTwo.put("45", createMarkerOptions(new LatLng(57.699870669407694, 11.983653903007507),"45"));
        seatsFloorTwo.put("46", createMarkerOptions(new LatLng(57.699859203407414, 11.984754614531994), "46"));
        seatsFloorTwo.put("47", createMarkerOptions(new LatLng(57.700577254670165, 11.984697282314302), "47"));
        seatsFloorTwo.put("50", createMarkerOptions(new LatLng(57.70058119603001, 11.983718276023865), "50"));
        seatsFloorTwo.put("51", createMarkerOptions(new LatLng(57.70150937433458, 11.984190009534359), "51"));
        seatsFloorTwo.put("52", createMarkerOptions(new LatLng(57.701467274501816, 11.982674896717072), "52"));
        seatTakenFloorTwo.put("45", false);
        seatTakenFloorTwo.put("46", false);
        seatTakenFloorTwo.put("47", false);
        seatTakenFloorTwo.put("50", false);
        seatTakenFloorTwo.put("51", false);
        seatTakenFloorTwo.put("52", false);
    }

    private MarkerOptions createMarkerOptions (LatLng coords, String title) {
        MarkerOptions mo = new MarkerOptions()
                .position(coords)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(title);
        return mo;
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public void changeFloor(Floor newFloor) {
        currentFloor = newFloor;
        refreshMap();
    }

    private void refreshMap() {
        mMap.clear();

        // Move camera to the library
        LatLng library = new LatLng(57.697844,11.9775544);
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(library, zoomLevel));

        // Place floor plan overlay on the map (orientation + position are not accurate, but
        // that is good enough, since we don't actually display a map.

        int floorPlanId = getCurrentFloorPlan();

        GroundOverlayOptions floorMap1 = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(floorPlanId))
                .anchor(0, 1)
                .position(library, 500, 500);
        mMap.addGroundOverlay(floorMap1);

        // add only the ones of the current floor

        // Get chair coordinates
        ArrayList<MarkerOptions> chairs = getChairs(currentFloor);
        //int height = 100;
        //int width = 100;

        for (int i = 0; i < chairs.size(); i++){
            //    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.chair_free);
            //   Bitmap b=bitmapdraw.getBitmap();
            //   Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            Marker marker = mMap.addMarker(chairs.get(i));
            HashMap<String, Boolean> seatTaken;
            if (currentFloor == Floor.FLOOR_E)
                seatTaken = seatTakenFloorE;
            else if (currentFloor == Floor.FLOOR_MINUS_ONE)
                seatTaken = seatTakenFloorMinusOne;
            else if (currentFloor == Floor.FLOOR_ONE)
                seatTaken = seatTakenFloorOne;
            else
                seatTaken = seatTakenFloorTwo;
            if (seatTaken.get(marker.getTitle())) {
                marker.setIcon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                );
            }
            else {
                marker.setIcon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                );

            }

        }
        mMap.setOnMarkerClickListener(this);
    }

    public void bookSeat(int seat, int floor) {

    }

    public int getCurrentFloorPlan() {
        if (currentFloor == Floor.FLOOR_MINUS_ONE)
            return R.drawable.floor_minus_one;
        else if (currentFloor == Floor.FLOOR_E)
            return R.drawable.floor_e;
        else if (currentFloor == Floor.FLOOR_ONE)
            return R.drawable.floor_1;
        else
            return R.drawable.floor_2;
    }

}


