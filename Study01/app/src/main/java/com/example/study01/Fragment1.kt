package com.example.study01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.study01.databinding.Fragment1Binding

class Fragment1 : Fragment() {
    private var data: String? = null
    private lateinit var binding: Fragment1Binding
    private lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment1Binding.inflate(inflater, container, false)

        with(binding){
            tv1.text = "Saved Value: $data"

            btn1.setOnClickListener {
                data = et1.text.toString()
                tv1.text = "Saved Value: $data"
                et1.text.clear()
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                binding.tv1.text = result.data?.getStringExtra("data")
            }
        }
        binding.btnActivity.setOnClickListener {
            val intent = Intent(requireActivity(), SubActivity::class.java)
            intent.putExtra("data", data)
            launcher.launch(intent)
        }

        return binding.root
    }

    fun getData(): String? {
        return data
    }

    fun setData(data: String) {
        this.data = data
    }
}
