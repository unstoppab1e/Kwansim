package com.kr.kwansim.utiils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kr.kwansim.databinding.MyFishBinding
import com.kr.kwansim.ui.myfish.FishDetailActivity

class MyFishAdapter: RecyclerView.Adapter<MyFishAdapter.MyViewHolder>() {

    var datalist = mutableListOf<MyFishVO>()//리사이클러뷰에서 사용할 데이터 미리 정의 -> 나중에 MainActivity등에서 datalist에 실제 데이터 추가
    var context : Context? = null
    inner class MyViewHolder(private val binding: MyFishBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(dogData:MyFishVO){
            binding.myFishTitle.text =dogData.myFish

            binding.linearLayout.setOnClickListener {
                val intent = Intent(context,FishDetailActivity::class.java)
                intent.putExtra("title",dogData.myFishTitle)
                context!!.startActivity(intent)
            }
        }
    }


    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding=MyFishBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int =datalist.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(datalist[position])
    }
}
