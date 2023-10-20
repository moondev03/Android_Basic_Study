package com.example.study01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.study01.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubBinding
    private var data: String? = null
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent = getIntent()
        data = intent.getStringExtra("data")
        binding.tv1.text = "Saved Value: $data"

        binding.btnActivity.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("data", binding.tv1.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }

        with(binding){
            btn1.setOnClickListener {
                data = et1.text.toString()
                tv1.text = "Saved Value: $data"
                et1.text.clear()
            }
        }
    }
}