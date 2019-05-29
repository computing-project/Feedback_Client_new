/*
 * AUTHOR：Yolanda.
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved.
 *
 */
package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.feedback.R;

import java.util.ArrayList;
import java.util.List;

import bean.ThreeBean;

/**
 * <p>三级目录的适配器。</p>
 * Created in 2016/3/22 18:06.
 *
 * @author Yolanda;
 */
public class ThreeAdapter extends BaseAdapter {

    private Context mContext;
    private OnThreeItemClickListener itemClickListener;
    private List<ThreeBean> mThreeBeans = new ArrayList<>();
    /**
     * 一级id
     */
    private int groupId = -1;
    /**
     * 二级id
     */
    private int chilcId = -1;

    public ThreeAdapter(Context context, OnThreeItemClickListener itemClickListener) {

        this.mContext = context;
        this.itemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(List<ThreeBean> threeBeans, int groupId, int chilcId) {
        this.groupId = groupId;
        this.chilcId = chilcId;
        mThreeBeans.clear();
        if (threeBeans != null) {
            mThreeBeans.addAll(threeBeans);
        }
        super.notifyDataSetChanged();
    }

    /**
     * 返回第一级选中的Item的Position，当没有选中时返回-1。
     *
     * @return Position。
     */
    public int getOneItemSelect() {
        return groupId;
    }

    /**
     * 返回第二级选中的Item的Position，当没有选中时返回-1。
     *
     * @return Position。
     */
    public int getTwoItemSelect() {
        return chilcId;
    }

    /**
     * 返回第三级选中的Item集合。
     *
     * @return {@code List<Three>}。
     */
    public List<ThreeBean> getThreeSelect() {
        List<ThreeBean> threeBeans = new ArrayList<>();
        for (ThreeBean threeBean : mThreeBeans) {
            if (threeBean.isChecked())
                threeBeans.add(threeBean);
        }
        return threeBeans;
    }



    public interface OnThreeItemClickListener {
        void onClick(int childId);
    }

    @Override
    public int getCount() {
        return mThreeBeans.size();
    }

    @Override
    public ThreeBean getItem(int position) {
        return mThreeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ThreeViewHolder threeViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_three_title, parent, false);
            threeViewHolder = new ThreeViewHolder(convertView);
            convertView.setTag(threeViewHolder);
        } else
            threeViewHolder = (ThreeViewHolder) convertView.getTag();
        threeViewHolder.setPosition(position);
        return convertView;
    }

    /**
     * 一级的holder。
     */
    class ThreeViewHolder implements View.OnClickListener {
        private TextView mTvTitle;

        private int position;

        private ThreeViewHolder(View view) {
            view.setOnClickListener(this);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title_three);
        }

        /**
         * 设置Item的数据。
         *
         * @param position 第几个Item。
         */
        public void setPosition(int position) {
            this.position = position;
            ThreeBean threeBean = getItem(position);
            mTvTitle.setText(threeBean.getTitle());
            mTvTitle.setSelected(threeBean.isChecked());
        }

        @Override
        public void onClick(View v) {
            // 点击Item的时候选中或者反选当前Item，这里没有让其它item反选，说明就是多选

            for (ThreeBean threeBean : mThreeBeans) {
                threeBean.setChecked(false);
            getItem(position).setChecked(true);
            notifyDataSetChanged();
        }

            if (itemClickListener != null)
                itemClickListener.onClick(position);
    }
}
}
