package com.emilabdurahmanli.githubtask.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emilabdurahmanli.githubtask.R
import com.emilabdurahmanli.githubtask.databinding.RecyclerRowBinding
import com.emilabdurahmanli.githubtask.network.Item
import com.squareup.picasso.Picasso

class RecyclerAdapter(var list : List<Item>, var listener : OnItemClickListener) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(var binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(list[position].description == null){
            holder.binding.DescriptionText.setText("Description: ${"Default text of this task"}")
            holder.binding.seeAllButton.visibility = View.GONE
        }else{
            holder.binding.DescriptionText.setText("Description: ${list[position].description}")
            if(holder.binding.DescriptionText.lineCount == 3){
                holder.binding.seeAllButton.visibility = View.VISIBLE
            }else{
                holder.binding.seeAllButton.visibility = View.GONE
            }
        }
        holder.binding.starCountText.setText("Star Count: ${list[position].stargazers_count}")
        Picasso.get().load(list[position].owner.avatar_url).into(holder.binding.userAvatarIV)
        holder.binding.usernameText.setText("Username: ${list[position].name}\nLogin: ${list[position].owner.login}")
        holder.binding.root.setOnClickListener {
            listener.onCLick(list[position])
        }

        holder.binding.seeAllButton.setOnClickListener {
            holder.binding.DescriptionText.maxLines = Integer.MAX_VALUE
            holder.binding.seeAllButton.visibility = View.GONE

        }

    }

}

interface OnItemClickListener{
    fun onCLick(item: Item)
}