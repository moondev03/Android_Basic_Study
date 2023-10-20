package com.example.study01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.study01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var fragment1: Fragment1
    lateinit var fragment2: Fragment2
    lateinit var fragment3: Fragment3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragment1 = Fragment1()
        fragment2 = Fragment2()
        fragment3 = Fragment3()

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment1).commit()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                 R.id.fragment1 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment1).commit()
                }
                R.id.fragment2 -> {
                    val bundle = Bundle()
                    bundle.putString("key", fragment1.getData())
                    fragment2.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment2).commit()
                }
                R.id.fragment3 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment3).commit()
                }
            }
            true
        }
    }
}
