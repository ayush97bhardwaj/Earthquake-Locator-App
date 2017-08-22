/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport_V2;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.query.Filter;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.R.color.white;
import static android.os.Build.VERSION_CODES.M;
import static com.example.android.quakereport_V2.EarthquakesListFragment.changeBtn;
import static com.example.android.quakereport_V2.EarthquakesListFragment.earthquakeListView;
import static com.example.android.quakereport_V2.EarthquakesListFragment.p;
import static com.example.android.quakereport_V2.FilterActivity.checker;
import static com.example.android.quakereport_V2.FilterActivity.circle;
import static com.example.android.quakereport_V2.FilterActivity.counter;
import static com.example.android.quakereport_V2.MapFragment.mMap;
import static com.example.android.quakereport_V2.MapFragment.mapView;
import static com.example.android.quakereport_V2.R.array.location;
//import static com.example.android.quakereport_V2.R.id.change;
import static com.example.android.quakereport_V2.R.id.to;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;


public class MainEarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Word>>/*, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener*/ {

    public static final int LOADER_ID = 1;
    public static final String LOG_TAG = MainEarthquakeActivity.class.getName();
    public static String myLat="0";
    public static String myLng="0";
    public static Double myLat2=28.6134261;
    public static Double myLng2=77.0351721;
    public LatLng myLocation;
    public static int noConnection = 0;
    public static boolean LimitReached=false;

    public static ViewPager viewPager;
    public CategoryAdapter adapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public static GoogleApiClient mGoogleApiClient = null;
    public static LocationManager locationManager;
    public static LocationListener locationListener;

    public static TextView mEmptyStateTextView;
    public static SwipeRefreshLayout mySwipeRefreshLayout;
//    LocationRequest mLocationRequest;

    public final static int MY_REQUEST_FINE_LOCATION = 123;
    public static final String DEFAULT_EARTHQUAKES_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=5&limit=10";
    //    public static final String TOP_EARTHQUAKES_URL="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=3&limit=20&minlatitude=6&maxlatitude=36&minlongitude=68&maxlongitude=98";
    public static String TOP_EARTHQUAKES_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=2";
    //    public static String TOP_EARTHQUAKES_URL ="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=7&starttime=2014-01-01&endtime";
//    public static String TOP_EARTHQUAKES_URL ="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=3&starttime=&endtime=&latitude=28.6134261&longitude=77.0351721&maxradiuskm=2000";
    public static WordAdapter mAdapter;

    public static LoaderManager loader;

    @RequiresApi(api = M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "in main: " + "1");
        setContentView(R.layout.earthquake_activity);

        Log.i(LOG_TAG, "in main: " + "2");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);


        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
//
//
//
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            buildAlertMessageNoGps();
//        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLat = Double.toString(location.getLatitude());
                myLng = Double.toString(location.getLongitude());

                Log.e(TAG, "onLocationChanged: " + myLat + " " + myLng);
//                myLocation=new LatLng(Double.parseDouble(myLat),Double.parseDouble(myLng));
//                mMap.addMarker(new MarkerOptions().position(myLocation).title("home"));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e(TAG, "onStatusChanged: " + provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e(TAG, "onProviderEnabled: ");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.e(TAG, "onProviderDisabled: ");
            }
        };

//        Log.e("dj", "on location changed: " + location.getLatitude() + " & " + location.getLongitude());

//        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1337);
//        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1338);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, locationListener);

        // Find a reference to the {@link ListView} in the layout

//        earthquakeListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new WordAdapter(this, new ArrayList<Word>());


        //Checking if Internet Connection is available
        //if connection is available then loading the data
        checkConnectionAndLoad();

//        //USING ASYNC LOADER
//        loader = getLoaderManager();
//        loader.initLoader(LOADER_ID, null, this);
//
        // Create a new {@link ArrayAdapter} of earthquakes

//        if(DEFAULT_EARTHQUAKES_URL!=TOP_EARTHQUAKES_URL) {
//            Log.e(TAG, "onCreateRestart: 212" );
//            loader.destroyLoader(LOADER_ID);
//            loader.initLoader(LOADER_ID,
// null, this);
//        }

//
//        // Find a reference to the {@link ListView} in the layout
//        ListView earthquakeListView = (ListView) findViewById(R.id.list);
//
//        // Set the adapter on the {@link ListView}
//        // so the list can be populated in the user interface
//        Log.i(LOG_TAG, "loadInBackground: "+"10");
//        earthquakeListView.setAdapter(mAdapter);
//
        Log.i(LOG_TAG, "in main: " + "3");

        // Create an instance of GoogleAPIClient.
