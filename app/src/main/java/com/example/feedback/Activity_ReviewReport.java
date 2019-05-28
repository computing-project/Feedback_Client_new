package com.example.feedback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_ReviewReport extends Activity {
    private ListView listView_projects;
    private ListView listView_students;
    private ArrayList<ProjectInfo> projectList;
    private int indexOfProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__review_report);

        Button button_back_title = findViewById(R.id.button_back_title);
        button_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        init();
    }

    public void init()
    {
        projectList = AllFunctions.getObject().getProjectList();
        ArrayList<String> projectNameList = new ArrayList<String>();
        for(ProjectInfo p: projectList)
            projectNameList.add(p.getProjectName());
        ArrayAdapter<String> adpter = new ArrayAdapter<String>
                (this, R.layout.list_item_projectlist_default, projectNameList);
        listView_projects = findViewById(R.id.listView_projects_reviewReport);
        listView_students = findViewById(R.id.listView_students_reviewReport);
        listView_projects.setAdapter(adpter);
        listView_projects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                indexOfProject = position;
                ProjectInfo project = projectList.get(position);
                MyAdapter myAdapter = new MyAdapter(project.getStudentInfo(),Activity_ReviewReport.this);
                listView_students.setAdapter(myAdapter);
                listView_students.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        myAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }



    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<StudentInfo> studentList;

        public MyAdapter(ArrayList<StudentInfo> studentList, Context context) {
            this.studentList = studentList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return studentList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_student_withbutton, parent, false);

            TextView textView_studentID = convertView.findViewById(R.id.textView_studentID_studentsWithButton);
            textView_studentID.setText(studentList.get(position).getNumber());
            //test mark Num
            textView_studentID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Total Mark of "+studentList.get(position).getFirstName()+" is "+studentList.get(position).getTotalMark(), Toast.LENGTH_SHORT).show();
                }
            });
            TextView textView_studentName = convertView.findViewById(R.id.textView_fullname_studentsWithButton);
            textView_studentName.setText(studentList.get(position).getFirstName()+" "+studentList.get(position).getMiddleName()+" "+studentList.get(position).getSurname());
            TextView textView_studentEmail = convertView.findViewById(R.id.textView_email_studentsWithButton);
            textView_studentEmail.setText(studentList.get(position).getEmail());
            Button button_viewReport = convertView.findViewById(R.id.button_start_studentsWithButton);
            button_viewReport.setText("report");
            if(studentList.get(position).getTotalMark() < 0.0) {
                button_viewReport.setEnabled(false);
                button_viewReport.setVisibility(View.INVISIBLE);
            }
            button_viewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Activity_ReviewReport.this, Activity_Reaper_Mark.class);
                    intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
                    intent.putExtra("indexOfStudent",String.valueOf(position));
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
