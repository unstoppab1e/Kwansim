package com.kr.kwansim.ui.myfish.fragment

import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kr.kwansim.common.hanVo
import com.kr.kwansim.databinding.FragmentReBinding
import com.kr.kwansim.ui.myfish.vo.HwansuVo
import java.util.*


class ReFragment : Fragment() {

    private var _binding: FragmentReBinding? = null
    private val binding get() = _binding!!
    private var dayText = "";

    companion object {
        fun newInstance() = ReFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var items = ArrayList<String>()
        for (i in 1..100) {
            items.add("${i}")
        }

        var items2 = ArrayList<String>()
        for (i in 1..7) {
            items2.add("${i}")
        }


        val myAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item , items)
        val myAdapter2 = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item , items2)
        binding.spinner1.adapter = myAdapter
        binding.spinner2.adapter = myAdapter2
        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                binding.b1.text = items[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                binding.b2.text = items2[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.btnHwansuAdd.setOnClickListener {
            hanVo.reDay = dayText
            hanVo.reCount = binding.b2.text.toString()
            hanVo.reWeak = binding.b1.text.toString()
            requireActivity().onBackPressed()
        }

        binding.backIc.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.ts1.setOnClickListener { clickMethod(binding.ts1,"월",) }
        binding.ts2.setOnClickListener { clickMethod(binding.ts2, "화") }
        binding.ts3.setOnClickListener { clickMethod(binding.ts3, "수") }
        binding.ts4.setOnClickListener { clickMethod(binding.ts4, "목") }
        binding.ts5.setOnClickListener { clickMethod(binding.ts5, "금") }
        binding.ts6.setOnClickListener { clickMethod(binding.ts6, "토") }
        binding.ts7.setOnClickListener { clickMethod(binding.ts7, "일") }

    }

    fun clickMethod (view: TextView, s: String) {
        if (dayText.contains(s)) {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), com.kr.kwansim.R.color.white))
            view.setTextColor(ContextCompat.getColor(requireContext(), com.kr.kwansim.R.color.gray))
            dayText = dayText.replace(s,"")
        } else {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), com.kr.kwansim.R.color.background_re))
            view.setTextColor(ContextCompat.getColor(requireContext(), com.kr.kwansim.R.color.white))
            dayText += s
        }
    }

    fun dbAdd() {
//        if (dayText.length == Integer.parseInt(binding.b2.text.toString())) {
//            hanVo.reDay = dayText
//        } else {
//            return ""
//        }
//        if (binding.editTimeSetting.text.toString() != "") {
//            addVo.noticeTime = binding.editTimeSetting.text.toString()
//        } else {
//            return "알림 시간을 입력하세요"
//        }
//        if (binding.editDayText.text.toString() != "") {
//            addVo.startDay =  binding.editDayText.text.toString()
//        } else {
//            return "사작 날짜를 입력하세요"
//        }
//        if (addVo.experienceYN == "") {
//            return "경험 유무를 선택 하세요"
//        }

        hanVo.reDay = dayText

    }
}