package com.project.worcul

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.project.worcul.databinding.FragmentArticleBinding
import org.json.JSONArray
import org.json.JSONObject

class ArticleFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    private lateinit var LinkModel: ArrayList<ArticleDataFile>

    private lateinit var binding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        val view = binding.root
        LinkModel = arrayListOf<ArticleDataFile>()

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val itemList = ArrayList<ArticleDataFile>()

        binding.progressBar.visibility = View.VISIBLE
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET,
            "https://free-news.p.rapidapi.com/v1/search?q=learn%20coding&lang=en",
                Response.Listener<String> { response ->

                val jsonOBJECT = JSONObject(response);
                Log.d("here", response)
                try {
                    val jsonArrayMain = jsonOBJECT.getJSONArray("articles")
                    for (i in 0..jsonArrayMain.length()) {
                        val jsonobjectMain = jsonArrayMain.getJSONObject(i)
                        val author = jsonobjectMain.getString("author")
                        val title = jsonobjectMain.getString("title")
                        val content = jsonobjectMain.getString("summary")
                        val url = jsonobjectMain.getString("link").toString()
                        val urlImage = jsonobjectMain.getString("media").toString()
                        val source = jsonobjectMain.getString("clean_url")
                        val time = jsonobjectMain.getString("published_date")

                        itemList.add(
                            ArticleDataFile(
                                title,
                                content,
                                url,
                                urlImage,
                                source,
                                author,
                                time
                            )
                        )
                    }
                    binding.progressBar.visibility = View.GONE
//



                } catch (e: Exception) {


                    e.printStackTrace()
                    Toast.makeText(
                        activity,
                        "$e", Toast.LENGTH_LONG
                    ).show()
                }

                var adaptor = ArticleAdapter(itemList)
                binding.recyclerView.adapter = adaptor
                binding.progressBar.visibility = View.GONE


            },
            Response.ErrorListener { error ->

                Toast.makeText(
                    activity,
                    "$error", Toast.LENGTH_LONG
                ).show()
                binding.progressBar.visibility = View.GONE
            }) {
            override fun getParams(): Map<String, String> {

                val params1: HashMap<String, String> = HashMap()
                return params1
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["X-RapidAPI-Key"] = "680a1a98a8msh600afe9795a6dd2p12e917jsncc8401cf533e"
                params["X-RapidAPI-Host"] = "free-news.p.rapidapi.com"

                return params
            }

        }
        val rq = Volley.newRequestQueue(activity)
        rq.add(stringRequest)

        recyclerView.adapter = ArticleAdapter(itemList)


        return view
    }

}