package tech.glasgowneuro.attysecg_hrv;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.Surface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import android.util.Log;

/**
 * Created by paul on 31/01/17.
 */



public class MySimpleView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder = null;
    private Canvas canvas = null;

    private static final int STROKE_WIDTH = 2;
    private Paint mBasePaint, mDegreesPaint, mCenterPaint, mRectPaint, paintWhite;
    private RectF mRect;
    private int centerX, centerY, radius;

    String TAG = "MySimpleView";

    public MySimpleView(Context context) {
        super(context);
        Log.d(TAG, "MySimpleView: constructor #1");
        init();
    }

    public MySimpleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "MySimpleView: constructor #2");
        init();
    }

    public MySimpleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "MySimpleView: constructor #3");
        init();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        initYpos(width);
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
    }


    private void init() {
        Log.d(TAG, "MySimpleView: init()");

        holder = getHolder();
//        holder.setFormat(PixelFormat.OPAQUE);

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(Color.RED);
        mRectPaint.setStyle(Paint.Style.FILL);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(Color.WHITE);
        mCenterPaint.setStyle(Paint.Style.FILL);

        mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBasePaint.setStyle(Paint.Style.STROKE);
        mBasePaint.setStrokeWidth(STROKE_WIDTH);
        mBasePaint.setColor(Color.BLUE);

        mDegreesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDegreesPaint.setStyle(Paint.Style.STROKE);
        mDegreesPaint.setStrokeWidth(STROKE_WIDTH);
        mDegreesPaint.setColor(Color.GREEN);

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);


    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Log.d(TAG, "MySimpleView: onDraw()");


        // getHeight() is not reliable, use getMeasuredHeight() on first run:
        // Note: mRect will also be null after a configuration change,
        // so in this case the new measured height and width values will be used:
/*        if (mRect == null)
        {
            // take the minimum of width and height here to be on he safe side:
            centerX = getMeasuredWidth()/ 2;
            centerY = getMeasuredHeight()/ 2;
            radius = Math.min(centerX,centerY);

            // mRect will define the drawing space for drawArc()
            // We have to take into account the STROKE_WIDTH with drawArc() as well as drawCircle():
            // circles as well as arcs are drawn 50% outside of the bounds defined by the radius (radius for arcs is calculated from the rectangle mRect).
            // So if mRect is too large, the lines will not fit into the View
            int startTop = STROKE_WIDTH / 2;
            int startLeft = startTop;

            int endBottom = 2 * radius - startTop;
            int endRight = endBottom;

            mRect = new RectF(startTop, startLeft, endRight, endBottom);
        }
*/
        // subtract half the stroke width from radius so the blue circle fits inside the View
//        canvas.drawCircle(centerX, centerY, radius - STROKE_WIDTH / 2, mBasePaint);
        // Or draw arc from degree 192 to degree 90 like this ( 258 = (360 - 192) + 90:
        // canvas.drawArc(mRect, 192, 258, false, mBasePaint);

        // draw an arc from 90 degrees to 192 degrees (102 = 192 - 90)
        // Note that these degrees are not like mathematical degrees:
        // they are mirrored along the y-axis and so incremented clockwise (zero degrees is always on the right hand side of the x-axis)
//        canvas.drawArc(mRect, 90, 102, false, mDegreesPaint);

        // subtract stroke width from radius so the white circle does not cover the blue circle/ arc
//        canvas.drawCircle(centerX, centerY, radius - STROKE_WIDTH, mCenterPaint);

//        Paint paintTemp = new Paint();
//        paintTemp.setColor(Color.WHITE);
//        canvas.drawCircle((getMeasuredWidth() / 2), (getMeasuredHeight() / 2), (getMeasuredHeight() / 2), paintTemp);
    }

    public synchronized void setRadius(int rad) {
        radius = rad;
        drawHRV();
    }


    public synchronized void drawHRV() {

        if (holder == null) {
            Log.d(TAG, "MySimpleView: drawHRV - holder is null");
            return;
        }
        Surface surface = holder.getSurface();
        if (!surface.isValid()) {
            Log.d(TAG, "MySimpleView: drawHRV - surface is null");
            return;
        }
        canvas = holder.lockCanvas();
        if (canvas == null) {
            Log.d(TAG, "MySimpleView: drawHRV - canvas is null");
            return;

        }
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//          canvas.drawPaint(paint);

        Log.d(TAG, "MySimpleView: radius is " + radius);
//          canvas.drawCircle((canvas.getWidth() / 2), (canvas.getHeight() / 2), radius, paintWhite);
        Paint paintTemp = new Paint();
        paintTemp.setColor(Color.WHITE);
        canvas.drawCircle((getMeasuredWidth() / 2), (getMeasuredHeight() / 2), getMeasuredHeight(), paintTemp);

        holder.unlockCanvasAndPost(canvas);
        canvas = null;
    }
}