package com.kr.kwansim.ui.myfish

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.kr.kwansim.R
import com.kr.kwansim.common.fishA
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.ActivityFishDetailBinding
import com.kr.kwansim.utiils.ReminderAdapter
import com.kr.kwansim.utiils.ReminderVO
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class FishDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFishDetailBinding.inflate(layoutInflater) }
    private var selectedDate = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    private val yearFormatter = DateTimeFormatter.ofPattern("YYY")
    val adapter = ReminderAdapter()
    val mDatas=mutableListOf<ReminderVO>()
    var title : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title =  intent.getStringExtra("title").toString()


        val data = fishBlow[title]
        val data2 = data!!["fishAdd"] as Map<String, String>

        binding.title.text = "${data2["fishTitle"]}"
        binding.day1.text =  "${data2["startDay"]} 등록"


        val today = Calendar.getInstance()
        val sf = SimpleDateFormat("yyyy-MM-dd")
        val date = sf.parse(data2["startDay"])
        val calcuDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)

        val challengeDay = (calcuDate+1).toInt()

        binding.day2.text =  "D+${challengeDay}일"

        binding.a1.text = Html.fromHtml(String.format(resources.getString(R.string.fish_text1),"가로&nbsp&nbsp&nbsp&nbsp","${data2["fishWidth"]} cm"))
        binding.a2.text = Html.fromHtml(String.format(resources.getString(R.string.fish_text1),"세로&nbsp&nbsp&nbsp&nbsp","${data2["fishHeight"]} cm"))
        binding.a3.text = Html.fromHtml(String.format(resources.getString(R.string.fish_text1),"물높이&nbsp&nbsp","${data2["waterHeight"]} cm"))
        binding.a4.text = Html.fromHtml(String.format(resources.getString(R.string.fish_text1),"소형어&nbsp&nbsp","${data2["fishSize"]} cm"))

        for (document in fishBlow[title]!!.entries) {
            dataAdd(document.value as Map<String, String>, document.key, "")
        }



        adapter.datalist = mDatas
        binding.mainReminderRecycler.adapter=adapter //리사이클러뷰에 어댑터 연결
        binding.mainReminderRecycler.layoutManager= LinearLayoutManager(this)

        binding.backIc.setOnClickListener{
            onBackPressed()
        }
    }



    fun dataAdd(value: Map<String, String>, key: String, now: String) {
        var keyTmp = ""
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val firstDate: LocalDate = LocalDate.parse(
            "${
                if (now != "") {
                    now
                } else {
                    LocalDate.now()
                }
            }", formatter
        )
        val secondDate: LocalDate = LocalDate.parse(value["startDay"], formatter)

        if (value["alert"].toString().contains("true")) {
            if (key == "hwanSu") {

                val k = value["fishWidth"]!!.toInt() * value["fishHeight"]!!.toInt() * value["waterHeight"]!!.toInt()
                val t = fishA(k,value["fishSize"]!!.toInt(),value["startDay"].toString(),"","hwanSu")
                if(t == ""){
                    return
                }
                if(t == ""){
                    return
                }
                keyTmp = "${value["fishTitle"].toString()}의 ${t}".toString()

                val days = dayFormatter.format(firstDate)
                var cnt = 0
                if (value["reWeak"].toString() != "") {
                    cnt = Integer.parseInt(value["reWeak"].toString())
                }
                val lastWeek = secondDate.plusWeeks(cnt.toLong())
                if (!value["reDay"].toString().contains(days)) {
                    return
                } else if (!lastWeek.isAfter(firstDate)) {
                    return
                }
            } else if (key == "fishAdd") {
                val k = value["fishWidth"]!!.toInt() * value["fishHeight"]!!.toInt() * value["waterHeight"]!!.toInt()
                val t = fishA(k,value["fishSize"]!!.toInt(),value["startDay"].toString())
                if(t == ""){
                    return
                }
                keyTmp = "${value["fishTitle"].toString()}의 ${t}".toString()

            } else {
                val k = value["fishWidth"]!!.toInt() * value["fishHeight"]!!.toInt() * value["waterHeight"]!!.toInt()
                val t = fishA(k,value["fishSize"]!!.toInt(),value["startDay"].toString(),"","bol",value["cnt"].toString())
                if(t == ""){
                    return
                }
                if(t == ""){
                    return
                }
                keyTmp = "${value["fishTitle"].toString()}의 ${t}".toString()
                val days = dayFormatter.format(firstDate)
                var cnt = 0
                if (value["reWeak"].toString() != "") {
                    cnt = Integer.parseInt(value["reWeak"].toString())
                }
                val lastWeek = secondDate.plusWeeks(cnt.toLong())
                if (!value["reDay"].toString().contains(days)) {
                    return
                } else if (!lastWeek.isAfter(firstDate)) {
                    return
                }
            }
            when {
                secondDate.isBefore(firstDate) -> {
                    mDatas.add(
                        ReminderVO(
                            "false",
                            title,
                            keyTmp,
                            value["startDay"].toString(),
                            "오늘 | ${value["noticeTime"].toString()}"
                        )
                    )
                }
                secondDate.isEqual(firstDate) -> {
                    mDatas.add(
                        ReminderVO(
                            "false",
                            title,
                            keyTmp,
                            value["startDay"].toString(),
                            "오늘 | ${value["noticeTime"].toString()}"
                        )
                    )
                }
            }
        }
    }
}