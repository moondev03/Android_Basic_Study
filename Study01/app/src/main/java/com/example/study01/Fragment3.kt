package com.example.study01

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.study01.databinding.Fragment3Binding

class Fragment3 : Fragment() {
    private lateinit var binding : Fragment3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment3Binding.inflate(inflater, container, false)


        with(binding){
            toggleButton.setOnClickListener {

                if (toggleButton.text.toString() == "펼침") {
                    toggleButton.text = "모음"

                    guideline1.setGuidelinePercent(0.1F)
                    guideline2.setGuidelinePercent(0.1F)
                    guideline3.setGuidelinePercent(0.9F)
                    guideline4.setGuidelinePercent(0.6F)
                } else {
                    toggleButton.text = "펼침"

                    guideline1.setGuidelinePercent(0.2F)
                    guideline2.setGuidelinePercent(0.2F)
                    guideline3.setGuidelinePercent(0.8F)
                    guideline4.setGuidelinePercent(0.5F)
                }
            }
        }
        return binding.root
    }
}