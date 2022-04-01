package com.project.worcul;

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.worcul.databinding.ArticleListItemBinding
import com.project.worcul.databinding.WorculListItemBinding
import com.squareup.picasso.Picasso


class ArticleAdapter(
    val LinkList: ArrayList<ArticleDataFile>,
) :

    RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() { //class  which will take prameter(list of strings)
// ------------------------------------------------------------------------------------------------------------------------------------------

    private lateinit var binding: ArticleListItemBinding

    //---------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {  //function take parameter()


        binding =
            ArticleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(
            binding
        ) //returning MyViewHolder class with a view inside it
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val Link = LinkList[position]
        holder.friction(Link, position, LinkList)
        binding.shareButton.setOnClickListener {
            val i = Intent(it.context, WriteActivity::class.java)
            i.putExtra("link", LinkList.get(position).getTitle().toString())
            i.putExtra("description", LinkList.get(position).geturlOfPost().toString())
            it.context.startActivity(i)
        }

        binding.url.setOnClickListener {
            val link = LinkList.get(position).geturlOfPost().toString()
            val myUri: Uri = Uri.parse(link)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(myUri.toString())
            it.context.startActivity(i)
        }
        Glide.with(holder.itemView.context).load(LinkList.get(position).geturlOfImage()).centerInside().into(binding.profile)
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    override fun getItemCount(): Int {
        return LinkList.size
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    class MyViewHolder(
        ItemViewBinding: ArticleListItemBinding,
    ) :
        RecyclerView.ViewHolder(ItemViewBinding.root) {

        private val binding = ItemViewBinding
        fun friction(Link: ArticleDataFile, position: Int, list: ArrayList<ArticleDataFile>) {
            val context = itemView.getContext();
            val title = Link.getTitle().toString()
            val author = Link.getauthorofPost().toString()
           binding.name.text = "$title - $author"
            binding.print.text = Link.getsourceName().toString()
            binding.url.text = Link.geturlOfPost().toString()
            binding.timeText.text = Link.getTime().toString()
            binding.text.text = Link.getContent().toString()


        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------

}