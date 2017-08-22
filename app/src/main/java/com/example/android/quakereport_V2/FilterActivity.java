package com.example.android.quakereport_V2;

import android.*;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.format;
import static android.R.attr.order;
import static android.R.attr.radius;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.R.style.Theme;
import static android.os.Build.VERSION_CODES.M;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.LOADER_ID;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.LOG_TAG;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.MY_REQUEST_FINE_LOCATION;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.TOP_EARTHQUAKES_URL;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.loader;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.locationListener;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.locationManager;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.myLat;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.myLat2;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.myLng;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.myLng2;
import static com.example.android.quakereport_V2.MapFragment.mMap;
import static com.example.android.quakereport_V2.R.array.limit;
import static com.example.android.quakereport_V2.R.id.filterButton;
import static com.example.android.quakereport_V2.R.id.from;
import static com.example.android.quakereport_V2.R.style.AppTheme;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class FilterActivity extends AppCompatActivity{

    public String day;
    public Calendar myCalendar;
    public static int counter=0;
    public EditText from;
    public EditText to;
    public static CircleOptions circle;
    public static int checker=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.filter);

        final Drawable upArrow =ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this,android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Spinner spinner = (Spinner) findViewById(R.id.locationSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.location, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        new selectItem(spinner);


//
//
//
        final Spinner spinner2 = (Spinner) findViewById(R.id.magSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.magnitude, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        new selectItem(spinner2);
//
//
//
        final Spinner spinner3 = (Spinner) findViewById(R.id.limitSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.limit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapter3);
        new selectItem(spinner3);
//
//
//
        myCalendar = Calendar.getInstance();
        from = (EditText) findViewById(R.id.from);
        to = (EditText) findViewById(R.id.to);

        creatingDialog(from);
        creatingDialog(to);

        String strCurrentDate = DateFormat.getDateInstance().format(new Date());
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e(LOG_TAG, "onCreate: " + from.getText());
//        if(from.getText()==null) {
        format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(newDate);
        to.setText(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(newDate);
        calendar2.add(Calendar.DATE, -30);
        Log.e(LOG_TAG, "onCreate: " + date + " are u sure ..its the correct date" + calendar2.getTime());
        format = new SimpleDateFormat("E MMM dd hh:mm:ss z yyyy");
        try {
            newDate = format.parse(calendar2.getTime().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(newDate);
        from.setText(date);
        Log.e(LOG_TAG, "onCreate: lets see if it turns out ok :" + date);


//
//
//
//
        final Spinner spinner4 = (Spinner) findViewById(R.id.orderbySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.orderby, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner4.setAdapter(adapter4);
        new selectItem(spinner4);

//
//
//

        Button filterButton=(Button) findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mag= (String) spinner2.getSelectedItem();
                String limit= (String) spinner3.getSelectedItem();

                String order=(String) spinner4.getSelectedItem();
                if(limit.equals("No Limit"))
                    limit="";
                Log.e(LOG_TAG, "onCreate: "+ mag+" "+limit);
                String rad=(String) spinner.getSelectedItem();

                String part[]=rad.split(" ");
                Log.e(TAG, "onClick: "+myLat+" "+myLng);

                checker=2;

                circle=new CircleOptions();
                 circle.center(new LatLng(myLat2,myLng2))
                        .fillColor(0x55ff9987).strokeWidth(3).strokeColor(0x80ff9987);

                if(rad.equals("Worldwide")) {
                    TOP_EARTHQUAKES_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=" + order + "&minmag=" + mag + "&limit=" + limit + "&starttime=" + from.getText() + "&endtime=" + to.getText();
                }else {
                    checker=1;
                    circle.radius(Double.parseDouble(part[0])*1000);
                    TOP_EARTHQUAKES_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby="+order+"&minmag=" + mag + "&limit=" + limit + "&starttime=" + from.getText() + "&endtime=" + to.getText() +"&latitude="+myLat+"&longitude="+myLng+"&maxradiuskm="+part[0];
                }
                Log.e(LOG_TAG, "onClick: "+TOP_EARTHQUAKES_URL );
                Intent intent=new Intent(v.getContext(), MainEarthquakeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                loader.restartLoader(LOADER_ID,null,);
                v.getContext().startActivity(intent);
            }
        });

    }

    public void creatingDialog(final EditText views){
        views.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.e(LOG_TAG, "onClick: "+ views.getHint());

                if(views.getHint().equals("from")) {
                    counter = 1;
                    Log.e(LOG_TAG, "onClick: "+counter+" "+views.getHint());
                }
                new DatePickerDialog(v.getContext(),myDateListener,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public DatePickerDialog.OnDateSetListener myDateListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            day=year+"-"+String.format("%02d",month+1)+"-"+String.format("%02d",dayOfMonth);

            Log.e(LOG_TAG, "onDateSet: "+ day);
            if(counter==1)
                from.setText(day);
            else
                to.setText(day);

            counter=0;
        }
    };

    public class selectItem{
        public selectItem(final Spinner spinner){
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e(LOG_TAG, "onItemSelected: "+getResources().getResourceEntryName(spinner.getId()));
                    parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.e(LOG_TAG, "onNothingSelected: "+ spinner.getId());
                    parent.getItemAtPosition(0);
                }

            });
        }
    }

}
