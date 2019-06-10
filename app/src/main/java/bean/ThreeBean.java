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


public class ThreeBean implements Checkable {

    private String title;


    private boolean isChecked;

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
