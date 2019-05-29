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

/**
 * <p>二级Item的Bean。</p>
 * Created in 2016/3/22 18:16.
 *
 * @author Yolanda;
 */
public class TwoBean implements Checkable {

    /**
     * 第二级Item显示的文字。
     */
    private String title;

    /**
     * 第二级是否被选中。
     */
    private boolean isChecked;



    public TwoBean() {
    }

    public TwoBean(Boolean checked, String title) {
        isChecked = checked;
        this.title = title;
    }


    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
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
}
