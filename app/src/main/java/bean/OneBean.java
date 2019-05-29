/*
 * AUTHOR：Yolanda.
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved.
 *
 */
package bean;

import java.util.List;

/**
 * <p>一级Item的Bean。</p>
 * Created in 2016/3/22 17:38.
 *
 * @author Yolanda;
 */
public class OneBean {

    /**
     * 第一级Item显示的文字
     */
    private String title;

    /**
     * 第一级标题对应的第二级内容
     */
    private List<TwoBean> operation;

    public OneBean() {
    }

    public OneBean(List<TwoBean> operation, String title) {
        this.operation = operation;
        this.title = title;
    }

    public List<TwoBean> getOperation() {
        return operation;
    }

    public void setOperation(List<TwoBean> operation) {
        this.operation = operation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
