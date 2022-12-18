package com.kr.kwansim.ui.myfish

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kr.kwansim.common.bolVo
import com.kr.kwansim.databinding.ActivityHwansuBinding
import com.kr.kwansim.databinding.ActivityReTwoBinding
import java.util.ArrayList

class ReTwoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityReTwoBinding.inflate(layoutInflater) }
    private var dayText = "";

    var title : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var items = ArrayList<String>()
        for (i in 1..100) {
            items.add("${i}")
        }

        var items2 = ArrayList<String>()
        for (i in 1..7) {
            items2.add("${i}")
        }


        val myAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item , items)
        val myAdapter2 = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item , items2)
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
            bolVo.reDay = dayText
            bolVo.reCount = binding.b2.text.toString()
            bolVo.reWeak = binding.b1.text.toString()
            onBackPressed()
        }

        binding.backIc.setOnClickListener {
            onBackPressed()
        }

        binding.ts1.setOnClickListener { clickMethod( binding.ts1,"월",) }
        binding.ts2.setOnClickListener { clickMethod(binding.ts2, "화") }
        binding.ts3.setOnClickListener { clickMethod(binding.ts3, "수") }
        binding.ts4.setOnClickListener { clickMethod(binding.ts4, "목") }
        binding.ts5.setOnClickListener { clickMethod(binding.ts5, "금") }
        binding.ts6.setOnClickListener { clickMethod(binding.ts6, "토") }
        binding.ts7.setOnClickListener { clickMethod(binding.ts7, "일") }

    }

    fun clickMethod (view: TextView, s: String) {
        if (dayText.contains(s)) {
            view.setBackgroundColor(ContextCompat.getColor(this, com.kr.kwansim.R.color.white))
            view.setTextColor(ContextCompat.getColor(this, com.kr.kwansim.R.color.gray))
            dayText = dayText.replace(s,"")
        } else {
            view.setBackgroundColor(ContextCompat.getColor(this, com.kr.kwansim.R.color.background_re))
            view.setTextColor(ContextCompat.getColor(this, com.kr.kwansim.R.color.white))
            dayText += s
        }
    }

}