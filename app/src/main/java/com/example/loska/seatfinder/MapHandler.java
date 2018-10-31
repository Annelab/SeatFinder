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
import java.util.LinkedList;

public class MapHandler implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private HashMap<String, Boolean> seatTakenFloorMinusOne;
    private HashMap<String, Boolean> seatTakenFloorE;
    private HashMap<String, Boolean> seatTakenFloorOne;
    private HashMap<String, Boolean> seatTakenFloorTwo;
    private int rid;
    private Floor currentFloor = Floor.FLOOR_E;

    private HashMap<String, MarkerOptions> moFloorMinusOne;
    private HashMap<String, MarkerOptions> moFloorE;
    private HashMap<String, MarkerOptions> moFloorOne;
    private HashMap<String, MarkerOptions> moFloorTwo;

    private LinkedList<String> bookedByUser;



    private boolean FLAG_loadSeats = false;

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

        moFloorMinusOne = new HashMap<String, MarkerOptions>();
        moFloorE = new HashMap<String, MarkerOptions>();
        moFloorOne = new HashMap<String, MarkerOptions>();
        moFloorTwo = new HashMap<String, MarkerOptions>();

        bookedByUser = new LinkedList<>();

        FLAG_loadSeats = true;
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

        if (FLAG_loadSeats) {
            loadSeats();
            FLAG_loadSeats = false;
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        refreshMap();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        return true;
    }

    private ArrayList<MarkerOptions> getChairs(Floor floor) {
        // Might eventually want to read the coordinates from an XML

        ArrayList<MarkerOptions> markers = new ArrayList<>();

        if (floor == Floor.FLOOR_E) {
            for (String seat : moFloorE.keySet()) {
                markers.add(moFloorE.get(seat));
            }
        }
        else if (floor == Floor.FLOOR_MINUS_ONE) {
            for (String seat : moFloorMinusOne.keySet()) {
                markers.add(moFloorMinusOne.get(seat));
            }
        }
        else if (floor == Floor.FLOOR_ONE) {
            for (String seat : moFloorOne.keySet()) {
                markers.add(moFloorOne.get(seat));
            }
        }
        else {
            for (String seat : moFloorTwo.keySet()) {
                markers.add(moFloorTwo.get(seat));
            }
        }

        return markers;
    }

    private void loadSeats() {
        moFloorE.put("1", createMarkerOptions(new LatLng(57.699883568653654, 11.981836706399918),"1"));
        moFloorE.put("2", createMarkerOptions(new LatLng(57.69963024095474, 11.981253661215305), "2"));
        moFloorE.put("3", createMarkerOptions(new LatLng(57.69946272838439, 11.981538645923136), "3"));
        moFloorE.put("4", createMarkerOptions(new LatLng(57.69970459120565, 11.982051953673363), "4"));
        moFloorE.put("5", createMarkerOptions(new LatLng(57.700887545870884, 11.98092643171549), "5"));
        moFloorE.put("6", createMarkerOptions(new LatLng(57.700565430587986, 11.980301141738892), "6"));
        seatTakenFloorE.put("1", false);
        seatTakenFloorE.put("2", false);
        seatTakenFloorE.put("3", false);
        seatTakenFloorE.put("4", false);
        seatTakenFloorE.put("5", false);
        seatTakenFloorE.put("6", false);

        moFloorOne.put("34", createMarkerOptions(new LatLng(57.99863568657654, 11.981856706399918),"34"));
        moFloorOne.put("35", createMarkerOptions(new LatLng(57.69964024095574, 11.981257361215305), "35"));
        moFloorOne.put("36", createMarkerOptions(new LatLng(57.69945272838139, 11.981536545923136), "36"));
        moFloorOne.put("37", createMarkerOptions(new LatLng(57.7002459120765, 11.982050963673363), "37"));
        moFloorOne.put("38", createMarkerOptions(new LatLng(57.700897545870484, 11.98094642171549), "38"));
        moFloorOne.put("39", createMarkerOptions(new LatLng(57.700575430587916, 11.980321141737892), "39"));
        seatTakenFloorOne.put("34", false);
        seatTakenFloorOne.put("35", false);
        seatTakenFloorOne.put("36", false);
        seatTakenFloorOne.put("37", false);
        seatTakenFloorOne.put("38", false);
        seatTakenFloorOne.put("39", false);

        moFloorTwo.put("45", createMarkerOptions(new LatLng(57.699870669407694, 11.983653903007507),"45"));
        moFloorTwo.put("46", createMarkerOptions(new LatLng(57.699859203407414, 11.984754614531994), "46"));
        moFloorTwo.put("47", createMarkerOptions(new LatLng(57.700577254670165, 11.984697282314302), "47"));
        moFloorTwo.put("50", createMarkerOptions(new LatLng(57.70058119603001, 11.983718276023865), "50"));
        moFloorTwo.put("51", createMarkerOptions(new LatLng(57.70150937433458, 11.984190009534359), "51"));
        moFloorTwo.put("52", createMarkerOptions(new LatLng(57.701467274501816, 11.982674896717072), "52"));
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
        float zoomLevel = 15.77f;
        LatLng cameraPos = new LatLng(57.7006729221021,11.9818876683712);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPos, zoomLevel));

        // Place floor plan overlay on the map (orientation + position are not accurate, but
        // that is good enough, since we don't actually display a map.

        int floorPlanId = getCurrentFloorPlan();

        GroundOverlayOptions floorMap1 = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(floorPlanId))
                .anchor(0, 1)
                .position(library, 500, 500);
        mMap.addGroundOverlay(floorMap1);

        // Get chair coordinates
        ArrayList<MarkerOptions> chairs = getChairs(currentFloor);

        for (int i = 0; i < chairs.size(); i++){
            Marker marker = mMap.addMarker(chairs.get(i));
            HashMap<String, Boolean> seatTaken;
            if (currentFloor == Floor.FLOOR_E) {
                seatTaken = seatTakenFloorE;
            }
            else if (currentFloor == Floor.FLOOR_MINUS_ONE) {
                seatTaken = seatTakenFloorMinusOne;
            }
            else if (currentFloor == Floor.FLOOR_ONE)
                seatTaken = seatTakenFloorOne;
            else
                seatTaken = seatTakenFloorTwo;

            Log.d("DebugLog","seat 46 taken? -> " + seatTaken.get("46"));
            Log.d("DebugLog", "seat: " + marker.getTitle());
            Log.d("DebugLog","seat really taken? -> " + seatTaken.get(marker.getTitle()));

            if (seatTaken.get(marker.getTitle())) {
                marker.setIcon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                );
            }
            else {
                marker.setIcon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                );
            }

            if (bookedByUser.contains(marker.getTitle()))
            {
                marker.setIcon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                );
            }

        }
        mMap.setOnMarkerClickListener(this);
    }

    public void bookSeat(int seat, int floor) {
        MarkerOptions marker = null;
        bookedByUser.add("" + seat);
        if (floor == 0) {
            seatTakenFloorE.put("" + seat, true);
            changeFloor(Floor.FLOOR_E);
        }
        else if (floor == 1) {
            seatTakenFloorOne.put("" + seat, true);
            changeFloor(Floor.FLOOR_ONE);
        }
        else if (floor == 2) {
            seatTakenFloorTwo.put("" + seat, true);
            changeFloor(Floor.FLOOR_TWO);
        }

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