//        if (mGoogleApiClient == null) {
//            Log.e(TAG, "onCreate: is it being called" );
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//
//        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
            Log.e(TAG, "onCreate: " + " permissions not granted");

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_FINE_LOCATION);

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20, 10, locationListener);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
//                    mGoogleApiClient.connect();

                    Toast.makeText(this, "Gps Enabled",
                            Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
                        Log.e(TAG, "onRequestPermissionsResult: Still not connected" );
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
//                        View loadingIndicator = findViewById(R.id.loading_indicator);
//                        loadingIndicator.setVisibility(View.GONE);

                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);

                    Toast.makeText(this, "Gps Not Enabled",
                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Cannot find Your Location",
                            Toast.LENGTH_LONG).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void checkConnectionAndLoad(){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.

            loader = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loader.initLoader(LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message

            noConnection=1;
        }

    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        int[] location=new int[2];
//        earthquakeListView.getLocationInWindow(location);
//        Log.e(TAG, "onCreateView: "+location[0]+" "+location[1]);
//        p=new Point();
//        p.x=location[0];
//        p.y=location[1];
//        Log.e(TAG, "onCreateView: "+p.x+" "+p.y);
//    }

    //    public GPSTracker(Context context) {
//        this.mContext = context;
//        getLocation();
//    }
//
//    public Location getLocation() {
//        try {
//            locationManager = (LocationManager) mContext
//                    .getSystemService(LOCATION_SERVICE);
//
//            // getting GPS status
//            isGPSEnabled = locationManager
//                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            // getting network status
//            isNetworkEnabled = locationManager
//                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                // no network provider is enabled
//            } else {
//                this.canGetLocation = true;
//                // First get location from Network Provider
//                if (isNetworkEnabled) {
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
//                    Log.d("Network", "Network");
//                    if (locationManager != null) {
//                        location = locationManager
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
//                // if GPS Enabled get lat/long using GPS Services
//                if (isGPSEnabled) {
//                    if (location == null) {
//                        locationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.d("GPS Enabled", "GPS Enabled");
//                        if (locationManager != null) {
//                            location = locationManager
//                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if (location != null) {
//                                latitude = location.getLatitude();
//                                longitude = location.getLongitude();
//                            }
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return location;
//    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    public void filterIntent(MenuItem mi) {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Check if user triggered a refresh:
            case R.id.menu_refresh:
                Log.i(LOG_TAG, "Refresh menu item selected");

                // Signal SwipeRefreshLayout to start the progress indicator
                final SwipeRefreshLayout mySwipeRefreshLayout1=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);
                mySwipeRefreshLayout1.setRefreshing(true);

                // Start the refresh background task.
                // This method calls setRefreshing(false) when it's finished.

                checkConnectionAndLoad();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mySwipeRefreshLayout1.setRefreshing(false);
                    }
                }, 1000);
                return true;
        }

        // User didn't trigger a refresh, let the superclass handle this action
        return super.onOptionsItemSelected(item);

    }



    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
//        mGoogleApiClient.disconnect();
    }

    Location mLastLocation;

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            Log.e(TAG, "onConnected: permission not granted++++++++++++++++++" );
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//        }
//
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        Log.e(TAG, "onConnected: "+ mLastLocation+"whatsgoin on");
//        if (mLastLocation != null) {
////            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
////            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//            Log.e(TAG, "onConnected: "+mLastLocation.getLatitude()+" huuauauauaua "+mLastLocation.getLongitude());
//            myLat=""+mLastLocation.getLatitude();
//            myLng=""+mLastLocation.getLongitude();
//
//
////            myLocation=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
////            mMap.addMarker(new MarkerOptions().position(myLocation).title("home"));
////        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
//        }
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static class earthquakeLoader extends AsyncTaskLoader<List<Word>> {

        String mUrl;

        public earthquakeLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<Word> loadInBackground() {
            if (mUrl == null)
                return null;

            Log.e(LOG_TAG, "loadInBackground: " + "1");
            List<Word> earthquakesList = QueryUtils.fetchEarthquakeData(mUrl);
            return earthquakesList;
        }
    }

    @Override
    public Loader<List<Word>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "loadInBackground: " + "2");
        Log.e(TAG, "onCreateLoader: " + TOP_EARTHQUAKES_URL);
        return new earthquakeLoader(this, TOP_EARTHQUAKES_URL);
    }


    @Override
    public void onLoadFinished(Loader<List<Word>> loader, final List<Word> data) {

        Toast.makeText(MainEarthquakeActivity.this,data.size()+" Results are being shown",
                Toast.LENGTH_LONG).show();

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_earthquakes);

        Log.e(LOG_TAG, "loadInBackground: " + "3");
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            Log.e(LOG_TAG, "loadInBackground: " + "4");
            mAdapter.addAll(data);
        }

        Log.e(TAG, "onCreateViewMAP: " + "11");
        mapView = (MapView) findViewById(R.id.mapsView);
        Log.e(TAG, "onCreateViewMAP: " + "12");
        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.e(TAG, "onCreateViewMAP: " + "13");
                mMap = googleMap;

                UiSettings uiSettings = mMap.getUiSettings();
                uiSettings.setCompassEnabled(true);
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setMyLocationButtonEnabled(true);

                LatLng placei;
