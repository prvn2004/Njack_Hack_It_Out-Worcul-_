package com.project.worcul.LoginFragments.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.worcul.*
import com.project.worcul.databinding.FragmentLoginBinding

class loginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = Firebase.auth   // a class in firebase console(already written)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!
        binding.imageButton.setOnClickListener {
            Toast.makeText(activity, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }



        binding.signInButton.setOnClickListener {
            val email = binding.mailaddressPatient.text.toString()
                .trim()    //storing typed email text on a variable
            val password = binding.passwordPatient.text.toString()
                .trim()    //storing typed password on a variable
            if (email.isEmpty() || password.isEmpty()) {      //giving condition of if email and password tray are empty or not
                Toast.makeText(activity, "enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signIn(email, password)
            //if email and password are not empty then implementing function(signin) and passing 2 parameter email and password
        }

        return view
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        val account = activity?.let { GoogleSignIn.getLastSignedInAccount(it) }
        account?.let { UpdateUI(it) }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
                val editor = preferences.edit()
                editor.putString("login", "Login")
                editor.apply()
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun signIn(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE

        activity?.let {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                        binding.progressBar.visibility = View.GONE


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar.visibility = View.GONE

                        return@addOnCompleteListener
                    }

                    // [END sign_in_with_email]
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (auth.currentUser != null) {
            if (auth.currentUser!!.isEmailVerified) {
                val email = binding.mailaddressPatient.text.toString()
                    .trim()    //storing typed email text on a variable
                val password = binding.passwordPatient.text.toString()
                    .trim()    //storing typed password on a variable
                val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
                val editor = preferences.edit()
                editor.putString("EmailLogin", "login")
                editor.putString("EMAIL", email)
                editor.apply()
                val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                val database1 =
                    Firebase.database.getReference().child("Users").child("UserName").child("$uid")
                database1.get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val snapshot = it.result
                        val email = snapshot.child("nameUser").getValue(String::class.java)
                        Toast.makeText(activity, "user:$email", Toast.LENGTH_SHORT).show()
                    } else {
                    }
                }

                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(activity, "Email not verified", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}