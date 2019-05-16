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
    }

    public void toPart1(View view)
    {
        Intent intent = new Intent(this, Assessment_Preparation_Activity.class);
        startActivity(intent);
    }
}
