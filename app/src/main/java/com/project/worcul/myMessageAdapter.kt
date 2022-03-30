package com.project.worcul;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.project.worcul.databinding.MymessageListItemBinding
import com.project.worcul.databinding.WorculListItemBinding

class myMessageAdapter(
    val LinkList: ArrayList<myMessageDataFile>,
) :

    RecyclerView.Adapter<myMessageAdapter.MyViewHolder>() { //class  which will take prameter(list of strings)
// ------------------------------------------------------------------------------------------------------------------------------------------


    private lateinit var binding: MymessageListItemBinding

    //-------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): myMessageAdapter.MyViewHolder {  //function take parameter()


        binding =
            MymessageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

//                setOnClickListener { listener(Link) }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    override fun getItemCount(): Int {  // this function is counting the size of list
        return LinkList.size // returning the size of list
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    class MyViewHolder(
        ItemViewBinding: MymessageListItemBinding,
    ) :
        RecyclerView.ViewHolder(ItemViewBinding.root) {

        private val binding = ItemViewBinding
        fun friction(Link: myMessageDataFile, position: Int, list: ArrayList<myMessageDataFile>) {

            val context = itemView.getContext();
            binding.name.text = Link.getName()
            binding.text.text = Link.getText()
            binding.profile.setImageResource(R.drawable.profile)

        }

    }

//--------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------

}
