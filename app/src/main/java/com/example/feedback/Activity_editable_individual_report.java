package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity_editable_individual_report extends Activity {
    private int indexOfProject;
    private int indexOfStudent;
    private int indexOfMark;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable_individual_report);
        Intent intent = getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("indexOfProject"));
        indexOfStudent = Integer.parseInt(intent.getStringExtra("indexOfStudent"));
        indexOfMark = Integer.parseInt(intent.getStringExtra("indexOfMark"));

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
                Intent intent = new Intent(Activity_editable_individual_report.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        Button button_finalReport = findViewById(R.id.button_finalReport_report);
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
        StudentInfo student = AllFunctions.getObject().getProjectList().get(indexOfProject).getStudentInfo().get(indexOfStudent);
        Mark mark = AllFunctions.getObject().getMarkListForMarkPage().get(indexOfMark);
        Button button_editReport_individual = findViewById(R.id.button_edit_report);
        button_editReport_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_editable_individual_report.this, Activity_Assessment.class);
                intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
                intent.putExtra("indexOfGroup", "-999");
                intent.putExtra("indexOfStudent",String.valueOf(indexOfStudent));
                AllFunctions.getObject().getProjectList().get(indexOfProject).getStudentInfo().get(indexOfStudent).setMark(mark);
                startActivity(intent);
            }
        });
        if(!mark.getLecturerEmail().equals(AllFunctions.getObject().getMyEmail()))
        {
            button_editReport_individual.setVisibility(View.INVISIBLE);
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
                "<p>"+ student.getFirstName()+" "+student.getMiddleName()+" "+student.getSurname()+" --- "+student.getNumber() +"</p >" +
                "<h2 style=\"font-weight: normal\">Subject</h2>" +
                "<p>"+project.getSubjectCode()+" --- "+project.getSubjectName()+"</p >" +
                "<h2 style=\"font-weight: normal\">Project</h2>" +
                "<p>"+project.getProjectName()+"</p >" +
                "<h2 style=\"font-weight: normal\">Mark Attained</h2>" +
                "<p>"+mark.getTotalMark()+"%</p >" +
                "<h2 style=\"font-weight: normal\">Assessor</h2>" + "<p>";
        for(int i=0; i<project.getAssistant().size(); i++)
            htmlString = htmlString + project.getAssistant().get(i)+"<br>";
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
        TextView textView_pdfContent = findViewById(R.id.textView_pdfContent_report);
        textView_pdfContent.setText(Html.fromHtml(htmlString));
    }

    public void finalReport(View view)
    {
        Intent intent = new Intent(Activity_editable_individual_report.this, Activity_SendReport_Individual.class);
        intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
        intent.putExtra("indexOfStudent",String.valueOf(indexOfStudent));
        intent.putExtra("indexOfMark",String.valueOf(indexOfMark));
        startActivity(intent);
    }

}
