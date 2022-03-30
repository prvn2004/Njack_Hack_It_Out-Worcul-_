package com.project.worcul;

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.project.worcul.databinding.GitrepoListItemBinding
import java.security.AccessController.getContext

class GitRepoAdapter(
    val LinkList: ArrayList<GitRepoDataFile>,
    private val listener: (GitRepoDataFile) -> Unit
) :

    RecyclerView.Adapter<GitRepoAdapter.MyViewHolder>() { //class  which will take prameter(list of strings)
// ------------------------------------------------------------------------------------------------------------------------------------------


    private lateinit var binding: GitrepoListItemBinding

    //-------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GitRepoAdapter.MyViewHolder {  //function take parameter()


        binding =
            GitrepoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
        binding.ShareButton.setOnClickListener {
            listener(Link)
            val i = Intent(it.context, WriteActivity::class.java)
            i.putExtra("link", binding.Url.text.toString())
            i.putExtra("description", binding.description.text.toString())
            it.context.startActivity(i)
        }

//                setOnClickListener { listener(Link) }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    override fun getItemCount(): Int {  // this function is counting the size of list
        return LinkList.size // returning the size of list
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    class MyViewHolder(
        ItemViewBinding: GitrepoListItemBinding,
    ) :
        RecyclerView.ViewHolder(ItemViewBinding.root) {

        private val binding = ItemViewBinding
        fun friction(Link: GitRepoDataFile, position: Int, list: ArrayList<GitRepoDataFile>) {

            val context = itemView.getContext();
            binding.name.text = Link.getName()
            binding.description.text = Link.getdescription()
            binding.Url.text = Link.getUrl()
        }

    }

//--------------------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------------------

}
