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

//        Intent intent = new Intent(this, Assessment_Preparation_Activity.class);
//        startActivity(intent);
//        finish();

        test = AllFunctions.getObject();
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

    public void updateProjectTimerTest(View view)
    {
        ProjectInfo projectTest = test.getProjectList().get(0);
        test.projectTimer(projectTest,5,0,
                1,30);
    }

    public void importStudentTest(View view)
    {
        ProjectInfo project = test.getProjectList().get(0);
        test.readExcel(project, "/mnt/sdcard/StudentInformation.csv");
    }

    public void addStudent_test(View view)
    {
        ProjectInfo project = test.getProjectList().get(0);
        test.addStudent(project, "484567","Ripper",
                "M","Ding","Dding@gmail.com");
    }

    public void deleteStudentTest(View view)
    {
        ProjectInfo project = test.getProjectList().get(0);
        test.deleteStudent (project, "484567");
    }

    public void editStudentTest(View view)
    {
        ProjectInfo project = test.getProjectList().get(0);
        test.editStudent(project, "484567","Ripper",
                "after","Ding","Dding@gmail.com");
    }


    public void importCriteriaListTest(View view)
    {
        DefaultCriteriaList defaultL = new DefaultCriteriaList();
        ArrayList<Criteria> criteriaList =  defaultL.getDefaultCriteriaList();
        ProjectInfo projectTest = test.getProjectList().get(0);
        test.projectCriteria(projectTest,criteriaList);
    }

    public void sendMarkTest(View view){

        Criteria c = new Criteria();
        SubSection ss = new SubSection();
        SubSection ss2 = new SubSection();
        ShortText st = new ShortText();
        ShortText st2 = new ShortText();
        ArrayList<String> longtext1 = new ArrayList<String>();
        longtext1.add("abcdefg");
        st.setLongtext(longtext1);
        ArrayList<String> longtext2 = new ArrayList<String>();
        longtext2.add("abc22222");
        st2.setLongtext(longtext2);
        ArrayList<ShortText> shortTextList = new ArrayList<ShortText>();
        shortTextList.add(st);
        ss.setShortTextList(shortTextList);
        ss.setName("name");
        ArrayList<ShortText> shortTextList2 = new ArrayList<ShortText>();
        shortTextList2.add(st2);
        ss2.setShortTextList(shortTextList2);
        ss2.setName("name222222");
        ArrayList<SubSection> subsectionList = new ArrayList<SubSection>();
        subsectionList.add(ss);
        subsectionList.add(ss2);
        c.setSubsectionList(subsectionList);
        c.setMarkIncrement("fkfkfkfk");
        c.setMaximunMark(10);
        c.setName("lastkkkttt");
        c.setWeighting(20);

        ProjectInfo project = test.getProjectList().get(0);
        Mark mark = new Mark();
        ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();
        criteriaList.add(c);
        ArrayList<Double> markList = new ArrayList<Double>();
        markList.add(1.0);
        mark.setCriteriaList(criteriaList);
        mark.setMarkList(markList);

        test.sendMark(project, "484567", mark);

    }


}
