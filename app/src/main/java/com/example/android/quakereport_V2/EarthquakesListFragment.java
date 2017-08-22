package com.example.android.quakereport_V2;


//import android.support.v4.app.LoaderManager;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.max;
import static android.R.attr.y;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.LOADER_ID;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.LOG_TAG;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.TOP_EARTHQUAKES_URL;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.mEmptyStateTextView;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.mGoogleApiClient;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.mAdapter;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.mySwipeRefreshLayout;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.noConnection;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.viewPager;
import static com.example.android.quakereport_V2.MapFragment.mMap;
//import static com.example.android.quakereport_V2.R.id.change;
import static com.example.android.quakereport_V2.R.id.toMaps;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarthquakesListFragment extends Fragment{

    public EarthquakesListFragment() {
        // Required empty public constructor
    }

    public static Point p;
    public static Button changeBtn;
    public View rootView;

    public static ListView earthquakeListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.earthqauke_list_activity, container, false);

        Log.i(LOG_TAG, "loadInBackground: "+"9");


        earthquakeListView = (ListView) rootView.findViewById(R.id.list);
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        // Set empty state text to display "No earthquakes found."

        if(noConnection==1)
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        noConnection=0;

        earthquakeListView.setEmptyView(mEmptyStateTextView);

        mySwipeRefreshLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.

                        MainEarthquakeActivity updatePage=new MainEarthquakeActivity();
//                        updatePage.checkConnectionAndLoad();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 100ms
                                mySwipeRefreshLayout.setRefreshing(false);
                            }
                        }, 1000);

                    }
                }
        );

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        Log.i(LOG_TAG, "loadInBackground: "+"10");
        earthquakeListView.setAdapter(mAdapter);


        // Set the color on the magnitude circle
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Word currentEarthquake = mAdapter.getItem(position);

//                LinearLayout getPosition=(LinearLayout) rootView.findViewById(R.id.itemForPopup);
                int[] location=new int[2];
                getViewByPosition(position,earthquakeListView).getLocationOnScreen(location);
//                getPosition.getLocationOnScreen(location);
                Log.e(TAG, "onCreateView: "+location[0]+" "+location[1]);
                p=new Point();
                p.x=location[0];
                p.y=location[1];
                Log.e(TAG, "onCreateView: "+p.x+" "+p.y);

                if(p!=null)
                    showPopup(getActivity(),p,currentEarthquake);
//
//                // Convert the String URL into a URI object (to pass into the Intent constructor)
//                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
//
//                // Create a new intent to view the earthquake URI
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
//
//                // Send the intent to launch a new activity
//                startActivity(websiteIntent);
            }

        });



//        changeBtn.getLocationInWindow(location);
//        Log.e(TAG, "onCreateView: "+location[0]+" "+location[1]);
//        p=new Point();
//        p.x=location[0];
//        p.y=location[1];
//        Log.e(TAG, "onCreateView: "+p.x+" "+p.y);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Log.i(LOG_TAG, "loadInBackground: "+"12");
        return rootView;
    }

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void showPopup(final Activity context, Point p, final Word currentEarthquake) {
        int popupWidth = 400;
        int popupHeight = 150;

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        int OFFSET_X = 150;
        int OFFSET_Y = -70;

        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x+OFFSET_X , p.y+OFFSET_Y);

        Button toMaps = (Button) layout.findViewById(R.id.toMaps);
        toMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                viewPager.setCurrentItem(1);
                LatLng latlng=new LatLng(currentEarthquake.getLat(),currentEarthquake.getLongi());

                Date dateObject=new Date(currentEarthquake.getTimeInMilliseconds());
                SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy  h:mm a");
                String formattedDate=dateFormat.format(dateObject);

                String info=currentEarthquake.getPlace();
                String details="Mag-"+currentEarthquake.getMag()+" Time- "+formattedDate;
                mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title(info)
                        .snippet(details)
                        .alpha(0)
                ).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));

            }
        });


        Button toUsgs=(Button) layout.findViewById(R.id.usgs);
        toUsgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }


}
