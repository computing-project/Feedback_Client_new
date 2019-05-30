package com.example.feedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Activity_Student_Group extends Activity {

    private MyAdapter myAdapter;
    private ArrayList<StudentInfo> students;
    private ListView listView;
    private int indexOfStudent = -999;
    private int indexOfProject;
    private ProjectInfo project;
    private String path;

    private String studentID;
    private String firstName;
    private String middleName;
    private String surname;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__student__group);

        Intent intent =getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("index"));
        project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        listView = (ListView) findViewById(R.id.listView_ingroupStudent);
        init();


    }

    public void init()
    {

        students = project.getStudentInfo();

        myAdapter = new MyAdapter(students, this);

        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                myAdapter.notifyDataSetChanged();
            }
        });
        TextView textView_projectName = findViewById(R.id.textView_projectName_studentManagement);
        textView_projectName.setText(project.getProjectName());
        TextView textView_helloUser = findViewById(R.id.textView_helloUser_studentManagement);
        textView_helloUser.setText("Hello, "+AllFunctions.getObject().getUsername());
        TextView textView_logout = findViewById(R.id.textView_logout_studentManagement);
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Student_Group.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    //button delete click.
    public void deleteStudent(View view)
    {
        if (listView.getCheckedItemCount() == 1) {
            SparseBooleanArray checkedItemsStudents = listView.getCheckedItemPositions();
            int studentIndex = -1;
            if (checkedItemsStudents != null) {
                for(int i=0; i<project.getStudentInfo().size(); i++)
                {
                    if(checkedItemsStudents.get(i) == true)
                    {
                        studentIndex = i;
                        break;
                    }
                }
                AllFunctions.getObject().deleteStudent(project, students.get(studentIndex).getNumber());
                students.remove(studentIndex);
                init();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please choose only 1 student to delete.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void back_studentManagement(View view)
    {
        finish();
    }

    public void save_studentManagement(View view)
    {
        Intent intent = new Intent(this, Assessment_Preparation_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void group_studentManagement(View v)
    {
        if (listView.getCheckedItemCount() > 1) {
            SparseBooleanArray checkedItemsStudents = listView.getCheckedItemPositions();
            if (checkedItemsStudents != null) {
                int maxGroupNum = AllFunctions.getObject().getMaxGroupNumber(indexOfProject);
                for (int i = 0; i < checkedItemsStudents.size(); i++) {
                    if (checkedItemsStudents.valueAt(i)) {
                        project.getStudentInfo().get(i).setGroup(maxGroupNum + 1);
                        AllFunctions.getObject().groupStudent(project,
                                project.getStudentInfo().get(i).getNumber(),
                                maxGroupNum + 1);
                    }
                }
                Collections.sort(project.getStudentInfo(), new SortByGroup());
                for(int i=0; i<project.getStudentInfo().size(); i++)
                    listView.setItemChecked(i,false);
                myAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please choose more than 1 students to form a group.",
                    Toast.LENGTH_SHORT).show();

        }
    }


    public void unGroup_studentManagement(View v)
    {
        if (listView.getCheckedItemCount() > 0) {
            SparseBooleanArray checkedItemsStudents = listView.getCheckedItemPositions();
            if (checkedItemsStudents != null) {
                for (int i = 0; i < checkedItemsStudents.size(); i++) {
                    if (checkedItemsStudents.valueAt(i)) {
                        project.getStudentInfo().get(i).setGroup(-999);
                        AllFunctions.getObject().groupStudent(project,
                                project.getStudentInfo().get(i).getNumber(),
                                -999);
                    }
                }
                Collections.sort(project.getStudentInfo(), new SortByGroup());
                for(int i=0; i<project.getStudentInfo().size(); i++)
                    listView.setItemChecked(i,false);
                myAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please choose at least 1 student to unGroup.",
                    Toast.LENGTH_SHORT).show();

        }
    }



    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<StudentInfo> studentList;
        int position_use;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_student_group, parent, false);
            TextView textView_groupNum = (TextView) convertView.findViewById(R.id.textView_groupnum_instudentlist);
            if(studentList.get(position).getGroup() == -999)
                textView_groupNum.setText("");
            else
                textView_groupNum.setText(String.valueOf(studentList.get(position).getGroup()));

            TextView textView_studentID = convertView.findViewById(R.id.textView_studentID_instudentlist);
            textView_studentID.setText(studentList.get(position).getNumber());
            TextView textView_studentName = convertView.findViewById(R.id.textView_fullname_instudentlist);
            textView_studentName.setText(studentList.get(position).getFirstName()+" "+studentList.get(position).getMiddleName()+" "+studentList.get(position).getSurname());
            TextView textView_studentEmail = convertView.findViewById(R.id.textView_email_instudentlist);
            textView_studentEmail.setText(studentList.get(position).getEmail());


            if(listView.isItemChecked(position))
                convertView.setBackgroundColor(Color.parseColor("#D2EBF7"));
            else
                convertView.setBackgroundColor(Color.TRANSPARENT);
            return convertView;
        }
    }

    private static final int FILE_SELECT_CODE = 0;

    public void import_StudentManagement(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            // Potentially direct the user to the Market with a Dialog
        }

    }

    private static final String TAG = "ChooseFile";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path

                    path = FileUtils.getPath(this, uri);
                    AllFunctions.getObject().readExcel(project,path);
                    System.out.println("call the readExcel method: "+path);
                    init();
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


        //button addStudent click.
        public void addStudent(View v) {

            LayoutInflater layoutInflater = LayoutInflater.from(Activity_Student_Group.this);//获得layoutInflater对象
            final View view = layoutInflater.from(Activity_Student_Group.this).inflate(R.layout.dialog_add_student, null);//获得view对象

            Dialog dialog = new AlertDialog.Builder(Activity_Student_Group.this).setTitle("Add Student").setView(view).setPositiveButton("Done", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    EditText editText_studentID_addStudent = (EditText) view.findViewById(R.id.editText_studentID_addStudent);//获取控件
                    EditText editText_firstName_addStudent = (EditText) view.findViewById(R.id.editText_firstName_addStudent);//获取控件
                    EditText editText_middleName_addStudent = (EditText) view.findViewById(R.id.editText_middleName_addStudent);//获取控件
                    EditText editText_surname_addStudent = (EditText) view.findViewById(R.id.editText_surname_addStudent);//获取控件
                    EditText editText_email_addStudent = (EditText) view.findViewById(R.id.editText_email_addStudent);//获取控件

                    studentID = editText_studentID_addStudent.getText().toString();
                    firstName = editText_firstName_addStudent.getText().toString();
                    middleName = editText_middleName_addStudent.getText().toString();
                    surname = editText_surname_addStudent.getText().toString();
                    email = editText_email_addStudent.getText().toString();

                    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";

                    if(studentID.equals("")) {
                        Toast.makeText(getApplicationContext(), "StudentID cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    else if(firstName.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "FirstName cannot be empty", Toast.LENGTH_SHORT).show();

                    }
                    else if(surname.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "LastName cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    else if(!email.matches(emailPattern))
                    {
                        Toast.makeText(getApplicationContext(), "Please input a valid Email", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (AllFunctions.getObject().searchStudent(project, studentID) == -999) {
                            AllFunctions.getObject().addStudent(project, studentID, firstName, middleName, surname, email);
                            init();
                        } else {
                            Toast.makeText(getApplicationContext(), "student with ID:" + studentID + " is already exits.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            }).create();
            dialog.show();
        }



    public void editStudent_inStudentManagement(View v) {

        if (listView.getCheckedItemCount() == 1) {
            SparseBooleanArray checkedItemsStudents = listView.getCheckedItemPositions();
            if (checkedItemsStudents != null) {
                for (int i = 0; i < project.getStudentInfo().size(); i++) {
                    if (checkedItemsStudents.get(i) == true) {
                        indexOfStudent = i;
                        break;
                    }
                }

                LayoutInflater layoutInflater = LayoutInflater.from(Activity_Student_Group.this);//获得layoutInflater对象
                View view = layoutInflater.from(Activity_Student_Group.this).inflate(R.layout.dialog_add_student, null);//获得view对象

                EditText editText_studentID_addStudent = (EditText) view.findViewById(R.id.editText_studentID_addStudent);//获取控件
                editText_studentID_addStudent.setEnabled(false);
                editText_studentID_addStudent.setText(students.get(indexOfStudent).getNumber());
                EditText editText_firstName_addStudent = (EditText) view.findViewById(R.id.editText_firstName_addStudent);//获取控件
                editText_firstName_addStudent.setText(students.get(indexOfStudent).getFirstName());
                EditText editText_middleName_addStudent = (EditText) view.findViewById(R.id.editText_middleName_addStudent);//获取控件
                editText_middleName_addStudent.setText(students.get(indexOfStudent).getMiddleName());
                EditText editText_surname_addStudent = (EditText) view.findViewById(R.id.editText_surname_addStudent);//获取控件
                editText_surname_addStudent.setText(students.get(indexOfStudent).getSurname());
                EditText editText_email_addStudent = (EditText) view.findViewById(R.id.editText_email_addStudent);//获取控件
                editText_email_addStudent.setText(students.get(indexOfStudent).getEmail());

                Dialog dialog = new AlertDialog.Builder(Activity_Student_Group.this).setTitle("Edit Student").setView(view).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        studentID = editText_studentID_addStudent.getText().toString();
                        firstName = editText_firstName_addStudent.getText().toString();
                        middleName = editText_middleName_addStudent.getText().toString();
                        surname = editText_surname_addStudent.getText().toString();
                        email = editText_email_addStudent.getText().toString();

                        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";

                        if (studentID.equals("")) {
                            Toast.makeText(getApplicationContext(), "StudentID cannot be empty", Toast.LENGTH_SHORT).show();
                        } else if (firstName.equals("")) {
                            Toast.makeText(getApplicationContext(), "FirstName cannot be empty", Toast.LENGTH_SHORT).show();

                        } else if (surname.equals("")) {
                            Toast.makeText(getApplicationContext(), "LastName cannot be empty", Toast.LENGTH_SHORT).show();
                        } else if (!email.matches(emailPattern)) {
                            Toast.makeText(getApplicationContext(), "Please input a valid Email", Toast.LENGTH_SHORT).show();
                        } else {
                            AllFunctions.getObject().editStudent(project, studentID, firstName, middleName, surname, email);
                            students.get(indexOfStudent).setStudentInfo(studentID, firstName,
                                    middleName, surname, email);
                            init();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }).create();
                dialog.show();

            }
        }
            else {
                Toast.makeText(getApplicationContext(),
                        "Please choose only 1 student to edit.",
                        Toast.LENGTH_SHORT).show();
            }
    }

    public class SortByGroup implements Comparator {

        public int compare(Object o1, Object o2) {
            StudentInfo s1 = (StudentInfo) o1;
            StudentInfo s2 = (StudentInfo) o2;
            if (s1.getGroup() > s2.getGroup() && s2.getGroup() == -999) {

                return -1;

            }else if(s1.getGroup() < s2.getGroup() && s1.getGroup() == -999){
                return 1;
            }else if(s1.getGroup() > s2.getGroup()){
                return 1;
            }else if(s1.getGroup() == s2.getGroup()){
                return 1;
            }else return -1;
        }

    }


}

