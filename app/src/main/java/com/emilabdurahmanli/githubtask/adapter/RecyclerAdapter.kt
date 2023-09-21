package com.emilabdurahmanli.githubtask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emilabdurahmanli.githubtask.databinding.RecyclerRowBinding
import com.emilabdurahmanli.githubtask.network.Item
import com.squareup.picasso.Picasso

class RecyclerAdapter(var list : List<Item>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(var binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.DescriptionText.setText("Description: ${list[position].description}")
        holder.binding.starCountText.setText("Star Count: ${list[position].stargazers_count}")
        Picasso.get().load(list[position].owner.avatar_url).into(holder.binding.userAvatarIV)
        holder.binding.usernameText.setText("Username: ${list[position].name} Login: ${list[position].owner.login}")
    }

}