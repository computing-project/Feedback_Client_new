package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_SendReport_Group extends AppCompatActivity {
    private int indexOfProject;
    private int indexOfGroup;
//    private int indexOfMark;
    private ArrayList<StudentInfo> studentInfoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__send_report__group);

        Intent intent = getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("indexOfProject"));
        ProjectInfo project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        indexOfGroup = Integer.parseInt(intent.getStringExtra("indexOfGroup"));
//        indexOfMark = Integer.parseInt(intent.getStringExtra("indexOfMark"));
        studentInfoArrayList = new ArrayList<StudentInfo>();
        for(int i=0; i<project.getStudentInfo().size(); i++)
        {
            if(project.getStudentInfo().get(i).getGroup() == indexOfGroup)
                studentInfoArrayList.add(project.getStudentInfo().get(i));
        }

        Button button_back_title = findViewById(R.id.button_back_title);
        button_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView textView_helloUser = findViewById(R.id.textView_helloUser);
        textView_helloUser.setText("Hello, "+AllFunctions.getObject().getUsername());
        TextView textView_logout = findViewById(R.id.textView_logout);
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_SendReport_Group.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        init();

    }

    private void init()
    {
        ProjectInfo project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        ArrayList<Mark> markList = AllFunctions.getObject().getMarkListForMarkPage();
        Button button_sendSingle = findViewById(R.id.button_sendStudent_sendReportGroup);
        button_sendSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<studentInfoArrayList.size(); i++)
                    AllFunctions.getObject().sendPDF(project,studentInfoArrayList.get(i).getNumber(),1);
                Intent intent = new Intent(Activity_SendReport_Group.this, Activity_Reaper_Mark.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        Button button_sendBoth = findViewById(R.id.button_sendBoth_sendReportGroup);
        button_sendBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<studentInfoArrayList.size(); i++)
                    AllFunctions.getObject().sendPDF(project,studentInfoArrayList.get(i).getNumber(),2);
                Intent intent = new Intent(Activity_SendReport_Group.this, Activity_Reaper_Mark.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        Button button_finish = findViewById(R.id.btn_finish_sendReportGroup);
        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_SendReport_Group.this, Activity_Reaper_Mark.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        TextView textView_totalMark = findViewById(R.id.textView_totalMark_sendReportGroup);
        textView_totalMark.setText("Mark:"+(int)markList.get(0).getTotalMark()+"%");

        String htmlString =
                "<html>" +
                        "<body>" +
                        "<h1 style=\"font-weight: normal\">"+project.getProjectName()+"</h1>" +
                        "<hr>" +
                        "<p>"+ "Group: "+indexOfGroup +"</p >" +                        "<h2 style=\"font-weight: normal\">Subject</h2>" +
                        "<p>"+project.getSubjectCode()+" --- "+project.getSubjectName()+"</p >" +
                        "<h2 style=\"font-weight: normal\">Project</h2>" +
                        "<p>"+project.getProjectName()+"</p >" +
                        "<h2 style=\"font-weight: normal\">Mark Attained</h2>" +
                        "<p>"+markList.get(0).getTotalMark()+"%</p >" +
                        "<h2 style=\"font-weight: normal\">Assessor</h2>" + "<p>";
        for(int i=0; i<project.getAssistant().size(); i++) {
            htmlString = htmlString + project.getAssistant().get(i)+"<br>";
        }
        htmlString += "<h2 style=\"font-weight: normal\">Students</h2>" + "<p>";
        for(int i=0; i<studentInfoArrayList.size(); i++)
            htmlString = htmlString + studentInfoArrayList.get(i).getNumber()+"---"+studentInfoArrayList.get(i).getFirstName()+" "+studentInfoArrayList.get(i).getMiddleName()+" "+studentInfoArrayList.get(i).getSurname()+"<br>";


        htmlString = htmlString +
                "</p >" +
                "<h2 style=\"font-weight: normal\">Assessment Date</h2>" +
                "<p>"+"test date"+"</p ><br><br><br><hr>" +
                "<div>";

        htmlString += "<h2 style=\"font-weight: normal\">MarkedCriteria</h2>" + "<p>";
        for(int i=0; i<markList.get(0).getCriteriaList().size(); i++)
        {
            htmlString += "<h3 style=\"font-weight: normal\"><span style=\"float:left\">" + markList.get(0).getCriteriaList().get(i).getName() + "</span>" +
                    "<span style=\"float:right\">   "+ markList.get(0).getMarkList().get(i) +"/"+ markList.get(0).getCriteriaList().get(i).getMaximunMark() + "</span></h3>";
            for(int j=0; j<markList.size(); j++)
            {
                htmlString += "<h4 style=\"font-weight: normal;color: #014085\">"+markList.get(j).getLecturerName()+":</h4>";
                if(markList.get(j).getCriteriaList().size() > 0)
                    for(int k=0; k<markList.get(j).getCriteriaList().get(i).getSubsectionList().size(); k++)
                    {
                        htmlString += "<p>&lt;" + markList.get(j).getCriteriaList().get(i).getSubsectionList().get(k).getName() + ":&gt;"
                                + markList.get(j).getCriteriaList().get(i).getSubsectionList().get(k).getShortTextList().get(0).getLongtext() + "</p >";
                    }
            }
            htmlString += "<br>";
        }

        htmlString +=  "<h2 style=\"font-weight: normal\">CommentOnlyCriteria</h2>" + "<p>";
        for(int i=0; i<markList.get(0).getCommentList().size(); i++)
        {
            htmlString += "<h3 style=\"font-weight: normal\"><span style=\"float:left\">" + markList.get(0).getCommentList().get(i).getName() + "</span></h3>";
            for(int j=0; j<markList.size(); j++)
            {
                htmlString += "<h4 style=\"font-weight: normal;color: #014085\">"+markList.get(j).getLecturerName()+":</h4>";
                if(markList.get(j).getCommentList().size() > 0)
                    for(int k=0; k<markList.get(j).getCommentList().get(i).getSubsectionList().size(); k++)
                    {
                        htmlString += "<p>&lt;" + markList.get(j).getCommentList().get(i).getSubsectionList().get(k).getName() + ":&gt;"
                                + markList.get(j).getCommentList().get(i).getSubsectionList().get(k).getShortTextList().get(0).getLongtext() + "</p >";
                    }
            }
            htmlString += "<br>";
        }


        htmlString +=
                "</div>" +
                        "</body>" +
                        "</html>";
        TextView textView_pdfContent = findViewById(R.id.textView_pdfContent_sendReportGroup);
        textView_pdfContent.setText(Html.fromHtml(htmlString));
    }
}

