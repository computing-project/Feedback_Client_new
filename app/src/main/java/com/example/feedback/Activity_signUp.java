package com.example.feedback;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_signUp extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        handler = new Handler(){
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 111: //means Sign Up successfully and go to login page
                        Toast.makeText(getApplicationContext(), "Sign Up successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_signUp.this, Activity_Login.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                    case 110: //Sign Up failed.
                        Toast.makeText(Activity_signUp.this,
                                "The email address is already occupied. Please try another one.", Toast.LENGTH_SHORT).show();
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
        TextView textView_Login = findViewById(R.id.textView_login_insignup);
        textView_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_signUp.this, Activity_Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void signup(View view)
    {
        EditText editText_FirstName = findViewById(R.id.editText_firstName_insignup);
        EditText editText_MiddleName = findViewById(R.id.editText_middleName_insignup);
        EditText editText_LastName = findViewById(R.id.editText_lastname_insignup);
        EditText editText_email = findViewById(R.id.editText_email_insignup);
        EditText editText_password = findViewById(R.id.editText_password_insignup);
        EditText editText_passwordConfirm = findViewById(R.id.editText_confirmPassword_insignup);
        String firstName = editText_FirstName.getText().toString();
        String middleName = editText_MiddleName.getText().toString();
        String lastName = editText_LastName.getText().toString();
        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();
        String passwordConfirm = editText_passwordConfirm.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";
        if(firstName.equals(""))
        {
            Toast.makeText(getApplicationContext(),"First name cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if(lastName.equals("")){
            Toast.makeText(getApplicationContext(),"Last name cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if(password.equals("")){
            Toast.makeText(getApplicationContext(),"Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if(!(password.equals(passwordConfirm)))
        {
            Toast.makeText(getApplicationContext(),"Two passwords don't match", Toast.LENGTH_SHORT).show();
        }
        else if(email.matches(emailPattern)) {
            AllFunctions.getObject().register(firstName, middleName, lastName, email, password);
        }
        else{
            Toast.makeText(getApplicationContext(),"Invalid Email format", Toast.LENGTH_SHORT).show();
        }

    }
}
