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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_Student_Group extends Activity {

    MyAdapter myAdapter;
    ArrayList<StudentInfo> students;
    ListView listView;
    int indexOfStudent = -999;
    int indexOfProject;
    ProjectInfo project;
    String path;

    String studentID;
    String firstName;
    String middleName;
    String surname;
    String email;

    double rawX, rawY;

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
        AllFunctions.getObject().deleteStudent(project,students.get(indexOfStudent).getNumber());
        students.remove(indexOfStudent);
        init();
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

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("the "+position+" elem has been clicked!");
                    for(int i=0; i<parent.getChildCount(); i++)
                        parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    view.setBackgroundColor(Color.rgb(135,206,250));
                    indexOfStudent = position;
                }
            });


//            final View dragView = convertView;
////            convertView.setOnLongClickListener(new View.OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View v) {
////                    // DND框架要求传递的数据
////                    ClipData.Item item = new ClipData.Item(String.valueOf(position));
////
////                    ClipData clipData = new ClipData("", new String[] {
////                            ClipDescription.MIMETYPE_TEXT_PLAIN
////                    }, item);
////
////                    // 开始当前View的拖动操作，将当前拖动对象的position当作localState传递到拖动事件中
////                    dragView.startDrag(clipData, new View.DragShadowBuilder(dragView), position_use, 0);
////                    return true;
////                }
////            });
////
////            convertView.setOnDragListener(new View.OnDragListener() {
////                @Override
////
////
////                public boolean onDrag(View v, DragEvent event) {
////
////                    // Defines a variable to store the action type for the incoming event
////                    final int action = event.getAction();
////
////                    // Handles each of the expected events
////                    switch(action) {
////
////                        case DragEvent.ACTION_DRAG_STARTED:
////                            // Determines if this View can accept the dragged data
////                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
////                                v.invalidate();
////
////                                return true;
////                            }
////                            return false;
////
////                        case DragEvent.ACTION_DRAG_ENTERED:
////
////                            v.setBackgroundColor(Color.GREEN);
////                            v.invalidate();
////
////                            return true;
////
////                        case DragEvent.ACTION_DRAG_LOCATION:
////                            return true;
////
////                        case DragEvent.ACTION_DRAG_EXITED:
////                            v.setBackgroundColor(Color.TRANSPARENT);
////                            v.invalidate();
////                            return true;
////
////                        case DragEvent.ACTION_DROP:
////
////                            v.setBackgroundColor(Color.TRANSPARENT);
////                            // Gets the item containing the dragged data
////                            ClipData.Item item = event.getClipData().getItemAt(0);
////                            final int srcPosition = Integer.parseInt(item.getText().toString());
////
////                            //change group num
////                            if(students.get(position).getGroup()!= -999)
////                                students.get(srcPosition).setGroup(students.get(position).getGroup());
////                            else
////                            {
////                                int maxGroupNumNow = AllFunctions.getObject().getMaxGroupNumber(indexOfProject);
////                                students.get(srcPosition).setGroup(maxGroupNumNow+1);
////                                students.get(position).setGroup(maxGroupNumNow+1);
////                            }
////
////                            StudentInfo studentTemporary = students.get(srcPosition);
////                            students.remove(srcPosition);
////                            if(position+1>students.size())
////                                students.add(studentTemporary);
////                            else
////                                students.add(position+1,studentTemporary);
////
////                            // myAdapter.notifyDataSetChanged();
////                            init(indexOfProject);
////
////                            // Invalidates the view to force a redraw
////                            v.invalidate();
////
////                            // Returns true. DragEvent.getResult() will return true.
////                            return true;
////
////                        case DragEvent.ACTION_DRAG_ENDED:
////
////                            // Turns off any color tinting
////                            // Invalidates the view to force a redraw
////                            v.invalidate();
////
////                            // returns true; the value is ignored.
////                            return true;
////                        // An unknown action type was received.
////                        default:
////                            Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
////                            break;
////                    }
////                    return false;
////                }
////
////            });

//    final ListView l = (ListView) parent;
////            convertView.setOnTouchListener(new View.OnTouchListener() {
////                @Override
////                public boolean onTouch(View v, MotionEvent event) {
////                    int action = event.getAction();
////                    switch (action)
////                    {
////                        case MotionEvent.ACTION_DOWN:
////                            int rawX1 = (int) event.getRawX() - (int) listView.getX();
////                            int rawY2 = (int) event.getRawY() - (int) listView.getY();
////                            System.out.println("横坐标: "+rawX1+"  纵坐标: "+rawY2);
////                            System.out.println("第几个开始:"+l.pointToPosition(rawX1, rawY2));
////                            break;
////                        case MotionEvent.ACTION_MOVE:
//////                            double moveX = event.getX() - rawX;
//////                            double moveY = event.getY() - rawY;
//////                            System.out.println("移动过程中坐标: "+moveX+" "+moveY);
//////                            if(moveX > 400)
//////                                v.setBackgroundColor(Color.GRAY);
////                            break;
////                        case MotionEvent.ACTION_UP:
////                            System.out.println("第几个:"+listView.pointToPosition((int)event.getX(), (int)event.getY()));
////                            break;
////
////                        default:
////                            break;
////                    }
////                    return true;
////                }
////            });

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

                    AllFunctions.getObject().addStudent(project, studentID, firstName, middleName,surname,email);
                    init();


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

                AllFunctions.getObject().editStudent(project, studentID, firstName, middleName,surname,email);
                init();


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

