package com.example.feedback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.util.ArrayList;

public class Activity_MarkAllocation extends Activity {
    private int indexOfProject;
    private GridView gridView;
    //private ListView listView;
    private ProjectInfo project;
    private ArrayList<Criteria> markedCriteriaList;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__mark_allocation);

        Intent intent =getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("index"));

        init();
    }


    public void init()
    {
        project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        markedCriteriaList = project.getCriteria();
        System.out.println("检查criteriaList是否传入："+markedCriteriaList.size());

        MyAdapter myAdapter = new MyAdapter(markedCriteriaList, this);
        gridView = findViewById(R.id.gridView_CriteriaList_markAllocation);
        gridView.setAdapter(myAdapter);
      //  listView = findViewById(R.id.listView_criteriaList_markAllocation);
      //  listView.setAdapter(myAdapter);
    }

    //button 'save'.
    public void save_markAllocation(View view)
    {
        AllFunctions.getObject().projectCriteria(project, project.getCriteria(), project.getCommentList());
        Intent intent = new Intent(this, Assessment_Preparation_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //button 'back'.
    public void back_markAllocation(View view)

    {
//        Intent intent = new Intent(this, Activity_CriteriaList.class);
//        intent.putExtra("index", String.valueOf(indexOfProject));
//        startActivity(intent);
        finish();
    }

    //button 'next'.
    public void next_markAllocation(View view)
    {
        AllFunctions.getObject().projectCriteria(project, project.getCriteria(), project.getCommentList());
        Intent intent = new Intent(this, Activity_Student_Group.class);
        intent.putExtra("index", String.valueOf(indexOfProject));
        startActivity(intent);
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
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_markallocation, parent, false);

            TextView textView_criteriaName = convertView.findViewById(R.id.textView_criteriaName_gridItem);
            textView_criteriaName.setText(markedCriteriaList.get(position).getName());
            EditText editText_maxMark = convertView.findViewById(R.id.editText_maxMark_gridItem);
            editText_maxMark.setText(String.valueOf(criteriaList.get(position).getMaximunMark()));
            String markIncrement = criteriaList.get(position).getMarkIncrement();
            if(markIncrement != null)
            switch (markIncrement)
            {
//                case "quarter":
                case "1/4":
                    RadioButton radioButton_quarter = convertView.findViewById(R.id.radioButton_quarter_gridItem);
                    radioButton_quarter.setChecked(true);
                    break;
//                case "half":
                case "1/2":
                    RadioButton radioButton_half = convertView.findViewById(R.id.radioButton_half_gridItem);
                    radioButton_half.setChecked(true);
                    break;
//                case "full":
                case "1":
                    RadioButton radioButton_full = convertView.findViewById(R.id.radioButton_full_gridItem);
                    radioButton_full.setChecked(true);
                    break;
                default:
                    break;
            }


            RadioGroup radioGroup = convertView.findViewById(R.id.radioGroup_markIncrement_gridItem);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup rG, int checkID) {
                    switch(checkID)
                    {
                        case R.id.radioButton_quarter_gridItem:
//                            criteriaList.get(position).setMarkIncrement("quarter");
                            criteriaList.get(position).setMarkIncrement("1/4");
                            break;
                        case R.id.radioButton_half_gridItem:
//                            criteriaList.get(position).setMarkIncrement("half");
                            criteriaList.get(position).setMarkIncrement("1/2");
                            break;
                        case R.id.radioButton_full_gridItem:
//                            criteriaList.get(position).setMarkIncrement("full");
                            criteriaList.get(position).setMarkIncrement("1");
                            break;
                        default:
                            break;
                    }
                }
            });

            Button button_plus = convertView.findViewById(R.id.button_plus_gridItem);
            button_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int mark = Integer.parseInt(editText_maxMark.getText().toString());
                    markedCriteriaList.get(position).setMaximunMark(mark+1);
                    editText_maxMark.setText(String.valueOf(mark+1));
                }
            });

            Button button_minus = convertView.findViewById(R.id.button_minus_gridItem);
            button_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int mark = Integer.parseInt(editText_maxMark.getText().toString());
                    markedCriteriaList.get(position).setMaximunMark(mark-1);
                    editText_maxMark.setText(String.valueOf(mark-1));
                }
            });

            Button button_commentDetail = convertView.findViewById(R.id.button_commentsDetail_gridItem);
            button_commentDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater layoutInflater = LayoutInflater.from(Activity_MarkAllocation.this);//获得layoutInflater对象
                    final View view2 = layoutInflater.from(Activity_MarkAllocation.this).inflate(R.layout.dialog_showcommentcriterialayer_markallocation, null);//获得view对象
                    TextView textView_CriteriaName = view2.findViewById(R.id.textView_criteriaName_showComment);
                    textView_CriteriaName.setText(markedCriteriaList.get(position).getName());
                    ArrayList<SubSection> subSectionsList = markedCriteriaList.get(position).getSubsectionList();
                    expandableListView = view2.findViewById(R.id.expandablelistView_dialogShowCommentCriteriaLayer);


                   MyBaseExpandableListAdapter myBaseExpandableListAdapter = new MyBaseExpandableListAdapter(subSectionsList, view2.getContext());
                    expandableListView.setAdapter(myBaseExpandableListAdapter);
                    Dialog dialog = new android.app.AlertDialog.Builder(Activity_MarkAllocation.this).setView(view2).create();

                    dialog.show();
                }
            });

            return convertView;
        }
    }






    public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

        private ArrayList<SubSection> subSections;
        private Context mContext;

        public MyBaseExpandableListAdapter(ArrayList<SubSection> subSections, Context mContext) {
            this.subSections = subSections;
            this.mContext = mContext;
        }

        @Override
        public int getGroupCount() {
            return subSections.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return subSections.get(groupPosition).getShortTextList().size();
        }

        @Override
        public SubSection getGroup(int groupPosition) {
            return subSections.get(groupPosition);
        }

        @Override
        public ShortText getChild(int groupPosition, int childPosition) {
            return subSections.get(groupPosition).getShortTextList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            ViewHolderGroup groupHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.dialog_showcomment_subsectionlayer_markallocation, parent, false);
                groupHolder = new ViewHolderGroup();
                groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.textView_subsectionName_showCommentSubsectionLayer);
                convertView.setTag(groupHolder);
            }else{
                groupHolder = (ViewHolderGroup) convertView.getTag();
            }
            groupHolder.tv_group_name.setText(subSections.get(groupPosition).getName());
            return convertView;
        }

        //取得显示给定分组给定子位置的数据用的视图
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolderItem itemHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.dialog_showcomment_longtextlayer_markallocation, parent, false);
                itemHolder = new ViewHolderItem();
                itemHolder.tv_name = (TextView) convertView.findViewById(R.id.textView_longText_showCommentLongtextLayer);
                convertView.setTag(itemHolder);
            }else{
                itemHolder = (ViewHolderItem) convertView.getTag();
            }
            itemHolder.tv_name.setText(subSections.get(groupPosition).getShortTextList().get(childPosition).getName());
            return convertView;
        }

        //设置子列表是否可选中
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


        private class ViewHolderGroup{
            private TextView tv_group_name;
        }

        private class ViewHolderItem{
            private TextView tv_name;
        }

    }



