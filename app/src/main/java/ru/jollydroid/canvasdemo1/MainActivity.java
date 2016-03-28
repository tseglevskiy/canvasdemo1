package ru.jollydroid.canvasdemo1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SurfaceView sv;
    private SurfaceHolder holder;
    private Surface surface;

    public static final String TAG = "happy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sv = (SurfaceView) findViewById(R.id.sv);
        holder = sv.getHolder();

        holder.addCallback(callback);
    }

    SurfaceHolder.Callback2 callback = new SurfaceHolder.Callback2() {
        @Override
        public void surfaceRedrawNeeded(SurfaceHolder holder) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            startPaint();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            stopPaint();
            startPaint();

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopPaint();
        }
    };

    private void startPaint() {
        surface = holder.getSurface();
        Rect rect = holder.getSurfaceFrame();

        Log.d(TAG, "rect.left " + rect.left);
        Log.d(TAG, "rect.right " + rect.right);
        Log.d(TAG, "rect.top " + rect.top);
        Log.d(TAG, "rect.bottom " + rect.bottom);
        Log.d(TAG, "rect.width " + rect.width());
        Log.d(TAG, "rect.height " + rect.height());

        Log.d(TAG, "sv.getX " + sv.getX());
        Log.d(TAG, "sv.getY " + sv.getY());
        Log.d(TAG, "sv.getTop " + sv.getTop());
        Log.d(TAG, "sv.getBottom " + sv.getBottom());
        Log.d(TAG, "sv.getLeft " + sv.getLeft());
        Log.d(TAG, "sv.getRight " + sv.getRight());
        Log.d(TAG, "sv.getWidth " + sv.getWidth());
        Log.d(TAG, "sv.getHeight " + sv.getHeight());

        sv.setOnTouchListener(touchListener);


    }

    private void stopPaint() {
        surface = null;

        sv.setOnTouchListener(null);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                paintStartDot(event.getX(), event.getY());

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                paintEndDot(event.getX(), event.getY());

            }

            return true;
        }

    };

    private int paintColor = 0xffff0000;
    private Paint drawPaint = new Paint();

    {
        drawPaint.setColor(paintColor);
//        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    float lastX, lastY;

    private void paintStartDot(float x, float y) {
        Canvas canvas = surface.lockCanvas(null);

        canvas.drawPoint(x, y, drawPaint);
        surface.unlockCanvasAndPost(canvas);

        lastX = x;
        lastY = y;
    }

    private void paintEndDot(float x, float y) {
        Canvas canvas = surface.lockCanvas(null);

        canvas.drawLine(lastX, lastY, x, y, drawPaint);

        surface.unlockCanvasAndPost(canvas);

        lastX = x;
        lastY = y;
    }
}