//
//                View customMarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
//
//
                ClusterManager<MyItem> mClusterManager;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(data.get(0).getLongi(), data.get(0).getLongi()),2));

                // Initialize the manager with the context and the map.
                // (Activity extends context, so we can pass 'this' in the constructor.)
                mClusterManager = new ClusterManager<MyItem>(MainEarthquakeActivity.this, mMap);

                // Point the map's listeners at the listeners implemented by the cluster
                // manager.
                mMap.setOnCameraIdleListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);


                for (int i = 0; i < data.size(); i++) {
//                    Log.i(TAG, "onMapReady: " + "1");
                    placei = new LatLng(data.get(i).getLat(), data.get(i).getLongi());
//                    Log.i(LOG_TAG, "onCreate: " + data.get(i).getLat() + " " + data.get(i).getLongi());
                    Date dateObject=new Date(data.get(i).getTimeInMilliseconds());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy  h:mm a");
                    String formattedDate=dateFormat.format(dateObject);

                    String info=data.get(i).getPlace();
                    String details="Mag-"+data.get(i).getMag()+" Time- "+formattedDate;

//                    MarkerOptions marker=new MarkerOptions()
//                            .position(placei)
//                            .title(info)
//                            .zIndex(Float.parseFloat(""+mAdapter.getItem(i).getMag()))
//                            .snippet(details);
//                    if(mAdapter.getItem(i).getMag()>=6)
//                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_red_32));
//                    else if(mAdapter.getItem(i).getMag()<=3)
//                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_orange));
//                    else
//                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_purple_32));

//                    mMap.addMarker(marker);
//                    Log.i(TAG, "onMapReady: " + "2");
                    MyItem myItems=new MyItem(data.get(i).getLat(),data.get(i).getLongi(),info,details);
                    mClusterManager.addItem(myItems);

                }
                if(checker==1) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLat2,myLng2),3));
                    mMap.addCircle(circle);
                }
                else if(checker==2)
                    circle.visible(true);


//                    myLocation = new LatLng(Double.parseDouble(myLat),Double.parseDouble(myLng));
//                    mMap.addMarker(new MarkerOptions()
//                            .position(myLocation)
//                            .title("home")
//                            .zIndex(1)
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//

                // Add cluster items (markers) to the cluster manager.
//                double lat = 51.5145160;
//                double lng = -0.1270060;
////
////                // Add ten cluster items in close proximity, for purposes of this example.
//                for (int i = 0; i < 10; i++) {
//                    double offset = i / 60d;
//                    lat = lat + offset;
//                    lng = lng + offset;
//                    MyItem offsetItem = new MyItem(lat, lng);
//                    mClusterManager.addItem(offsetItem);
//                }
            }
        });
    }


    private void setUpClusterer() {
        // Position the map.

    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
    }

    // Convert a view to bitmap
//    public Bitmap createDrawableFromView(OnMapReadyCallback context, View view) {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
//        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
//        view.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//
//        return bitmap;
//    }

    @Override
    public void onLoaderReset(Loader<List<Word>> loader) {
        mAdapter.clear();

    }

//    public class MyLocationListener implements LocationListener{
//
//        @Override
//        public void onLocationChanged(Location location) {
//
//            Log.e(TAG, "onLocationChanged: latitude"+ location.getLatitude()+"longitude: "+location.getLongitude());
//        }
//    }


//    Get user location
//
//    protected synchronized void buildGoogleApiClient() {
//        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
//                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//    LatLng latLng;
//    @Override
//    public void onConnected(Bundle bundle) {
//        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//            //place marker at current position
//            //mGoogleMap.clear();
//            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Current Position");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//            currLocationMarker = mMap.addMarker(markerOptions);
//        }
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(5000); //5 seconds
//        mLocationRequest.setFastestInterval(3000); //3 seconds
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//
//
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
//    }
//
//    marker.png currLocationMarker;
//    @Override
//    public void onLocationChanged(Location location) {
//
//        //place marker at current position
//        //mGoogleMap.clear();
//        if (currLocationMarker != null) {
//            currLocationMarker.remove();
//        }
//        latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        currLocationMarker = mMap.addMarker(markerOptions);
//
//        Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();
//
//        //zoom to current position:
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
//
//        //If you only need one location, unregister the listener
//        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//
//    }

}

