package ru.jollydroid.canvasdemo1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SurfaceView sv;
    private SurfaceHolder holder;
    private Surface surface;
    private Bitmap paperBitmap;

    public static final String TAG = "happy";
    private Canvas paperCanvas;

    

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
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            stopPaint();
            startPaint(width, height);

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopPaint();
        }
    };

    private void startPaint(int width, int height) {
        surface = holder.getSurface();
        paperBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        paperCanvas = new Canvas(paperBitmap);

        sv.setOnTouchListener(touchListener);
    }

    private void stopPaint() {
        sv.setOnTouchListener(null);

        surface = null;
        if (paperBitmap != null) {
            paperBitmap.recycle();
            paperBitmap = null;
            paperCanvas = null;
        }

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
        if (paperCanvas == null) return;

        paperCanvas.drawPoint(x, y, drawPaint);

        drawPaperBitmap(x, y);

    }

    private void paintEndDot(float x, float y) {
        if (paperCanvas == null) return;

        paperCanvas.drawLine(lastX, lastY, x, y, drawPaint);

        drawPaperBitmap(x, y);
    }

    private void drawPaperBitmap(float x, float y) {
        Canvas canvas = surface.lockCanvas(null);
        canvas.drawBitmap(paperBitmap, 0, 0, null);
        surface.unlockCanvasAndPost(canvas);

        lastX = x;
        lastY = y;
    }
}
