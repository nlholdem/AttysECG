package tech.glasgowneuro.attysecg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import android.util.Log;

import java.util.ArrayList;
import java.util.ListIterator;


/**
 * Created by paul on 06/02/17.
 */

public class HRVView extends View {
    final String TAG = "HRVView";

    private final int MAXSAMPLES = 100, STROKEWIDTH = 4, INNERCIRCLEWIDTH = 40;
    private float heartRate = 50;
    private ArrayList<Float> HRVValues;

    private Paint paintClear = null;
    private Paint paintWhite = null;
    private Paint paintCircle = null;

    public HRVView(Context context){
        super(context);
        Log.d(TAG, "Constructor #1");
        init();
    }

    public HRVView(Context context, AttributeSet attrs){
        super(context, attrs);
        Log.d(TAG, "Constructor #2");
        init();
    }
    public HRVView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        Log.d(TAG, "Constructor #3");
        init();
    }

    private void init(){
        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
        paintCircle = new Paint();
        paintClear = new Paint();
        paintClear.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        HRVValues = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        ListIterator li = HRVValues.listIterator();

        canvas.drawPaint(paintClear);

        int i = HRVValues.size();
        while (li.hasNext()){
            heartRate = (float) li.next();
            paintCircle.setColor(heartRateToColour(heartRate));

//            Log.d(TAG, "RadiusList = " + li.previous());
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, INNERCIRCLEWIDTH + i * STROKEWIDTH, paintCircle);
            i--;

        }
        Log.d(TAG, "HeartRate:  " + heartRate + " Color: " + paintCircle.getColor() + " i: " + i);
    }


    private int heartRateToColour(float HR){

        float maxHR  = 100, minHR = 30;
        // normalise HR

        HR = 256 * (HR - minHR) / (maxHR - minHR);
        // lookup colour in colourmap

        return Color.argb(255, (int) HR, 0, 0);

    }

    public void setHeartRate(float rad){
//        heartRate = (int) rad;
        HRVValues.add(rad);

        if(HRVValues.size() > MAXSAMPLES){
            HRVValues.remove(0);
        }
        Log.d(TAG, "HRVValues length: " + HRVValues.size());
    }


    public float getHeartRate(){
        return heartRate;
    }

}
