package com.example.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


        tv_assessment_student = (TextView) findViewById(R.id.tv_assessment_student);
        studentList = new ArrayList<Integer>();

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

        //Log.d("1111111111", String.valueOf(studentList.size()));

        tv_assessment_total_mark = (TextView) findViewById(R.id.tv_assessment_total_mark);

        if(project.getStudentInfo().get(studentList.get(0)).getMark() != null){

            for(int j = 0; j < project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().size(); j++){
                totalWeighting = totalWeighting + project.getStudentInfo().get(studentList.get(0)).getMark().getCriteriaList().get(j).getMaximunMark();
            }

            for(int k = 0; k < project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().size(); k++){
                totalMark = totalMark + project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().get(k) *
                        (project.getStudentInfo().get(studentList.get(0)).getMark().getCriteriaList().get(k).getMaximunMark() / totalWeighting);
            }

            tv_assessment_total_mark.setText(totalMark + "%");
            Log.d("111", String.valueOf(totalWeighting));
        }else{
            tv_assessment_total_mark.setText("0%");
            for(int m = 0; m < studentList.size(); m++){
                project.getStudentInfo().get(m).setMark(new Mark());
                for(int n = 0; n < project.getCriteria().size(); n++){
                    project.getStudentInfo().get(m).getMark().getCriteriaList().add(project.getCriteria().get(n));
                    project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).getSubsectionList().clear();
                    project.getStudentInfo().get(m).getMark().getMarkList().add(0.0);
                }
                for(int n = 0; n < project.getCommentList().size(); n++){
                    project.getStudentInfo().get(m).getMark().getCommentList().add(project.getCommentList().get(n));
                    project.getStudentInfo().get(m).getMark().getCommentList().get(n).getSubsectionList().clear();
                }
            }

            for(int j = 0; j < project.getCriteria().size(); j++){
                totalWeighting = totalWeighting + project.getCriteria().get(j).getMaximunMark();
                Log.d("22222", String.valueOf(project.getCriteria().get(j).getMaximunMark()));
            }

        }



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
        lv_commentOnly.setAdapter(myAdapter2);
        lv_otherComment.setAdapter(myAdapter3);

    }

    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Criteria> criteriaList;
        private Double increment = 0.0;

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

            if(criteriaList.get(position).getMarkIncrement().equals("1")){
                increment = 1.0;
            }else if(criteriaList.get(position).getMarkIncrement().equals("1/2")){
                increment = 0.5;
            }else if(criteriaList.get(position).getMarkIncrement().equals("1/4")) {
                increment = 0.25;
            }

            TextView tv_red = convertView.findViewById(R.id.tv_red);
            TextView tv_yellow = convertView.findViewById(R.id.tv_yellow);
            TextView tv_green = convertView.findViewById(R.id.tv_green);

            sb_mark = (SeekBar) convertView.findViewById(R.id.sb_mark);
            tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            //tv_mark.setText("10");
            sb_mark.setMax((int)(10.0/increment));
            final View view2 = convertView;
            //sb_mark.setProgress(0);
            sb_mark.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Double progressDisplay = progress * increment;
                    tv_mark = (TextView) view2.findViewById(R.id.tv_mark);
                    tv_mark.setText(String.valueOf(progressDisplay) + " / 10");

                    for(int i = 0; i < studentList.size(); i++){
                        if(project.getStudentInfo().get(i).getMark().getMarkList() == null){
                            project.getStudentInfo().get(i).getMark().getMarkList().add(progressDisplay * project.getCriteria().get(position).getMaximunMark() / 10);

                        }else{
                            project.getStudentInfo().get(i).getMark().getMarkList().set(position, progressDisplay * project.getCriteria().get(position).getMaximunMark() / 10);

                        }

                    }

                    totalMark();
                    tv_assessment_total_mark.setText(String.format("%.2f", project.getStudentInfo().get(0).getMark().getTotalMark()) + "%");



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

    public void totalMark() {
        for (int i = 0; i < studentList.size(); i++) {
            Double sum = 0.0;
            for (int k = 0; k < project.getStudentInfo().get(i).getMark().getMarkList().size(); k++) {

                sum = sum + 100 * project.getStudentInfo().get(i).getMark().getMarkList().get(k) / totalWeighting;

                Log.d("111", String.valueOf(project.getStudentInfo().get(i).getMark().getMarkList().size()));

                Log.d("111", String.valueOf(project.getStudentInfo().get(i).getMark().getMarkList().get(k)));
                Log.d("111", String.valueOf(sum));
                project.getStudentInfo().get(i).getMark().setTotalMark(sum);
            }


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
            tv_list_comment_only.setText(commentList.get(position).getName());

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
