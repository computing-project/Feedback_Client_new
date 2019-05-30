package com.example.feedback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class Activity_Assessment extends Activity implements View.OnClickListener {

    MyAdapter myAdapter;
    MyAdapter2 myAdapter2;
    MyAdapter3 myAdapter3;

    int indexOfProject;
    int indexOfStudent;
    int indexOfGroup;
    ArrayList<Integer> studentList;

    private static ProjectInfo project;
    ArrayList<Criteria> criteriaList;
    ArrayList<Criteria> commentList;

    ListView lv_individual;
    ListView lv_commentOnly;
    ListView lv_otherComment;
    TextView tv_time;
    Button btn_assessment_start;
    Button btn_assessment_refresh;
    Button btn_assessment_save;


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

//    Double increment = 0.0;

    private long durationTime = 0;
    private long warningTime = 0;
    private boolean isPause = false;
    private CountDownTimer countDownTimer;
    private long leftTime = 0;
    private int flag = 0;


    static private int matrixOfMarkedCriteria[][];
    static private int matrixOfCommentOnly[][];
    static private int matrixCriteriaLongtext[][];
    static private int matrixCommentLongText[][];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        Intent intent =getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("indexOfProject"));
        indexOfStudent= Integer.parseInt(intent.getStringExtra("indexOfStudent"));
        indexOfGroup = Integer.parseInt(intent.getStringExtra("indexOfGroup"));
        project = AllFunctions.getObject().getProjectList().get(indexOfProject);

        initMatrix();

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


            for(int m = 0; m < studentList.size(); m++){
                for(int n = 0; n < project.getCriteria().size(); n++){
                    project.getStudentInfo().get(studentList.get(m)).getMark().getCriteriaList().get(n).getSubsectionList().clear();


                }
                for(int n = 0; n < project.getCommentList().size(); n++){
                    project.getStudentInfo().get(studentList.get(m)).getMark().getCommentList().get(n).getSubsectionList().clear();


                }
            }





            for(int j = 0; j < project.getCriteria().size(); j++){
                totalWeighting = totalWeighting + project.getCriteria().get(j).getMaximunMark();

            }

            for(int k = 0; k < project.getCriteria().size(); k++){
//                totalMark = totalMark + project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().get(k) *
//                        (project.getCriteria().get(k).getMaximunMark() / totalWeighting);
                totalMark = project.getStudentInfo().get(studentList.get(0)).getMark().getTotalMark();
                Log.d("1115", String.valueOf(project.getCriteria().get(k).getMaximunMark()));

            }

            tv_assessment_total_mark.setText(String.format("%.2f", project.getStudentInfo().get(studentList.get(0)).getMark().getTotalMark()) + "%");
            Log.d("1112", String.valueOf(totalWeighting));
            Log.d("1113", String.valueOf(totalMark));

        }else{
            tv_assessment_total_mark.setText("0%");
            for(int m = 0; m < studentList.size(); m++){
                project.getStudentInfo().get(studentList.get(m)).setMark(new Mark());
                for(int n = 0; n < project.getCriteria().size(); n++){
                    project.getStudentInfo().get(studentList.get(m)).getMark().getCriteriaList().add(new Criteria());
                    project.getStudentInfo().get(studentList.get(m)).getMark().getCriteriaList().get(n).setName(project.getCriteria().get(n).getName());
                    project.getStudentInfo().get(studentList.get(m)).getMark().getCriteriaList().get(n).setMaximunMark(project.getCriteria().get(n).getMaximunMark());
                    project.getStudentInfo().get(studentList.get(m)).getMark().getMarkList().add(0.0);

//                    for(int l = 0; l < project.getCriteria().get(n).getSubsectionList().size(); l++){
//                        project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).getSubsectionList().add(new SubSection());
//                        project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).getSubsectionList().get(l).setName(project.getCriteria().get(n).getSubsectionList().get(l).getName());

//                        for(int p = 0; p < project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().size(); p++){
//                            project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).getSubsectionList().get(l).getShortTextList().add(new ShortText());
//                            project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).getSubsectionList().get(l).getShortTextList().get(p).setName(project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().get(p).getName());
//
//                        }
//                    }
                }
                for(int n = 0; n < project.getCommentList().size(); n++){
                    project.getStudentInfo().get(studentList.get(m)).getMark().getCommentList().add(new Criteria());
                    project.getStudentInfo().get(studentList.get(m)).getMark().getCommentList().get(n).setName(project.getCommentList().get(n).getName());

//                    for(int l = 0; l < project.getCommentList().get(n).getSubsectionList().size(); l++){
//                        project.getStudentInfo().get(m).getMark().getCommentList().get(n).getSubsectionList().add(new SubSection());
//                        project.getStudentInfo().get(m).getMark().getCommentList().get(n).getSubsectionList().get(l).setName(project.getCommentList().get(n).getSubsectionList().get(l).getName());
//
//                        for(int p = 0; p < project.getCommentList().get(n).getSubsectionList().get(l).getShortTextList().size(); p++){
//                            project.getStudentInfo().get(m).getMark().getCommentList().get(n).getSubsectionList().get(l).getShortTextList().add(new ShortText());
//                            project.getStudentInfo().get(m).getMark().getCommentList().get(n).getSubsectionList().get(l).getShortTextList().get(p).setName(project.getCommentList().get(n).getSubsectionList().get(l).getShortTextList().get(p).getName());
//
//                        }
//                    }
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


    @Override
    protected void onNewIntent(Intent intent){
        lv_individual.setAdapter(myAdapter);

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
        setListViewHeightBasedOnChildren(lv_individual);
        lv_commentOnly.setAdapter(myAdapter2);
        setListViewHeightBasedOnChildren(lv_commentOnly);
        lv_otherComment.setAdapter(myAdapter3);
        setListViewHeightBasedOnChildren(lv_otherComment);

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
            final View view10 = convertView;
            TextView tv_criteria_name = convertView.findViewById(R.id.tv_criteria_name);
            tv_criteria_name.setText(project.getCriteria().get(position).getName());

                if(project.getCriteria().get(position).getMarkIncrement().equals("1")){
                    increment = 1.0;
                }else if(project.getCriteria().get(position).getMarkIncrement().equals("1/2")){
                    increment = 0.5;
                }else if(project.getCriteria().get(position).getMarkIncrement().equals("1/4")) {
                    increment = 0.25;
                }
            Log.d("111115", project.getCriteria().get(position).getMarkIncrement());
            Log.d("111116", String.valueOf(increment));

            TextView tv_red = view10.findViewById(R.id.tv_red);
            TextView tv_yellow = view10.findViewById(R.id.tv_yellow);
            TextView tv_green = view10.findViewById(R.id.tv_green);

            ArrayList<Integer> weightList = new ArrayList<>();

            weightList.add(0, 0);
            weightList.add(1, 0);
            weightList.add(2, 0);

//            for(int n = 0; n < project.getStudentInfo().get(studentList.get(0)).getMark().getCriteriaList().size(); n++){
//                for(int l = 0; l < project.getStudentInfo().get(studentList.get(0)).getMark().getCriteriaList().get(n).getSubsectionList().size(); l++){
//                    for(int p = 0; p < project.getStudentInfo().get(studentList.get(0)).getMark().getCriteriaList().get(n).getSubsectionList().get(l).getShortTextList().size(); p++) {
//                        if(project.getStudentInfo().get(studentList.get(0)).getMark().getCriteriaList().get(n).getSubsectionList().get(l).getShortTextList().get(p).getLongtext().size() == 0){
//                        }else if(project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().get(p).getGrade() == 1){
//                            weightList.set(0, (weightList.get(0)+1));
//                        }else if(project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().get(p).getGrade() == 2){
//                            weightList.set(1, (weightList.get(0)+1));
//                        }else if(project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().get(p).getGrade() == 3){
//                            weightList.set(2, (weightList.get(0)+1));
//                        }
//                    }
//                }
//            }
//            Log.d("77777", String.valueOf(weightList.get(0)) + " " + String.valueOf(weightList.get(1)) + " " + String.valueOf(weightList.get(2)));

            if(getMatrixMarkedCriteria(position).size() != 0){
                for(int i = 0; i < getMatrixMarkedCriteria(position).size(); i ++){

                    int j = getMatrixMarkedCriteria(position).get(i).get(0);
                    int m = getMatrixMarkedCriteria(position).get(i).get(1);

                    if(project.getCriteria().get(position).getSubsectionList().get(j).getShortTextList().get(m).getGrade() == 1){
                        weightList.set(0, (weightList.get(0)+1));

                    }else if(project.getCriteria().get(position).getSubsectionList().get(j).getShortTextList().get(m).getGrade() == 2){
                        weightList.set(1, (weightList.get(1)+1));
                    }else if(project.getCriteria().get(position).getSubsectionList().get(j).getShortTextList().get(m).getGrade() == 3){
                        weightList.set(2, (weightList.get(2)+1));
                    }

                }
            }

            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, weightList.get(0));

            tv_red.setLayoutParams(param1);

            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, weightList.get(1));

            tv_yellow.setLayoutParams(param2);

            LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, weightList.get(2));

            tv_green.setLayoutParams(param3);


            Button btn_assessment_comment = convertView.findViewById(R.id.btn_assessment_comment_back);
            btn_assessment_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Activity_Assessment.this, Activity_assessment_comment.class);
                    intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
                    intent.putExtra("indexOfCriteria",String.valueOf(position));
                    intent.putExtra("indexOfComment",String.valueOf(-999));

                    startActivity(intent);
                }
            });


            sb_mark = (SeekBar) convertView.findViewById(R.id.sb_mark);
            tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            //tv_mark.setText("10");
            sb_mark.setMax((int)(project.getCriteria().get(position).getMaximunMark()/increment));
            final View view2 = convertView;
            sb_mark.setProgress((int)(project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().get(position)/increment));
            tv_mark.setText((project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().get(position) + " / " +project.getCriteria().get(position).getMaximunMark()));
            Log.d("111117", String.valueOf(increment));


            sb_mark.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if(project.getCriteria().get(position).getMarkIncrement().equals("1")){
                        increment = 1.0;
                    }else if(project.getCriteria().get(position).getMarkIncrement().equals("1/2")){
                        increment = 0.5;
                    }else if(project.getCriteria().get(position).getMarkIncrement().equals("1/4")) {
                        increment = 0.25;
                    }

                    Double progressDisplay = progress * increment;
                    tv_mark = (TextView) view2.findViewById(R.id.tv_mark);
                    tv_mark.setText(String.valueOf(progressDisplay) + " / " + project.getCriteria().get(position).getMaximunMark());
                    Log.d("111118", String.valueOf(increment));

                    for(int i = 0; i < studentList.size(); i++){
                        if(project.getStudentInfo().get(studentList.get(i)).getMark().getMarkList() == null){
                            project.getStudentInfo().get(studentList.get(i)).getMark().getMarkList().add(position, progressDisplay);

                        }else{
                            project.getStudentInfo().get(studentList.get(i)).getMark().getMarkList().set(position, progressDisplay);

                        }
                        Log.d("1114", String.valueOf(increment) + " " + String.valueOf(progressDisplay) + " " + String.valueOf(progress));

                    }

                    totalMark();
                    tv_assessment_total_mark.setText(String.format("%.2f", project.getStudentInfo().get(studentList.get(0)).getMark().getTotalMark()) + "%");



                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });






            return convertView;
        }

    }

    public void totalMark() {
        for (int i = 0; i < studentList.size(); i++) {
            Double sum = 0.0;
            for (int k = 0; k < project.getStudentInfo().get(studentList.get(i)).getMark().getMarkList().size(); k++) {

                sum = sum + project.getStudentInfo().get(studentList.get(i)).getMark().getMarkList().get(k) *( 100.0/totalWeighting);

                project.getStudentInfo().get(studentList.get(i)).getMark().setTotalMark(sum);
            }
            Log.d("11111119", String.valueOf(sum));

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
                    btn_assessment_start.setBackgroundResource(R.drawable.ic_pause);
                    flag = 1;
                    btn_assessment_refresh.setEnabled(false);
                    break;
                }else if(flag == 1){
                    if (!isPause) {
                        isPause = true;
                        countDownTimer.cancel();

                    }

                    btn_assessment_start.setBackgroundResource(R.drawable.ic_start);
                    flag = 2;
                    btn_assessment_refresh.setEnabled(true);
                    break;

                }else{
                    if (leftTime != 0 && isPause) {
                        //将上次当前剩余时间作为新的时长                        initTimer(leftTime);
                        countDownTimer.start();
                        isPause = false;

                    }
                    btn_assessment_refresh.setEnabled(false);
                    btn_assessment_start.setBackgroundResource(R.drawable.ic_pause);

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

            Button btn_comment_only_comment = convertView.findViewById(R.id.btn_comment_only_comment);
            btn_comment_only_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Activity_Assessment.this, Activity_assessment_comment.class);
                    intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
                    intent.putExtra("indexOfCriteria",String.valueOf(-999));
                    intent.putExtra("indexOfComment",String.valueOf(position));

                    startActivity(intent);
                }
            });

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

            TextView tv_other_comment = convertView.findViewById(R.id.tv_other_comment);
            tv_other_comment.setText("For " + project.getStudentInfo().get(studentList.get(position)).getFirstName() + " "
                    + project.getStudentInfo().get(studentList.get(position)).getMiddleName() + " "
                    + project.getStudentInfo().get(studentList.get(position)).getSurname());
            Button btn_assessment_save = convertView.findViewById(R.id.btn_assessment_save);
            et_other_comment = convertView.findViewById(R.id.et_other_comment);

            if(project.getStudentInfo().get(studentList.get(position)).getMark() != null){
                et_other_comment.setText(project.getStudentInfo().get(studentList.get(position)).getMark().getComment());

            }
            final View view4 = convertView;
            btn_assessment_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    et_other_comment = view4.findViewById(R.id.et_other_comment);


                    String otherComment = et_other_comment.getText().toString();

                    project.getStudentInfo().get(studentList.get(position)).getMark().setComment(otherComment);


                }
            });



            return convertView;
        }
    }



    public void finish_assessment(View view){

        if(checkAllCriteria()){
            addSubsectionToMarkObject();

            for(int i = 0; i < studentList.size(); i++){
        //            criteriaArrayList_reduce(project.getStudentInfo().get(i).getMark());
                project.getStudentInfo().get(studentList.get(i)).setTotalMark(project.getStudentInfo().get(studentList.get(i)).getMark().getTotalMark());
                AllFunctions.getObject().sendMark(project, project.getStudentInfo().get(studentList.get(i)).getNumber(), project.getStudentInfo().get(studentList.get(i)).getMark() );
            }

            Intent intent = new Intent(Activity_Assessment.this, Activity_Reaper_Mark.class);
            intent.putExtra("indexOfProject", String.valueOf(indexOfProject));
            intent.putExtra("indexOfStudent", String.valueOf(indexOfStudent));
            intent.putExtra("indexOfGroup", String.valueOf(indexOfGroup));
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "You have one or more comments not selected", Toast.LENGTH_SHORT).show();
        }

    }





    public void back_assessment(View view){
        finish();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

//    private void criteriaArrayList_reduce(Mark mark_before)
//    {
//        ArrayList<Criteria> criteria_before = mark_before.getCriteriaList();
//        ArrayList<Criteria> criteria_project = project.getCriteria();
//        String longTexts[][] = new String[criteria_before.size()][20];
//        int matrix[][] = new int[criteria_before.size()][20];
//
//
//        for(int i=0; i<criteria_before.size(); i++)
//        {
//            for(int q=0; q<20; q++)
//                matrix[i][q] = -999;
//
//            //criteria layer
//            for(int j=0; j<criteria_before.get(i).getSubsectionList().size(); j++)
//            {
//                //subsection layer
//                OUT_ShortTextLayer:
//                for(int k=0; k<criteria_before.get(i).getSubsectionList().get(j).getShortTextList().size(); k++)
//                {
//                    //shortText layer
//                    if(criteria_before.get(i).getSubsectionList().get(j).getShortTextList().get(k).getLongtext().size() == 1)
//                    {
//                        longTexts[i][j] = criteria_before.get(i).getSubsectionList().get(j).getShortTextList().get(k).getLongtext().get(0);
//                        matrix[i][j] = k;
//                        break OUT_ShortTextLayer;
//                    }
//                }
//            }
//        }
//        for(int i=0; i<mark_before.getCriteriaList().size(); i++)
//        {
//            mark_before.getCriteriaList().get(i).getSubsectionList().clear();
//        }
//
//        for(int i=0; i<mark_before.getCriteriaList().size(); i++)
//        {
//            //criteria layer
//            OUT_SubsectionLayer:
//            for(int j=0; j<criteria_project.get(i).getSubsectionList().size(); j++)
//            {
//                if(matrix[i][j] == -999)
//                    ;
//                else
//                {
//                    SubSection subSection = new SubSection();
//                    subSection.setName(criteria_project.get(i).getSubsectionList().get(j).getName());
//                    ShortText shortText = new ShortText();
//                    shortText.setName(criteria_project.get(i).getSubsectionList().get(j).getShortTextList().get(matrix[i][j]).getName());
//                    shortText.getLongtext().add(longTexts[i][j]);
//                    subSection.getShortTextList().add(shortText);
//                    mark_before.getCriteriaList().get(i).getSubsectionList().add(subSection);
//                }
//            }
//
//        }
//        System.out.println("接下来是Mark的信息：");
//        for(int i=0; i<mark_before.getCriteriaList().size(); i++)
//        {
//            for(int j=0; j<mark_before.getCriteriaList().get(i).getSubsectionList().size(); j++)
//                System.out.println("第"+i+"个Criteria的第"+j+"个Subsection的名字是"
//                        +mark_before.getCriteriaList().get(i).getSubsectionList().get(j).getName()+"\n"+
//                        "选择的shortText是"+mark_before.getCriteriaList().get(i).getSubsectionList().get(j).getShortTextList().get(0).getName());
//        }
//    }

    private void initMatrix()
    {
        matrixOfMarkedCriteria = new int[project.getCriteria().size()][10];
        matrixCriteriaLongtext = new int[project.getCriteria().size()][10];
        for(int i=0; i<project.getCriteria().size(); i++)
            for(int j=0; j<10; j++)
                matrixOfMarkedCriteria[i][j] = -999;
        matrixOfCommentOnly = new int[project.getCommentList().size()][10];
        matrixCommentLongText = new int[project.getCommentList().size()][10];
        for(int i=0; i<project.getCommentList().size(); i++)
            for(int j=0; j<10; j++)
                matrixOfCommentOnly[i][j] = -999;
    }

    static public void saveCommentToMatrixCriteria(int criteriaIndex, int subsectionIndex, int shortIndex, int longIndex)
    {
        matrixOfMarkedCriteria[criteriaIndex][subsectionIndex] = shortIndex;
        matrixCriteriaLongtext[criteriaIndex][subsectionIndex] = longIndex;
    }

    static public void saveCommentToMatrixCommentOnly(int criteriaIndex, int subsectionIndex, int shortIndex, int longIndex)
    {
        matrixOfCommentOnly[criteriaIndex][subsectionIndex] = shortIndex;
        matrixCommentLongText[criteriaIndex][subsectionIndex] = longIndex;
    }

    static public ArrayList<ArrayList<Integer>> getMatrixMarkedCriteria(int criteriaIndex)
    {
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<ArrayList<Integer>>();
        for(int i=0; i<project.getCriteria().get(criteriaIndex).getSubsectionList().size(); i++)
        {
            if(matrixOfMarkedCriteria[criteriaIndex][i] != -999)
            {
                ArrayList<Integer> arrayList_ls = new ArrayList<Integer>();
                arrayList_ls.add(i);
                arrayList_ls.add(matrixOfMarkedCriteria[criteriaIndex][i]);
                arrayList_ls.add(matrixCriteriaLongtext[criteriaIndex][i]);
                arrayLists.add(arrayList_ls);
            }

        }
        return arrayLists;
    }

    static public ArrayList<ArrayList<Integer>> getMatrixCommentOnly(int criteriaIndex)
    {
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<ArrayList<Integer>>();
        for(int i=0; i<project.getCommentList().get(criteriaIndex).getSubsectionList().size(); i++)
        {
            if(matrixOfCommentOnly[criteriaIndex][i] != -999)
            {
                ArrayList<Integer> arrayList_ls = new ArrayList<Integer>();
                arrayList_ls.add(i);
                arrayList_ls.add(matrixOfCommentOnly[criteriaIndex][i]);
                arrayList_ls.add(matrixCommentLongText[criteriaIndex][i]);
                arrayLists.add(arrayList_ls);
            }

        }
        return arrayLists;
    }

    static public boolean markedCriteriaSelectedAll(int criteriaIndex)
    {
        for(int i=0; i<project.getCriteria().get(criteriaIndex).getSubsectionList().size();i++)
        {
            if(matrixOfMarkedCriteria[criteriaIndex][i]==-999)
                return false;
        }
        return true;
    }

    static public boolean commentOnlySelectedAll(int criteriaIndex)
    {
        for(int i=0; i<project.getCommentList().get(criteriaIndex).getSubsectionList().size();i++)
        {
            if(matrixOfCommentOnly[criteriaIndex][i]==-999)
                return false;
        }
        return true;
    }

    private void addSubsectionToMarkObject()
    {
        for(int i=0; i<studentList.size(); i++)
        {
            ArrayList<Criteria> criteriaArrayList = project.getStudentInfo().get(studentList.get(i)).getMark().getCriteriaList();
            for(int j=0; j<criteriaArrayList.size(); j++)
            {
                for(int k=0; k<project.getCriteria().get(j).getSubsectionList().size(); k++)
                {
                    String longText_ls = project.getCriteria().get(j).getSubsectionList().get(k).getShortTextList().get(matrixOfMarkedCriteria[j][k]).getLongtext().get(matrixCriteriaLongtext[j][k]);
                    ShortText shortText_ls = new ShortText();
                    shortText_ls.setName(project.getCriteria().get(j).getSubsectionList().get(k).getShortTextList().get(matrixOfMarkedCriteria[j][k]).getName());
                    shortText_ls.setGrade(project.getCriteria().get(j).getSubsectionList().get(k).getShortTextList().get(matrixOfMarkedCriteria[j][k]).getGrade());
                    shortText_ls.getLongtext().add(longText_ls);
                    SubSection subSection_ls = new SubSection();
                    subSection_ls.setName(project.getCriteria().get(j).getSubsectionList().get(k).getName());
                    subSection_ls.getShortTextList().add(shortText_ls);
                    criteriaArrayList.get(j).getSubsectionList().add(subSection_ls);
                }
            }

            ArrayList<Criteria> commentOnlyList = project.getStudentInfo().get(studentList.get(i)).getMark().getCommentList();
            for(int j=0; j<commentOnlyList.size(); j++)
            {
                for(int k=0; k<project.getCommentList().get(j).getSubsectionList().size(); k++)
                {
                    String longText_ls = project.getCommentList().get(j).getSubsectionList().get(k).getShortTextList().get(matrixOfCommentOnly[j][k]).getLongtext().get(matrixCommentLongText[j][k]);
                    ShortText shortText_ls = new ShortText();
                    shortText_ls.setName(project.getCommentList().get(j).getSubsectionList().get(k).getShortTextList().get(matrixOfCommentOnly[j][k]).getName());
                    shortText_ls.setGrade(project.getCommentList().get(j).getSubsectionList().get(k).getShortTextList().get(matrixOfCommentOnly[j][k]).getGrade());

                    shortText_ls.getLongtext().add(longText_ls);
                    SubSection subSection_ls = new SubSection();
                    subSection_ls.setName(project.getCommentList().get(j).getSubsectionList().get(k).getName());
                    subSection_ls.getShortTextList().add(shortText_ls);
                    commentOnlyList.get(j).getSubsectionList().add(subSection_ls);
                }
            }
        }
    }

    private boolean checkAllCriteria()
    {
        for(int i=0; i<project.getCriteria().size(); i++)
        {
            if(matrixOfMarkedCriteria[i][0] == -999)
                return false;
        }
        for (int i=0; i<project.getCommentList().size(); i++)
        {
            if(matrixOfCommentOnly[i][0] == -999)
                return false;
        }
        return true;
    }

    private void MarkObjectToMatrix(Mark mark)
    {
        initMatrix();
        for(int i=0; i<mark.getCriteriaList().size(); i++)
        {
            //criteria layer
            for(int j=0; j<project.getCriteria().get(i).getSubsectionList().size(); j++)
            {
                //subsection layer
                OUT:
                for(int k=0; k<project.getCriteria().get(i).getSubsectionList().get(j).getShortTextList().size(); k++)
                {
                    //shortText layer
                    if(project.getCriteria().get(i).getSubsectionList().get(j).getShortTextList().get(k).getName().
                            equals(mark.getCriteriaList().get(i).getSubsectionList().get(j).getShortTextList().get(0).getName()))
                    {
                        for(int p=0; p<project.getCriteria().get(i).getSubsectionList().get(j).getShortTextList().get(k).getLongtext().size(); p++)
                        {
                            if(project.getCriteria().get(i).getSubsectionList().get(j).getShortTextList().get(k).getLongtext().get(p).
                                    equals(mark.getCriteriaList().get(i).getSubsectionList().get(j).getShortTextList().get(0).getLongtext().get(0)))
                            {
                                matrixOfMarkedCriteria[i][j] = k;
                                matrixCriteriaLongtext[i][j] = p;
                                break OUT;
                            }
                        }
                    }
                }
            }
        }

        for(int i=0; i<mark.getCommentList().size(); i++)
        {
            //criteria layer
            for(int j=0; j<project.getCommentList().get(i).getSubsectionList().size(); j++)
            {
                //subsection layer
                OUT:
                for(int k=0; k<project.getCommentList().get(i).getSubsectionList().get(j).getShortTextList().size(); k++)
                {
                    //shortText layer
                    if(project.getCommentList().get(i).getSubsectionList().get(j).getShortTextList().get(k).getName().
                            equals(mark.getCommentList().get(i).getSubsectionList().get(j).getShortTextList().get(0).getName()))
                    {
                        for(int p=0; p<project.getCommentList().get(i).getSubsectionList().get(j).getShortTextList().get(k).getLongtext().size(); p++)
                        {
                            if(project.getCommentList().get(i).getSubsectionList().get(j).getShortTextList().get(k).getLongtext().get(p).
                                    equals(mark.getCommentList().get(i).getSubsectionList().get(j).getShortTextList().get(0).getLongtext().get(0)))
                            {
                                matrixOfCommentOnly[i][j] = k;
                                matrixCommentLongText[i][j] = p;
                                break OUT;
                            }
                        }
                    }
                }
            }
        }

    }


}
