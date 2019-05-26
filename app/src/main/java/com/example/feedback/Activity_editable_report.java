package com.example.feedback;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Activity_editable_report extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        init();
    }

    private void init()
    {
        String htmlString =
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>PDF Report</title>" +
                "</head>" +
                "<body>" +
                "<h1 style=\"font-weight: normal\">Project Name Report</h1>" +
                "<hr>" +
                "<p> Feedback for Jaya Kummar — 985789</p >" +
                "<h2 style=\"font-weight: normal\">Subject</h2>" +
                "<p>CHEN2001 - Chemical Process Analysis</p >" +
                "<h2 style=\"font-weight: normal\">Project</h2>" +
                "<p>Project</p >" +
                "<h2 style=\"font-weight: normal\">Mark Attained</h2>" +
                "<p>72%</p >" +
                "<h2 style=\"font-weight: normal\">Assessor</h2>" +
                "<p>David Shallcross, Xxxxxxx Xxxx</p >" +
                "<h2 style=\"font-weight: normal\">Assessment Date</h2>" +
                "<p>Friday, 20 Novermber, 2018</p ><br><br><br><hr>" +
                "<br><br>" +
                "<div>" +
                "<h3 style=\"font-weight: normal\"><span style=\"float:left\">Presentation Structure&nbsp;(20%)</span><span style=\"float:right\">7/10</span></h3><br><br>" +
                "<h4 style=\"font-weight: normal;color: #014085\">David Shallcross:</h4>" +
                "<p>&lt;Introduction general:&gt;xxxxxxxxxxxxxx</p >" +
                "<p>&lt;Presentation length:&gt;xxxxxxxxxxxxxx</p >" +
                "<h4 style=\"font-weight: normal;color: #014085\">Leaon Sterling:</h4>" +
                "<p>&lt;Introduction general:&gt;xxxxxxxxxxxxxx</p >" +
                "<p>&lt;Presentation length:&gt;xxxxxxxxxxxxxx</p ><br><br>" +
                "</div>" +
                "</body>" +
                "</html>";
        TextView textView_pdfContent = findViewById(R.id.textView_pdfContent_report);
        textView_pdfContent.setText(Html.fromHtml(htmlString));
    }
}
