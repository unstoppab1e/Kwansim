package com.kr.kwansim.ui.myfish.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kr.kwansim.R
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.FragmentFishAddBinding
import com.kr.kwansim.ui.myfish.vo.FishAddVO
import java.util.*
import kotlin.collections.HashMap


class FishAddFragment : Fragment() {

    private var _binding: FragmentFishAddBinding? = null
    private val binding get() = _binding!!
    private val addVo : FishAddVO = FishAddVO("","","","","","","","")

    companion object {
        fun newInstance() = FishAddFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFishAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd1.setOnClickListener {
            addVo.experienceYN = "N"
            val startFragment = StartFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.my_fish_frame,startFragment).addToBackStack(null)
                .commit()
        }
        binding.btnAdd2.setOnClickListener { addVo.experienceYN = "Y" }

        binding.btnFinhAdd.setOnClickListener {
            val text = dbAdd()
            var builder = AlertDialog.Builder(context)
            builder.setTitle("다이얼로그 임시")
            builder.setMessage("${text}")
            var listener = DialogInterface.OnClickListener { p0, p1 ->
                if(text.toString().contains("정상")) {
                    requireActivity().onBackPressed()
                }
                p0.dismiss()
            }
            builder.setPositiveButton("확인", listener)
            builder.show()
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
            val dialog = DatePickerDialog(requireContext(), callbackMethod, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }

        val callbackMethod2 =
            OnTimeSetListener { view, hourOfDay, minute -> binding.editTimeSetting.setText(hourOfDay.toString() + "시" + minute + "분") }

        binding.editTimeSetting.setOnClickListener {
            val cal = Calendar.getInstance()
            val dialog = TimePickerDialog(requireContext(), callbackMethod2, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true)
            dialog.show()
        }

        binding.backIc.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    fun dbAdd() : String{

        Log.v("test", "test" + binding.switch2.isChecked)

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
//        if (binding.editTimeSetting.text.toString() != "") {
//            addVo.noticeTime = binding.editTimeSetting.text.toString()
//        } else {
//            return "알림 시간을 입력하세요"
//        }
        if (binding.editDayText.text.toString() != "") {
            addVo.startDay =  binding.editDayText.text.toString()
        } else {
            return "사작 날짜를 입력하세요"
        }
        if (addVo.experienceYN == "") {
            return "경험 유무를 선택 하세요"
        }
        if (binding.editFishSize.text.toString() != "") {
            addVo.fishSize =  binding.editFishSize.text.toString()
        } else {
            return "관상어 크기를 입력해주세요."
        }

        addVo.alert = "true"
        val db = Firebase.firestore
        var chkBoolean = true

        val map = HashMap<String,FishAddVO>()
        map["fishAdd"] = addVo


        db.collection("user").document(user!!.uid).collection("fishbowl").document("${System.currentTimeMillis()}").set(map)
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
        db.collection("user").document(user!!.uid).collection("fishbowl").get().addOnSuccessListener {result ->
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