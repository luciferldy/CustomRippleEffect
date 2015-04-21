package com.main.maybe.customrippleeffect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Maybe霏 on 2015/4/21.
 */
public class CustomView extends View {

    private final String TAG_NAME = CustomView.class.getSimpleName();
    // 圆从最小到最大，需要多长时间
    private int DURATION = 2000;
    // 想要画的园的最大半径
    private float radiusMax = 0;
    // View的最大宽度
    private int WIDTH;
    // View的最大高度
    private int HEIGHT;
    // 记录手指点下去的x坐标
    private float x;
    // 记录手指点下去的y坐标
    private float y;
    // 对timer的放大系数
    private int FRAME_RATE = 10;
    // 通过timer的不断自增，达到让圆不断增大的效果
    private int timer = 0;
    // Handler更新界面用的
    private Handler canvasHandler;
    // 是否正在播放动画
    private boolean animationRunnig = false;
    // 画笔
    private Paint paint;
    // 结合handler用来刷新当前界面的任务
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public CustomView(Context context) {
        super(context);
        initPaint(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG_NAME, "onSizeChanged, w:"+w+" h:"+h);
        WIDTH = w;
        HEIGHT = h;
    }

    // 初始化画笔
    private void initPaint(Context context){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.white));
        canvasHandler = new Handler();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        animateRipple(event);
        return super.onTouchEvent(event);
    }

    public void animateRipple(MotionEvent event){
        // 当前没有播放动画时，执行这些赋值操作
        if (!animationRunnig){
            radiusMax = Math.max(WIDTH, HEIGHT);
            Log.d(TAG_NAME, "radius:"+radiusMax);
            this.x = event.getX();
            this.y = event.getY();
            animationRunnig = true;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (animationRunnig){
            if (DURATION <= timer*FRAME_RATE){
                animationRunnig = false;
                timer = 0;
                canvas.restore();
                Log.d(TAG_NAME, "animation end.");
                return;
            }else{
                // 每隔FRAME_RATE刷新一下
                canvasHandler.postDelayed(runnable, FRAME_RATE);
            }

            if (timer == 0)
                canvas.save();

            float currentRadius = radiusMax * (((float) timer * FRAME_RATE) / DURATION);
            Log.d(TAG_NAME, "current radius:"+currentRadius);
            canvas.drawCircle(x, y, currentRadius, paint);
            timer++;
        }
    }
}
