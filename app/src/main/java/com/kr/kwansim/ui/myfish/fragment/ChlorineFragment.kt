package com.kr.kwansim.ui.myfish.fragment

import android.R
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kr.kwansim.common.bolVo
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.common.hanVo
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.FragmentChlorineBinding
import com.kr.kwansim.databinding.FragmentFishAddBinding
import com.kr.kwansim.databinding.FragmentHwanSuBinding
import com.kr.kwansim.ui.myfish.vo.BolVO
import com.kr.kwansim.ui.myfish.vo.FishAddVO
import com.kr.kwansim.ui.myfish.vo.HwansuVo
import java.util.*


class ChlorineFragment : Fragment() {

    private var _binding: FragmentChlorineBinding? = null
    private val binding get() = _binding!!
    var title : String = ""
    companion object {
        fun newInstance() = ChlorineFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChlorineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val items = ArrayList<String>()
        val items4 = ArrayList<String>()
        items4.add("")
        items.add("어항을 선택해주세요.")
        for (i in fishBlow) {
            items4.add(i.key)
            val data = fishBlow[i.key]
            val data2 = data!!["fishAdd"] as Map<String, String>
            items.add("${data2.get("fishTitle")}")
        }


        val myAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, items)

        binding.spinner.adapter = myAdapter
        binding.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.editFishTitle.text = items.get(position)
                bolVo.fishTitle = items.get(position)
                title = items4.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }



        val items2 = ArrayList<String>()
        items2.add("비율을 선택해주세요.")
        for (i in 10 .. 50 step(10)) {
            items2.add("${i}%")
        }

        val myAdapter2 = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, items2)

        binding.spinner2.adapter = myAdapter2
        binding.spinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                try {
                    val title = binding.editFishTitle.text.toString()
                    val data = fishBlow[title]
                    if (data != null) {
                        val data2 = data!!["fishAdd"] as Map<String, String>
                        val temp =
                            (Integer.parseInt(data2["fishWidth"]) * Integer.parseInt(data2["fishHeight"]) * Integer.parseInt(
                                data2["waterHeight"]
                            )) / 1000.0
                        binding.editChi.text = "${((temp) * Integer.parseInt(items2[position].substring(0, 1))) / 100.0}"
                        bolVo.cnt = binding.editChi.text.toString()
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }



        binding.btnAdd1.setOnClickListener { bolVo.experienceYN = "N" }
        binding.btnAdd2.setOnClickListener { bolVo.experienceYN = "Y" }

        binding.btnChlAdd.setOnClickListener {
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
            val dialog = DatePickerDialog(requireContext(), callbackMethod, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH))
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

        binding.btnRe.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(com.kr.kwansim.R.id.my_fish_frame,ReTwoFragment()).addToBackStack(null)
                .commit()
        }

    }

    fun dbAdd() : String{
        if (binding.editFishTitle.text.toString() == "") {
            return "어항을 선택해주세요"
        }
        if (binding.editTimeSetting.text.toString() != "") {
            bolVo.noticeTime = binding.editTimeSetting.text.toString()
        } else {
            return "알림 시간을 선택하세요"
        }
        if (binding.editDayText.text.toString() != "") {
            bolVo.startDay =  binding.editDayText.text.toString()
        } else {
            return "사작 날짜를 선택하세요"
        }
        bolVo.alert = "${binding.switch2.isChecked}"
        val db = Firebase.firestore
        var chkBoolean = true
        val map = HashMap<String, BolVO>()
        map["bol"] = bolVo
        db.collection("user").document(user!!.uid).collection("fishbowl").document("${title}").set(map,  SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                bolVo = BolVO()
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