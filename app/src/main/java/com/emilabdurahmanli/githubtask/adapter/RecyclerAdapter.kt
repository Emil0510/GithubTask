package com.emilabdurahmanli.githubtask.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emilabdurahmanli.githubtask.databinding.RecyclerRowBinding
import com.emilabdurahmanli.githubtask.network.model.Item
import com.emilabdurahmanli.githubtask.network.model.ItemRoom
import com.emilabdurahmanli.githubtask.network.model.Owner
import com.squareup.picasso.Picasso

class RecyclerAdapter(
    var list: List<Item>,
    var favoriteList: List<ItemRoom>,
    private var listener: OnItemClickListener,
    private var favoriteListener: OnFavoriteButtonClickListener
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var favoriteItemList = mutableListOf<Item>()

    inner class ViewHolder(var binding: RecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.seeAllButton.visibility = View.GONE
        if (list[position].description == null) {
            holder.binding.DescriptionText.setText("Description: ${"Default text of this task"}")
        } else {
            holder.binding.DescriptionText.setText("Description: ${list[position].description?.trim()}")
            if (holder.binding.DescriptionText.lineCount > 3 && !list[position].isSeen) {
                holder.binding.seeAllButton.visibility = View.VISIBLE
            }
        }
        holder.binding.starCountText.setText("Star Count: ${list[position].stargazers_count}")
        Picasso.get().load(list[position].owner.avatar_url).into(holder.binding.userAvatarIV)
        holder.binding.usernameText.setText("Username: ${list[position].name?.trim()}\nLogin: ${list[position].owner.login?.trim()}")
        holder.binding.root.setOnClickListener {
            listener.onCLick(list[position])
        }

        holder.binding.seeAllButton.setOnClickListener {
            holder.binding.DescriptionText.maxLines = Integer.MAX_VALUE
            holder.binding.seeAllButton.visibility = View.GONE
            list[position].isSeen = true
        }


        holder.binding.favoriteButton.isSelected = favoriteItemList.contains(list[position])


        holder.binding.favoriteButton.setOnClickListener {
            it.isSelected = (!it.isSelected)
            val itemRoom = ItemRoom(
                list[position].id,
                list[position].name,
                list[position].full_name,
                list[position].private,
                list[position].owner.login,
                list[position].owner.avatar_url,
                list[position].description,
                list[position].stargazers_count,
                list[position].language,
                list[position].forks,
                list[position].created_at,
                list[position].html_url
            )
            favoriteListener.onCLick(itemRoom, it.isSelected)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(itemList : List<Item>, favoriteItemList : List<ItemRoom> ){
        list = itemList
        favoriteList = favoriteItemList
        for (i in favoriteList.indices) {
            val item = Item(
                favoriteList[i].id,
                favoriteList[i].name,
                favoriteList[i].full_name,
                favoriteList[i].private,
                Owner(favoriteList[i].login, favoriteList[i].avatar_url),
                favoriteList[i].description,
                favoriteList[i].stargazers_count,
                favoriteList[i].language,
                favoriteList[i].forks,
                favoriteList[i].created_at,
                favoriteList[i].html_url
            )
            this.favoriteItemList.add(item)
        }
        notifyDataSetChanged()
    }

}

interface OnFavoriteButtonClickListener {
    fun onCLick(item: ItemRoom, isAddFavorite: Boolean)
}

interface OnItemClickListener {
    fun onCLick(item: Item)
}