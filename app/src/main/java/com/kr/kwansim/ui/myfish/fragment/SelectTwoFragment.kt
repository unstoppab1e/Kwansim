package com.kr.kwansim.ui.myfish.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kr.kwansim.R
import com.kr.kwansim.common.hanVo
import com.kr.kwansim.databinding.SelectFragmentBinding
import com.kr.kwansim.databinding.SelectTwoFragmentBinding
import com.kr.kwansim.ui.myfish.vo.HwansuVo


/**
 * Class : IdCardMainFragment
 * Created by 빙창선 on 2019-11-15.
 */


class SelectTwoFragment : Fragment() {
    private var _binding: SelectTwoFragmentBinding? = null
    private val binding get() = _binding!!
    private var title = ""

    companion object {
        fun newInstance() = SelectTwoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         title = requireArguments().getString("type","")
        _binding = SelectTwoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingTwoTitle.text = title

        binding.settingTwo1.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.my_fish_frame,FishAddFragment()).addToBackStack(null)
                .commit()
        }
        binding.settingTwo2.setOnClickListener {
            hanVo = HwansuVo()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.my_fish_frame,HwanSuFragment()).addToBackStack(null)
                .commit()
        }

        binding.settingTwo3.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.my_fish_frame,ChlorineFragment()).addToBackStack(null)
                .commit()
        }

    }

}
