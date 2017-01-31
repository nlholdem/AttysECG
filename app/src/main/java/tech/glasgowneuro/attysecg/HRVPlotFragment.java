package tech.glasgowneuro.attysecg;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYPlot;

/**
 * Created by paul on 25/01/17.
 */

public class HRVPlotFragment extends Fragment {

    String TAG = "HRVPlotFragment";

    private static final int HISTORY_SIZE = 60;
    private SimpleXYSeries hrvHistorySeries = null;
    private XYPlot hrvPlot = null;
    private TextView hrvText = null;

    private View view = null;
    private Canvas canvas = null;
    private Paint paintWhite = new Paint();


    /**
     * Called when the activity is first created.
     */




    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreate, creating HRV Fragment");

        if (container == null) {
            return null;
        }

        view = inflater.inflate(R.layout.hrvplotfragment, container, false);
        Log.d(TAG, "HRVPlotFragment is accelerated: " + view.isHardwareAccelerated());
        paintWhite.setColor(Color.WHITE);


        // setup the APR Levels plot:
        hrvPlot = (XYPlot) view.findViewById(R.id.hrvPlotView);
        hrvText = (TextView) view.findViewById(R.id.hrvTextView);

        hrvHistorySeries = new SimpleXYSeries("Heart rate variability");
        if (hrvHistorySeries == null) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "hrvHistorySeries == null");
            }
        }
        hrvHistorySeries.useImplicitXVals();

        hrvPlot.setRangeBoundaries(0, 200, BoundaryMode.FIXED);
        hrvPlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED);
        hrvPlot.addSeries(hrvHistorySeries,
                new LineAndPointFormatter(
                        Color.rgb(100, 255, 255), null, null, null));
        hrvPlot.setDomainLabel("Heartbeat #");
        hrvPlot.setRangeLabel("");

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        if ((height > 1000) && (width > 1000)) {
            hrvPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 25);
            hrvPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 25);
        } else {
            hrvPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 50);
            hrvPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 50);
        }

        Paint paint = new Paint();
        paint.setColor(Color.argb(128, 0, 255, 0));
        hrvPlot.getGraph().setDomainGridLinePaint(paint);
        hrvPlot.getGraph().setRangeGridLinePaint(paint);

        return view;

    }

    class MyRelativeLayout extends RelativeLayout
    {
        public MyRelativeLayout(Context context){
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas){
            Log.d(TAG, "*** HRVPlot: onDraw()");

            if(hrvHistorySeries != null){
                for (int i = 0; i < hrvHistorySeries.size(); i++){
                    canvas.drawCircle((canvas.getWidth() / 2), (canvas.getHeight() / 2), (float)hrvHistorySeries.getY(i), paintWhite);
                }
            }
        }

    }


    public synchronized void addValue(final float v) {



        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
/*                    if (bpmText != null) {
                        hrvText.setText(String.format("%03d BPM", (int) v));
                    }
*/
                }
            });
        }

        if (hrvHistorySeries == null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "hrvHistorySeries == null");
            }
            return;
        }
        // get rid the oldest sample in history:
        if (hrvHistorySeries.size() > HISTORY_SIZE) {
            hrvHistorySeries.removeFirst();
        }

        // add the latest history sample:
        hrvHistorySeries.addLast(null, v);
//        hrvPlot.redraw();

        if(view != null){
            view.invalidate();
        }
    }
}
