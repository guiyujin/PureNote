package com.guiyujin.purenote.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

public class ThingsReminder {
    private static String calenderEventURL = null;
    static {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            calenderEventURL = "content://com.android.calendar/events";
        } else {
            calenderEventURL = "content://calendar/events";
        }
    }

    public static void OpenCalendar(Context context, String content) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Uri.parse(calenderEventURL))
                .putExtra("beginTime", System.currentTimeMillis())
                .putExtra("endTime", System.currentTimeMillis() + 24*60*60*1000)
                .putExtra("title", content)
                .putExtra("description", content);
        context.startActivity(intent);
    }
}
