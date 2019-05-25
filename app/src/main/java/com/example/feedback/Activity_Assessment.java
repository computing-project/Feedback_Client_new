package com.example.feedback;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import showcomments.ChildEntity;
import showcomments.ParentAdapter;
import showcomments.ParentEntity;

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

    private long durationTime = 0;
    private long warningTime = 0;
    private boolean isPause = false;
    private CountDownTimer countDownTimer;
    private long leftTime = 0;
    private int flag = 0;

    private ArrayList<ParentEntity> parents;
    private ExpandableListView eList;

    private ParentAdapter adapter;


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

            for(int j = 0; j < project.getCriteria().size(); j++){
                totalWeighting = totalWeighting + project.getCriteria().get(j).getMaximunMark();

            }

            for(int k = 0; k < project.getCriteria().size(); k++){
                totalMark = totalMark + project.getStudentInfo().get(studentList.get(0)).getMark().getMarkList().get(k) *
                        (project.getCriteria().get(k).getMaximunMark() / totalWeighting);
                Log.d("1115", String.valueOf(project.getCriteria().get(k).getMaximunMark()));

            }

            tv_assessment_total_mark.setText(String.format("%.2f", project.getStudentInfo().get(0).getMark().getTotalMark()) + "%");
            Log.d("1112", String.valueOf(totalWeighting));
            Log.d("1113", String.valueOf(totalMark));

        }else{
            tv_assessment_total_mark.setText("0%");
            for(int m = 0; m < studentList.size(); m++){
                project.getStudentInfo().get(m).setMark(new Mark());
                for(int n = 0; n < project.getCriteria().size(); n++){
                    project.getStudentInfo().get(m).getMark().getCriteriaList().add(new Criteria());
                    project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).setName(project.getCriteria().get(n).getName());
                    project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).setMaximunMark(project.getCriteria().get(n).getMaximunMark());
                    project.getStudentInfo().get(m).getMark().getMarkList().add(0.0);

                    for(int l = 0; l < project.getCriteria().get(n).getSubsectionList().size(); l++){
                        project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).getSubsectionList().add(new SubSection());
                        project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).getSubsectionList().get(l).setName(project.getCriteria().get(n).getSubsectionList().get(l).getName());

                        for(int p = 0; p < project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().size(); p++){
                            project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).getSubsectionList().get(l).getShortTextList().add(new ShortText());
                            project.getStudentInfo().get(m).getMark().getCriteriaList().get(n).getSubsectionList().get(l).getShortTextList().get(p).setName(project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().get(p).getName());

                        }
                    }
                }
                for(int n = 0; n < project.getCommentList().size(); n++){
                    project.getStudentInfo().get(m).getMark().getCommentList().add(new Criteria());
                    project.getStudentInfo().get(m).getMark().getCommentList().get(n).setName(project.getCommentList().get(n).getName());

                    for(int l = 0; l < project.getCommentList().get(n).getSubsectionList().size(); l++){
                        project.getStudentInfo().get(m).getMark().getCommentList().get(n).getSubsectionList().add(new SubSection());
                        project.getStudentInfo().get(m).getMark().getCommentList().get(n).getSubsectionList().get(l).setName(project.getCommentList().get(n).getSubsectionList().get(l).getName());

                        for(int p = 0; p < project.getCommentList().get(n).getSubsectionList().get(l).getShortTextList().size(); p++){
                            project.getStudentInfo().get(m).getMark().getCommentList().get(n).getSubsectionList().get(l).getShortTextList().add(new ShortText());
                            project.getStudentInfo().get(m).getMark().getCommentList().get(n).getSubsectionList().get(l).getShortTextList().get(p).setName(project.getCommentList().get(n).getSubsectionList().get(l).getShortTextList().get(p).getName());

                        }
                    }
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
            tv_criteria_name.setText(criteriaList.get(position).getName());

            if(criteriaList.get(position).getMarkIncrement().equals("1")){
                increment = 1.0;
            }else if(criteriaList.get(position).getMarkIncrement().equals("1/2")){
                increment = 0.5;
            }else if(criteriaList.get(position).getMarkIncrement().equals("1/4")) {
                increment = 0.25;
            }

            TextView tv_red = view10.findViewById(R.id.tv_red);
            TextView tv_yellow = view10.findViewById(R.id.tv_yellow);
            TextView tv_green = view10.findViewById(R.id.tv_green);

            ArrayList<Integer> weightList = new ArrayList<>();

            weightList.add(0, 0);
            weightList.add(1, 0);
            weightList.add(2, 0);

            for(int n = 0; n < project.getStudentInfo().get(0).getMark().getCriteriaList().size(); n++){
                for(int l = 0; l < project.getStudentInfo().get(0).getMark().getCriteriaList().get(n).getSubsectionList().size(); l++){
                    for(int p = 0; p < project.getStudentInfo().get(0).getMark().getCriteriaList().get(n).getSubsectionList().get(l).getShortTextList().size(); p++) {
                        if(project.getStudentInfo().get(0).getMark().getCriteriaList().get(n).getSubsectionList().get(l).getShortTextList().get(p).getLongtext().size() == 0){
                        }else if(project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().get(p).getGrade() == 1){
                            weightList.set(0, (weightList.get(0)+1));
                        }else if(project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().get(p).getGrade() == 2){
                            weightList.set(1, (weightList.get(0)+1));
                        }else if(project.getCriteria().get(n).getSubsectionList().get(l).getShortTextList().get(p).getGrade() == 3){
                            weightList.set(2, (weightList.get(0)+1));
                        }
                    }
                }
            }
            Log.d("77777", String.valueOf(weightList.get(0)) + " " + String.valueOf(weightList.get(1)) + " " + String.valueOf(weightList.get(2)));


            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, weightList.get(0));

            tv_red.setLayoutParams(param1);

            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, weightList.get(1));

            tv_yellow.setLayoutParams(param2);

            LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, weightList.get(2));

            tv_green.setLayoutParams(param3);


            Button btn_assessment_comment = convertView.findViewById(R.id.btn_assessment_comment);
            btn_assessment_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater layoutInflater = LayoutInflater.from(Activity_Assessment.this);//获得layoutInflater对象
                    final View view9 = layoutInflater.from(Activity_Assessment.this).inflate(R.layout.dialog_showcomments_markallocation, null);

                    loadData(position);

                    eList = (ExpandableListView) view9.findViewById(R.id.expandable_showComments_markAllocation);

                    eList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                            for (int i = 0; i < parents.size(); i++) {
                                if (i != groupPosition) {
                                    eList.collapseGroup(i);
                                }
                            }
                        }
                    });

                    adapter = new ParentAdapter(mContext, parents);

                    eList.setAdapter(adapter);



                    adapter.setOnChildTreeViewClickListener(new ParentAdapter.OnChildTreeViewClickListener() {
                        @Override
                        public void onClickPosition(int parentPosition, int groupPosition, int childPosition) {
                            String childName = parents.get(parentPosition).getChilds()
                                    .get(groupPosition).getChildNames().get(childPosition)
                                    .toString();
                            for(int i = 0; i < studentList.size(); i++){

                                if(project.getStudentInfo().get(i).getMark().getCriteriaList().get(position).getSubsectionList().get(parentPosition).getShortTextList().get(groupPosition).getLongtext().size() == 0){

                                    project.getStudentInfo().get(i).getMark().getCriteriaList().get(position).getSubsectionList().get(parentPosition).getShortTextList().get(groupPosition).getLongtext().add(childName);

                                    if(project.getCriteria().get(position).getSubsectionList().get(parentPosition).getShortTextList().get(groupPosition).getGrade() == 1){
                                        weightList.set(0, (weightList.get(0)+1));

                                    }else if(project.getCriteria().get(position).getSubsectionList().get(parentPosition).getShortTextList().get(groupPosition).getGrade() == 2){
                                        weightList.set(1, (weightList.get(1)+1));
                                    }else if(project.getCriteria().get(position).getSubsectionList().get(parentPosition).getShortTextList().get(groupPosition).getGrade() == 3){
                                        weightList.set(2, (weightList.get(2)+1));
                                    }

                                }else{

                                    project.getStudentInfo().get(i).getMark().getCriteriaList().get(position).getSubsectionList().get(parentPosition).getShortTextList().get(groupPosition).getLongtext().set(0, childName);

                                }


                                Log.d("77777", String.valueOf(weightList.get(0)) + " " + String.valueOf(weightList.get(1)) + " " + String.valueOf(weightList.get(2)));



                                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                                        0, LinearLayout.LayoutParams.MATCH_PARENT, weightList.get(0));

                                tv_red.setLayoutParams(param1);

                                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                                        0, LinearLayout.LayoutParams.MATCH_PARENT, weightList.get(1));

                                tv_yellow.setLayoutParams(param2);

                                LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                                        0, LinearLayout.LayoutParams.MATCH_PARENT, weightList.get(2));

                                tv_green.setLayoutParams(param3);

                            }
                            Toast.makeText(
                                    mContext,
                                    "点击的下标为： parentPosition=" + parentPosition
                                            + "   groupPosition=" + groupPosition
                                            + "   childPosition=" + childPosition + "\n点击的是："
                                            + childName, Toast.LENGTH_SHORT).show();


                        }
                    });

                    Dialog dialog = new android.app.AlertDialog.Builder(Activity_Assessment.this).setView(view9).create();

                    dialog.show();
                    dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                }
            });




            sb_mark = (SeekBar) convertView.findViewById(R.id.sb_mark);
            tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            //tv_mark.setText("10");
            sb_mark.setMax((int)(10.0/increment));
            final View view2 = convertView;
            sb_mark.setProgress((int)(project.getStudentInfo().get(0).getMark().getMarkList().get(position)*10 / project.getCriteria().get(position).getMaximunMark()/increment));
            tv_mark.setText((project.getStudentInfo().get(0).getMark().getMarkList().get(position)*10 / project.getCriteria().get(position).getMaximunMark()) + " / 10");
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
                        Log.d("1114", String.valueOf(project.getStudentInfo().get(i).getMark().getMarkList()));

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






            return convertView;
        }

    }

    public void totalMark() {
        for (int i = 0; i < studentList.size(); i++) {
            Double sum = 0.0;
            for (int k = 0; k < project.getStudentInfo().get(i).getMark().getMarkList().size(); k++) {

                sum = sum + 100 * project.getStudentInfo().get(i).getMark().getMarkList().get(k) / totalWeighting;

                project.getStudentInfo().get(i).getMark().setTotalMark(sum);
            }


        }
    }

    private void loadData(int indexOfCriteria) {

        parents = new ArrayList<ParentEntity>();
        Criteria criteria = project.getCriteria().get(indexOfCriteria);

        for (int i = 0; i < criteria.getSubsectionList().size(); i++) {

            ParentEntity parent = new ParentEntity();

            parent.setGroupName(criteria.getSubsectionList().get(i).getName());

            parent.setGroupColor(getResources().getColor(
                    android.R.color.black));

            ArrayList<ChildEntity> childs = new ArrayList<ChildEntity>();

            for (int j = 0; j < criteria.getSubsectionList().get(i).getShortTextList().size(); j++) {

                ChildEntity child = new ChildEntity();

                child.setGroupName(criteria.getSubsectionList().get(i).getShortTextList().get(j).getName());

                child.setGroupColor(Color.parseColor("#000000"));

                ArrayList<String> childNames = new ArrayList<String>();

                ArrayList<Integer> childColors = new ArrayList<Integer>();

                for (int k = 0; k < criteria.getSubsectionList().get(i).getShortTextList().get(j).getLongtext().size(); k++) {

                    childNames.add(criteria.getSubsectionList().get(i).getShortTextList().get(j).getLongtext().get(k));

                    childColors.add(Color.parseColor("#000000"));

                }

                child.setChildNames(childNames);

                childs.add(child);

            }

            parent.setChilds(childs);

            parents.add(parent);

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
                        //将上次当前剩余时间作为新的时长
                        initTimer(leftTime);
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
            final View view4 = convertView;
            btn_assessment_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    et_other_comment = view4.findViewById(R.id.et_other_comment);


                    String otherComment = et_other_comment.getText().toString();

                    for(int i = 0; i < studentList.size(); i++){
                        project.getStudentInfo().get(i).getMark().setComment(otherComment);

                    }
                }
            });



            return convertView;
        }
    }



    public void finish_assessment(View view)
    {
        for(int i = 0; i < studentList.size(); i++){
            AllFunctions.getObject().sendMark(project, project.getStudentInfo().get(studentList.get(i)).getNumber(), project.getStudentInfo().get(i).getMark() );
        }

//        Intent intent = new Intent(this, Assessment_Preparation_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        finish();
    }


