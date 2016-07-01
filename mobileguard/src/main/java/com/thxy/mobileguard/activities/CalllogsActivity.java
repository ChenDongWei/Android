package com.thxy.mobileguard.activities;

import com.thxy.mobileguard.domain.ContantBean;
import com.thxy.mobileguard.engine.ReadContantsEngine;

import java.util.List;

/**
 * 显示所有好友信息的界面
 */
public class CalllogsActivity extends BaseFriendsCallSmsActivity {

    /**
     *提取数据的方法，需要覆盖此方法完成数据的显示
     * @return
     */
    @Override
    public List<ContantBean> getDatas() {
        return ReadContantsEngine.readCalllog(getApplicationContext());
    }
}
