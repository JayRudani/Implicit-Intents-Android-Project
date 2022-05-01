package com.example.assessment3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.DatePickerDialog;

import java.sql.Time;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText title;
    EditText description;
    EditText emails;
    Button addEvent;
    Button startTime;
    Button endTime;
    CheckBox allDay;
    int hour, minute;

    //public static final int CAMERA_ACTION_CODE=1;
    ImageView image;
    Button capture;

    EditText alarmHour;
    EditText alarmMinute;
    Button setTime;
    Button setAlarm;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        emails = findViewById(R.id.emailinvites);
        addEvent=findViewById(R.id.btnAddEvent);
        startTime = findViewById(R.id.btnStartTime);
        endTime = findViewById(R.id.btnEndTime);
        allDay=findViewById(R.id.allDayEvent);

        image = findViewById(R.id.imageBox);
        capture = findViewById(R.id.btnCapture);

        alarmHour=findViewById(R.id.setHour);
        alarmMinute=findViewById(R.id.setMin);
        setTime=findViewById(R.id.btnSetTime);
        setAlarm=findViewById(R.id.btnSetAlarm);


        addEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!title.getText().toString().isEmpty()){

                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE,title.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY,allDay.isChecked());
                    //intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,Integer.parseInt(startTime.getText().toString()));
                    //intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,Integer.parseInt(endTime.getText().toString()));
                    intent.putExtra(CalendarContract.Events.DESCRIPTION,title.getText().toString());
                    intent.putExtra(Intent.EXTRA_EMAIL,emails.getText().toString());

                    if(intent.resolveActivity(getPackageManager())!=null){
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this,"No app available to perform this activity", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        capture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent,1);
                }else{
                    Toast.makeText(MainActivity.this,"No app available to perform this activity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTime.setOnClickListener((v) -> {
            calendar=Calendar.getInstance();
            currentHour=calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute=calendar.get(Calendar.MINUTE);

            timePickerDialog=new TimePickerDialog(MainActivity.this,(timePicker,hourOfDay,minutes)->{
                alarmHour.setText(String.format("%02d",hourOfDay));
                alarmMinute.setText(String.format("%02d",minutes));
            },currentHour,currentMinute,false);
            timePickerDialog.show();
        });

        setAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!alarmHour.getText().toString().isEmpty() && !alarmMinute.getText().toString().isEmpty()){
                    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                    intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(alarmHour.getText().toString()));
                    intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(alarmMinute.getText().toString()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "No app available to perform this activity", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Please Choose a time", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null){
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap)bundle.get("data");
            image.setImageBitmap(photo);
        }
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                if(view.getId()==R.id.btnStartTime) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    startTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                }else
                {
                    hour = selectedHour;
                    minute = selectedMinute;
                    endTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                }
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}