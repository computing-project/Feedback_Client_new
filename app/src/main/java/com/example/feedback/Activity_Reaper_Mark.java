package com.example.feedback;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_Reaper_Mark extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__reaper__mark);
    }

    private void init()
    {

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

            ListView listView_gridCriteria = convertView.findViewById(R.id.listView_criteriaMark_gridItemMark);



            Button button_viewReport = convertView.findViewById(R.id.button_viewReport_gridItemMark);
            button_viewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_criteria_andmark, parent, false);

            TextView textView_markWithTotalMark = convertView.findViewById(R.id.textView_markTotalMark_listItemCriteriaMark);
            textView_markWithTotalMark.setText(markItem.getMarkList().get(position)+"/"+
                    markItem.getCriteriaList().get(position).getMaximunMark());
            TextView textView_criteriaName = convertView.findViewById(R.id.textView_criteriaName_listItemCriteriaMark);
            textView_criteriaName.setText(markItem.getCriteriaList().get(position).getName());
            
            return convertView;
        }
    }
}
