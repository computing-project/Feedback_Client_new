package com.example.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    AllFunctions test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, Assessment_Preparation_Activity.class);
        startActivity(intent);
        finish();

       // test = AllFunctions.getObject();
      //  Intent intent = new Intent(this, TestLoginActivity.class);
      //  startActivity(intent);

    }

    public void registerTest(View view)
    {
        test.register("Yu", "", "Chao",
                "123@gmail.com", "123asd");
    }

    public void loginTest(View view)
    {
        test.login("123@gmail.com", "123asd");
    }

    public void createProjectTest(View view)
    {
        test.createProject("Assignment1_20190427","Computing Project",
                "COMP90000","This is a test project");
    }

    public void updateProjectAboutTest(View view)
    {
        test.createProject("Assignment1_20190427","Computing Project",
                "COMP90000","This is a test project After updating!!");
    }

    public void updateProjectTimer(View view)
    {
        ProjectInfo projectTest = test.getProjectList().get(0);
        test.projectTimer(projectTest,5,0,
                1,30);
    }

    public void importStudent(View view)
    {
        ProjectInfo project = test.getProjectList().get(0);
        test.readExcel(project, "/mnt/sdcard/StudentInformation.csv");
    }

    public void addStudent(View view)
    {
        ProjectInfo project = test.getProjectList().get(0);
        test.addStudent(project, "484567","Ripper",
                "M","Ding","Dding@gmail.com");
    }

    public void deleteStudent(View view)
    {
        ProjectInfo project = test.getProjectList().get(0);
        test.deleteStudent (project, "484567");
    }

    public void editStudent(View view)
    {
        ProjectInfo project = test.getProjectList().get(0);
        test.editStudent(project, "484567","Ripper",
                "after","Ding","Dding@gmail.com");
    }


    public void importCriteriaList(View view)
    {
        DefaultCriteriaList defaultL = new DefaultCriteriaList();
        ArrayList<Criteria> criteriaList =  defaultL.getDefaultCriteriaList();
        ProjectInfo projectTest = test.getProjectList().get(0);
        test.projectCriteria(projectTest,criteriaList);
    }

    public void sendMark(View view){


    }


}
