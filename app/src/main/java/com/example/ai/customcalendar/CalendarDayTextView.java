package com.example.ai.customcalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CalendarDayTextView extends AppCompatTextView {

    /**
     * 判断是否是当天
     */
    public boolean isToday=false;

    private Paint paint=new Paint();

    public CalendarDayTextView(Context context) {
        super(context);
    }

    public CalendarDayTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl();
    }



    public CalendarDayTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl();
    }

    private void initControl() {
        /**
         * 描边，描圆圈
         */
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#ff0000"));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isToday){
            /**
             * 移动到TextView的中心点位置
             * 把当前画布的原点移到(getWidth()/2,getHeight()/2),后面的操作都以(getWidth()/2,getHeight()/2)作为参照点，默认原点为(0,0)
             */
            canvas.translate(getWidth()/2,getHeight()/2);

            /**
             *
             cx：圆心的x坐标。
             cy：圆心的y坐标。
             radius：圆的半径。
             paint：绘制时所使用的画笔。

             */
            canvas.drawCircle(0,0,getWidth()/2,paint);
        }

    }
}
