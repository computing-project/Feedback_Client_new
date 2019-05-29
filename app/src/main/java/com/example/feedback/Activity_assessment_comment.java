package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.OneTwoAdapter;
import adapter.ThreeAdapter;
import bean.OneBean;
import bean.ThreeBean;
import bean.TwoBean;

public class Activity_assessment_comment extends Activity {

    private Criteria criteria;

    private int indexOfProject;
    private int indexOfCriteria;
    private int indexOfComment;

    /**
     * 第三级适配器
     */
    private ThreeAdapter threeListAdapter;
    /**
     * 第三级数据
     */
    private List<ThreeBean> threeBeans;


    static int subsectionIndex;
    static int shortTextIndex;
    static int longTextIndex;

    ArrayList<ArrayList<Integer>> savedIndexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_comment);

        Intent intent = getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("indexOfProject"));
        indexOfCriteria = Integer.parseInt(intent.getStringExtra("indexOfCriteria"));
        indexOfComment = Integer.parseInt(intent.getStringExtra("indexOfComment"));
        ProjectInfo project = AllFunctions.getObject().getProjectList().get(indexOfProject);

        if(indexOfCriteria == -999){
            criteria = project.getCommentList().get(indexOfComment);
            savedIndexList = Activity_Assessment.getMatrixCommentOnly(indexOfComment);

        }else{
            criteria = project.getCriteria().get(indexOfCriteria);
            savedIndexList = Activity_Assessment.getMatrixMarkedCriteria(indexOfCriteria);

        }

        if(savedIndexList.size() == 0){
            System.out.println("zero");
        }else{
            for(int i = 0; i < savedIndexList.size(); i ++){
                for(int j = 0; j < savedIndexList.get(i).size(); j ++){
                    System.out.print(savedIndexList.get(i).get(j) + " ");

                }
                System.out.println();
            }
        }


        // 第三级
        ListView listView = (ListView) findViewById(R.id.lv_main);
        threeListAdapter = new ThreeAdapter(this, onThreeItemClickListener);
        listView.setAdapter(threeListAdapter);


        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.elv_main);
        OneTwoAdapter expandCheckAdapter = new OneTwoAdapter(this, onTwoItemClickListener);
        expandableListView.setAdapter(expandCheckAdapter);
        // 第一二级
        List<OneBean> oneBeans = new ArrayList<>();
        for (int i = 0; i < criteria.getSubsectionList().size(); i++) {
            List<TwoBean> twoBeans = new ArrayList<>();
            for (int j = 0; j < criteria.getSubsectionList().get(i).getShortTextList().size(); j++) {
                twoBeans.add(new TwoBean(false, criteria.getSubsectionList().get(i).getShortTextList().get(j).getName()));

                if(savedIndexList.size() != 0) {
                    for (int m = 0; m < savedIndexList.size(); m++) {
                        for (int n = 0; n < 3; n++){

                            if (i == savedIndexList.get(m).get(0) && j == savedIndexList.get(m).get(1)) {
                                twoBeans.get(j).setChecked(true);
                            }


                        }
                    }
                }


            }

            oneBeans.add(new OneBean(twoBeans, criteria.getSubsectionList().get(i).getName()));
        }

        // 这里刷新就数据，假设是从服务器拿来的数据
        expandCheckAdapter.notifyDataSetChanged(oneBeans);
    }

    private OneTwoAdapter.OnTwoItemClickListener onTwoItemClickListener = new OneTwoAdapter.OnTwoItemClickListener() {
        @Override
        public void onClick(int groupId, int childId) {
            if (threeBeans == null)
                threeBeans = new ArrayList<>();
            threeBeans.clear();

            // 这里模拟请求第三级的数据
            for (int i = 0; i < criteria.getSubsectionList().get(groupId).getShortTextList().get(childId).getLongtext().size(); i++) {
                threeBeans.add(new ThreeBean(false, criteria.getSubsectionList().get(groupId).getShortTextList().get(childId).getLongtext().get(i), i));

                if(savedIndexList.size() != 0) {
                    for (int m = 0; m < savedIndexList.size(); m++) {
                        for (int n = 0; n < 3; n++){

                            if (groupId == savedIndexList.get(m).get(0) && childId == savedIndexList.get(m).get(1) && i == savedIndexList.get(m).get(2)) {
                                threeBeans.get(i).setChecked(true);                            }


                        }
                    }
                }
            }
            threeListAdapter.notifyDataSetChanged(threeBeans, groupId, childId);





//            String message = "第一级选中的是第" + threeListAdapter.getOneItemSelect() + "，第二级选中的是第" + threeListAdapter.getTwoItemSelect();
//            Toast.makeText(Activity_assessment_comment.this, message, Toast.LENGTH_LONG).show();
//            // 拿到第三级选中的列表，这里可以这样拿，也可以直接从我们数据源中拿
//            List<ThreeBean> threeSelect = threeListAdapter.getThreeSelect();
//            if (threeSelect.size() > 0) {
//                String messageThree = "第三级选中了" + TextUtils.join(", ", threeSelect);
//                Toast.makeText(Activity_assessment_comment.this, messageThree, Toast.LENGTH_LONG).show();
//            }
        }
    };

    private ThreeAdapter.OnThreeItemClickListener onThreeItemClickListener = new ThreeAdapter.OnThreeItemClickListener() {
        @Override
        public void onClick(int childId) {
//            if (threeBeans == null)
//                threeBeans = new ArrayList<>();
//            threeBeans.clear();
//
//            // 这里模拟请求第三级的数据
//            for (int i = 0; i < criteriaList.get(0).getSubsectionList().get(groupId).getShortTextList().get(childId).getLongtext().size(); i++) {
//                threeBeans.add(new ThreeBean(false, criteriaList.get(0).getSubsectionList().get(groupId).getShortTextList().get(childId).getLongtext().get(i), i));
//            }
//            threeListAdapter.notifyDataSetChanged(threeBeans, groupId, childId);



            String message = "第一级选中的是第" + threeListAdapter.getOneItemSelect() + "，第二级选中的是第" + threeListAdapter.getTwoItemSelect();
            Toast.makeText(Activity_assessment_comment.this, message, Toast.LENGTH_LONG).show();
            // 拿到第三级选中的列表，这里可以这样拿，也可以直接从我们数据源中拿
            List<ThreeBean> threeSelect = threeListAdapter.getThreeSelect();
            if (threeSelect.size() > 0) {
                String messageThree = "第三级选中了" + TextUtils.join(", ", threeSelect);
                Toast.makeText(Activity_assessment_comment.this, messageThree, Toast.LENGTH_LONG).show();

                subsectionIndex = threeListAdapter.getOneItemSelect();
                shortTextIndex = threeListAdapter.getTwoItemSelect();
                longTextIndex = Integer.valueOf(TextUtils.join(", ", threeSelect));


                if(indexOfCriteria == -999){

                    Activity_Assessment.saveCommentToMatrixCommentOnly(indexOfComment, threeListAdapter.getOneItemSelect(), threeListAdapter.getTwoItemSelect(), Integer.valueOf(TextUtils.join(", ", threeSelect)));
                }else{
                    Activity_Assessment.saveCommentToMatrixCriteria(indexOfCriteria, threeListAdapter.getOneItemSelect(), threeListAdapter.getTwoItemSelect(), Integer.valueOf(TextUtils.join(", ", threeSelect)));
                }

                refreshComment();



                Log.d("10000000000", subsectionIndex +" " + shortTextIndex + " " + longTextIndex);
            }
        }

    };

   public void commentDone(View view){

       if(indexOfCriteria == -999){
            if(Activity_Assessment.commentOnlySelectedAll(indexOfComment)){
                finish();
                Intent intent = new Intent(this, Activity_Assessment.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }else{
                Toast.makeText(this, "You need to select a comment for each subsection", Toast.LENGTH_SHORT).show();
            }
       }else{
           if (Activity_Assessment.markedCriteriaSelectedAll(indexOfCriteria)) {
               finish();
               Intent intent = new Intent(this, Activity_Assessment.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
               startActivity(intent);

           }else {
               Toast.makeText(this, "You need to select a comment for each subsection", Toast.LENGTH_SHORT).show();

           }
       }




   }

   public void refreshComment(){

       if(indexOfCriteria == -999){
           savedIndexList = Activity_Assessment.getMatrixCommentOnly(indexOfComment);
       }else{
           savedIndexList = Activity_Assessment.getMatrixMarkedCriteria(indexOfCriteria);
       }
   }


}
