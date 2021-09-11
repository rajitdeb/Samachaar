package com.rajit.samachaar.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajit.samachaar.adapter.FavouritesAdapter
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.databinding.FragmentFavoritesBinding
import com.rajit.samachaar.interfaces.OnArticleClickListener
import com.rajit.samachaar.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(), OnArticleClickListener {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: FavouritesAdapter by lazy { FavouritesAdapter(this) }
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)

        binding.favouritesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mAdapter
        }

        mainViewModel.favouriteArticles.observe(viewLifecycleOwner, {
            mAdapter.setData(it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArticleClick(article: Article, category: String) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailsActivity(
            article = article,
            categoryName = category
        )
        findNavController().navigate(action)
    }
}