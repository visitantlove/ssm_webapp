package com.xiaoli.web.utils;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;

/**
 * 自定义输出源
 *
 * 实现log4j将不同级别日志输出到不同文件
 */
public class MyAppender extends DailyRollingFileAppender {
    @Override
    public boolean isAsSevereAsThreshold(Priority priority) {
        return this.threshold.equals(priority);
    }
}
