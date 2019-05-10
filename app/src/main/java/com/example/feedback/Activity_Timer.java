package com.example.feedback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Activity_Timer extends AppCompatActivity {
    int durationMin, durationSec, warningMin, warningSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__timer);
        init();
    }

    private void init()
    {
        EditText editText_durationMin = findViewById(R.id.editText_durationMin_Timer);
        editText_durationMin.setText(String.valueOf(durationMin));
        EditText editText_durationSec = findViewById(R.id.editText_durationSec_Timer);
        editText_durationSec.setText(String.valueOf(durationSec));
        EditText editText_warningMin = findViewById(R.id.editText_warningMin_Timer);
        editText_warningMin.setText(String.valueOf(warningMin));
        EditText editText_warningSec = findViewById(R.id.editText_warningSec_Timer);
        editText_warningSec.setText(String.valueOf(warningSec));

    }

    public void addDurationMin(View view)
    {

    }
}
