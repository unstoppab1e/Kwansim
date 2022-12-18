package com.kr.kwansim.utiils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import com.kr.kwansim.R
import com.kr.kwansim.ui.main.MainActivity
import com.kr.kwansim.ui.myfish.MyFishActivity
import com.kr.kwansim.ui.setting.SettingActivity


class BottomNav: LinearLayout {
    lateinit var nav1 : Button
    lateinit var nav2 : Button
    lateinit var nav3 : Button

    constructor(context: Context?) : super(context){
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(context)
        getAttrs(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context)
    }

    private fun init(context:Context?){
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_menu,this,false)
        addView(view)

        nav1 = findViewById(R.id.btn_main_shared1)
        nav2 = findViewById(R.id.btn_main_shared2)
        nav3 = findViewById(R.id.btn_main_shared3)

        nav1.setOnClickListener {
            (context as Activity).startActivity(Intent(context, MainActivity::class.java))
            (context as Activity).finish()
        }

        nav2.setOnClickListener {
            (context as Activity).startActivity(Intent(context,MyFishActivity::class.java))
            (context as Activity).finish()
        }

        nav3.setOnClickListener {

            (context as Activity).startActivity(Intent(context, SettingActivity::class.java))
            (context as Activity).finish()
        }

    }

    private fun getAttrs(attrs:AttributeSet?){
        //아까 만들어뒀던 속성 attrs 를 참조함
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.BottomNav)
        setTypeArray(typedArray)
    }
    //디폴트 설정
    @SuppressLint("ResourceAsColor")
    private fun setTypeArray(typedArray : TypedArray){
        val nav = typedArray.getText(R.styleable.BottomNav_nav1)

        if (nav == "1") {
            nav1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home1, 0, 0);
            nav1.isEnabled = false
        } else if (nav == "2") {
            nav2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fi1, 0, 0);
            nav2.isEnabled = false
        } else {
            nav3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.acc1, 0, 0);
            nav3.isEnabled = false
        }

        typedArray.recycle()
    }
}