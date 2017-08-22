package com.example.android.quakereport_V2;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import static com.example.android.quakereport_V2.MainEarthquakeActivity.LOG_TAG;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.mAdapter;
import static com.example.android.quakereport_V2.R.id.date;
import static com.example.android.quakereport_V2.R.id.exactLocation;
import static com.example.android.quakereport_V2.R.id.mag;
import static com.example.android.quakereport_V2.R.id.primaryPlace;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * Created by ayush2009 on 29/3/17.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    public WordAdapter(Context context, ArrayList<Word> word) {
        super(context, 0, word);
        Log.i(TAG, "WordAdapter: "+"1");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView=convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view, parent, false);
        }
        Word currentWord=getItem(position);

        TextView magnitude=(TextView) listItemView.findViewById(R.id.mag);
        String mag = DecimalFormatter(currentWord.getMag());
        magnitude.setText(mag);
//
//
        TextView magText=(TextView) listItemView.findViewById(R.id.mag);

        int magnitudeColor = getMagnitudeColor(currentWord.getMag());
        magText.setTextColor(magnitudeColor);



        String originalLocation=currentWord.getPlace();

        TextView exactLocation=(TextView) listItemView.findViewById(R.id.exactLocation);

        TextView primaryLocation=(TextView) listItemView.findViewById(R.id.primaryPlace);

        if(originalLocation.contains(" of "))
        {
            String[] part=originalLocation.split(" of ");
//            part[0]+="of";

            Log.e(TAG, "getView: "+originalLocation);

            exactLocation.setText(part[0]+" of");

            primaryLocation.setText(part[1]);
        }else{
            exactLocation.setText("Near of");

            primaryLocation.setText(originalLocation);
        }

//
//
        Date dateObject=new Date(currentWord.getTimeInMilliseconds());

        TextView date=(TextView) listItemView.findViewById(R.id.date);
        String formattedDate=formatDate(dateObject);
        date.setText(formattedDate);


        TextView time=(TextView) listItemView.findViewById(R.id.time);
        String formattedTime=formatTime(dateObject);
        time.setText(formattedTime);

//
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
//        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
//        int magnitudeColor = getMagnitudeColor(currentWord.getMag());
//
//        // Set the color on the magnitude circle
//        magText.setTextColor(magnitudeColor);

        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String DecimalFormatter(Double magnitude) {
        DecimalFormat formatter=new DecimalFormat("0.0");
        return formatter.format(magnitude);
    }

    public int getMagnitudeColor(double mag){

        int magColorId=0;
        int magFloor=(int) Math.floor(mag);
        switch (magFloor){
            case 0:
            case 1:
                magColorId=R.color.magnitude1;
                break;
            case 2:
                magColorId=R.color.magnitude2;
                break;
            case 3:
                magColorId=R.color.magnitude3;
                break;
            case 4:
                magColorId=R.color.magnitude4;
                break;
            case 5:
                magColorId=R.color.magnitude5;
                break;
            case 6:
                magColorId=R.color.magnitude6;
                break;
            case 7:
                magColorId=R.color.magnitude7;
                break;
            case 8:
                magColorId=R.color.magnitude8;
                break;
            case 9:
                magColorId=R.color.magnitude9;
                break;
            case 10:
                magColorId=R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),magColorId);
    }

}