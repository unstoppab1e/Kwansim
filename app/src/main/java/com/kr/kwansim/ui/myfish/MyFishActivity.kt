package com.kr.kwansim.ui.myfish

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.databinding.ActivityMyFishBinding
import com.kr.kwansim.utiils.MyFishAdapter
import com.kr.kwansim.utiils.MyFishVO

class MyFishActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMyFishBinding.inflate(layoutInflater) }
    private val mDatas = mutableListOf<MyFishVO>()
    val adapter = MyFishAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(fishBlow.size > 0) {
            binding.fishBlowBlank.visibility = View.GONE
            binding.mainReminderRecycler.visibility = View.VISIBLE
            for (i in fishBlow) {
                val data = fishBlow[i.key]
                val data2 = data!!["fishAdd"] as Map<String, String>
                mDatas.add(MyFishVO(i.key,data2["fishTitle"].toString()))
            }
        } else {
            binding.fishBlowBlank.visibility = View.VISIBLE
            binding.mainReminderRecycler.visibility = View.GONE
        }

        adapter.datalist = mDatas
        binding.mainReminderRecycler.adapter=adapter //리사이클러뷰에 어댑터 연결
        binding.mainReminderRecycler.layoutManager= LinearLayoutManager(this)
        binding.myFishFab.setOnClickListener {
            startActivity(Intent(this,MyFishInsertActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        mDatas.clear()
        for (i in fishBlow) {
            val data = fishBlow[i.key]
            val data2 = data!!["fishAdd"] as Map<String, String>
            mDatas.add(MyFishVO(i.key,data2["fishTitle"].toString()))
        }
        adapter.notifyItemInserted(mDatas.size);
    }
}