//    public class MyExpandableListAdapter2 extends BaseExpandableListAdapter {
//
//        private ArrayList<ShortText> shortTexts;
//        private Context mContext;
//
//        public MyExpandableListAdapter2(ArrayList<ShortText> shortTexts, Context mContext) {
//            this.shortTexts = shortTexts;
//            this.mContext = mContext;
//        }
//
//        @Override
//        public int getGroupCount() {
//            return shortTexts.size();
//        }
//
//        @Override
//        public int getChildrenCount(int groupPosition) {
//            return shortTexts.get(groupPosition).getLongtext().size();
//        }
//
//        @Override
//        public ShortText getGroup(int groupPosition) {
//            return shortTexts.get(groupPosition);
//        }
//
//        @Override
//        public String getChild(int groupPosition, int childPosition) {
//            return shortTexts.get(groupPosition).getLongtext().get(childPosition);
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//
//            ViewHolderGroup groupHolder;
//            if(convertView == null){
//                convertView = LayoutInflater.from(mContext).inflate(
//                        R.layout.dialog_showcomment_subsectionlayer_markallocation, parent, false);
//                groupHolder = new ViewHolderGroup();
//                groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.textView_subsectionName_showCommentSubsectionLayer);
//                convertView.setTag(groupHolder);
//            }else{
//                groupHolder = (ViewHolderGroup) convertView.getTag();
//            }
//            groupHolder.tv_group_name.setText("子类第"+groupPosition);
//            return convertView;
//        }
//
//        //取得显示给定分组给定子位置的数据用的视图
//        @Override
//        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//            ViewHolderItem itemHolder;
//            if(convertView == null){
//                convertView = LayoutInflater.from(mContext).inflate(
//                        R.layout.dialog_showcomment_longtextlayer_markallocation, parent, false);
//                itemHolder = new ViewHolderItem();
//                itemHolder.tv_name = (TextView) convertView.findViewById(R.id.textView_longText_showCommentLongtextLayer);
//                convertView.setTag(itemHolder);
//            }else{
//                itemHolder = (ViewHolderItem) convertView.getTag();
//            }
//            itemHolder.tv_name.setText(shortTexts.get(groupPosition).getLongtext().get(childPosition));
//            return convertView;
//        }
//
//        //设置子列表是否可选中
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            return true;
//        }
//
//
//        private class ViewHolderGroup{
//            private TextView tv_group_name;
//        }
//
//        private class ViewHolderItem{
//            private TextView tv_name;
//        }

    }





















