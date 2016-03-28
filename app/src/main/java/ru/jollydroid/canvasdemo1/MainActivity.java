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

    public static final String TAG = "happy";

    /*
     * SufraceView можно использовать только после его инициализации.
     * О ней мы узнает из колбэка
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sv = (SurfaceView) findViewById(R.id.sv);
        assert sv != null;
        holder = sv.getHolder();

        holder.addCallback(callback);
    }

    /*
     * Колбэк.
     * surfaceChanged() будет вызвано как при первом создании, так и при изменении
     */

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

    /*
     * Рисовать мы будем в Bitmap, затем копировать его в SufraceView.
     * Соответственно, Bitmap надо создать.
     *
     * Переноса уже нарисованной картинки при изменении из старого битмапа в новый нет.
     */

    private Bitmap paperBitmap;
    private Canvas paperCanvas;

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

    /*
     * Обработка касаний. При касании ставим первую точку линии,
     * дальше продолжаем.
     *
     * Касание в нескольких точках не обрабатывается.
     */
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                drawStartDot(event.getX(), event.getY());

            } else if (event.getAction() == MotionEvent.ACTION_MOVE
                    || event.getAction() == MotionEvent.ACTION_UP)
            {
                drawEndDot(event.getX(), event.getY());
            }

            return true;
        }

    };

    /*
     * Инициализация элемента, из которого будет состоять линия
     */

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

    /*
     * рисуем точку и линию
     */

    float lastX, lastY;

    private void drawStartDot(float x, float y) {
        if (paperCanvas == null) return;

        paperCanvas.drawPoint(x, y, drawPaint);

        drawPaperBitmap(x, y);

    }

    private void drawEndDot(float x, float y) {
        if (paperCanvas == null) return;

        paperCanvas.drawLine(lastX, lastY, x, y, drawPaint);

        drawPaperBitmap(x, y);
    }

    /*
     * Копирование битмапа с картинкой в Surface.
     * Прямо в Bitmap от Surface рисовать нельзя - он может прийти, не соответствующий
     * текущему отображаемому состоянию.
     *
     * http://stackoverflow.com/a/36267113/1263771
     */
    private void drawPaperBitmap(float x, float y) {
        Canvas canvas = surface.lockCanvas(null);
        canvas.drawBitmap(paperBitmap, 0, 0, null);
        surface.unlockCanvasAndPost(canvas);

        lastX = x;
        lastY = y;
    }
}
