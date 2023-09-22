package com.emilabdurahmanli.githubtask.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emilabdurahmanli.githubtask.R
import com.emilabdurahmanli.githubtask.databinding.FragmentDetailsBinding
import com.emilabdurahmanli.githubtask.network.Item
import com.squareup.picasso.Picasso


class DetailsFragment : Fragment() {


    private lateinit var binding : FragmentDetailsBinding
    private lateinit var item : Item
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
        item = arguments?.getSerializable("Item") as Item
        binding.backButton.setOnClickListener {
                loadFragment(ItemsFragment())
        }

        Picasso.get().load(item.owner.avatar_url).into(binding.userAvatarImage)
        binding.usernameLoginTV.setText("Username: ${item.name}\nLogin: ${item.owner.login}")
        binding.descriptionTV.setText("Description: ${item.description}")
        binding.starsCountTV.setText("Stars: ${item.stargazers_count}")
        binding.forksCount.setText("Forks: ${item.forks}")
        binding.languageText.setText("Language: ${item.language}")
        binding.createdDateText.setText("Created date: ${item.created_at}")

    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }

}