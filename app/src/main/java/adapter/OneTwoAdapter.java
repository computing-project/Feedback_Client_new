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
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.feedback.R;


import java.util.ArrayList;
import java.util.List;

import bean.OneBean;
import bean.TwoBean;


public class OneTwoAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private OnTwoItemClickListener itemClickListener;

    private List<OneBean> oneBeans = new ArrayList<>();

    public OneTwoAdapter(Context context, OnTwoItemClickListener itemClickListener) {
        this.mContext = context;
        this.itemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(List<OneBean> oneBeans) {
        this.oneBeans.clear();
        if (oneBeans != null)
            this.oneBeans.addAll(oneBeans);
        super.notifyDataSetChanged();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        OneViewHolder oneViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_one_title, parent, false);
            oneViewHolder = new OneViewHolder(convertView);
            convertView.setTag(oneViewHolder);
        } else
            oneViewHolder = (OneViewHolder) convertView.getTag();
        oneViewHolder.setPosition(groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TwoViewHolder twoViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_two_title, parent, false);
            twoViewHolder = new TwoViewHolder(convertView);
            convertView.setTag(twoViewHolder);
        } else
            twoViewHolder = (TwoViewHolder) convertView.getTag();
        twoViewHolder.setPosition(groupPosition, childPosition);
        return convertView;
    }


    class OneViewHolder {
        private TextView mTvTitle;

        private OneViewHolder(View view) {
            mTvTitle = (TextView) view.findViewById(R.id.tv_title_one);
        }

        public void setPosition(int position) {
            OneBean oneBean = getGroup(position);
            mTvTitle.setText(oneBean.getTitle());
        }
    }


    class TwoViewHolder implements View.OnClickListener {
        private TextView mTvTitle;

        private int position;

        private int childPosition;

        private TwoViewHolder(View view) {
            view.setOnClickListener(this);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title_two);
        }

        public void setPosition(int position, int childPosition) {
            this.position = position;
            this.childPosition = childPosition;
            TwoBean twoBean = getChild(position, childPosition);
            mTvTitle.setText(twoBean.getTitle());
            mTvTitle.setSelected(twoBean.isChecked());
        }

        @Override
        public void onClick(View v) {
            {
                // single select

                List<TwoBean> twoBeans = getGroup(position).getOperation();
                for (TwoBean twoBean : twoBeans)
                    twoBean.setChecked(false);

                getChild(position, childPosition).setChecked(true);
                notifyDataSetChanged();
            }
            // refresh
            if (itemClickListener != null)
                itemClickListener.onClick(position, childPosition);
        }
    }

    public interface OnTwoItemClickListener {
        void onClick(int groupId, int childId);
    }

    @Override
    public int getGroupCount() {
        return oneBeans.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return oneBeans.get(groupPosition).getOperation().size();
    }

    @Override
    public OneBean getGroup(int groupPosition) {
        return oneBeans.get(groupPosition);
    }

    @Override
    public TwoBean getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).getOperation().get(childPosition);
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
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
