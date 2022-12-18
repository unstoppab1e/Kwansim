package com.kr.kwansim.ui.main

import VisiblePositionChangeListener
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.Size
import com.kr.kwansim.R
import com.kr.kwansim.common.appAll
import com.kr.kwansim.common.fishA
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.ActivityMainBinding
import com.kr.kwansim.utiils.ReminderAdapter
import com.kr.kwansim.utiils.ReminderVO
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var value = 0
    private var selectedDate = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    private val yearFormatter = DateTimeFormatter.ofPattern("YYY")

    private val currentMonth = YearMonth.now()
    private val firstMonth = currentMonth.minusMonths(4)
    private val lastMonth = currentMonth.plusMonths(4)
    private val daysOfWeek = arrayOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    val mDatas=mutableListOf<ReminderVO>()
    val db = Firebase.firestore
    val adapter = ReminderAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appAll = Activity();

        binding.titleNickName.text = Html.fromHtml(String.format(resources.getString(R.string.main_title_name_text),"${user?.displayName}"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.titleNickName.text = Html.fromHtml(String.format(resources.getString(R.string.main_title_name_text),"${user?.displayName}"), Html.FROM_HTML_MODE_LEGACY)
        } else {
            binding.titleNickName.text = Html.fromHtml(String.format(resources.getString(R.string.main_title_name_text),"${user?.displayName}"))
        }

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.bind(day)
            }
        }

        binding.mainLeft.setOnClickListener{
            value -= 1
            binding.calendarView.smoothScrollToPosition(value)
        }

        binding.mainRight.setOnClickListener{
            value += 1
            binding.calendarView.smoothScrollToPosition(value)
        }

        binding.calendarView.setup(firstMonth, lastMonth, daysOfWeek.first())
        binding.calendarView.scrollToDate(LocalDate.now())
        val cap = binding.calendarView.adapter
        value = cap!!.itemCount / 2

        val dm = DisplayMetrics()
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)

        binding.calendarView.apply {
            val dayWidth = (dm.widthPixels-130) / 7
            val dayHeight = binding.calendarView.height.toInt()
            binding.calendarView.daySize = Size(dayWidth, dayHeight)
        }

        binding.calendarView.addOnScrollListener(VisiblePositionChangeListener(
            binding.calendarView.layoutManager as LinearLayoutManager, object : VisiblePositionChangeListener.OnChangeListener {
                override fun onFirstVisiblePositionChanged(position: Int) {
                    value = position
                }
                override fun onLastVisiblePositionChanged(position: Int) {
                }
                override fun onFirstInvisiblePositionChanged(position: Int) {
                    value = position+1
                }
                override fun onLastInvisiblePositionChanged(position: Int) {
                }
            }
        ))

        adapter.datalist = mDatas
        binding.mainReminderRecycler.adapter=adapter //리사이클러뷰에 어댑터 연결
        binding.mainReminderRecycler.layoutManager=LinearLayoutManager(this)

        db.collection("user").document(user!!.uid).collection("fishbowl").get().addOnSuccessListener {result ->
        for (document in result) {
            dataCall(document.id)
        }
        }.addOnFailureListener{}
    }

    fun dataCall(data : String){
        db.collection("user").document(user!!.uid).collection("fishbowl").document(data).get().addOnSuccessListener { result ->
            try {
                fishBlow[data] = result.data!! as HashMap<String, String>
                for (document in result.data!!.entries) {
                    dataAdd(document.value as Map<String, String>, document.key, "",data)
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
            if ( mDatas.size > 0 ) {
                binding.mainBlank.visibility = View.GONE
                binding.mainReminderRecycler.visibility = View.VISIBLE
                adapter.notifyItemInserted(mDatas.size);
            } else {
                binding.mainBlank.visibility = View.VISIBLE
                binding.mainReminderRecycler.visibility = View.GONE
            }
        }.addOnFailureListener{
        }
    }

    fun dataAdd(value: Map<String, String>, key: String, now: String, data: String) {
        var keyTmp = ""
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val firstDate: LocalDate = LocalDate.parse("${if(now != "") { now } else { LocalDate.now() }}", formatter)
        val secondDate: LocalDate = LocalDate.parse(value["startDay"], formatter)

        if(value["alert"].toString().contains("true")) {
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
                val t = fishA(k,value["fishSize"]!!.toInt(),value["startDay"].toString(),now)
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
            if (secondDate.isBefore(firstDate)) {
                mDatas.add(
                    ReminderVO(
                        "true",
                        data,
                        keyTmp,
                        value["startDay"].toString(),
                        "오늘 | ${value["noticeTime"].toString()}"
                    )
                )
            } else if (secondDate.isEqual(firstDate)) {
                mDatas.add(
                    ReminderVO(
                        "true",
                        data,
                        keyTmp,
                        value["startDay"].toString(),
                        "오늘 | ${value["noticeTime"].toString()}"
                    )
                )
            }
        }
    }




//    override fun onResume() {
//        super.onResume()
//        db.collection("user").document(user!!.uid).collection("fishbowl").get().addOnSuccessListener {result ->
//            for (document in result) {
//                dataCall(document.id)
//            }
//        }.addOnFailureListener{}
//    }

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        private val dateText: TextView = view.findViewById(R.id.cal_date)
        private val dayText: TextView = view.findViewById(R.id.cal_day)
        private val layout: LinearLayout  = view.findViewById(R.id.cal_layout)
        lateinit var day: CalendarDay


        init {
            view.setOnClickListener {

                val firstDay = binding.calendarView.findFirstVisibleDay()
                val lastDay = binding.calendarView.findLastVisibleDay()

                if (firstDay == day) {
                    binding.calendarView.smoothScrollToDate(day.date)
                } else if (lastDay == day) {
                    binding.calendarView.smoothScrollToDate(day.date.minusDays(4))
                }

                if (selectedDate != day.date) {
                    val oldDate = selectedDate
                    selectedDate = day.date
                    binding.calendarView.notifyDateChanged(day.date)
                    oldDate?.let { binding.calendarView.notifyDateChanged(it)}
                }

                Handler().postDelayed({
                    mDatas.clear()
                    adapter.datalist = mDatas
                    binding.mainReminderRecycler.adapter=adapter
                    binding.mainReminderRecycler.layoutManager=LinearLayoutManager(applicationContext)
                    try{
                        for (map in fishBlow) {
                            for (document in map.value) {
                                dataAdd(
                                    document.value as Map<String,String>,
                                    document.key,
                                    "${day.date}",
                                    map.key
                                )
                            }
                        }
                        if ( mDatas.size > 0 ) {
                            binding.mainBlank.visibility = View.GONE
                            binding.mainReminderRecycler.visibility = View.VISIBLE
                            adapter.notifyItemInserted(mDatas.size);
                        } else {
                            binding.mainBlank.visibility = View.VISIBLE
                            binding.mainReminderRecycler.visibility = View.GONE
                        }
                    } catch (e : Exception) {
                        binding.mainBlank.visibility = View.VISIBLE
                        binding.mainReminderRecycler.visibility = View.GONE
                    }
                },0)
            }
        }

        // 날짜에 따라 textview에 값을 bind해줌
        fun bind(day: CalendarDay) {
            this.day = day
            dateText.text = dateFormatter.format(day.date)
            dayText.text = dayFormatter.format(day.date)
            dateText.setTextColor(getColor(R.color.black))
            dayText.setTextColor(getColor(R.color.black))

            when (dayText.text) {
                "토" -> dayText.setTextColor(getColor(R.color.blue))
                "일" -> dayText.setTextColor(getColor(R.color.red))
            }

            layout.background = getDrawable(R.drawable.day_gradient_blank)

            if (day.date == selectedDate) {
                val calendar = Calendar.getInstance()
                calendar.set(day.date.year,day.date.monthValue,day.day)
                calendar.firstDayOfWeek = 4
                binding.mainTxtMonth.text = "${yearFormatter.format(day.date)}년 ${monthFormatter.format(day.date)}  ${calendar.get(Calendar.WEEK_OF_MONTH)} 주차"
                layout.background = getDrawable(R.drawable.day_gradient)
                dateText.setTextColor(getColor(R.color.white))
                dayText.setTextColor(getColor(R.color.white))
            }
        }

    }
}