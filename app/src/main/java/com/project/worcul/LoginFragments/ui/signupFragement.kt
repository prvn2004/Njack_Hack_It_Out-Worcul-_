package com.project.worcul.LoginFragments.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.worcul.FirebaseMessageDataFile
import com.project.worcul.MainActivity
import com.project.worcul.R
import com.project.worcul.UserDetailsDataFile
import com.project.worcul.databinding.FragmentLoginBinding
import com.project.worcul.databinding.FragmentSignupFragementBinding

class signupFragement : Fragment() {
    private lateinit var binding: FragmentSignupFragementBinding
    private lateinit var auth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentSignupFragementBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = Firebase.auth   // a class in firebase console(already written)
        database = Firebase.database.reference

        binding.signUpPatient.setOnClickListener {
            val email = binding.mailaddress.text.toString().trim()   //storing typed email text on a variable
            val password = binding.password.text.toString().trim()   //storing typed password on a variable
            if (email.isEmpty() || password.isEmpty()) {  //giving condition of if email and password tray are empty or not
                Toast.makeText(activity, "enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createAccount(email, password)    //if email and password are not empty then implementing function(signup) and passing 2 parameter email and password
        }


        return view
    }

    private fun createAccount(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE

        activity?.let {
            auth.createUserWithEmailAndPassword(email, password)    //a function of firebase taking email and password
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {   //if values are passed on this then
                        auth.currentUser?.sendEmailVerification()     //implementing a function(sendemailverification()) to send email to entered email
                            ?.addOnCompleteListener { task ->
                                val user = auth.currentUser
                                if (task.isSuccessful) {
                                    Toast.makeText(activity, "Email is sent", Toast.LENGTH_SHORT)
                                        .show()
                                    showFragment(loginFragment())
                                    // Sign in success, update UI with the signed-in user's informationval
                                    val user = auth.currentUser
                                    val name = binding.PatientName.text.toString().trim()
                                    val email = binding.mailaddress.text.toString().trim()   //storing typed email text on a variable
                                    val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

                                    writeNewUser(name, uid, email)
                                    binding.progressBar.visibility = View.GONE



                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(
                                        activity, "failed to create an account.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    updateUI(null)
                                    binding.progressBar.visibility = View.GONE

                                }
                            }
                    } else {
                        Toast.makeText(activity, "already an account", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE

                    }
                }
        }
    }

    private fun writeNewUser(nameOfUser: String,UID: String, EmailOfUser: String) {
        val User1 = UserDetailsDataFile(nameOfUser, EmailOfUser)
        database.child("Users").child("UserNames").child("$UID").setValue(User1)
    }

    fun showFragment(fragment: Fragment) {
        val fram1 = activity?.supportFragmentManager?.beginTransaction()
        fram1?.replace(R.id.fragLayout2, fragment)
        fram1?.commit()
    }

    private fun updateUI(nothing: Nothing?) {

    }
}