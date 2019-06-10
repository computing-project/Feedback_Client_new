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


public class ThreeAdapter extends BaseAdapter {

    private Context mContext;
    private OnThreeItemClickListener itemClickListener;
    private List<ThreeBean> mThreeBeans = new ArrayList<>();

    private int groupId = -1;

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


    public int getOneItemSelect() {
        return groupId;
    }


    public int getTwoItemSelect() {
        return chilcId;
    }


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


    class ThreeViewHolder implements View.OnClickListener {
        private TextView mTvTitle;

        private int position;

        private ThreeViewHolder(View view) {
            view.setOnClickListener(this);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title_three);
        }


        public void setPosition(int position) {
            this.position = position;
            ThreeBean threeBean = getItem(position);
            mTvTitle.setText(threeBean.getTitle());
            mTvTitle.setSelected(threeBean.isChecked());
        }

        @Override
        public void onClick(View v) {

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
