package com.example.feedback;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Activity_Realtime_Assessment extends Activity {
    private ListView listView_projects;
    private ListView listView_students;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_assessment_page);
    }

    public void init()
    {

    }
}
