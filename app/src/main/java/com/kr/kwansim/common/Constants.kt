package com.kr.kwansim.common

import android.app.Activity
import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.kr.kwansim.ui.myfish.vo.BolVO
import com.kr.kwansim.ui.myfish.vo.HwansuVo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


public var user : FirebaseUser? = null
public var appAll : Activity? =null
public var fishBlow : HashMap<String , HashMap<String,String>> = HashMap<String,HashMap<String,String>>()

var hanVo : HwansuVo = HwansuVo()
var bolVo : BolVO = BolVO()

fun fishA (max : Int, size : Int, day : String, day2: String = "",type : String = "",bol : String = "" ) : String {
    val today = Calendar.getInstance()
    val sf = SimpleDateFormat("yyyy-MM-dd")
    val date = sf.parse(day)
    var date3: Date?

    if (day2 != "") {
        date3 = sf.parse(day2)
    } else {
        date3 = today.time
    }

    val calcuDate = (date3.time - date.time) / (60 * 60 * 24 * 1000)

    var a : Int = ((max/1000)/size)

    var challengeDay = (calcuDate).toInt()


    if (challengeDay == 0) {
      return "어항속에 물을 채우고 염소제거하세요"
    }

    if (challengeDay-1 == 0) {
        var t = (max/1000) / (40*20)
        return "${t}ML 박테리아제를 넣어야되요"
    }

    if (challengeDay-5 == 0) {
        return "1마리를 넣을 수 있어요"
    }

    if (type == "hwanSu") {
        var t = (max/1000) / (40*20)
        return "${t}ML 박테리아제를 넣어야되요."
    }

    if (type == "bol") {
        return "${bol}L 물을 염소제거하세요"
    }

    challengeDay = challengeDay-5

    if(challengeDay < 0) {
        return ""
    }

    if (challengeDay%3 == 0) {
        var t = ((challengeDay/3)*3)+1

        if (t > a) {
            if((t -a ) > 3) {
                return ""
            } else {
                 return "${a}마리 넣을 수 있어요"
            }
        } else {
            return "${t}마리 넣을 수 있어요"
        }

    } else {
        return  ""
    }
}