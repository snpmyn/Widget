package example.onepartylibrary.focusresize.bean;

/**
 * Created on 2019/9/5.
 *
 * @author 郑少鹏
 * @desc FocusResizeBean
 */
public class FocusResizeBean {
    private String title;
    private String subTitle;
    private int drawable;

    /**
     * constructor
     *
     * @param title    标题
     * @param subTitle 子标题
     * @param drawable 位图
     */
    public FocusResizeBean(String title, String subTitle, int drawable) {
        this.title = title;
        this.subTitle = subTitle;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getDrawable() {
        return drawable;
    }
}
