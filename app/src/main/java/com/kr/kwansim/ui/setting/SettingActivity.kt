package com.kr.kwansim.ui.setting

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kr.kwansim.R
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.ActivitySettingBinding
import com.kr.kwansim.ui.login.LoginActivity
import com.kr.kwansim.ui.myfish.vo.FishAddVO

class SettingActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.logout.setOnClickListener {
            startActivity(Intent(applicationContext,LoginActivity::class.java))
            finish()
        }

        binding.out.setOnClickListener {
            val db = Firebase.firestore
            db.collection("user").document(user!!.uid).delete().addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!" + user!!.uid)
                startActivity(Intent(applicationContext,LoginActivity::class.java))
                finish()
            }
        }

        binding.switch2.setOnCheckedChangeListener { compoundButton, b ->
        }
    }
}