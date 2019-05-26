package com.example.feedback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import showcomments.ChildEntity;
import showcomments.ParentAdapter;
import showcomments.ParentEntity;

public class Activity_MarkAllocation extends Activity {
    private int indexOfProject;
    private GridView gridView;
    //private ListView listView;
    private ProjectInfo project;
    private ArrayList<Criteria> markedCriteriaList;
    ArrayList<Criteria> allCriteriaList;
    private int markedCriteriaNum;
    private ExpandableListView expandableListView;
    private ArrayList<ParentEntity> parents;
    private ExpandableListView eList;

    private ParentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__mark_allocation);

        Intent intent = getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("index"));

        init();
    }


    public void init() {
        project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        markedCriteriaList = project.getCriteria();
        markedCriteriaNum = project.getCriteria().size();
        allCriteriaList = new ArrayList<>();
        allCriteriaList.addAll(markedCriteriaList);
        allCriteriaList.addAll(project.getCommentList());

        MyAdapter myAdapter = new MyAdapter(allCriteriaList, this);
        gridView = findViewById(R.id.gridView_CriteriaList_markAllocation);
        gridView.setAdapter(myAdapter);
        //  listView = findViewById(R.id.listView_criteriaList_markAllocation);
        //  listView.setAdapter(myAdapter);
        TextView textView_projectName = findViewById(R.id.textView_projectName_markAllocation);
        textView_projectName.setText(project.getProjectName());
        TextView textView_helloUser = findViewById(R.id.textView_helloUser_markAllocation);
        textView_helloUser.setText("Hello, " + AllFunctions.getObject().getUsername());
        TextView textView_logout = findViewById(R.id.textView_logout_markAllocation);
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_MarkAllocation.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    //button 'save'.
    public void save_markAllocation(View view) {
        AllFunctions.getObject().projectCriteria(project, project.getCriteria(), project.getCommentList());
        Intent intent = new Intent(this, Assessment_Preparation_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //button 'back'.
    public void back_markAllocation(View view) {
//        Intent intent = new Intent(this, Activity_CriteriaList.class);
//        intent.putExtra("index", String.valueOf(indexOfProject));
//        startActivity(intent);
        finish();
    }

    //button 'next'.
    public void next_markAllocation(View view) {
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
            if (position < markedCriteriaNum) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_markallocation, parent, false);

                TextView textView_criteriaName = convertView.findViewById(R.id.textView_criteriaName_gridItem);
                textView_criteriaName.setText(criteriaList.get(position).getName());
                EditText editText_maxMark = convertView.findViewById(R.id.editText_maxMark_gridItem);
                editText_maxMark.setText(String.valueOf(criteriaList.get(position).getMaximunMark()));
                String markIncrement = criteriaList.get(position).getMarkIncrement();
                if (markIncrement != null)
                    switch (markIncrement) {
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
                        switch (checkID) {
                            case R.id.radioButton_quarter_gridItem:
//                            criteriaList.get(position).setMarkIncrement("quarter");
                                markedCriteriaList.get(position).setMarkIncrement("1/4");
                                break;
                            case R.id.radioButton_half_gridItem:
//                            criteriaList.get(position).setMarkIncrement("half");
                                markedCriteriaList.get(position).setMarkIncrement("1/2");
                                break;
                            case R.id.radioButton_full_gridItem:
//                            criteriaList.get(position).setMarkIncrement("full");
                                markedCriteriaList.get(position).setMarkIncrement("1");
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
                        markedCriteriaList.get(position).setMaximunMark(mark + 1);
                        editText_maxMark.setText(String.valueOf(mark + 1));
                    }
                });

                Button button_minus = convertView.findViewById(R.id.button_minus_gridItem);
                button_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int mark = Integer.parseInt(editText_maxMark.getText().toString());
                        markedCriteriaList.get(position).setMaximunMark(mark - 1);
                        editText_maxMark.setText(String.valueOf(mark - 1));
                    }
                });


                Button button_commentDetail = convertView.findViewById(R.id.button_commentsDetail_gridItem);
                button_commentDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater layoutInflater = LayoutInflater.from(Activity_MarkAllocation.this);//获得layoutInflater对象
                        final View view2 = layoutInflater.from(Activity_MarkAllocation.this).inflate(R.layout.dialog_showcomments_markallocation, null);

                        loadData(position);

                        eList = (ExpandableListView) view2.findViewById(R.id.expandable_showComments_markAllocation);

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
                                Toast.makeText(
                                        mContext,
                                        "点击的下标为： parentPosition=" + parentPosition
                                                + "   groupPosition=" + groupPosition
                                                + "   childPosition=" + childPosition + "\n点击的是："
                                                + childName, Toast.LENGTH_SHORT).show();
                            }
                        });

                        Dialog dialog = new android.app.AlertDialog.Builder(Activity_MarkAllocation.this).setView(view2)
                                .create();

                        dialog.show();


                    }
                });
            }
            else
            {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_commentonly, parent, false);

                TextView textView_criteriaName = convertView.findViewById(R.id.textView_criteriaName_gridItemCommentOnly);
                textView_criteriaName.setText(criteriaList.get(position).getName());

                Button button_commentDetail = convertView.findViewById(R.id.button_showComments_gridItemCommentOnly);
                button_commentDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater layoutInflater = LayoutInflater.from(Activity_MarkAllocation.this);//获得layoutInflater对象
                        final View view2 = layoutInflater.from(Activity_MarkAllocation.this).inflate(R.layout.dialog_showcomments_markallocation, null);

                        loadData(position);

                        eList = (ExpandableListView) view2.findViewById(R.id.expandable_showComments_markAllocation);

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
                                Toast.makeText(
                                        mContext,
                                        "点击的下标为： parentPosition=" + parentPosition
                                                + "   groupPosition=" + groupPosition
                                                + "   childPosition=" + childPosition + "\n点击的是："
                                                + childName, Toast.LENGTH_SHORT).show();


//                                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());//获得layoutInflater对象
//                                final View view3 = layoutInflater.from(view.getContext()).inflate(R.layout.dialog_edit_and_delete_comment, null);//获得view对象
//                                EditText editText_longtextDetail = (EditText) view3.findViewById(R.id.editText_longTextDetail_markAllocation);//获取控件
//                                editText_longtextDetail.setText(criteriaList.get(position).getSubsectionList().get(parentPosition).getShortTextList().get(groupPosition).getLongtext().get(childPosition));
//                                Dialog dialog3 = new AlertDialog.Builder(view.getContext()).setView(view3).setPositiveButton("Edit", new DialogInterface.OnClickListener() {
//
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        criteriaList.get(position).getSubsectionList().get(parentPosition).getShortTextList().get(groupPosition)
//                                                .getLongtext().set(childPosition,editText_longtextDetail.getText().toString());
//                                       // adapter.notifyDataSetChanged();
//                                    }
//                                }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        criteriaList.get(position).getSubsectionList().get(parentPosition).getShortTextList().get(groupPosition)
//                                                .getLongtext().remove(childPosition);
//                                    //    adapter.notifyDataSetChanged();
//
//                                    }
//                                }).create();
//                                dialog3.show();
                            }
                        });

                        Dialog dialog = new android.app.AlertDialog.Builder(Activity_MarkAllocation.this).setView(view2).create();

                        dialog.show();
                        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);



                    }
                });
            }
            return convertView;
        }
    }

    private void loadData(int indexOfCriteria) {

        parents = new ArrayList<ParentEntity>();
        Criteria criteria = allCriteriaList.get(indexOfCriteria);

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

}
