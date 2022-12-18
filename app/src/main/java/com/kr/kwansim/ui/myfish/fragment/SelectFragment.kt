package com.kr.kwansim.ui.myfish.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kr.kwansim.R
import com.kr.kwansim.databinding.SelectFragmentBinding


/**
 * Class : IdCardMainFragment
 * Created by 빙창선 on 2019-11-15.
 */


class SelectFragment : Fragment() {
    private var _binding: SelectFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SelectFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SelectFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingTwo = SelectTwoFragment()
        val args = Bundle()

        binding.settingBtnBig.setOnClickListener {
            args.putString("type","중형어")
            settingTwo.arguments = args
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.my_fish_frame,settingTwo)
                .commit()
        }

        binding.settingBtnSmall.setOnClickListener {
            args.putString("type","소형어")
            settingTwo.arguments = args
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.my_fish_frame,settingTwo)
                .commit()
        }
    }
}
