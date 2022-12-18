package com.kr.kwansim.ui.myfish.fragment

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kr.kwansim.common.fishBlow
import com.kr.kwansim.common.user
import com.kr.kwansim.databinding.FragmentFishAddBinding
import com.kr.kwansim.databinding.FragmentHwanSuBinding
import com.kr.kwansim.ui.myfish.vo.FishAddVO
import com.kr.kwansim.ui.myfish.vo.FishVO
import com.kr.kwansim.ui.myfish.vo.HwansuVo
import java.util.*
import android.app.TimePickerDialog.OnTimeSetListener
import com.kr.kwansim.common.hanVo
import com.kr.kwansim.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIc.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnHwansuAdd.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}