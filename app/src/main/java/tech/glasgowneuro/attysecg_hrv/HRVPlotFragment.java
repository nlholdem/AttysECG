package tech.glasgowneuro.attysecg_hrv;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;


/**
 * Created by paul on 06/02/17.
 */

public class HRVPlotFragment extends Fragment {

    String TAG = "HRVPlotFragment";

    private int history_size = 250;
    private float range = 1;

    private SimpleXYSeries vectorHistorySeries = null;
    private HRVView hrvView = null;


    View view = null;

    void setHistorySize(int historySize) {
        history_size = historySize;
    }



    /**
     * Called when the activity is first created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

//        Log.d(TAG, "onCreate, creating Fragment");

        if (container == null) {
            return null;
        }


        view = inflater.inflate(R.layout.hrvplotfragment, container, false);
        if (view == null) {
//                Log.e(TAG, "view == NULL!");
        }
//        Log.d(TAG, "Created view");

        hrvView = (HRVView) view.findViewById(R.id.HRVPlotView);
        return view;
    }


/*    public synchronized void redraw() {

        if (vectorPlot != null) {
            vectorPlot.redraw();
        }
    }
*/
    public synchronized void addValue(final float x, float samplingRate){

        if(hrvView != null) {
            hrvView.setHeartRate(x, samplingRate);

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
//            Log.d(TAG, "addValue: " + x);
        }
        else {
//            Log.d(TAG, "addValue: hrvView is null!");
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
