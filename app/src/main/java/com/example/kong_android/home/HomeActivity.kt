package com.example.kong_android.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kong_android.R
import com.example.kong_android.my.MyFragment
import com.example.kong_android.plan.PlanFragment
import com.example.kong_android.record.RecordFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener{ menuItem ->
            val selectedFragment = when (menuItem.itemId) {
                R.id.navigation_home -> HomeFragment()
                R.id.navigation_record -> RecordFragment()
                R.id.navigation_plan -> PlanFragment()
                R.id.navigation_my -> MyFragment()
                else -> null
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it as Fragment)
                    .commit()
            }
            true
        }

        // 기본으로 HomeFragment를 로드
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.navigation_home
        }
    }
}