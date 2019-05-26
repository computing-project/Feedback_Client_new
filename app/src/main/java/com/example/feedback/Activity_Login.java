package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Login extends Activity {

    AllFunctions allFunctions;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test_);
        init();
        resetHandler();
    }

    protected void onNewIntent(Intent intent) {
        init();
      resetHandler();
    }

    protected  void resetHandler()
    {
        handler = new Handler(){
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 100:
                        Toast.makeText(Activity_Login.this, "The email and password are not correct. Please check and try again.", Toast.LENGTH_SHORT).show();
                        break;
                    case 101: //means login successfully and go to next page
                        Intent intent = new Intent(Activity_Login.this, Homepage.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };
        AllFunctions.getObject().setHandler(handler);
    }

    private void init()
    {
        EditText editView_email = findViewById(R.id.editText_email_inlogin);
        editView_email.setText("");
        EditText editText_password = findViewById(R.id.editText_password_inlogin);
        editText_password.setText("");
        allFunctions = AllFunctions.getObject();
        TextView textView_signup = (TextView)findViewById(R.id.textView_signup_inlogin);
        textView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Login.this, Activity_signUp.class);
                startActivity(intent);
            }
        });
    }

    public void login(View view)
    {
        AllFunctions.getObject().setHandler(handler);
        EditText editText_email = findViewById(R.id.editText_email_inlogin);
        EditText editText_password = findViewById(R.id.editText_password_inlogin);
        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();

        allFunctions.login(email, password);

    }
}
