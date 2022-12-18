package com.kr.kwansim.ui.myfish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kr.kwansim.R
import com.kr.kwansim.databinding.ActivityMyFishBinding
import com.kr.kwansim.databinding.ActivityMyFishInsertBinding
import com.kr.kwansim.ui.myfish.fragment.SelectFragment

class MyFishInsertActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMyFishInsertBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragment by lazy { supportFragmentManager.beginTransaction() }
        fragment.replace(R.id.my_fish_frame, SelectFragment())
        fragment.commit()
    }

}