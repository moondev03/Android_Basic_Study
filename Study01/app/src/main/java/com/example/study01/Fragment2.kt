package com.example.study01

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.study01.databinding.Fragment2Binding

class Fragment2 : Fragment() {
    private lateinit var binding : Fragment2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment2Binding.inflate(inflater, container, false)

        var data = arguments?.getString("key")

        with(binding){
            tv1.text = "Saved Value: $data"

            bnt1.setOnClickListener {
                data = et1.text.toString()
                tv1.text = "Saved Value: $data"

                (requireActivity() as MainActivity).fragment1.setData(data!!)

                et1.text.clear()
            }
        }
        return binding.root
    }
}