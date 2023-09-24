package com.emilabdurahmanli.githubtask.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.emilabdurahmanli.githubtask.R
import com.emilabdurahmanli.githubtask.databinding.FragmentDetailsBinding
import com.emilabdurahmanli.githubtask.network.model.Item
import com.emilabdurahmanli.githubtask.network.model.ItemRoom
import com.emilabdurahmanli.githubtask.network.model.Owner
import com.emilabdurahmanli.githubtask.room.AppDatabase
import com.squareup.picasso.Picasso


class DetailsFragment : Fragment() {


    private lateinit var binding : FragmentDetailsBinding
    private lateinit var item : Item
    private lateinit var viewModel: ItemsFragmentViewModel
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ItemsFragmentViewModel::class.java]
        item = arguments?.getSerializable("Item") as Item
        val fromFavorite = arguments?.getBoolean("fromFavorite")
        binding.backButton.setOnClickListener {
            if(fromFavorite == true){
                loadFragment(FavoritesFragment())
            }else{
                loadFragment(ItemsFragment())
            }
        }
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "FavoriteItems"
        ).build()

        viewModel.getFavoriteList( db.itemDao())

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
            binding.favoriteBtn.isSelected = itemList.contains(item)
        })

        Picasso.get().load(item.owner.avatar_url).into(binding.userAvatarImage)
        binding.usernameLoginTV.setText("Username: ${item.name?.trim()}\nLogin: ${item.owner.login?.trim()}")
        binding.descriptionTV.setText("Description: ${item.description?.trim()}")
        binding.starsCountTV.setText("Stars: ${item.stargazers_count}")
        binding.forksCount.setText("Forks: ${item.forks}")
        binding.languageText.setText("Language: ${item.language}")
        binding.createdDateText.setText("Created date: ${item.created_at}")
        binding.urlText.setText(item.html_url)

        binding.urlText.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(item.html_url)
            startActivity(i)
        }


        binding.favoriteBtn.setOnClickListener {
            val itemRoom = ItemRoom(
                item.id,
                item.name,
                item.full_name,
                item.private,
                item.owner.login,
                item.owner.avatar_url,
                item.description,
                item.stargazers_count,
                item.language,
                item.forks,
                item.created_at,
                item.html_url
            )
            if(binding.favoriteBtn.isSelected){
                viewModel.deleteFromFavorites(db.itemDao(), itemRoom)
            }else{
                viewModel.addToFavorites(db.itemDao(), itemRoom)
            }
            binding.favoriteBtn.isSelected = !(binding.favoriteBtn.isSelected)
        }

    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }

}