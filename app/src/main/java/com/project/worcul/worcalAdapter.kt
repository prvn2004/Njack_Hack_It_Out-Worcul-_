package com.project.worcul;

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.worcul.databinding.WorculListItemBinding
import com.squareup.picasso.Picasso


class MyAdapter(
    val LinkList: ArrayList<worculDataFile>,
) :

    RecyclerView.Adapter<MyAdapter.MyViewHolder>() { //class  which will take prameter(list of strings)
// ------------------------------------------------------------------------------------------------------------------------------------------

    private lateinit var binding: WorculListItemBinding

    //---------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {  //function take parameter()


        binding =
            WorculListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
        Glide.with(holder.itemView.context).load(LinkList.get(position).getLink()).centerInside().into(binding.imageView7)
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    override fun getItemCount(): Int { // this function is counting the size of list
        return LinkList.size // returning the size of list
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    class MyViewHolder(
        ItemViewBinding: WorculListItemBinding,
    ) :
        RecyclerView.ViewHolder(ItemViewBinding.root) {

        private val binding = ItemViewBinding
        fun friction(Link: worculDataFile, position: Int, list: ArrayList<worculDataFile>) {
            val context = itemView.getContext();
            val linkString = Link.getLink().toString()
            val name = Link.getName().toString()
            binding.name.text = name
            binding.text.text = Link.getText().toString()
            binding.profile.setImageResource(R.drawable.profile)
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------

}