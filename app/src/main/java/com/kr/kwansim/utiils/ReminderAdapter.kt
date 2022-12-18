package com.kr.kwansim.utiils

import android.R
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.databinding.ReminderCalenderBinding
import com.kr.kwansim.ui.myfish.ChlorinActivity
import com.kr.kwansim.ui.myfish.FishAddActivity
import com.kr.kwansim.ui.myfish.HansuActivity
import java.util.ArrayList

class ReminderAdapter: RecyclerView.Adapter<ReminderAdapter.MyViewHolder>() {

    var datalist = mutableListOf<ReminderVO>()//리사이클러뷰에서 사용할 데이터 미리 정의 -> 나중에 MainActivity등에서 datalist에 실제 데이터 추가
    var context : Context? = null
    var bol = false;
    inner class MyViewHolder(private val binding: ReminderCalenderBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(dogData:ReminderVO){
            if (datalist.indexOf(dogData) == 0) {
                binding.line1.visibility = View.VISIBLE
            }
            binding.reminderTitle.text=dogData.reminderTitle
            binding.reminderTimes.text= dogData.reminderDats
            binding.reminderLayout.setOnClickListener {
                bol = true
                if (binding.reminderTitle.maxLines > 1) {
                    binding.reminderTitle.maxLines = 1
                } else {
                    binding.reminderTitle.maxLines = 4
                }
            }

            val items = ArrayList<String>()
            items.add("수정하기")
            items.add("삭제하기")

            binding.imageView9.setOnClickListener{
                bol = true
                binding.spinner.setSelection(1)
            }

            if(dogData.cont.contains("false")) {
                val myAdapter =
                    ArrayAdapter(context!!, R.layout.simple_spinner_dropdown_item, items)
                binding.spinner.adapter = myAdapter

                binding.spinner?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            if (bol) {
                                if (items.get(position) == "수정하기") {
                                    if (dogData.reminderTitle.contains("어항")) {
                                        val intent = Intent(context, FishAddActivity::class.java)
                                        intent.putExtra("title", dogData.title)
                                        context!!.startActivity(intent)
                                    } else if (dogData.reminderTitle.contains("환수")) {
                                        val intent = Intent(context, HansuActivity::class.java)
                                        intent.putExtra("title", dogData.title)
                                        context!!.startActivity(intent)
                                    } else if (dogData.reminderTitle.contains("염소")) {
                                        val intent = Intent(context, ChlorinActivity::class.java)
                                        intent.putExtra("title", dogData.title)
                                        context!!.startActivity(intent)
                                    }
                                } else {

                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                        }
                    }
            } else {
                binding.imageView9.visibility = View.GONE
            }
        }
    }


    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding=ReminderCalenderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int =datalist.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

}
