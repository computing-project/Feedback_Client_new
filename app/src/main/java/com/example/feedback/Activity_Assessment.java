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
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_Assessment extends AppCompatActivity implements View.OnClickListener {

    MyAdapter myAdapter;
    int indexOfProject;
    ProjectInfo project;
    ArrayList<Criteria> criteriaList;

    ListView lv_individual;
    TextView tv_time;
    Button btn_start;
    Button btn_refresh;

    SeekBar test_sb;
    TextView test_text;

    private long time = 60000;
    private boolean isPause = false;
    private CountDownTimer countDownTimer;
    private long leftTime = 0;
    private int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        Intent intent =getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("index"));
        project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        lv_individual = (ListView) findViewById(R.id.lv_individual);
        init();


        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_time.setText(String.format("%02d", time/1000/60) +":"+String.format("%02d", time/1000%60));

        btn_start = findViewById(R.id.btn_start);
        btn_refresh = findViewById(R.id.btn_refresh);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_refresh).setOnClickListener(this);

        btn_start.setEnabled(true);
        btn_refresh.setEnabled(false);
        initTimer(time);

    }

    public void init()
    {

        criteriaList = project.getCriteria();

        myAdapter = new MyAdapter(criteriaList, this);

        lv_individual.setAdapter(myAdapter);

    }

    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Criteria> criteriaList;

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

            final TextView tv_red = findViewById(R.id.tv_red);
            final TextView tv_yellow = findViewById(R.id.tv_yellow);
            final TextView tv_green = findViewById(R.id.tv_green);
            //Button btn_color = findViewById(R.id.btn_color);
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

            bindViews();

            return convertView;
        }

    }



    private void bindViews() {
        test_sb = (SeekBar) findViewById(R.id.sb_mark);
        test_text = (TextView) findViewById(R.id.tv_mark);
        test_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                test_text.setText(progress + " / 100");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void initTimer(long millisUntilFinished){

        btn_start.setEnabled(true);

        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
            public void onTick(long millisUntilFinished) {
                leftTime = millisUntilFinished;
                if(leftTime < 10000){
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
            case R.id.btn_start:
                if(flag == 0){
                    isPause = false;
                    countDownTimer.start();
                    btn_start.setText("PAUSE");
                    flag = 1;
                    btn_refresh.setEnabled(false);
                    break;
                }else if(flag == 1){
                    if (!isPause) {
                        isPause = true;
                        countDownTimer.cancel();

                    }

                    btn_start.setText("START");
                    flag = 2;
                    btn_refresh.setEnabled(true);
                    break;

                }else{
                    if (leftTime != 0 && isPause) {
                        //将上次当前剩余时间作为新的时长
                        initTimer(leftTime);
                        countDownTimer.start();
                        isPause = false;

                    }
                    btn_refresh.setEnabled(false);
                    btn_start.setText("PAUSE");
                    flag = 1;
                    break;
                }

            case R.id.btn_refresh:
                countDownTimer.cancel();
                btn_refresh.setEnabled(false);
                tv_time.setText(String.format("%02d", time/1000/60) +":"+String.format("%02d", time/1000%60));
                initTimer(time);
                flag = 0;
                break;
            default:
                break;
        }
    }



}
