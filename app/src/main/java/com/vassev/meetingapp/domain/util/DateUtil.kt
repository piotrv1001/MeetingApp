package com.vassev.meetingapp.domain.util

import android.icu.text.DateFormatSymbols

class DateUtil {

    companion object {

        fun getMonthName(month: Int): String {
            return DateFormatSymbols().getMonths()[month - 1]
        }

        fun getFormattedPlan(fromHour: Int, fromMinute: Int, toHour: Int, toMinute: Int): String {
            var fromMinuteFormatted = fromMinute.toString()
            if(fromMinute < 10) {
                fromMinuteFormatted = "0$fromMinute"
            }
            var toMinuteFormatted = toMinute.toString()
            if(toMinute < 10) {
                toMinuteFormatted = "0$toMinute"
            }
            return "$fromHour:$fromMinuteFormatted - $toHour:$toMinuteFormatted"
        }
    }

}