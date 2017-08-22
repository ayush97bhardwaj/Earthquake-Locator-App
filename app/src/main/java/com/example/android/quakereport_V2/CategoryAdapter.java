package com.example.android.quakereport_V2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import static com.example.android.quakereport_V2.MainEarthquakeActivity.LOG_TAG;

/**
 * Created by ayush2009 on 23/4/17.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;
    public CategoryAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext=context;
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            Log.i(LOG_TAG, "getItem: "+"1");
            return new EarthquakesListFragment();
        } else {Log.i(LOG_TAG, "getItem: "+"2");
            return new MapFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.list);
        } else {
            return mContext.getString(R.string.title_activity_maps);
        }
    }
}


//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        if (position == 0) {
//            return mContext.getString(R.string.category_numbers);
//        } else if (position == 1) {
//            return mContext.getString(R.string.category_family);
//        } else if (position == 2) {
//            return mContext.getString(R.string.category_colors);
//        } else {
//            return mContext.getString(R.string.category_phrases);
//        }
//    }

