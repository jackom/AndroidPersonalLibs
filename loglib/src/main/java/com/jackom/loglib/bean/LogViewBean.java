package com.jackom.loglib.bean;

import com.jackom.loglib.LogPriority;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author：jackom
 * @date：5/2/21 on 5:44 PM
 * @desc：
 */
public class LogViewBean {
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public String title;
    public String contents;
    public @LogPriority.Priority int priority;

    public LogViewBean(@LogPriority.Priority int priority, String title, String contents) {
        this.priority = priority;
        this.title = title;
        this.contents = contents;
    }

    public String getFormatMsg(@LogPriority.Priority int priority, String title, String contents) {
        return formatDate(System.currentTimeMillis()) + " | " + priority + " | " + title + " | " + contents;
    }

    private String formatDate(long time) {
        return sDateFormat.format(time);
    }

}
