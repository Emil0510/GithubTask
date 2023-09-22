package com.emilabdurahmanli.githubtask.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.emilabdurahmanli.githubtask.R
import com.emilabdurahmanli.githubtask.adapter.OnItemClickListener
import com.emilabdurahmanli.githubtask.adapter.RecyclerAdapter
import com.emilabdurahmanli.githubtask.databinding.FragmentItemsBinding
import com.emilabdurahmanli.githubtask.network.Item
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class ItemsFragment : Fragment(), OnItemSelectedListener, OnItemClickListener{


    private lateinit var binding: FragmentItemsBinding
    private lateinit var viewModel: ItemsFragmentViewModel


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

        binding.itemsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.observeResponse().observe(viewLifecycleOwner, Observer {
            binding.itemsRecyclerView.adapter = RecyclerAdapter(it.items, this)
        })

        binding.dateSpinner.adapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            listOf("Last month", "Last week", "Last day")
        )

        binding.dateSpinner.onItemSelectedListener = this


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

        val dateOutput = format.format(date)
        viewModel.getResponse(dateOutput)

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
        fragment.arguments = bundle
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }

}