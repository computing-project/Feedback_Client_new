package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_About extends Activity {

    String index;
    ProjectInfo project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__about);
        Intent intent =getIntent();
        index = intent.getStringExtra("index");
        if(index.equals("-999"))
            ;
        else
            init(Integer.parseInt(index));

    }

    private void init(int i)
    {
        project = AllFunctions.getObject().getProjectList().get(i);
        EditText editText_projectName = findViewById(R.id.editText_projectname_inabout);
        editText_projectName.setText(project.getProjectName());
        editText_projectName.setEnabled(false);
        EditText editText_subjectName = findViewById(R.id.editText_subjectname_inabout);
        editText_subjectName.setText(project.getSubjectName());
        EditText editText_subjectCode = findViewById(R.id.editText_subjectcode_inabout);
        editText_subjectCode.setText(project.getSubjectCode());
        EditText editText_projectDes = findViewById(R.id.editText_projectdescription_inabout);
        editText_projectDes.setText(project.getDescription());
        TextView textView_projectName = findViewById(R.id.textView_projectName_about);
        textView_projectName.setText(project.getProjectName());
        TextView textView_helloUser = findViewById(R.id.textView_helloUser_about);
        textView_helloUser.setText("Hello, "+AllFunctions.getObject().getUsername());
    }

    //save button click
    public void save_About(View view)
    {
        EditText editText_projectName = findViewById(R.id.editText_projectname_inabout);
        EditText editText_subjectName = findViewById(R.id.editText_subjectname_inabout);
        EditText editText_subjectCode = findViewById(R.id.editText_subjectcode_inabout);
        EditText editText_projectDes = findViewById(R.id.editText_projectdescription_inabout);
        String projectName = editText_projectName.getText().toString();
        String subjectName = editText_subjectName.getText().toString();
        String subjectCode = editText_subjectCode.getText().toString();
        String projectDes = editText_projectDes.getText().toString();
        if(index.equals("-999"))
            AllFunctions.getObject().createProject(projectName,subjectName,subjectCode,projectDes);
        else
            AllFunctions.getObject().updateProject(project,projectName,subjectName,subjectCode,projectDes);
        Intent intent = new Intent(this, Assessment_Preparation_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void back_About(View view)
    {
        finish();
    }

    public void next_About(View view)
    {
        EditText editText_projectName = findViewById(R.id.editText_projectname_inabout);
        EditText editText_subjectName = findViewById(R.id.editText_subjectname_inabout);
        EditText editText_subjectCode = findViewById(R.id.editText_subjectcode_inabout);
        EditText editText_projectDes = findViewById(R.id.editText_projectdescription_inabout);
        String projectName = editText_projectName.getText().toString();
        String subjectName = editText_subjectName.getText().toString();
        String subjectCode = editText_subjectCode.getText().toString();
        String projectDes = editText_projectDes.getText().toString();
        if(index.equals("-999")) {
            AllFunctions.getObject().createProject(projectName, subjectName, subjectCode, projectDes);
            int indextToSend = AllFunctions.getObject().getProjectList().size() -1;
            Intent intent = new Intent(this, Activity_Timer.class);
            intent.putExtra("index", String.valueOf(indextToSend));
            startActivity(intent);
        }
        else {
            AllFunctions.getObject().updateProject(project, projectName, subjectName, subjectCode, projectDes);
            Intent intent = new Intent(this, Activity_Timer.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }
}
