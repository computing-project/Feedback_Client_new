package com.example.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;

public class Activity_Assessment extends AppCompatActivity implements View.OnClickListener {

    MyAdapter myAdapter;
    MyAdapter2 myAdapter2;
    MyAdapter3 myAdapter3;

    int indexOfProject;
    int indexOfStudent;
    int indexOfGroup;
    ArrayList<Integer> studentList;

    ProjectInfo project;
    ArrayList<Criteria> criteriaList;
    ArrayList<Criteria> commentList;

    ListView lv_individual;
    ListView lv_commentOnly;
    ListView lv_otherComment;
    TextView tv_time;
    Button btn_assessment_start;
    Button btn_assessment_refresh;

    TextView tv_assessment_student;
    TextView tv_assessment_total_mark;
    Button btn_assessment_finish;

    SeekBar sb_mark;
    TextView tv_mark;
    Double singleMark;
    Double totalMark = 0.0;
    ArrayList<Double> markList;
    int totalWeighting = 0;

    TextView tv_list_comment_only;
    Button btn_comment_only_comment;

    TextView tv_other_comment;
    EditText et_other_comment;

    private long durationTime = 0;
    private long warningTime = 0;
    private boolean isPause = false;
    private CountDownTimer countDownTimer;
    private long leftTime = 0;
    private int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        Intent intent =getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("indexOfProject"));
        indexOfStudent= Integer.parseInt(intent.getStringExtra("indexOfStudent"));
        indexOfGroup= Integer.parseInt(intent.getStringExtra("indexOfGroup"));
        project = AllFunctions.getObject().getProjectList().get(indexOfProject);

        if(indexOfGroup == -999){
            tv_assessment_student.setText(project.getStudentInfo().get(indexOfStudent).getNumber() + " " +
                    project.getStudentInfo().get(indexOfStudent).getFirstName() + " " +
                    project.getStudentInfo().get(indexOfStudent).getMiddleName() + " " +
                    project.getStudentInfo().get(indexOfStudent).getSurname());
            studentList.add(indexOfStudent);
        }else {
            tv_assessment_student.setText("Group " + indexOfGroup);
            for(int i = 0; i < project.getStudentInfo().size(); i++){
                if(project.getStudentInfo().get(i).getGroup() == indexOfGroup){
                    studentList.add(i);
                }
            }
        }

        tv_mark = (TextView) findViewById(R.id.tv_mark);
        for(int j = 0; j < project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().size(); j++){
            totalWeighting = totalWeighting + project.getStudentInfo().get(studentList.get(0)).getMark().getCriteriaList().get(j).getWeighting();
        }

        for(int k = 0; k < project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().size(); k++){
            totalMark = totalMark + project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().get(k) *
                    (project.getStudentInfo().get(studentList.get(0)).getMark().getCriteriaList().get(k).getWeighting() / totalWeighting);
        }

        tv_mark.setText(totalWeighting + "%");

        lv_individual = (ListView) findViewById(R.id.lv_individual);
        lv_commentOnly = (ListView) findViewById(R.id.lv_commentOnly);
        lv_otherComment = (ListView) findViewById(R.id.lv_otherComment);
        init();


        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_time.setText(String.format("%02d", durationTime/1000/60) +":"+String.format("%02d", durationTime/1000%60));

        btn_assessment_start = findViewById(R.id.btn_assessment_start);
        btn_assessment_refresh = findViewById(R.id.btn_assessment_refresh);

        findViewById(R.id.btn_assessment_start).setOnClickListener(this);
        findViewById(R.id.btn_assessment_refresh).setOnClickListener(this);

        btn_assessment_start.setEnabled(true);
        btn_assessment_refresh.setEnabled(false);
        initTimer(durationTime);

    }

    public void init()
    {

        criteriaList = project.getCriteria();
        commentList = project.getCommentList();
        durationTime = project.getDurationMin() * 60000 + project.getDurationSec() * 1000;
        warningTime = project.getWarningMin() * 60000 + project.getWarningSec() * 1000;
        myAdapter = new MyAdapter(criteriaList, this);
        myAdapter2 = new MyAdapter2(commentList, this);
        myAdapter3 = new MyAdapter3(studentList, this);

        lv_individual.setAdapter(myAdapter);

    }

    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Criteria> criteriaList;
        private Double increment;

        public MyAdapter(ArrayList<Criteria> criteriaList, Context context) {
            this.criteriaList = criteriaList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return criteriaList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_individual_assessment, parent, false);

            TextView tv_criteria_name = convertView.findViewById(R.id.tv_criteria_name);
            tv_criteria_name.setText(criteriaList.get(position).getName());

            if(criteriaList.get(position).getMarkIncrement().equals("full")){
                increment = 1.0;
            }else if(criteriaList.get(position).getMarkIncrement().equals("half")){
                increment = 0.5;
            }else if(criteriaList.get(position).getMarkIncrement().equals("quarter")) {
                increment = 0.25;
            }

            TextView tv_red = findViewById(R.id.tv_red);
            TextView tv_yellow = findViewById(R.id.tv_yellow);
            TextView tv_green = findViewById(R.id.tv_green);

            sb_mark = (SeekBar) findViewById(R.id.sb_mark);

            sb_mark.setProgress(0);
            sb_mark.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Double progressDisplay = progress * increment;
                    sb_mark.setMax((int)(10/increment));
                    tv_mark.setText(progressDisplay + " / 10");
                    for(int i = 0; i < studentList.size(); i++){
                        project.getStudentInfo().get(i).getMark().getMarkList().set(position, progressDisplay);

                        for(int k = 0; k < project.getStudentInfo().get(i).getMark().getMarkList().size(); k++){
                            totalMark = totalMark + project.getStudentInfo().get(i).getMark().getMarkList().get(k) *
                                    (project.getStudentInfo().get(studentList.get(0)).getMark().getCriteriaList().get(k).getWeighting() / totalWeighting);
                        }
                        project.getStudentInfo().get(i).getMark().setTotalMark(totalMark);
                        tv_mark.setText(totalWeighting + "%");
                    }


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

