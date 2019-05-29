package com.example.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
    /**
     * 第三级适配器
     */
    private ThreeAdapter threeListAdapter;
    /**
     * 第三级数据
     */
    private List<ThreeBean> threeBeans;

    ArrayList<Criteria> criteriaList = DefaultCriteriaList.getDefaultCriteriaList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 第三级
        ListView listView = (ListView) findViewById(R.id.lv_main);
        threeListAdapter = new ThreeAdapter(this);
        listView.setAdapter(threeListAdapter);


        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.elv_main);
        OneTwoAdapter expandCheckAdapter = new OneTwoAdapter(this, onTwoItemClickListener);
        expandableListView.setAdapter(expandCheckAdapter);
        // 第一二级
        List<OneBean> oneBeans = new ArrayList<>();
        for (int i = 0; i < criteriaList.get(0).getSubsectionList().size(); i++) {
            List<TwoBean> twoBeans = new ArrayList<>();
            for (int j = 0; j < criteriaList.get(0).getSubsectionList().get(i).getShortTextList().size(); j++) {
                twoBeans.add(new TwoBean(false, criteriaList.get(0).getSubsectionList().get(i).getShortTextList().get(j).getName()));
            }
            oneBeans.add(new OneBean(twoBeans, criteriaList.get(0).getSubsectionList().get(i).getName()));
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
            for (int i = 0; i < criteriaList.get(0).getSubsectionList().get(groupId).getShortTextList().get(childId).getLongtext().size(); i++) {
                threeBeans.add(new ThreeBean(false, criteriaList.get(0).getSubsectionList().get(groupId).getShortTextList().get(childId).getLongtext().get(i), i));
            }
            threeListAdapter.notifyDataSetChanged(threeBeans, groupId, childId);
        }
    };

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.menu_sure) {

            String message = "第一级选中的是第" + threeListAdapter.getOneItemSelect() + "，第二级选中的是第" + threeListAdapter.getTwoItemSelect();
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            // 拿到第三级选中的列表，这里可以这样拿，也可以直接从我们数据源中拿
            List<ThreeBean> threeSelect = threeListAdapter.getThreeSelect();
            if (threeSelect.size() > 0) {
                String messageThree = "第三级选中了" + TextUtils.join(", ", threeSelect);
                Toast.makeText(this, messageThree, Toast.LENGTH_LONG).show();
            }
        }
        finish();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


}
