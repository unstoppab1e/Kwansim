package com.kr.kwansim.ui.myfish

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.ActivityFishAddBinding
import com.kr.kwansim.databinding.ActivityMyFishBinding
import com.kr.kwansim.ui.myfish.vo.FishAddVO
import java.util.*

class FishAddActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFishAddBinding.inflate(layoutInflater) }
    private val addVo : FishAddVO = FishAddVO("","","","","","","","")

    var title : String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = intent.getStringExtra("title").toString()

        val data = fishBlow[intent.getStringExtra("title")]
        val data2 = data!!["fishAdd"] as Map<String, String>

        binding.editFishTitle.setText(data2["fishTitle"].toString())
        binding.waterHeight.setText(data2["waterHeight"].toString())
        binding.sizeHeight.setText(data2["fishHeight"].toString())
        binding.sizeWidth.setText(data2["fishWidth"].toString())
        binding.editFishSize.setText(data2["fishSize"].toString())
        binding.editDayText.setText(data2["startDay"].toString())

        val callbackMethod = DatePickerDialog.OnDateSetListener{ view,year,month,day ->
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
            val dialog = DatePickerDialog(this, callbackMethod, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }

        binding.btnFinhAdd.setOnClickListener {
            val text = dbAdd()
            var builder = AlertDialog.Builder(this)
            builder.setTitle("다이얼로그 임시")
            builder.setMessage("${text}")
            var listener = DialogInterface.OnClickListener { p0, p1 ->
                if(text.toString().contains("정상")) {
                    onBackPressed()
                }
                p0.dismiss()
            }
            builder.setPositiveButton("확인", listener)
            builder.show()
        }
    }

    fun dbAdd() : String{

        if (binding.editFishTitle.text.toString() != "") {
            addVo.fishTitle = binding.editFishTitle.text.toString()
        } else {
            return "타이틀을 입력하세요"
        }
        if (binding.sizeWidth.text.toString() != "") {
            addVo.fishWidth = binding.sizeWidth.text.toString()
        } else {
            return "가로사이즈를 입력하세요"
        }
        if (binding.sizeHeight.text.toString() != "") {
            addVo.fishHeight = binding.sizeHeight.text.toString()
        } else {
            return "세로사이즈를 입력하세요"
        }
        if (binding.waterHeight.text.toString() != "") {
            addVo.waterHeight = binding.waterHeight.text.toString()
        } else {
            return "물 높이를 입력하세요"
        }
        if (binding.editDayText.text.toString() != "") {
            addVo.startDay =  binding.editDayText.text.toString()
        } else {
            return "사작 날짜를 입력하세요"
        }

        if (binding.editFishSize.text.toString() != "") {
            addVo.fishSize =  binding.editFishSize.text.toString()
        } else {
            return "관상어 크기를 입력해주세요."
        }

        addVo.alert = "true"
        val db = Firebase.firestore
        var chkBoolean = true

        val map = HashMap<String, FishAddVO>()
        map["fishAdd"] = addVo

        db.collection("user").document(user!!.uid).collection("fishbowl").document(title).set(map)
            .addOnSuccessListener { documentReference ->
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