//    private void removeEmptyShortText(int student_Index_toRemove)
//    {
//        ArrayList<Criteria> criteria_ls = (ArrayList<Criteria>) project.getStudentInfo().get(student_Index_toRemove).getMark().getCriteriaList().clone();
//       ArrayList<Criteria> criterias = project.getStudentInfo().get(student_Index_toRemove).getMark().getCriteriaList();
//        for(int i=0; i<criterias.size(); i++)
//        {
//            boolean subsection_is_empty = true;
//            for(int j=0; j<criterias.get(i).getSubsectionList().size();i++)
//            {
//                boolean shortText_is_empty = true;
//                for(int k=0; k<criterias.get(i).getSubsectionList().get(j).getShortTextList().size();k++)
//                {
//                    if(criterias.get(i).getSubsectionList().get(j).getShortTextList().get(k).getLongtext().size() != 0)
//                    {
//                        shortText_is_empty = false;
//                        break;
//                    }
//                }
//                if(shortText_is_empty == true)
//                {
//                    criteria_ls.get(i).getSubsectionList().get(j).getShortTextList().remove(k);
//                }
//            }
//        }
//
//    }

    private void removeEmptyShortText(int student_Index_toRemove)
    {
        ArrayList<Criteria> criteria_ls = (ArrayList<Criteria>) project.getStudentInfo().get(student_Index_toRemove).getMark().getCriteriaList().clone();
       ArrayList<Criteria> criterias = project.getStudentInfo().get(student_Index_toRemove).getMark().getCriteriaList();
        for(int i=0; i<criterias.size(); i++)
        {
            boolean subsection_is_empty = true;
            for(int j=0; j<criterias.get(i).getSubsectionList().size();i++)
            {
                boolean shortText_is_empty = true;
                for(int k=0; k<criterias.get(i).getSubsectionList().get(j).getShortTextList().size();k++)
                {
                    if(criterias.get(i).getSubsectionList().get(j).getShortTextList().get(k).getLongtext().size() != 0)
                    {
                        shortText_is_empty = false;
                        break;
                    }
                }
                if(shortText_is_empty == true)
                {
                   // criteria_ls.get(i).getSubsectionList().get(j).getShortTextList().remove(k);
                }
            }
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



}