//        btn_color.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
//                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//
//                tv_red.setLayoutParams(param1);
//
//                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
//                        0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
//
//                tv_yellow.setLayoutParams(param2);
//
//                LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
//                        0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
//
//                tv_green.setLayoutParams(param3);
//            }
//        });



            return convertView;
        }

    }



    public void initTimer(long millisUntilFinished){

        btn_assessment_start.setEnabled(true);

        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
            public void onTick(long millisUntilFinished) {
                leftTime = millisUntilFinished;
                if(leftTime < warningTime){
                    tv_time.setTextColor(android.graphics.Color.RED);
                }
                tv_time.setText(String.format("%02d", millisUntilFinished/1000/60) +":"+String.format("%02d", millisUntilFinished/1000%60));
            }

            public void onFinish() {
                tv_time.setText("00:00");
            }
        };

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_assessment_start:
                if(flag == 0){
                    isPause = false;
                    countDownTimer.start();
                    btn_assessment_start.setText("PAUSE");
                    flag = 1;
                    btn_assessment_refresh.setEnabled(false);
                    break;
                }else if(flag == 1){
                    if (!isPause) {
                        isPause = true;
                        countDownTimer.cancel();

                    }

                    btn_assessment_start.setText("START");
                    flag = 2;
                    btn_assessment_refresh.setEnabled(true);
                    break;

                }else{
                    if (leftTime != 0 && isPause) {
                        //将上次当前剩余时间作为新的时长
                        initTimer(leftTime);
                        countDownTimer.start();
                        isPause = false;

                    }
                    btn_assessment_refresh.setEnabled(false);
                    btn_assessment_start.setText("PAUSE");
                    flag = 1;
                    break;
                }

            case R.id.btn_assessment_refresh:
                countDownTimer.cancel();
                btn_assessment_refresh.setEnabled(false);
                tv_time.setText(String.format("%02d", durationTime/1000/60) +":"+String.format("%02d", durationTime/1000%60));
                initTimer(durationTime);
                flag = 0;
                break;
            default:
                break;
        }
    }

    public class MyAdapter2 extends BaseAdapter {

        private Context mContext;
        private ArrayList<Criteria> commentList;

        public MyAdapter2(ArrayList<Criteria> commentList, Context context) {
            this.commentList = commentList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_comment_only, parent, false);

            TextView tv_list_comment_only = convertView.findViewById(R.id.tv_list_comment_only);
//            tv_list_comment_only.setText("");

            return convertView;
        }
    }

    public class MyAdapter3 extends BaseAdapter {

        private Context mContext;
        private ArrayList<Integer> studentList;

        public MyAdapter3(ArrayList<Integer> studentList,
                          Context context) {
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_other_comment, parent, false);

            et_other_comment = convertView.findViewById(R.id.et_other_comment);
            String otherComment = et_other_comment.getText().toString();
            for(int i = 0; i < studentList.size(); i++){
                project.getStudentInfo().get(i).getMark().setComment(otherComment);
            }

            return convertView;
        }
    }



//    public void finish_assessment(View view)
//    {
//        durationMin = Integer.parseInt(editText_durationMin.getText().toString());
//        durationSec = Integer.parseInt(editText_durationSec.getText().toString());
//        warningMin = Integer.parseInt(editText_warningMin.getText().toString());
//        warningSec = Integer.parseInt(editText_warningSec.getText().toString());
//        System.out.println("Time in Timer: "+durationMin+":"+durationSec+"   "+warningMin+":"+warningSec);
//        AllFunctions.getObject().projectTimer(project,durationMin,durationSec,warningMin,warningSec);
//        Intent intent = new Intent(this, Assessment_Preparation_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//    }



}
