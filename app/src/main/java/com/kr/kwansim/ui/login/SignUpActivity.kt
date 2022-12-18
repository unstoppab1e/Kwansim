package com.kr.kwansim.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kr.kwansim.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}