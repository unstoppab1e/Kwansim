package com.kr.kwansim.common

import android.content.Context
import android.content.SharedPreferences

/**
 * Class : SharedPreferences
 * Created by 빙창선 on 2019-09-17.
 */

class SharedPreferences(context: Context) {
    private val mShared: SharedPreferences? = context.getSharedPreferences("Shared", 0)

    //사원번호 저장할 변수
    var settingAlert : String?
        get() = mShared!!.getString("SETTING_ALERT", "")
        set(value) = mShared!!.edit().putString("SETTING_ALERT", value).apply()

}