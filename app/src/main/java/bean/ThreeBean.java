/*
 * AUTHOR：Yolanda.
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved.
 *
 */
package bean;

import android.widget.Checkable;

import java.util.List;

/**
 * <p>三级Item的bean。</p>
 * Created in 2016/3/23 9:03.
 *
 * @author Yolanda;
 */
public class ThreeBean implements Checkable {

    /**
     * 三级的Item的文字。
     */
    private String title;

    /**
     * 是否选中。
     */
    private boolean isChecked;
    /**
     * 在List中的位置
     */
    private int index;

    private List<ThreeBean> operation;


    public ThreeBean() {
    }

    public ThreeBean(boolean checked, String title, int index) {
        isChecked = checked;
        this.title = title;
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<ThreeBean> getOperation() {
        return operation;
    }


    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        isChecked = !isChecked;
    }

    @Override
    public String toString() {
        return Integer.toString(index);
    }
}
