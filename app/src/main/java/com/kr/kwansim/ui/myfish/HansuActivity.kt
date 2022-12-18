package com.kr.kwansim.ui.myfish

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kr.kwansim.common.bolVo
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.common.hanVo
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.ActivityFishAddBinding
import com.kr.kwansim.databinding.ActivityHwansuBinding
import com.kr.kwansim.databinding.ActivityMyFishBinding
import com.kr.kwansim.ui.myfish.fragment.ReFragment
import com.kr.kwansim.ui.myfish.vo.HwansuVo
import java.util.*

class HansuActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHwansuBinding.inflate(layoutInflater) }
    var title : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        hanVo = HwansuVo()

        title = intent.getStringExtra("title").toString()
        val data = fishBlow[intent.getStringExtra("title")]
        val data2 = data!!["hwanSu"] as Map<String, String>

        binding.editFishTitle.setText(data2["fishTitle"].toString())
        binding.editTimeSetting.setText(data2["noticeTime"].toString())
        binding.editDayText.setText(data2["startDay"].toString())

        binding.switch2.isChecked = data2["alert"]!!.contains("true")
        hanVo.fishTitle = data2["fishTitle"]!!.toString()
        hanVo.reCount = data2["reCount"]!!.toString()
        hanVo.reDay = data2["reDay"]!!.toString()
        hanVo.reWeak = data2["reWeak"]!!.toString()

        binding.btnHwansuAdd.setOnClickListener {
            val text = dbAdd()
            var builder = AlertDialog.Builder(this)
            builder.setTitle("다이얼로그 임시")
            builder.setMessage("${text}")
            // 버튼 클릭시에 무슨 작업을 할 것인가!
            var listener = DialogInterface.OnClickListener { p0, p1 ->
                if(text.toString().contains("정상")) {
                    onBackPressed()
                }
                p0.dismiss()
            }
            builder.setPositiveButton("확인", listener)
            builder.show()
        }

        binding.btnRe.setOnClickListener {
            startActivity(Intent(this,ReActivity::class.java))
        }

        val callbackMethod = DatePickerDialog.OnDateSetListener{
                view,year,month,day ->
            var monthTmp = ""
            if(month+1 < 10 ) {
                monthTmp = "0${month+1}"
            } else {
                monthTmp = "${month+1}"
            }
            binding.editDayText.text =  ("${year}-${monthTmp}-${day}")
        }

        binding.editDayText.setOnClickListener {
            val cal = Calendar.getInstance()
            val dialog = DatePickerDialog(this, callbackMethod, cal.get(Calendar.YEAR),cal.get(
                Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }

        val callbackMethod2 =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                binding.editTimeSetting.setText(
                    hourOfDay.toString() + "시" + minute + "분"
                )
            }

        binding.editTimeSetting.setOnClickListener {
            val cal = Calendar.getInstance()
            val dialog = TimePickerDialog(this, callbackMethod2, cal.get(Calendar.HOUR), cal.get(
                Calendar.MINUTE), true)
            dialog.show()
        }

        binding.backIc.setOnClickListener {
            onBackPressed()
        }


    }
    fun dbAdd() : String{
        hanVo.fishTitle = binding.editFishTitle.text.toString()

        if (binding.editTimeSetting.text.toString() != "") {
            hanVo.noticeTime = binding.editTimeSetting.text.toString()
        } else {
            return "알림 시간을 입력하세요"
        }

        if (binding.editDayText.text.toString() != "") {
            hanVo.startDay =  binding.editDayText.text.toString()
        } else {
            return "사작 날짜를 입력하세요"
        }

        hanVo.alert = "${binding.switch2.isChecked}"
        val db = Firebase.firestore
        var chkBoolean = true

        val map = HashMap<String, HwansuVo>()
        map["hwanSu"] = hanVo
        db.collection("user").document(user!!.uid).collection("fishbowl").document(title).set(map,
            SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                hanVo = HwansuVo()
                reCall(db)
            }
            .addOnFailureListener { e ->
                chkBoolean = false
            }
        return if (chkBoolean) {
            "정상 처리되었습니다."
        } else {
            "입력에 실패하였습니다 관리자에게 문의해주세요."
        }
    }

    fun reCall(db: FirebaseFirestore) {
        fishBlow.clear()
        db.collection("user").document(user!!.uid).collection("fishbowl").get().addOnSuccessListener { result ->
            for (document in result) {
                dataCall(document.id,db)
            }
        }.addOnFailureListener{}
    }
    fun dataCall(data: String, db: FirebaseFirestore){
        db.collection("user").document(user!!.uid).collection("fishbowl").document(data).get().addOnSuccessListener { result ->
            fishBlow[data] = result.data!! as java.util.HashMap<String, String>
        }.addOnFailureListener{
        }
    }

}