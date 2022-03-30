package com.project.worcul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.project.worcul.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        setCurrentFragment(worculFragment())
        binding.bottomNaviagtion.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.Worcul -> setCurrentFragment(worculFragment())

                R.id.profile -> setCurrentFragment(profileFragment())

                R.id.Github -> setCurrentFragment(GithubFragment())

            }
            true
        }


    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_container, fragment)
            commit()
        }
    }
}