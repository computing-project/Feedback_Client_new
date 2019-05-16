package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginTest_Activity extends Activity {

    AllFunctions allFunctions;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test_);
        init();
        handler = new Handler(){
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 100:
                        Toast.makeText(LoginTest_Activity.this, "The email and password are not correct. Please check and try again.", Toast.LENGTH_SHORT).show();
                        break;
                    case 101: //means login successfully and go to next page
                        Intent intent = new Intent(LoginTest_Activity.this, Assessment_Preparation_Activity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
        AllFunctions.getObject().setHandler(handler);

    }

    protected void onNewIntent(Intent intent) {
        init();
        AllFunctions.getObject().setHandler(handler);
    }

    private void init()
    {
        allFunctions = AllFunctions.getObject();
        TextView textView_signup = (TextView)findViewById(R.id.textView_signup_inlogin);
        textView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginTest_Activity.this, SignupTest_Activity.class);
                startActivity(intent);
            }
        });
    }

    public void login(View view)
    {
        EditText editText_email = findViewById(R.id.editText_email_inlogin);
        EditText editText_password = findViewById(R.id.editText_password_inlogin);
        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();

        allFunctions.login(email, password);

    }
}
