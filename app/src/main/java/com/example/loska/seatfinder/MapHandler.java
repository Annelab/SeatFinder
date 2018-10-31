package com.example.loska.seatfinder;

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
    private HashMap<String, Boolean> seatTaken;
    private int rid;
    private Floor currentFloor = Floor.FLOOR_E;

    private HashMap<String, MarkerOptions> seatsFloorMinusOne;
    private HashMap<String, MarkerOptions> seatsFloorE;
    private HashMap<String, MarkerOptions> seatsFloorOne;
    private HashMap<String, MarkerOptions> seatsFloorTwo;

    public enum Floor {
        FLOOR_MINUS_ONE,
        FLOOR_E,
        FLOOR_ONE,
        FLOOR_TWO
    }


    public MapHandler(int id) {
        seatTaken = new HashMap<String, Boolean>();
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

    private ArrayList<MarkerOptions> getChairs(Floor floor) {
        // Might eventually want to read the coordinates from an XML

        ArrayList<MarkerOptions> markers = new ArrayList<>();

        if (floor == Floor.FLOOR_E) {
            for (String seat : seatsFloorE.keySet()) {
                markers.add(seatsFloorE.get(seat));
            }

        }

        return markers;
    }

    private void loadSeats() {
        seatsFloorE.put("Seat1", createMarkerOptions(new LatLng(57.699883568653654, 11.981836706399918),"Seat1"));
        seatsFloorE.put("Seat2", createMarkerOptions(new LatLng(57.69963024095474, 11.981253661215305), "Seat2"));
        seatsFloorE.put("Seat3", createMarkerOptions(new LatLng(57.69946272838439, 11.981538645923136), "Seat3"));
        seatsFloorE.put("Seat4", createMarkerOptions(new LatLng(57.69970459120565, 11.982051953673363), "Seat4"));
        seatsFloorE.put("Seat5", createMarkerOptions(new LatLng(57.700887545870884, 11.98092643171549), "Seat5"));
        seatsFloorE.put("Seat6", createMarkerOptions(new LatLng(57.700565430587986, 11.980301141738892), "Seat6"));


        // on floor 2: need seat 45, 46 and 52
    }

    private MarkerOptions createMarkerOptions (LatLng coords, String title) {
        MarkerOptions mo = new MarkerOptions()
                .position(coords)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(title);
        //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        seatTaken.put(title, false);
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

        int floorPlanId;
        if (currentFloor == Floor.FLOOR_MINUS_ONE)
            floorPlanId = R.drawable.floor_minus_one;
        else if (currentFloor == Floor.FLOOR_E)
            floorPlanId = R.drawable.floor_e;
        else if (currentFloor == Floor.FLOOR_ONE)
            floorPlanId = R.drawable.floor_1;
        else
            floorPlanId = R.drawable.floor_2;

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

            mMap.addMarker(chairs.get(i));
            seatTaken.put(chairs.get(i).getTitle(), false);
        }
        mMap.setOnMarkerClickListener(this);
    }
}
