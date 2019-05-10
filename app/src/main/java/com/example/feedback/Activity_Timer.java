package com.example.feedback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Activity_Timer extends AppCompatActivity {
    int durationMin, durationSec, warningMin, warningSec;
    int indexOfProject;
    ProjectInfo project;
    EditText editText_durationMin;
    EditText editText_durationSec;
    EditText editText_warningMin;
    EditText editText_warningSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__timer);

        Intent intent =getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("index"));

        project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        getTimeOfProject();
        init();
    }

    private void getTimeOfProject()
    {
        durationMin = project.getDurationMin();
        durationSec = project.getDurationSec();
        warningMin = project.getWarningMin();
        warningSec = project.getWarningSec();
    }

    private void init()
    {
        editText_durationMin = findViewById(R.id.editText_durationMin_Timer);
        editText_durationMin.setText(String.valueOf(durationMin));
        editText_durationSec = findViewById(R.id.editText_durationSec_Timer);
        editText_durationSec.setText(String.valueOf(durationSec));
        editText_warningMin = findViewById(R.id.editText_warningMin_Timer);
        editText_warningMin.setText(String.valueOf(warningMin));
        editText_warningSec = findViewById(R.id.editText_warningSec_Timer);
        editText_warningSec.setText(String.valueOf(warningSec));

    }

    public void addDurationMin(View view)
    {
        durationMin++;
        if(durationMin>59)
            durationMin = durationMin - 60;
        editText_durationMin.setText(String.valueOf(durationMin));
    }

    public void minusDurationMin(View view)
    {
        durationMin--;
        if(durationMin<0)
            durationMin = durationMin + 60;
        editText_durationMin.setText(String.valueOf(durationMin));
    }

    public void addDurationSec(View view)
    {
        durationSec = durationSec + 5;
        if(durationSec>59)
            durationSec = durationSec - 60;
        editText_durationSec.setText(String.valueOf(durationSec));
    }

    public void minusDurationSec(View view)
    {
        durationSec = durationSec - 5;
        if(durationSec<0)
            durationSec = durationSec + 60;
        editText_durationSec.setText(String.valueOf(durationSec));
    }

    public void addWarningMin(View view)
    {
        warningMin++;
        if(warningMin>59)
            warningMin = warningMin - 60;
        editText_warningMin.setText(String.valueOf(warningMin));
    }

    public void minusWarningMin(View view)
    {
        warningMin--;
        if(warningMin<0)
            warningMin = warningMin + 60;
        editText_warningMin.setText(String.valueOf(warningMin));
    }

    public void addWarningSec(View view)
    {
        warningSec = warningSec + 5;
        if(warningSec>59)
            warningSec = warningSec - 60;
        editText_warningSec.setText(String.valueOf(warningSec));
    }

    public void minusWarningSec(View view)
    {
        warningSec = warningSec - 5;
        if(warningSec<0)
            warningSec = warningSec + 60;
        editText_warningSec.setText(String.valueOf(warningSec));
    }

}
