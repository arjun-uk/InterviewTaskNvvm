package com.education.interviewtask.ui.utils


import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object AppUtils {
    fun getStringValueFromJson(jsonObj: JSONObject?, key: String?): String? {
        var value: String? = null
        if (jsonObj != null && key != null) {
            try {
                value = if (jsonObj.has(key)) jsonObj.getString(key).trim { it <= ' ' } else ""
                if ("false".equals(value.trim { it <= ' ' }, ignoreCase = true)) {
                    value = ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
                value = ""
            }
        }
        return value
    }


    fun getIntValueFromJson(jsonObj: JSONObject?, key: String?): Int {
        var value = 0
        val str: String?
        if (jsonObj != null && key != null) {
            try {
                str = if (jsonObj.has(key)) jsonObj.getString(key) else null
                if (str != null && !"false".equals(str, ignoreCase = true)) {
                    value = (str + "").toInt()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return value
    }
    fun getDoubleValueFromJson(jsonObj: JSONObject?, key: String?): Double {
        var value = 0.0
        val str: String?
        if (jsonObj != null && key != null) {
            try {
                str = if (jsonObj.has(key)) jsonObj.getString(key) else null
                if (str != null && !"false".equals(str, ignoreCase = true)) {
                    value = (str + "").toDouble()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return value
    }
    fun getFloatValueFromJson(jsonObj: JSONObject?, key: String?): Float {
        var value = 0.0f
        val str: String?
        if (jsonObj != null && key != null) {
            try {
                str = if (jsonObj.has(key)) jsonObj.getString(key) else null
                if (str != null && !"false".equals(str, ignoreCase = true)) {
                    value = (str + "").toFloat()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return value
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatIsoDate(isoDate: String, format: String = "dd/MM/yyyy"): String {
        val dateTime = OffsetDateTime.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern(format)
        return dateTime.format(formatter)
    }

}