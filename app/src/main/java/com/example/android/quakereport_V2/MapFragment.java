package com.example.android.quakereport_V2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.android.quakereport_V2.MainEarthquakeActivity.LOG_TAG;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.mAdapter;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment{


    public MapFragment() {
        // Required empty public constructor
    }


    public static GoogleMap mMap;

    public static MapView mapView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        // Gets the MapView from the XML layout and creates it
//        Log.e(TAG, "onCreateViewMAP: "+"11" );
//        mapView = (MapView) rootView.findViewById(R.id.mapsView);
//        mapView.onCreate(savedInstanceState);
//        Log.e(TAG, "onCreateViewMAP: "+"12" );
//        mapView.getMapAsync(new OnMapReadyCallback() {
//
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                Log.e(TAG, "onCreateViewMAP: "+"13" );
//                mMap = googleMap;
//                LatLng placei;
//
//                // Add a marker in Sydney and move the camera
//                for(int i=0;i<mAdapter.getCount();i++){
//                    Log.i(TAG, "onMapReady: "+"1");
//                    placei=new LatLng(mAdapter.getItem(i).getLat(), mAdapter.getItem(i).getLongi());
//                    Log.i(LOG_TAG, "onCreate: "+mAdapter.getItem(i).getLat()+" "+ mAdapter.getItem(i).getLongi());
//                    mMap.addMarker(new MarkerOptions().position(placei).title(i+" "+mAdapter.getItem(i).getPlace()+" "));
//                    Log.i(TAG, "onMapReady: "+"2");
//                }
//            }
//        });
        mapView=(MapView) rootView.findViewById(R.id.mapsView);
        mapView.onCreate(savedInstanceState);
        Log.e(TAG, "onCreateViewMAP: "+"14" );
////
//        Toolbar toolbar=(Toolbar) rootView.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


//        earthquakeAsyncTask task=new earthquakeAsyncTask();
//        task.execute(MainEarthquakeActivity.TOP_EARTHQUAKES_URL);
//        Log.i(MainEarthquakeActivity.LOG_TAG, "onMapReady: "+earthquakesList.get(0).getLat()+" "+earthquakesList.get(0).getLongi());

//        Log.e(LOG_TAG, "onCreate: "+ mAdapter.getItem(0).getLat() );

        return rootView;
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
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        LatLng placei;
//        // Add a marker in Sydney and move the camera
//        for(int i=0;i<mAdapter.getCount();i++){
//            Log.i(TAG, "onMapReady: "+"1");
//            placei=new LatLng(mAdapter.getItem(i).getLat(), mAdapter.getItem(i).getLongi());
//            Log.i(LOG_TAG, "onCreate: "+mAdapter.getItem(i).getLat()+" "+ mAdapter.getItem(i).getLongi());
//            mMap.addMarker(new MarkerOptions().position(placei).title(i+" "+mAdapter.getItem(i).getPlace()+" "));
//            Log.i(TAG, "onMapReady: "+"2");
//        }
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("marker.png in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(placei));
//    }
    // Add the mapView lifecycle to the activity's lifecycle methods


    @Override
    public void onStart() {
        Log.e(TAG, "onStart: "+"1" );
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onStart: "+"2" );
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        Log.e(TAG, "onStart: "+"3" );
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        Log.e(TAG, "onStart: "+"4" );
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        Log.e(TAG, "onStart: "+"5" );
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onStart: "+"6" );
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e(TAG, "onStart: "+"7" );
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
