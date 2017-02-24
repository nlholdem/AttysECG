package tech.glasgowneuro.attysecg_hrv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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

    private final int MAXSAMPLES = 200, STROKEWIDTH = 2, INNERCIRCLEWIDTH = 40;
    private float heartRate = 50;
    private float txtSizeMult = (float) 1.4;
    private ArrayList<Float> HRVValues;

    private Paint paintClear = null;
    private Paint paintWhite = null;
    private Paint paintBlack = null;
    private Paint paintCircle = null;
    private Paint paintTxt = null;

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
        paintBlack = new Paint();
        paintBlack.setColor(Color.BLACK);
        paintCircle = new Paint();
        paintClear = new Paint();
        paintClear.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        HRVValues = new ArrayList<>();
        paintTxt = new Paint();
        paintTxt.setColor(Color.argb(255, 255, 255, 0));
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        ArrayList<Float> TempHRVValues = new ArrayList<Float>(HRVValues);

        ListIterator li = TempHRVValues.listIterator();

        canvas.drawPaint(paintClear);

        int txtDiv = 25;
        Rect bounds = new Rect();
        String HRVTxt = "";
        HRVTxt = HRVTxt + "".format("%d", (int)heartRate);
        int centreX = getWidth() / 2;
        int centreY = getHeight() / 2;
        paintTxt.setTextSize((float)INNERCIRCLEWIDTH * txtSizeMult);

        int i = HRVValues.size();
        while (li.hasNext()){
            heartRate = (float) li.next();

            // todo: replace this with a lookup table calculated during init()
            paintCircle.setColor(heartRateToColour(heartRate));

//            Log.d(TAG, "RadiusList = " + li.previous());
            canvas.drawCircle(centreX, centreY, INNERCIRCLEWIDTH + i * STROKEWIDTH, paintClear);
            canvas.drawCircle(centreX, centreY, INNERCIRCLEWIDTH + i * STROKEWIDTH, paintCircle);
            canvas.drawText(HRVTxt, centreX - INNERCIRCLEWIDTH / 2, centreY + INNERCIRCLEWIDTH / 2, paintTxt);
            i--;

        }
        Log.d(TAG, "HeartRate:  " + heartRate + " Color: " + paintCircle.getColor() + " i: " + i);
    }


    private int heartRateToColour(float HR){

        float maxHR  = 100, minHR = 35;
        // normalise HR

        HR = 256 * (HR - minHR) / (maxHR - minHR);
        // lookup colour in colourmap

        return Color.argb(200, (int) (HR /1.8), (int) (HR / 1.5), (int) HR);

    }

    public synchronized void setHeartRate(float rad){
//        heartRate = (int) rad;
        HRVValues.add(rad);

        if(HRVValues.size() > MAXSAMPLES){
            HRVValues.remove(0);
        }
        Log.d(TAG, "HRVValues length: " + HRVValues.size());
    }


}
