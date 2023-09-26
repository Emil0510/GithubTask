package com.emilabdurahmanli.githubtask.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.emilabdurahmanli.githubtask.R
import com.emilabdurahmanli.githubtask.adapter.OnFavoriteButtonClickListener
import com.emilabdurahmanli.githubtask.adapter.OnItemClickListener
import com.emilabdurahmanli.githubtask.adapter.RecyclerAdapter
import com.emilabdurahmanli.githubtask.databinding.FragmentFavoritesBinding
import com.emilabdurahmanli.githubtask.network.model.Item
import com.emilabdurahmanli.githubtask.network.model.ItemRoom
import com.emilabdurahmanli.githubtask.network.model.Owner
import com.emilabdurahmanli.githubtask.room.AppDatabase


class FavoritesFragment : Fragment(), OnItemClickListener, OnFavoriteButtonClickListener {


    private lateinit var viewModel: ItemsFragmentViewModel
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ItemsFragmentViewModel::class.java]
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "FavoriteItems"
        ).build()
        viewModel.getFavoriteList(db.itemDao())
        binding.backBtnFavorites.setOnClickListener {
            loadFragment(ItemsFragment())
        }

        binding.favoritesRV.adapter = RecyclerAdapter(listOf(), listOf(),this, this)

        binding.favoritesRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.observeFavoriteList().observe(viewLifecycleOwner, Observer {
            val itemList = mutableListOf<Item>()
            for (i in it.indices) {
                val item = Item(
                    it[i].id,
                    it[i].name,
                    it[i].full_name,
                    it[i].private,
                    Owner(it[i].login, it[i].avatar_url),
                    it[i].description,
                    it[i].stargazers_count,
                    it[i].language,
                    it[i].forks,
                    it[i].created_at,
                    it[i].html_url
                )
                itemList.add(item)
            }

            (binding.favoritesRV.adapter as RecyclerAdapter).updateData(itemList, it)
        })


    }



    override fun onCLick(item: ItemRoom, isAddFavorite: Boolean) {
        if(isAddFavorite){
            viewModel.addToFavorites(db.itemDao(), item)
        }else{
            viewModel.deleteFromFavorites(db.itemDao(), item)
        }
    }

    override fun onCLick(item: Item) {
        loadFragmentWithBundle(DetailsFragment(),item )
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    private  fun loadFragmentWithBundle(fragment: Fragment, item : Item){
        val transaction = parentFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putSerializable("Item",item)
        bundle.putBoolean("fromFavorite", true)
        fragment.arguments = bundle
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }


}