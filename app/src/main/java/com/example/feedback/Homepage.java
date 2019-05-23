package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Homepage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        TextView textView_helloUser = findViewById(R.id.textView_helloUser_homepage);
        textView_helloUser.setText("Hello, "+AllFunctions.getObject().getUsername());
        TextView textView_logout = findViewById(R.id.textView8);
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, LoginTest_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    public void toPart1(View view)
    {
        Intent intent = new Intent(this, Assessment_Preparation_Activity.class);
        startActivity(intent);
    }

    public void toPart2(View view)
    {
        Intent intent = new Intent(this,Activity_Realtime_Assessment.class);
        startActivity(intent);
    }
}
