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

public class Activity_Realtime_Assessment extends Activity {
    private ListView listView_projects;
    private ListView listView_students;
    private ArrayList<ProjectInfo> projectList;
    private int indexOfProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_assessment_page);

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
        listView_projects = findViewById(R.id.listView_projects_realtimeAssessment);
        listView_students = findViewById(R.id.listView_students_realtimeAssessment);
        listView_projects.setAdapter(adpter);
        listView_projects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                indexOfProject = position;
                ProjectInfo project = projectList.get(position);
                MyAdapter myAdapter = new MyAdapter(project.getStudentInfo(),Activity_Realtime_Assessment.this);
                listView_students.setAdapter(myAdapter);
                listView_students.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        myAdapter.notifyDataSetChanged();
                    }
                });
                TextView textView_duration_title = findViewById(R.id.textView_duration_realtimeAssessment);
                textView_duration_title.setText(""+projectList.get(indexOfProject).getDurationMin()+":"+
                        +projectList.get(indexOfProject).getDurationSec()+" Presentation");
                TextView textView_numCandicate = findViewById(R.id.textView_numCandidates_realtimeAssessment);
                int numStudentHasMarked = 0;
                for(int i=0; i<projectList.get(indexOfProject).getStudentInfo().size(); i++)
                {
                    if(projectList.get(indexOfProject).getStudentInfo().get(i).getTotalMark()>0.0)
                        numStudentHasMarked++;
                }
                textView_numCandicate.setText(numStudentHasMarked+" of "+
                        projectList.get(indexOfProject).getStudentInfo().size()+" candidates marked");
                TextView textView_numAssessor = findViewById(R.id.textView_numAssessors_realtimeAssessment);
                textView_numAssessor.setText(projectList.get(indexOfProject).getAssistant().size()+" elevators");


                //  listView_students.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
        });

        Button button_groupAssessment = findViewById(R.id.button_assess_AsGroup_realtimeAssessment);
        button_groupAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listView_students.getCheckedItemCount() > 1) {
                    SparseBooleanArray checkedItemsStudents = listView_students.getCheckedItemPositions();
                    if (checkedItemsStudents != null) {
                        int maxGroupNum = AllFunctions.getObject().getMaxGroupNumber(indexOfProject);
                        for (int i = 0; i < checkedItemsStudents.size(); i++) {
                            if (checkedItemsStudents.valueAt(i)) {
                                projectList.get(indexOfProject).getStudentInfo().get(i).setGroup(maxGroupNum + 1);
                                AllFunctions.getObject().groupStudent(projectList.get(indexOfProject),
                                        projectList.get(indexOfProject).getStudentInfo().get(i).getNumber(),
                                        maxGroupNum + 1);
                            }
                        }

                        Intent intent = new Intent(Activity_Realtime_Assessment.this, Activity_Assessment.class);
                        intent.putExtra("indexOfProject", String.valueOf(indexOfProject));
                        intent.putExtra("indexOfStudent", String.valueOf(-999));
                        intent.putExtra("indexOfGroup", String.valueOf(maxGroupNum + 1));
                        startActivity(intent);
                    }

                }
                else
                {
                    Toast.makeText(Activity_Realtime_Assessment.this,
                            "Please choose more than 1 students to start the group assessment.",
                            Toast.LENGTH_SHORT).show();

                }
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
            TextView textView_studentName = convertView.findViewById(R.id.textView_fullname_studentsWithButton);
            textView_studentName.setText(studentList.get(position).getFirstName()+" "+studentList.get(position).getMiddleName()+" "+studentList.get(position).getSurname());
            TextView textView_studentEmail = convertView.findViewById(R.id.textView_email_studentsWithButton);
            textView_studentEmail.setText(studentList.get(position).getEmail());
            Button button_start = convertView.findViewById(R.id.button_start_studentsWithButton);
            button_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Activity_Realtime_Assessment.this,Activity_Assessment.class);
                    intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
                    intent.putExtra("indexOfStudent",String.valueOf(position));
                    intent.putExtra("indexOfGroup", String.valueOf(-999));
                    System.out.println("project: "+indexOfProject);
                    System.out.println("student: "+position);
                    startActivity(intent);
                }
            });
            if(studentList.get(position).getTotalMark() > 0.0)
            {
                button_start.setVisibility(View.INVISIBLE);
                button_start.setEnabled(false);
                convertView.setEnabled(false);
                listView_students.setItemChecked(position,false);
            }
            if(listView_students.isItemChecked(position))
                convertView.setBackgroundColor(Color.YELLOW);
            else
                convertView.setBackgroundColor(Color.TRANSPARENT);

            return convertView;
        }
    }

}
