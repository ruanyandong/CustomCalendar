package com.example.ai.customcalendar;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NewCalendar.NewCalendarListener{

    NewCalendar newCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newCalendar=findViewById(R.id.NewCalendar);
        newCalendar.setNewCalendarListener(this);

    }

    @Override
    public void onItemLongPress(Date day) {
        DateFormat df= SimpleDateFormat.getDateInstance();
        Toast.makeText(MainActivity.this,df.format(day), Toast.LENGTH_LONG).show();

    }
}
