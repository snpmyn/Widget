package example.screen.kit;

import com.zsp.library.screen.kit.ScreenDataKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019/6/12.
 *
 * @author 郑少鹏
 * @desc ScreenActivityKit
 */
public class ScreenActivityKit {
    /**
     * 结果
     */
    public Object sexResult;
    public List<Object> ageGroupResultList;
    public List<Object> consumptionCycleResultList;
    public Object numberOfConsumptionResult;
    /**
     * ScreenDataKit
     */
    private ScreenDataKit screenDataKit;

    /**
     * constructor
     */
    public ScreenActivityKit() {
        this.ageGroupResultList = new ArrayList<>();
        this.consumptionCycleResultList = new ArrayList<>();
        this.screenDataKit = new ScreenDataKit();
    }

    /**
     * 筛选分发
     *
     * @param classification 类别
     * @param condition      条件
     * @param selected       选否
     */
    public void screeningDistribution(String classification, String condition, boolean selected) {
        switch (classification) {
            case "性别":
                sexScreen(condition, selected);
                break;
            case "年龄段":
                ageGroupScreen(condition, selected);
                break;
            case "消费周期":
                consumptionCycleScreen(condition, selected);
                break;
            case "消费次数":
                numberOfConsumptionScreen(condition, selected);
                break;
            default:
                break;
        }
    }

    /**
     * 性别筛选
     *
     * @param condition 条件
     * @param selected  选否
     */
    private void sexScreen(String condition, boolean selected) {
        switch (condition) {
            case "男":
                sexResult = selected ? "male" : null;
                break;
            case "女":
                sexResult = selected ? "female" : null;
                break;
            default:
                break;
        }
    }

    /**
     * 年龄段筛选
     *
     * @param condition 条件
     * @param selected  选否
     */
    private void ageGroupScreen(String condition, boolean selected) {
        switch (condition) {
            case "18岁以下":
                screenDataKit.singleSelectPack(ageGroupResultList, selected, 0, 18);
                break;
            case "18～40岁":
                screenDataKit.singleSelectPack(ageGroupResultList, selected, 18, 40);
                break;
            case "40～60岁":
                screenDataKit.singleSelectPack(ageGroupResultList, selected, 40, 60);
                break;
            case "60岁以上":
                screenDataKit.singleSelectPack(ageGroupResultList, selected, 60, 100);
                break;
            default:
                break;
        }
    }

    /**
     * 消费周期筛选
     *
     * @param condition 条件
     * @param selected  选否
     */
    private void consumptionCycleScreen(String condition, boolean selected) {
        switch (condition) {
            case "1个月":
                screenDataKit.multiSelectPack(consumptionCycleResultList, selected, 1);
                break;
            case "3个月":
                screenDataKit.multiSelectPack(consumptionCycleResultList, selected, 3);
                break;
            case "6个月":
                screenDataKit.multiSelectPack(consumptionCycleResultList, selected, 6);
                break;
            default:
                break;
        }
    }

    /**
     * 消费次数筛选
     *
     * @param condition 条件
     * @param selected  选否
     */
    private void numberOfConsumptionScreen(String condition, boolean selected) {
        switch (condition) {
            case "1次":
                numberOfConsumptionResult = selected ? 1 : null;
                break;
            case "2次":
                numberOfConsumptionResult = selected ? 2 : null;
                break;
            case "3次":
                numberOfConsumptionResult = selected ? 3 : null;
                break;
            default:
                break;
        }
    }

    /**
     * 重置
     */
    public void resetting() {
        sexResult = null;
        if (ageGroupResultList.size() > 0) {
            ageGroupResultList.clear();
        }
        if (consumptionCycleResultList.size() > 0) {
            consumptionCycleResultList.clear();
        }
        numberOfConsumptionResult = null;
    }
}
