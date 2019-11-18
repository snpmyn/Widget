package com.zsp.smartrefreshlayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import static com.scwang.smartrefresh.layout.constant.RefreshState.Loading;
import static com.scwang.smartrefresh.layout.constant.RefreshState.Refreshing;

/**
 * Created on 2019/1/10.
 *
 * @author 郑少鹏
 * @desc SmartRefreshLayoutKit
 */
public class SmartRefreshLayoutKit {
    private SmartRefreshLayout smartRefreshLayout;

    /**
     * constructor
     *
     * @param smartRefreshLayout 控件
     */
    public SmartRefreshLayoutKit(SmartRefreshLayout smartRefreshLayout) {
        this.smartRefreshLayout = smartRefreshLayout;
    }

    /**
     * 配置
     */
    public void config() {
        // 刷时禁列表操作
        smartRefreshLayout.setDisableContentWhenRefresh(false);
        // 加时禁列表操作
        smartRefreshLayout.setDisableContentWhenLoading(false);
        // 内容不满一页不可上拉加
        smartRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
    }

    /**
     * 加载
     *
     * @return 加载否
     */
    public boolean load() {
        return smartRefreshLayout.getState() == Loading;
    }

    /**
     * 状变
     */
    public void stateChangeOnError() {
        switch (smartRefreshLayout.getState()) {
            case Refreshing:
                // 刷失败（不更时间）
                smartRefreshLayout.finishRefresh(false);
                break;
            case Loading:
                // 加失败
                smartRefreshLayout.finishLoadMore(false);
                break;
            default:
                break;
        }
    }

    /**
     * 状变
     *
     * @param refreshOrLoadSuccess 刷或加成否
     * @param dataSize             数据体积
     */
    public void stateChangeOnResponse(boolean refreshOrLoadSuccess, int dataSize) {
        switch (smartRefreshLayout.getState()) {
            case Refreshing:
                if (refreshOrLoadSuccess) {
                    smartRefreshLayout.finishRefresh();
                } else {
                    // 刷失败（不更时间）
                    smartRefreshLayout.finishRefresh(false);
                }
                break;
            case Loading:
                if (refreshOrLoadSuccess) {
                    if (dataSize > 0) {
                        smartRefreshLayout.finishLoadMore();
                    } else {
                        // 显全部加完并不再触发加事件
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    // 加失败
                    smartRefreshLayout.finishLoadMore(false);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 刷时集合处理
     *
     * @param lists 集合
     */
    public void collectionProcessOnRefresh(List<?>... lists) {
        if (smartRefreshLayout.getState() == Refreshing) {
            for (List<?> list : lists) {
                list.clear();
            }
        }
    }
}
