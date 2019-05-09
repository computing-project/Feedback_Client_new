package com.example.feedback;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_Student_Group extends AppCompatActivity {

    MyAdapter myAdapter;
    ArrayList<StudentInfo> students;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__student__group);
        listView = (ListView) findViewById(R.id.listView_ingroupStudent);
        students = new ArrayList<>();
        students.add(new StudentInfo("1111","fist1","midd1", "last11","11@qq.com"));
        students.add(new StudentInfo("2222","fist2","midd2", "last2","22@qq.com"));
        students.add(new StudentInfo("3333","fist3","midd333", "last33","33@qq.com"));
        init();
    }

    public void init()
    {

        myAdapter = new MyAdapter(students, this);

        listView.setAdapter(myAdapter);
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
            return null;
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


            final View dragView = convertView;
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // DND框架要求传递的数据
                    ClipData.Item item = new ClipData.Item(String.valueOf(position));

                    ClipData clipData = new ClipData("", new String[] {
                            ClipDescription.MIMETYPE_TEXT_PLAIN
                    }, item);

                    // 开始当前View的拖动操作，将当前拖动对象的position当作localState传递到拖动事件中
                    dragView.startDrag(clipData, new View.DragShadowBuilder(dragView), position_use, 0);
                    return true;
                }
            });

            convertView.setOnDragListener(new View.OnDragListener() {
                @Override


                public boolean onDrag(View v, DragEvent event) {

                    // Defines a variable to store the action type for the incoming event
                    final int action = event.getAction();

                    // Handles each of the expected events
                    switch(action) {

                        case DragEvent.ACTION_DRAG_STARTED:
                            // Determines if this View can accept the dragged data
                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                                v.invalidate();
                                return true;

                            }
                            return false;

                        case DragEvent.ACTION_DRAG_ENTERED:

                            v.setBackgroundColor(Color.GREEN);
                            v.invalidate();

                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:
                            v.setBackgroundColor(Color.TRANSPARENT);
                            v.invalidate();
                            return true;

                        case DragEvent.ACTION_DROP:

                            v.setBackgroundColor(Color.TRANSPARENT);
                            // Gets the item containing the dragged data
                            ClipData.Item item = event.getClipData().getItemAt(0);
                            final int srcPosition = Integer.parseInt(item.getText().toString());
                            System.out.println("srcPosition is"+srcPosition);
                            System.out.println("position is"+position);
                            students.get(srcPosition).setGroup(1);
                            students.get(position).setGroup(1);
                            StudentInfo studentTemporary = students.get(srcPosition);
                            students.remove(srcPosition);
                            if(position+1>students.size())
                                students.add(studentTemporary);
                            else
                                students.add(position+1,studentTemporary);

                            // myAdapter.notifyDataSetChanged();
                            init();

                            Toast.makeText(Activity_Student_Group.this, "Dragged data is " , Toast.LENGTH_LONG);

                            // Turns off any color tints


                            // Invalidates the view to force a redraw
                            v.invalidate();

                            // Returns true. DragEvent.getResult() will return true.
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            // Turns off any color tinting
                            // Invalidates the view to force a redraw
                            v.invalidate();

                            // Does a getResult(), and displays what happened.
                            if (event.getResult()) {
                                Toast.makeText(Activity_Student_Group.this, "The drop was handled.", Toast.LENGTH_LONG);

                            } else {
                                Toast.makeText(Activity_Student_Group.this, "The drop didn't work.", Toast.LENGTH_LONG);

                            }
                            // returns true; the value is ignored.
                            return true;
                        // An unknown action type was received.
                        default:
                            Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                            break;
                    }
                    return false;
                }

            });


            return convertView;
        }
    }

}
