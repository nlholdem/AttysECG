package tech.glasgowneuro.attysecg;


import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.androidplot.ui.SizeMetric;
import com.androidplot.ui.SizeMode;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYPlot;

import java.util.ArrayList;


/**
 * Created by paul on 06/02/17.
 */

public class HRVPlotFragment extends Fragment {

    String TAG = "HRVPlotFragment";

    private int history_size = 250;
    private float range = 1;

    private SimpleXYSeries vectorHistorySeries = null;
    private XYPlot vectorPlot = null;
    private HRVView hrvView = null;


    View view = null;

    void setHistorySize(int historySize) {
        history_size = historySize;
    }

    void setGain(float _gain) {
        range = 750 / _gain;
        setScale();
    }

    void setScale() {
        if (vectorPlot != null) {
            vectorPlot.setRangeBoundaries(range, -range, BoundaryMode.FIXED);
            vectorPlot.setDomainBoundaries(-range, range, BoundaryMode.FIXED);
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreate, creating Fragment");

        if (container == null) {
            return null;
        }

        view = inflater.inflate(R.layout.hrvplotfragment, container, false);
        if (view == null) {
                Log.e(TAG, "view == NULL!");
        }
        Log.d(TAG, "Created view");

        hrvView = (HRVView) view.findViewById(R.id.HRVPlotView);
        return view;
    }


    public synchronized void redraw() {

        if (vectorPlot != null) {
            vectorPlot.redraw();
        }
    }

    public synchronized void addValue(final float x){

        if(hrvView != null) {
            hrvView.setHeartRate(x);

            /*
            The following is needed in order to do operations on hrvView - if we don't do this,
            we get a crash with 'Only the original thread that created a view hierarchy can touch its views.'
            */
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    hrvView.invalidate();
                }
            });
            Log.d(TAG, "addValue: " + x);
        }
        else {
            Log.d(TAG, "addValue: jrvView is null!");
            return;
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
