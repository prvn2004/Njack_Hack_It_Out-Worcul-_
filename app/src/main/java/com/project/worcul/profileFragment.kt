package com.project.worcul

import android.R
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.worcul.databinding.FragmentProfileBinding
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL


class profileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient

    lateinit var recyclerView: RecyclerView
    private lateinit var LinkModel: ArrayList<myMessageDataFile>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        LinkModel = arrayListOf<myMessageDataFile>()

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(activity, WriteActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                com.project.worcul.R.id.logout -> {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    signOut()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
            true
        }
        getMessages()
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.project.worcul.R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!


        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto: Uri? = acct.photoUrl
            binding.textView.text = personName
                binding.email.text = personEmail
                val imageView = binding.imageView2
                binding.progressBar.visibility = View.VISIBLE
                Glide.with(requireActivity()).load(personPhoto).centerCrop().into(imageView)
                binding.progressBar.visibility = View.GONE

        } else {
            val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val email1 = preferences.getString("Email", "email@gmail.com")
            val name = preferences.getString("Email", "Unknown User")
            binding.textView.text = name.toString()
            binding.email.text = email1.toString()

        }


        return binding.root
    }

    private fun getMessages() {
        binding.progressBar.visibility = View.VISIBLE
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val database1 = Firebase.database.getReference().child("Users/messages/$uid")
        database1.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                binding.home.progressBar.setVisibility(View.GONE)
                if (snapshot.exists()) {
                    LinkModel.clear()

                    for (userSnapshot in snapshot.children) {
                        val trueUser =
                            userSnapshot.getValue(myMessageDataFile::class.java)
                        LinkModel.add(0, trueUser!!)
                    }
                    val recyclerView = binding.recyclerView
                    recyclerView.adapter = myMessageAdapter(LinkModel)
                    binding.progressBar.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE

            }

        })
    }

    private fun signOut() {
        binding.progressBar.visibility = View.VISIBLE

        activity?.let {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(it, OnCompleteListener<Void?> {
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    binding.progressBar.visibility = View.GONE

                })
        }
    }
}


