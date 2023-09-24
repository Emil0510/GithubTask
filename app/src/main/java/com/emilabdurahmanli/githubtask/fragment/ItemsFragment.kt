package com.emilabdurahmanli.githubtask.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.emilabdurahmanli.githubtask.R
import com.emilabdurahmanli.githubtask.adapter.OnFavoriteButtonClickListener
import com.emilabdurahmanli.githubtask.adapter.OnItemClickListener
import com.emilabdurahmanli.githubtask.adapter.RecyclerAdapter
import com.emilabdurahmanli.githubtask.databinding.FragmentItemsBinding
import com.emilabdurahmanli.githubtask.network.model.Item
import com.emilabdurahmanli.githubtask.network.model.ItemRoom
import com.emilabdurahmanli.githubtask.network.model.Response
import com.emilabdurahmanli.githubtask.room.AppDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class ItemsFragment : Fragment(), OnItemSelectedListener, OnItemClickListener, OnFavoriteButtonClickListener{


    private lateinit var binding: FragmentItemsBinding
    private lateinit var viewModel: ItemsFragmentViewModel
    private var favoriteList = listOf<ItemRoom>()
    private var itemList = mutableListOf<Item>()
    private var page = 1
    private lateinit var db : AppDatabase
    private lateinit var dateOutput : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentItemsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ItemsFragmentViewModel::class.java]
        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "FavoriteItems"
        ).build()

        binding.itemsRecyclerView.adapter = RecyclerAdapter(itemList, favoriteList, this, this)

        viewModel.getFavoriteList( db.itemDao())

        viewModel.observeFavoriteList().observe(viewLifecycleOwner, Observer {
            favoriteList = it
            (binding.itemsRecyclerView.adapter as RecyclerAdapter).updateData(itemList, favoriteList)
        })

        viewModel.observeResponse().observe(viewLifecycleOwner, Observer {
            if(page>1){
                it.items.forEach {
                    itemList.add(it)
                }
            }else{
                itemList = it.items as MutableList<Item>
            }
            (binding.itemsRecyclerView.adapter as RecyclerAdapter).updateData(itemList, favoriteList)
            binding.progressBar.visibility = View.GONE
        })

        binding.dateSpinner.adapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            listOf("Last month", "Last week", "Last day")
        )

        binding.dateSpinner.onItemSelectedListener = this


        binding.favoritesButtons.setOnClickListener {
            loadFragmentWithoutBundle(FavoritesFragment())
        }

        binding.itemsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!binding.itemsRecyclerView.canScrollVertically(1)){
                    page ++
                    viewModel.getResponse(dateOutput, page)
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val calendar: Calendar = Calendar.getInstance()
        val date: Date
        val format: SimpleDateFormat

        when (position) {
            0 -> {
                calendar.add(Calendar.MONTH, -1)
                date = calendar.getTime()
                format = SimpleDateFormat("yyyy-MM-dd")
            }

            1 -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                date = calendar.getTime()
                format = SimpleDateFormat("yyyy-MM-dd")
            }

            2 -> {

                calendar.add(Calendar.DAY_OF_YEAR, -1)
                date = calendar.getTime()
                format = SimpleDateFormat("yyyy-MM-dd")

            }

            else -> {
                calendar.add(Calendar.MONTH, -1)
                date = calendar.getTime()
                format = SimpleDateFormat("yyyy-MM-dd")
            }
        }
        page = 1
        dateOutput = format.format(date)
        viewModel.getResponse(dateOutput, page)
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onCLick(item: Item) {
        loadFragment(DetailsFragment(), item)
    }
    private  fun loadFragment(fragment: Fragment, item : Item){
        val transaction = parentFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putSerializable("Item",item)
        bundle.putBoolean("fromFavorite", false)
        fragment.arguments = bundle
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }
    private  fun loadFragmentWithoutBundle(fragment: Fragment){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }

    override fun onCLick(item: ItemRoom, isAddFavorite: Boolean) {
        if(isAddFavorite){
            viewModel.addToFavorites(db.itemDao(), item)
        }else{
            viewModel.deleteFromFavorites(db.itemDao(), item)
        }
    }

}