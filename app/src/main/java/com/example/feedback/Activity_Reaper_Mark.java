package com.example.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_Reaper_Mark extends AppCompatActivity {
    private int indexOfProject;
    private int indexOfStudent;
    private ArrayList<Mark> marks;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__reaper__mark);

        Intent intent = getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("indexOfProject"));
        indexOfStudent = Integer.parseInt(intent.getStringExtra("indexOfStudent"));

        handler = new Handler(){
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 301: //means getMark success
                        init();
                        break;
                    default:
                        break;
                }
            }
        };

        AllFunctions.getObject().setHandler(handler);

        AllFunctions.getObject().getMarks(AllFunctions.getObject().getProjectList().get(indexOfProject).getProjectName(),
                AllFunctions.getObject().getProjectList().get(indexOfProject).getStudentInfo().get(indexOfStudent).getNumber());
    }

    private void init()
    {
        marks = AllFunctions.getObject().getMarkListForMarkPage();
        for(int i=0; i<marks.size(); i++)
        {
            System.out.println("第"+i+"个Mark的lectureName是"+marks.get(i).getLecturerName());
            for (int j = 0; j < marks.get(i).getCriteriaList().size(); j++)
                System.out.println("第" + i + "个Mark的第" + j + "个criteria叫" + marks.get(i).getCriteriaList().get(j).getName());
        }
        MyAdapterForGridView myAdapterForGridView = new MyAdapterForGridView(marks, this);
        GridView listView_gridGroup = findViewById(R.id.listView_markItem_markPage);
        listView_gridGroup.setAdapter(myAdapterForGridView);

    }


    public class MyAdapterForGridView extends BaseAdapter {

        private Context mContext;
        private ArrayList<Mark> markList;

        public MyAdapterForGridView(ArrayList<Mark> markList, Context context) {
            this.markList = markList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return markList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_mark_markpage, parent, false);

            TextView textView_totalMark = convertView.findViewById(R.id.textView_totalMark_gridItemMark);
            textView_totalMark.setText(String.valueOf(markList.get(position).getTotalMark()));
            TextView textView_assessorName = convertView.findViewById(R.id.textView_assessorName_gridItemMark);
            textView_assessorName.setText(markList.get(position).getLecturerName());

            //System.out.println("第"+position+"个Mark里面有"+markList.get(position).getCriteriaList().size()+"个criteria");


            ListView listView_gridCriteria = convertView.findViewById(R.id.listView_criteriaMark_gridItemMark);
            MyAdapterForGridItem myAdapterForGridItem = new MyAdapterForGridItem(markList.get(position), convertView.getContext());
            listView_gridCriteria.setAdapter(myAdapterForGridItem);
            setListViewHeightBasedOnChildren(listView_gridCriteria);

            Button button_viewReport = convertView.findViewById(R.id.button_viewReport_gridItemMark);
            button_viewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Activity_Reaper_Mark.this, Activity_editable_report.class);
                    intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
                    intent.putExtra("indexOfStudent",String.valueOf(indexOfStudent));
                    startActivity(intent);
                }
            });


            return convertView;
        }
    }



    public class MyAdapterForGridItem extends BaseAdapter {

        private Context mContext;
        private Mark markItem;

        public MyAdapterForGridItem(Mark markItem, Context context) {
            this.markItem = markItem;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return markItem.getCriteriaList().size();
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
          //  System.out.println("这是第"+position+"个criteria");
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_criteria_andmark, parent, false);

            TextView textView_markWithTotalMark = convertView.findViewById(R.id.textView_markTotalMark_listItemCriteriaMark);
            textView_markWithTotalMark.setText(markItem.getMarkList().get(position)+"/"+
                    markItem.getCriteriaList().get(position).getMaximunMark());
            TextView textView_criteriaName = convertView.findViewById(R.id.textView_criteriaName_listItemCriteriaMark);
            textView_criteriaName.setText(markItem.getCriteriaList().get(position).getName());
            convertView.setEnabled(false);

            return convertView;
        }
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
