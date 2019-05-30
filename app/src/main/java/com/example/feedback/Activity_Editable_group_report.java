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

public class Activity_Editable_group_report extends Activity {
    private int indexOfProject;
    private int indexOfGroup;
    private int indexOfMark;
    private ArrayList<StudentInfo> studentInfoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__editable_group_report);

        Intent intent = getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("indexOfProject"));
        ProjectInfo project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        indexOfGroup = Integer.parseInt(intent.getStringExtra("indexOfGroup"));
        indexOfMark = Integer.parseInt(intent.getStringExtra("indexOfMark"));
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
        TextView textView_helloUser = findViewById(R.id.textView_helloUser_report);
        textView_helloUser.setText("Hello, "+AllFunctions.getObject().getUsername());
        TextView textView_logout = findViewById(R.id.textView_logout);
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Editable_group_report.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        Button button_finalReport = findViewById(R.id.button_finalReport_groupReport);
        if(!AllFunctions.getObject().getProjectList().get(indexOfProject).getAssistant().get(0).equals
                (AllFunctions.getObject().getMyEmail()))
        {
            button_finalReport.setVisibility(View.INVISIBLE);
        }

        init();


    }


    private void init()
    {
        ProjectInfo project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        Mark mark = AllFunctions.getObject().getMarkListForMarkPage().get(indexOfMark);
        Button button_edit = findViewById(R.id.button_edit_groupReport);
        if(!mark.getLecturerEmail().equals(AllFunctions.getObject().getMyEmail()))
        {
            button_edit.setVisibility(View.INVISIBLE);
        }
        TextView textView_totalMark = findViewById(R.id.textView_totalMark_report);
        textView_totalMark.setText("Mark:"+(int)mark.getTotalMark()+"%");
        TextView textView_assessorName = findViewById(R.id.textView_assessorName_report);
        textView_assessorName.setText("Assessor: "+ mark.getLecturerName());
        String htmlString =
                "<html>" +
                        "<body>" +
                        "<h1 style=\"font-weight: normal\">"+project.getProjectName()+"</h1>" +
                        "<hr>" +
                        "<p>"+ "Group: "+indexOfGroup +"</p >" +
                        "<h2 style=\"font-weight: normal\">Subject</h2>" +
                        "<p>"+project.getSubjectCode()+" --- "+project.getSubjectName()+"</p >" +
                        "<h2 style=\"font-weight: normal\">Project</h2>" +
                        "<p>"+project.getProjectName()+"</p >" +
                        "<h2 style=\"font-weight: normal\">Mark Attained</h2>" +
                        "<p>"+mark.getTotalMark()+"%</p >" +
                        "<h2 style=\"font-weight: normal\">Assessor</h2>" + "<p>";
        for(int i=0; i<project.getAssistant().size(); i++)
            htmlString = htmlString + project.getAssistant().get(i)+"<br>";

        htmlString += "<h2 style=\"font-weight: normal\">Students</h2>" + "<p>";
        for(int i=0; i<studentInfoArrayList.size(); i++)
            htmlString = htmlString + studentInfoArrayList.get(i).getNumber()+studentInfoArrayList.get(i).getFirstName()+" "+studentInfoArrayList.get(i).getMiddleName()+" "+studentInfoArrayList.get(i).getSurname()+"<br>";

        htmlString = htmlString +
                "</p >" +
                "<h2 style=\"font-weight: normal\">Assessment Date</h2>" +
                "<p>"+"test date"+"</p ><br><br><br><hr>" +
                "<div>";
        for(int i=0; i<mark.getCriteriaList().size(); i++)
        {
            htmlString += "<h3 style=\"font-weight: normal\"><span style=\"float:left\">" + mark.getCriteriaList().get(i).getName() + "</span>" +
                    "<span style=\"float:right\">"+ mark.getMarkList().get(i) +"/"+ mark.getCriteriaList().get(i).getMaximunMark() + "</span></h3>";
            for(int j=0; j<mark.getCriteriaList().get(i).getSubsectionList().size(); j++)
            {
                htmlString+= "<p>&lt;"+mark.getCriteriaList().get(i).getSubsectionList().get(j).getName()+
                        ":&gt;"+mark.getCriteriaList().get(i).getSubsectionList().get(j).getShortTextList().get(0).getLongtext()+"</p >";
            }
            htmlString += "<br>";
        }
        htmlString +=
                "</div>" +
                        "</body>" +
                        "</html>";
        TextView textView_pdfContent = findViewById(R.id.textView_pdfContent_GroupReport);
        textView_pdfContent.setText(Html.fromHtml(htmlString));
    }

    public void finalReport(View view)
    {
        Intent intent = new Intent(Activity_Editable_group_report.this, Activity_SendReport_Group.class);
        intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
        intent.putExtra("indexOfGroup",String.valueOf(indexOfGroup));
        startActivity(intent);
    }
}

