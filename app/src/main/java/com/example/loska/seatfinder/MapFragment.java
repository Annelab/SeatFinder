package com.example.loska.seatfinder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.SupportMapFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnFloor1;
    private Button btnFloor2;
    private Button btnFloor3;
    private Button btnFloor4;

    private ImageButton filterBtn;

    private MapHandler mapHandler;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mapHandler = new MapHandler(R.drawable.floorplan_e);

        SupportMapFragment mMapFragment = new SupportMapFragment();
        mMapFragment.getMapAsync(mapHandler);
        getChildFragmentManager().beginTransaction().add(R.id.mapWrapper, mMapFragment).addToBackStack(null).commit();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnFloor1 = view.findViewById(R.id.floor0);
        btnFloor1.setOnClickListener(this);
        btnFloor2 = view.findViewById(R.id.floor1);
        btnFloor2.setOnClickListener(this);
        btnFloor3 = view.findViewById(R.id.floor2);
        btnFloor3.setOnClickListener(this);
        btnFloor4 = view.findViewById(R.id.floor3);
        btnFloor4.setOnClickListener(this);

        filterBtn = view.findViewById(R.id.filterButton);
        filterBtn.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        SupportMapFragment mMapFragment = new SupportMapFragment();
        switch (v.getId()){
            case R.id.floor0:
                mapHandler.changeFloor(MapHandler.Floor.FLOOR_MINUS_ONE);
                break;

            case R.id.floor1:
                mapHandler.changeFloor(MapHandler.Floor.FLOOR_E);
                break;

            case R.id.floor2:
                mapHandler.changeFloor(MapHandler.Floor.FLOOR_ONE);
                break;

            case R.id.floor3:
                mapHandler.changeFloor(MapHandler.Floor.FLOOR_TWO);
                break;
            case R.id.filterButton:
                FilterFragment filter = FilterFragment.newInstance();
                getChildFragmentManager().beginTransaction().add(R.id.mapWrapper, filter).commit();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
