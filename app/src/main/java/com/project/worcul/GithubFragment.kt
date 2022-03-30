package com.project.worcul

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.project.worcul.databinding.FragmentGithubBinding
import org.json.JSONArray
import org.json.JSONObject


class GithubFragment : Fragment() {
    private lateinit var binding: FragmentGithubBinding
    lateinit var recyclerView: RecyclerView
    private lateinit var LinkModel: ArrayList<GitRepoDataFile>
    private var FetchDoctors =
        ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        LinkModel = arrayListOf<GitRepoDataFile>()

        binding = FragmentGithubBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val itemList = ArrayList<GitRepoDataFile>()

        binding.profileLink.setOnClickListener {
           val link =  binding.profileLink.text.toString()
            val myUri: Uri = Uri.parse(link)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(myUri.toString())
            startActivity(i)
        }

        binding.searchButton.setOnClickListener {
            if(binding.editText.text.isNullOrEmpty()) {
                Toast.makeText(activity, "please enter username", Toast.LENGTH_SHORT).show()
            }else{
                getDetails()
                getRepos()
            }
        }


        return view
    }

    private fun getRepos() {
        val itemList = ArrayList<GitRepoDataFile>()
        val mainText =  binding.editText.text.trim()
        val mainUrl ="https://api.github.com/users/$mainText/repos"

        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, mainUrl, Response.Listener<String> { response ->

                val jsonArray = JSONArray(response);
                Log.d("here", response)
                try {
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val description = jsonObject.getString("description")
                        val url = jsonObject.getString("html_url")

                        itemList.add(
                            GitRepoDataFile(
                                name,
                                description,
                                url
                                )
                        )

                    }
                } catch (e: Exception) {

                    e.printStackTrace()
                    Toast.makeText(
                        activity,
                        "UserName Incorrect", Toast.LENGTH_LONG
                    ).show()
                }

                var adaptor = GitRepoAdapter(itemList){
                    SendRepo(GitRepoDataFile())
                }
                binding.recyclerView.adapter = adaptor


            }, Response.ErrorListener { error ->

                Toast.makeText(
                    activity,
                    "Username incorrect", Toast.LENGTH_LONG
                ).show()
            }) {
            override fun getParams(): Map<String, String> {

                val params1: HashMap<String, String> = HashMap()
//
                return params1
            }

        }
        val rq = Volley.newRequestQueue(activity)
        rq.add(stringRequest)

        binding.recyclerView.adapter = GitRepoAdapter(itemList){
            SendRepo(GitRepoDataFile())
        }

    }

    private fun getDetails() {
        val mainText = binding.editText.text.trim()
        val mainUrl ="https://api.github.com/users/$mainText"

        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Loading Profile")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, mainUrl, Response.Listener<String> { response ->
                Log.d("here", response)
                try {
                        val jsonObject: JSONObject = JSONObject(response)
                        val name = jsonObject.getString("name")
                        val bio = jsonObject.getString("bio")
                    val publicrepos = jsonObject.getInt("public_repos").toString()
                    val Url = jsonObject.getString("html_url")
                    val photoUrl = jsonObject.getString("avatar_url")
                    val lastSeen = jsonObject.getString("updated_at")

                    binding.Name.text = name
                    binding.Bio.text = bio
                    binding.profileLink.text = Url
                    binding.lastseen.text = "Last Seen On Github: $lastSeen"

                    binding.repoNum.text = "public_repos: $publicrepos"
                    Glide.with(requireActivity()).load(photoUrl).centerCrop().into(binding.profile)
                    if (progressDialog.isShowing) progressDialog.dismiss()




                } catch (e: Exception) {

                    e.printStackTrace()
                    Toast.makeText(
                        activity,
                        "UserName Incorrect", Toast.LENGTH_LONG
                    ).show()

                    if (progressDialog.isShowing) progressDialog.dismiss()

                }


            }, Response.ErrorListener { error ->

                Toast.makeText(
                    activity,
                    "UserName Incorrect", Toast.LENGTH_LONG
                ).show()
                if (progressDialog.isShowing) progressDialog.dismiss()

            }) {
            override fun getParams(): Map<String, String> {

                val params1: HashMap<String, String> = HashMap()
//
                return params1
            }

        }
        val rq = Volley.newRequestQueue(activity)
        rq.add(stringRequest)
    }

    private fun SendRepo(Link: GitRepoDataFile) {
    }
}