//    public class MyAdapter_ForShowSubSection_CriteriaLayer extends BaseAdapter
//    {
//        private Context mContext;
//        private ArrayList<SubSection> subSections;
//
//        public MyAdapter_ForShowSubSection_CriteriaLayer(ArrayList<SubSection> subSections, Context context) {
//            this.subSections = subSections;
//            this.mContext = context;
//        }
//
//
//        @Override
//        public int getCount() {
//            return subSections.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return i;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup viewGroup) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.dialog_showcomment_subsectionlayer_markallocation, viewGroup, false);
//            TextView textView_subsectionName = view.findViewById(R.id.textView_SubsectionName_showCommentSubsectionLayer);
//            textView_subsectionName.setText(subSections.get(position).getName());
//
//            ArrayList<ShortText> shortTextList = subSections.get(position).getShortTextList();
//            MyAdapter_ForShowShortText_SubsectionLayer showShortText = new MyAdapter_ForShowShortText_SubsectionLayer(shortTextList,viewGroup.getContext());
//            ListView listView_shortText = view.findViewById(R.id.listView_ShortText_showCommentSubsectionLayer);
//            listView_shortText.setAdapter(showShortText);
//
//            return view;
//        }
//    }
//
//
//
//    public class MyAdapter_ForShowShortText_SubsectionLayer extends BaseAdapter
//    {
//        private Context mContext;
//        private ArrayList<ShortText> shortTexts;
//
//        public MyAdapter_ForShowShortText_SubsectionLayer(ArrayList<ShortText> shortTexts, Context context) {
//            this.shortTexts = shortTexts;
//            this.mContext = context;
//        }
//
//
//        @Override
//        public int getCount() {
//            return shortTexts.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return i;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup viewGroup) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.dialog_showcomment_shorttextlayer_markallocation, viewGroup, false);
//            TextView textView_shortTextName = view.findViewById(R.id.textView_shortTextName_showCommentShortTextLayer);
//            textView_shortTextName.setText(shortTexts.get(position).getName());
//            return view;
//        }
//    }
//
//
//

