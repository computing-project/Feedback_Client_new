package com.example.feedback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Assessment_Preparation_Activity extends Activity implements AdapterView.OnItemClickListener {

    ListView listView;
    ArrayList<String> alist;
    AllFunctions allFunctions = AllFunctions.getObject();
    ArrayList<ProjectInfo> projectList;
    MyAdapter_for_listView myAdapter;
    MyAdapterForAssessors adapterForAssessors;
    Button button_edit;
    int index_to_send = -999;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assessment__preparation_);

        init();
        System.out.println("Preparation: onCreate has been called!");

    }

    protected void onNewIntent(Intent intent) {
        init();
        System.out.println("Preparation: onNewIntent has been called!");
    }

    private void init() {
        handler = new Handler(){
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 201: //创建新项目成功
                        ;
                        break;
                    case 207: //邀请assessor成功
                        adapterForAssessors.notifyDataSetChanged();
                        break;
                    case 208: //邀请assessor失败
                        Toast.makeText(Assessment_Preparation_Activity.this,
                                "The email has not been registered. Please check and try again.", Toast.LENGTH_SHORT).show();
                        break;
                    case 209: //删除assessor失败
                        Toast.makeText(Assessment_Preparation_Activity.this,
                                "There is anything wrong on server. Please try later.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        AllFunctions.getObject().setHandler(handler);
        button_edit = findViewById(R.id.button_edit_inpreparation);
        resetDetailView();
        alist = new ArrayList<String>();

        projectList = allFunctions.getProjectList();
        for (ProjectInfo p : projectList)
            alist.add(p.getProjectName());

        ArrayAdapter<String> adpter = new ArrayAdapter<String>
                (Assessment_Preparation_Activity.this, R.layout.list_item_projectlist_default, alist);
        listView = (ListView) findViewById(R.id.listView_inpreparation);
        listView.setAdapter(adpter);
        listView.setOnItemClickListener(this);
        TextView textView_helloUser = findViewById(R.id.textView_helloUser_assessmentPreparation);
        textView_helloUser.setText("Hello, "+AllFunctions.getObject().getUsername());
        TextView textView_logout = findViewById(R.id.textView_logout_assessmentPreparation);
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Assessment_Preparation_Activity.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        index_to_send = position;
        for(int i=0; i<parent.getChildCount(); i++)
            parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        view.setBackgroundColor(Color.parseColor("#dbdbdb"));
        showOtherInfo(position);
        Button button_about = findViewById(R.id.button_about_inpreparation);
        button_about.setEnabled(true);
        Button button_timer = findViewById(R.id.button_time_inpreparation);
        button_timer.setEnabled(true);
        Button button_criteria = findViewById(R.id.button_criteria_inpreparation);
        button_criteria.setEnabled(true);
        Button button_student = findViewById(R.id.button_studentmanagement__inpreparation);
        button_student.setEnabled(true);
        Button button_assessor = findViewById(R.id.button_asseccor__inpreparation);
        button_assessor.setEnabled(true);
    }

    public void showOtherInfo(int index) {
        ProjectInfo projectInfo = allFunctions.getProjectList().get(index);
        TextView textView_projectName = findViewById(R.id.project_name_inpreparation);
        textView_projectName.setText(projectInfo.getProjectName());
        TextView textView_aboutDetail = findViewById(R.id.about_detail__inpreparation);
        textView_aboutDetail.setText("Subject Name: " + projectInfo.getSubjectName() + "\n" +
                "Subject Code: " + projectInfo.getSubjectCode() + "\n" +
                "Description: " + projectInfo.getDescription()+"\n");
        TextView textView_timeDetail = findViewById(R.id.time_detail__inpreparation);
        textView_timeDetail.setText("Assessment duration: " + projectInfo.getDurationMin() + ":" + projectInfo.getDurationSec() + "\n" +
                "Warning time: " + projectInfo.getWarningMin() + ":" + projectInfo.getWarningSec()+"\n");
        TextView textView_criteriaDetail = findViewById(R.id.criteria_detail__inpreparation);
        String criteriaDetailString = "Market Section\n";
        for(Criteria c: projectInfo.getCriteria())
        {
            criteriaDetailString = criteriaDetailString + c.getName() + "\n";
            criteriaDetailString = criteriaDetailString + "Maximum mark: " + c.getMaximunMark()+"\n";
            criteriaDetailString = criteriaDetailString + "Mark increments: " + c.getMarkIncrement()+"\n\n";
        }
        criteriaDetailString += "\nComments Only\n";
        for(Criteria c: projectInfo.getCommentList())
        {
            criteriaDetailString = criteriaDetailString + c.getName() + "\n";
        }
        textView_criteriaDetail.setText(criteriaDetailString);
        TextView textView_asseccorDetail = findViewById(R.id.asseccor_detail__inpreparation);
        String assessorDetailString = new String();
        for(int i=0; i<projectInfo.getAssistant().size(); i++)
            assessorDetailString = assessorDetailString + projectInfo.getAssistant().get(i)+"\n";
        textView_asseccorDetail.setText(assessorDetailString);



    }

    private void resetDetailView()
    {
        TextView textView_projectName = findViewById(R.id.project_name_inpreparation);
        textView_projectName.setText("Project Name");
        TextView textView_aboutDetail = findViewById(R.id.about_detail__inpreparation);
        textView_aboutDetail.setText("\n");
        TextView textView_timeDetail = findViewById(R.id.time_detail__inpreparation);
        textView_timeDetail.setText("\n");
        TextView textView_criteriaDetail = findViewById(R.id.criteria_detail__inpreparation);
        textView_criteriaDetail.setText("\n");
        TextView textView_asseccorDetail = findViewById(R.id.asseccor_detail__inpreparation);
        textView_asseccorDetail.setText("\n");
        Button button_about = findViewById(R.id.button_about_inpreparation);
        button_about.setEnabled(false);
        Button button_timer = findViewById(R.id.button_time_inpreparation);
        button_timer.setEnabled(false);
        Button button_criteria = findViewById(R.id.button_criteria_inpreparation);
        button_criteria.setEnabled(false);
        Button button_student = findViewById(R.id.button_studentmanagement__inpreparation);
        button_student.setEnabled(false);
        Button button_assessor = findViewById(R.id.button_asseccor__inpreparation);
        button_assessor.setEnabled(false);
    }

    //plus button click function
    public void plus_AssessmentPreparation(View view) {
        Intent intent = new Intent(this, Activity_About.class);
        intent.putExtra("index", "-999");
        startActivity(intent);
    }

    //edit button click function
    public void edit_AssessmentPreparation(View view) {
        String button_text = button_edit.getText().toString();
        if(button_text.equals("Edit")) {
            button_edit.setText("Done");
            listView = (ListView) findViewById(R.id.listView_inpreparation);
           projectList = allFunctions.getProjectList();
            myAdapter = new MyAdapter_for_listView(projectList, this);

            listView.setAdapter(myAdapter);
            listView.setOnItemClickListener(this);
        }
        if(button_text.equals("Done"))
        {
            button_edit.setText("Edit");
            init();
        }
    }


    public void about_AssessmentPreparation(View view)
    {
        Intent intent = new Intent(this, Activity_About.class);
        intent.putExtra("index", String.valueOf(index_to_send));
        startActivity(intent);
    }

    public void timer_AssessmentPreparation(View view)
    {
        Intent intent = new Intent(this, Activity_Timer.class);
        intent.putExtra("index", String.valueOf(index_to_send));
        startActivity(intent);    }

    public void studentManagement_AssessmentPreparation(View view)
    {
        Intent intent = new Intent(this, Activity_Student_Group.class);
        intent.putExtra("index", String.valueOf(index_to_send));
        startActivity(intent);
    }

    public void criteriaManagement_AssessmentPreparation(View view)
    {
        Intent intent = new Intent(this, Activity_CriteriaList.class);
        intent.putExtra("index", String.valueOf(index_to_send));
        startActivity(intent);
    }

    public void assessors_AssessmentPreparation(View view)
    {
        AllFunctions.getObject().setHandler(handler);
        LayoutInflater layoutInflater = LayoutInflater.from(this);//获得layoutInflater对象
        final View view2 = layoutInflater.from(this).inflate(R.layout.dialog_asseccors, null);//获得view对象

        ListView listView_assessors = view2.findViewById(R.id.listView_assessors_dialogAssessor);
        adapterForAssessors = new MyAdapterForAssessors(projectList.get(index_to_send).getAssistant(),this);
        listView_assessors.setAdapter(adapterForAssessors);
        Dialog dialog = new android.app.AlertDialog.Builder(this).setView(view2).setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showOtherInfo(index_to_send);
            }
        }).create();

        dialog.show();


        EditText editText_assessorName = view2.findViewById(R.id.editText_inviteAssessor_dialogAssessor);
        Button button_invite = view2.findViewById(R.id.button_invite_dialogAssessor);
        button_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText_assessorName.getText().toString().equals(""))
                    ;
                else
                {
                    //projectList.get(index_to_send).getAssistant().add(editText_assessorName.getText().toString());
                    allFunctions.inviteAssessor(projectList.get(index_to_send),editText_assessorName.getText().toString());
                    editText_assessorName.setText("");
                    Toast.makeText(Assessment_Preparation_Activity.this,
                            "The invitation has been sent.", Toast.LENGTH_SHORT).show();

                   // adapterForAssessors.notifyDataSetChanged();
                }
            }
        });


    }


    public class MyAdapter_for_listView extends BaseAdapter {

        private ArrayList<ProjectInfo> mProjectList;
        private Context mContext;

        public MyAdapter_for_listView(ArrayList<ProjectInfo> projectList, Context context) {
            this.mProjectList = projectList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mProjectList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_projectlist_withdelete, parent, false);
            TextView textView_listItem = (TextView) convertView.findViewById(R.id.textView_inlistView);
            textView_listItem.setText(mProjectList.get(position).getProjectName());
            Button button = convertView.findViewById(R.id.Bt_delete_inlist);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapter.notifyDataSetChanged();
                    allFunctions.deleteProject(position);
                }
            });
            return convertView;
        }
    }



    public class MyAdapterForAssessors extends BaseAdapter {

        private ArrayList<String> mAssessorList;
        private Context mContext;

        public MyAdapterForAssessors(ArrayList<String> assessorList, Context context) {
            this.mAssessorList = assessorList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mAssessorList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_assessors, parent, false);
            TextView textView_listItem = (TextView) convertView.findViewById(R.id.textView_assessorName);
            textView_listItem.setText(mAssessorList.get(position));
            Button button = convertView.findViewById(R.id.button_deleteAssessor);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allFunctions.deleteAssessor(projectList.get(index_to_send),mAssessorList.get(position));
                    mAssessorList.remove(position);
                    adapterForAssessors.notifyDataSetChanged();
                }
            });
            if(position == 0)
            {
                button.setVisibility(View.INVISIBLE);
                button.setEnabled(false);
            }
            return convertView;
        }
    }

}
