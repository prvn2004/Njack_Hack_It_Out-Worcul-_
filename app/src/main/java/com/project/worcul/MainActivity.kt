package com.project.worcul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.project.worcul.LoginFragments.ui.loginFragment
import com.project.worcul.LoginFragments.ui.signupFragement
import com.project.worcul.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showFragment(loginFragment())


    }
    fun showFragment(fragment: Fragment) {
        val fram1 = supportFragmentManager.beginTransaction()
        fram1.replace(R.id.fragLayout2, fragment)
        fram1.commit()
    }

    fun SignInClick(view: View){
        showFragment(loginFragment())
    }

    fun SignUpClick(view: View) {
        showFragment(signupFragement())
    }

}