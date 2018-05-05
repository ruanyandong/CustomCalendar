package com.example.ai.customcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 以组合系统控件的方式自定义控件
 */
public class NewCalendar extends LinearLayout {


    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView gridView;
    /**
     * 利用系统的日历控件来确定时间
     */
    private Calendar curDate=Calendar.getInstance();

    private String displayFormat;

    public void setNewCalendarListener(NewCalendarListener newCalendarListener) {
        this.newCalendarListener = newCalendarListener;
    }

    public NewCalendarListener newCalendarListener;

    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context,
                       @Nullable AttributeSet attrs) {
        super(context, attrs);

        initControl(context,attrs);
    }

    public NewCalendar(Context context,
                       @Nullable AttributeSet attrs,
                       int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initControl(context,attrs);
    }

    private void initControl(Context context,AttributeSet attrs){

        bindControl(context);
        bindControlEvent();


        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.NewCalendar);

        try{
            String format=ta.getString(R.styleable.NewCalendar_dateFormat);
            displayFormat=format;
            if (displayFormat==null){
                displayFormat="MMM yyyy";
            }
        }finally {
            ta.recycle();
        }


        renderCalendar();
    }



    /**
     * 将calendar_view布局文件里控件进行绑定
     */
    private void bindControl(Context context) {

        LayoutInflater inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view,this);

        btnPrev=this.findViewById(R.id.btnPrev);
        btnNext=this.findViewById(R.id.btnNext);
        txtDate=this.findViewById(R.id.txtDate);
        gridView=this.findViewById(R.id.calendar_grid);
    }

    /**
     * 对相应的控件进行交互点击事件的绑定
     */
    private void bindControlEvent() {

        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 单位是MONTH，日历向前翻一个月
                 */
                curDate.add(Calendar.MONTH,-1);

                renderCalendar();
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 单位是MONTH，日历向后翻一个月
                 */
                curDate.add(Calendar.MONTH,1);
                renderCalendar();
            }
        });

    }

    /**
     *渲染日历控件
     */
    private void renderCalendar() {
        SimpleDateFormat sdf=new SimpleDateFormat(displayFormat);

        txtDate.setText(sdf.format(curDate.getTime()));

        ArrayList<Date> cells=new ArrayList<>();

        Calendar calendar=(Calendar)curDate.clone();

        /**
         * 把calendar代表的日期设置为一个月的1号，其它所有的数值会被重新计算
         */
        calendar.set(Calendar.DAY_OF_MONTH,1);

        /**
         * 获得星期几（注意（这个与Date类是不同的）：1代表星期日、2代表星期1、3代表星期二，以此类推）
         */
        int prevDays=calendar.get(Calendar.DAY_OF_WEEK)-1;

        /**
         * 把calendar对象的日期减去prevDays，也就是calendar也就表示为prevDays天前的日期，其它所有的数值会被重新计算
         */
        calendar.add(Calendar.DAY_OF_MONTH,-prevDays);
        /**
         * 一个月最多是6行7列
         */
        int maxCellCount=6*7;
        /**
         * 生成月份表
         */
        while (cells.size()<maxCellCount){

            cells.add(calendar.getTime());

            calendar.add(Calendar.DAY_OF_MONTH,1);
        }


        gridView.setAdapter(new CalendarAdapter(getContext(),R.layout.calendar_text_day,cells));

        /**
         * 如果onItemLongClick返回false那么onItemClick仍然会被调用。而且是先调用onItemLongClick，然后调用onItemClick,如果返回true那么onItemClick就不会再被调用了。

         onItemClick以及onItemLongClick对弹出菜单的影响：onItemClick不影响弹出菜单；onItemLongClick如果返回true那么菜单不能弹出，只有在onItemLongClick如果返回false的时候才会弹出菜单。

         让包含button的item也能弹出菜单，回调onItemClick以及onItemLongClick的监听器，需要设置Button属性：

         android:focusable="false"
         android:focusable="false"
         android:longClickable="true"
         android:longClickable="true"
         */
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * @param parent 是识别是哪个listview；
             * @param view 是当前listview的item的view的布局，就是可以用这个view，获取里面的控件的id后操作控件

             * @param position 是当前item在listview中适配器里的位置
             * @param id 是当前item在listview里的第几行的位置
             * @return
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               if(newCalendarListener==null){
                   return false;
               }else{
                   newCalendarListener.onItemLongPress((Date)parent.getItemAtPosition(position));

                   return true;
               }

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newCalendarListener.onItemLongPress((Date)parent.getItemAtPosition(position));
            }
        });

    }

    private class CalendarAdapter extends ArrayAdapter<Date>{

        LayoutInflater inflater;

        public CalendarAdapter(@NonNull Context context,
                               int resource,
                               ArrayList<Date> dates) {
            super(context, resource,dates);
            inflater=LayoutInflater.from(context);
        }


        @NonNull
        @Override
        public View getView(int position,
                            @Nullable View convertView,
                            @NonNull ViewGroup parent) {

            final Date date=getItem(position);

            if (convertView==null){
                convertView=inflater.inflate(R.layout.calendar_text_day,parent,false);
            }

            /**
             * 返回一个月的第几天
             */
            int day=date.getDate();

            ((CalendarDayTextView)convertView).setText(String.valueOf(day));




            /**
             * 当天日期
             */
            Date now=new Date();


            //Calendar calendar=(Calendar)curDate.clone();
            //calendar.set(Calendar.DAY_OF_MONTH,1);
            /**
             * getActualMaximum函数，主要是用于获取一个指定日期的当月总天数
             */
            //int daysInMonth=calendar.getActualMaximum(Calendar.DATE);

            /**
             * 如果相等，就表示当月日期
             */
            if (now.getMonth()==date.getMonth()){
                ((CalendarDayTextView)convertView).setTextColor(Color.parseColor("#FF4081"));

            }else{
                /**
                 * 不是当月日期
                 */
                ((CalendarDayTextView)convertView).setTextColor(Color.parseColor("#339966"));

            }


            /**
             * 处于当天,就设置为选中状态
             */
            if (now.getDate()==date.getDate()
                    &&now.getMonth()==date.getMonth()
                    &&now.getYear()==date.getYear()){

                ((CalendarDayTextView)convertView).setTextColor(Color.parseColor("#ff0000"));
                ((CalendarDayTextView)convertView).isToday=true;
            }

            return convertView;
        }
    }

    public interface NewCalendarListener{

        void onItemLongPress(Date day);

    }

}
