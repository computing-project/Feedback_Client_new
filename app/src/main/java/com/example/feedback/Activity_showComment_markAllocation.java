package com.example.feedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_showComment_markAllocation extends Activity {
    private Criteria criteria;
    private ListView listView_longText;
    private MyAdapterForLongText myAdapterForLongText;
    private int indexOfSubsection = -999;
    private int indexOfShortText = -999;
    private int indexOfProject;
    private int indexOfCriteria;
    private Spinner spinner_shortText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment_markallocation);

        Intent intent = getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("indexOfProject"));
        indexOfCriteria = Integer.parseInt(intent.getStringExtra("indexOfCriteria"));
        ProjectInfo project = AllFunctions.getObject().getProjectList().get(indexOfProject);

        if(indexOfCriteria >= project.getCriteria().size())
            criteria = project.getCommentList().get(indexOfCriteria-project.getCriteria().size());
        else
            criteria = project.getCriteria().get(indexOfCriteria);
        init();

    }

    private void init()
    {
        ExpandableListView expandableListView_comments_left = findViewById(R.id.expandableListView_showComment);
        MyAdapterForCommentLeft myAdapterForCommentLeft = new MyAdapterForCommentLeft(this,criteria.getSubsectionList());
        expandableListView_comments_left.setAdapter(myAdapterForCommentLeft);
        expandableListView_comments_left.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for(int i=0; i<criteria.getSubsectionList().size(); i++)
                {
                    if(i != groupPosition && expandableListView_comments_left.isGroupExpanded(groupPosition))
                        expandableListView_comments_left.collapseGroup(i);
                }
            }
        });
    }

    public void edit_longText_showComment(View view)
    {
        if(listView_longText.getCheckedItemPosition() == -1)
            Toast.makeText(this, "Please choose a comment.",Toast.LENGTH_SHORT).show();
        else
        {
            LayoutInflater layoutInflater = LayoutInflater.from(Activity_showComment_markAllocation.this);//获得layoutInflater对象
            View view2 = layoutInflater.from(Activity_showComment_markAllocation.this).inflate(R.layout.dialog_edit_comment, null);//获得view对象

            final EditText editText_editLongText = (EditText) view2.findViewById(R.id.editText_editLongText_editComment);//获取控件
            editText_editLongText.setText(criteria.getSubsectionList().get(indexOfSubsection).getShortTextList().get(indexOfShortText).getLongtext().get(listView_longText.getCheckedItemPosition()));

            Dialog dialog = new AlertDialog.Builder(Activity_showComment_markAllocation.this).setTitle("Edit Comment").setView(view2).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String newLongText = editText_editLongText.getText().toString();
                    criteria.getSubsectionList().get(indexOfSubsection).getShortTextList().get(indexOfShortText).getLongtext().set(listView_longText.getCheckedItemPosition(),newLongText);
                    myAdapterForLongText.notifyDataSetChanged();
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

    public void back_showComment(View view)
    {finish();}

    public void delete_showComment(View view)
    {
        if(listView_longText.getCheckedItemPosition() == -1)
            Toast.makeText(this, "Please choose a comment.",Toast.LENGTH_SHORT).show();
        else {
            criteria.getSubsectionList().get(indexOfSubsection).getShortTextList().get(indexOfShortText).getLongtext().remove(listView_longText.getCheckedItemPosition());
            myAdapterForLongText.notifyDataSetChanged();
        }
    }

        public void add_showComment(View view)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(Activity_showComment_markAllocation.this);//获得layoutInflater对象
            final View view2 = layoutInflater.from(Activity_showComment_markAllocation.this).inflate(R.layout.dialog_add_comment, null);//获得view对象

            final Spinner spinner_subsection = view2.findViewById(R.id.spinner_subsection_addComment);
            MyAdapterForSubsectionSpinner myAdapterForSubsectionSpinner = new MyAdapterForSubsectionSpinner(view2.getContext(),criteria.getSubsectionList());
            spinner_subsection.setAdapter(myAdapterForSubsectionSpinner);
            spinner_subsection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position>=criteria.getSubsectionList().size())
                    {   spinner_shortText = view2.findViewById(R.id.spinner_shortText_addComment);
                        //MyAdapterForShortTextSpinner myAdapterForShortTextSpinner = new MyAdapterForShortTextSpinner(view2.getContext(), null);
                        spinner_shortText.setAdapter(null);}
                    else {
                        spinner_shortText = view2.findViewById(R.id.spinner_shortText_addComment);
                        MyAdapterForShortTextSpinner myAdapterForShortTextSpinner = new MyAdapterForShortTextSpinner(view2.getContext(), criteria.getSubsectionList().get(position).getShortTextList());
                        spinner_shortText.setAdapter(myAdapterForShortTextSpinner);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    return;
                }
            });

            Dialog dialog = new AlertDialog.Builder(Activity_showComment_markAllocation.this).setTitle("Add Comment").setView(view2).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    int whichSubsection = spinner_subsection.getSelectedItemPosition();
                    if(whichSubsection<criteria.getSubsectionList().size())
                    {
                        int whichShortText = spinner_shortText.getSelectedItemPosition();
                        if(whichShortText < criteria.getSubsectionList().get(whichSubsection).getShortTextList().size())
                        {
                            EditText editText_longText = view2.findViewById(R.id.editText_longText_addComment);
                            String longText = editText_longText.getText().toString();
                            criteria.getSubsectionList().get(whichSubsection).getShortTextList().get(whichShortText).getLongtext().add(longText);
                            init();
                        }
                        else
                        {
                            EditText editText_shortText = view2.findViewById(R.id.editText_shortText_addComment);
                            String shortText = editText_shortText.getText().toString();
                            EditText editText_shortTextGrade = view2.findViewById(R.id.editText_shortTextgrade_addComment);
                            String shortTextGrade = editText_shortTextGrade.getText().toString();
                            EditText editText_longText = view2.findViewById(R.id.editText_longText_addComment);
                            String longText = editText_longText.getText().toString();
                            ShortText shortText_ls = new ShortText();
                            shortText_ls.setName(shortText);
                            shortText_ls.setGrade(Integer.parseInt(shortTextGrade));
                            shortText_ls.getLongtext().add(longText);
                            criteria.getSubsectionList().get(whichSubsection).getShortTextList().add(shortText_ls);
                            init();
                        }
                    }
                    else
                    {
                        EditText editText_otherSubsection = view2.findViewById(R.id.editText_otherSubsection_addComment);
                        String subsectionName = editText_otherSubsection.getText().toString();
                        EditText editText_shortText = view2.findViewById(R.id.editText_shortText_addComment);
                        String shortText = editText_shortText.getText().toString();
                        EditText editText_shortTextGrade = view2.findViewById(R.id.editText_shortTextgrade_addComment);
                        String shortTextGrade = editText_shortTextGrade.getText().toString();
                        EditText editText_longText = view2.findViewById(R.id.editText_longText_addComment);
                        String longText = editText_longText.getText().toString();
                        ShortText shortText_ls = new ShortText();
                        shortText_ls.setName(shortText);
                        shortText_ls.setGrade(Integer.parseInt(shortTextGrade));
                        shortText_ls.getLongtext().add(longText);
                        SubSection subSection_ls = new SubSection();
                        subSection_ls.setName(subsectionName);
                        subSection_ls.getShortTextList().add(shortText_ls);
                        criteria.getSubsectionList().add(subSection_ls);
                        init();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            }).create();
            dialog.show();
        }

    class MyAdapterForCommentLeft extends BaseExpandableListAdapter
    {
        private Context mContext;
        private ArrayList<SubSection> subSections;
        public MyAdapterForCommentLeft(Context context, ArrayList<SubSection> subSections)
        {
            this.mContext = context;
            this.subSections = subSections;
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
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_subsection_showcomment, null);
            TextView textView_subsection = convertView.findViewById(R.id.textView_subsection_showComment);
            textView_subsection.setText(criteria.getSubsectionList().get(groupPosition).getName());
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_shorttext_showcomments, null);
            TextView textView_shortText = convertView.findViewById(R.id.textView_shortText_showComments);
            textView_shortText.setText(criteria.getSubsectionList().get(groupPosition).getShortTextList().get(childPosition).getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myAdapterForLongText = new MyAdapterForLongText(Activity_showComment_markAllocation.this,
                            subSections.get(groupPosition).getShortTextList().get(childPosition).getLongtext());
                    listView_longText = findViewById(R.id.listView_longText_showComment);
                    listView_longText.setAdapter(myAdapterForLongText);
                    indexOfSubsection = groupPosition;
                    indexOfShortText = childPosition;

                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class MyAdapterForLongText extends BaseAdapter
    {
        private Context mContext;
        private ArrayList<String> longTexts;

        public MyAdapterForLongText(Context context, ArrayList<String> longTexts)
        {
            this.mContext = context;
            this.longTexts = longTexts;
        }

        @Override
        public int getCount() {
            return longTexts.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_longtext_showcomments, null);
            TextView textView_longText = convertView.findViewById(R.id.textView_longText_showComments);
            textView_longText.setText(longTexts.get(position));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listView_longText.isItemChecked(position))
                        listView_longText.setItemChecked(position,false);
                    else
                        listView_longText.setItemChecked(position,true);
                }
            });
            if(listView_longText.isItemChecked(position))
                convertView.setBackgroundColor(Color.GRAY);
            else
                convertView.setBackgroundColor(Color.TRANSPARENT);
            return convertView;
        }
    }

    class MyAdapterForSubsectionSpinner extends BaseAdapter
    {
        private Context mContext;
        private ArrayList<SubSection> subSections;

        public MyAdapterForSubsectionSpinner(Context context, ArrayList<SubSection> subSections)
        {
            this.mContext = context;
            this.subSections = subSections;
        }

        @Override
        public int getCount() {
            return subSections.size()+1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_spinner, null);
            if(position<subSections.size()) {
                TextView textView = convertView.findViewById(R.id.textView_content_spinner);
                textView.setText(subSections.get(position).getName());
            }
            else
            {
                TextView textView = convertView.findViewById(R.id.textView_content_spinner);
                textView.setText("Others");
            }
            return convertView;
        }
    }


    class MyAdapterForShortTextSpinner extends BaseAdapter
    {
        private Context mContext;
        private ArrayList<ShortText> shortTexts;

        public MyAdapterForShortTextSpinner(Context context, ArrayList<ShortText> shortTexts)
        {
            this.mContext = context;
            this.shortTexts = shortTexts;
        }

        @Override
        public int getCount() {
            return shortTexts.size()+1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_spinner, null);
            if(position<shortTexts.size()) {
                TextView textView = convertView.findViewById(R.id.textView_content_spinner);
                textView.setText(shortTexts.get(position).getName());
            }
            else
            {
                TextView textView = convertView.findViewById(R.id.textView_content_spinner);
                textView.setText("Others");
            }
            return convertView;
        }
    }

}
