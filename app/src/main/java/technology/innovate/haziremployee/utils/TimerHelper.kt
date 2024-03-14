package technology.innovate.haziremployee.utils

import android.net.ParseException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimerHelper {
    public var totalHours = 0;

    fun findTime(savedTime : String,savedHours : String) : String{
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val nowTime = sdf.format(Date())

        val currentHourDifference: String = findDifference(savedTime, nowTime)

        val h1 = currentHourDifference.split(":").toTypedArray()
        val h2 = savedHours.split(":").toTypedArray()

        val endValues: String = addTwoHours(h1, h2)

        return endValues
    }

    private fun addTwoHours(h1: Array<String>, h2: Array<String>): String {
        val hour1 = h1[0].toInt()
        val minute1 = h1[1].toInt()
        val second1 = h1[2].toInt()
        val hour2 = h2[0].toInt()
        val minute2 = h2[1].toInt()
        val second2 = h2[2].toInt()
        var endHours = hour1 + hour2
        var minutes = minute1 + minute2
        var second = second1 + second2
        if (second >= 60) {
            val v = second / 60
            val r = second % 60
            second = r
            minutes += v
        }
        if (minutes >= 60) {
            val v = minutes / 60
            val r = minutes % 60
            minutes = r
            endHours += v
        }
        val endHourS: String
        val endMinuteS: String
        val endSecondS: String
        endHourS = if (endHours < 10) {
            String.format("%02d", endHours)
        } else endHours.toString() + ""
        endMinuteS = if (minutes < 10) {
            String.format("%02d", minutes)
        } else minutes.toString() + ""
        endSecondS = if (second < 10) {
            String.format("%02d", second)
        } else second.toString() + ""

        return "$endHourS:$endMinuteS:$endSecondS"
    }

    private fun findDifference(start_date: String, end_date: String): String {

        try {
            val d1 = parseDate(start_date)
            val d2 = parseDate(end_date)

            // Calucalte time difference
            // in milliseconds

            val differenceInMillis = d2!!.time - d1!!.time
            val formattedText = formatElapsedTime(differenceInMillis / 1000)


            // Calucalte time difference in
            // seconds, minutes, hours, years,
            // and days

            return formattedText.toString()
        } // Catch the Exception
        catch (e: ParseException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun parseDate(strDate: String?): Date? {
        val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        var date1: Date? = null
        try {
            date1 = dateFormat.parse(strDate)
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }
        return date1
    }

    private fun formatElapsedTime(seconds: Long): String? {
        var seconds = seconds
        val hours: Long = TimeUnit.SECONDS.toHours(seconds)
        seconds -= TimeUnit.HOURS.toSeconds(hours)
        val minutes: Long = TimeUnit.SECONDS.toMinutes(seconds)
        seconds -= TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%d:%d:%d", hours, minutes, seconds)
    }


}