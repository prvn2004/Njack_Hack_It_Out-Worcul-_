package com.project.worcul

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.worcul.databinding.FragmentWorculBinding
import com.project.worcul.databinding.WorculListItemBinding


class worculFragment : Fragment() {
    private lateinit var binding: FragmentWorculBinding
    lateinit var recyclerView: RecyclerView
    private lateinit var LinkModel: ArrayList<worculDataFile>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        LinkModel = arrayListOf<worculDataFile>()

        binding = FragmentWorculBinding.inflate(inflater, container, false)
        val view = binding.root
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(activity, WriteActivity::class.java)
            startActivity(intent)
        }

        getMessages()
//        val itemList = ArrayList<worculDataFile>()
//        val datafile = worculDataFile(
//            "praveen kumar",
//            R.drawable.profile,
//            "https://stackoverflow.com/questions/14050813/how-to-make-an-image-fit-into-a-circular-frame-in-android"
//        )
//        itemList.add(datafile)
//        itemList.add(datafile)
//        itemList.add(datafile)
//        itemList.add(datafile)
//        itemList.add(datafile)
//        itemList.add(datafile)
//        itemList.add(datafile)
//        itemList.add(datafile)
//        itemList.add(datafile)
//
//        var adaptor = MyAdapter(itemList)
//        binding.recyclerView.adapter = adaptor
        return view
    }

    private fun getMessages() {
        binding.progressBar.visibility = View.VISIBLE
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val database1 = Firebase.database.getReference().child("messages")
        database1.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                binding.home.progressBar.setVisibility(View.GONE)
                if (snapshot.exists()) {
                    LinkModel.clear()

                    for (userSnapshot in snapshot.children) {
                        val trueUser =
                            userSnapshot.getValue(worculDataFile::class.java)
                        LinkModel.add(0, trueUser!!)
                    }
                    val recyclerView = binding.recyclerView
                    recyclerView.adapter = MyAdapter(LinkModel)
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE

            }


        })
    }
}