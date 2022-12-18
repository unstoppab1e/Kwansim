package com.kr.kwansim.ui.myfish.fragment

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.FragmentFishAddBinding
import com.kr.kwansim.databinding.FragmentHwanSuBinding
import com.kr.kwansim.ui.myfish.vo.FishAddVO
import com.kr.kwansim.ui.myfish.vo.FishVO
import com.kr.kwansim.ui.myfish.vo.HwansuVo
import java.util.*
import android.app.TimePickerDialog.OnTimeSetListener
import com.google.firebase.firestore.FirebaseFirestore
import com.kr.kwansim.common.hanVo
import kotlin.collections.ArrayList

class HwanSuFragment : Fragment() {

    private var _binding: FragmentHwanSuBinding? = null
    private val binding get() = _binding!!
    var title : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHwanSuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd1.setOnClickListener { hanVo.experienceYN = "N" }
        binding.btnAdd2.setOnClickListener { hanVo.experienceYN = "Y" }

        val items = ArrayList<String>()
        val items2 = ArrayList<String>()
        items2.add("")
        items.add("어항을 선택해주세요.")
        for (i in fishBlow) {
            items2.add(i.key)
            val data = fishBlow[i.key]
            val data2 = data!!["fishAdd"] as Map<String, String>
            items.add("${data2.get("fishTitle")}")
        }

        val myAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, items)

        binding.spinner.adapter = myAdapter
        binding.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.editFishTitle.text = items.get(position)
                hanVo.fishTitle = items.get(position)
                title = items2.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.btnHwansuAdd.setOnClickListener {
            val text = dbAdd()
            var builder = AlertDialog.Builder(context)
            builder.setTitle("다이얼로그 임시")
            builder.setMessage("${text}")
            // 버튼 클릭시에 무슨 작업을 할 것인가!
            var listener = DialogInterface.OnClickListener { p0, p1 ->
                if(text.toString().contains("정상")) {
                    requireActivity().onBackPressed()
                }
                p0.dismiss()
            }
            builder.setPositiveButton("확인", listener)
            builder.show()
        }

        binding.btnRe.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(com.kr.kwansim.R.id.my_fish_frame,ReFragment()).addToBackStack(null)
                .commit()
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

        binding.btnAdd2.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun dbAdd() : String{
        if (binding.editFishTitle.text.toString() == "") {
            return "어항을 선택해주세요"
        }
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
        db.collection("user").document(user!!.uid).collection("fishbowl").document("${title}").set(map,
            SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                reCall(db);
                hanVo = HwansuVo()
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