package com.rajit.samachaar.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajit.samachaar.adapter.MyNewsAdapter
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.databinding.FragmentCategoryNewsViewerBinding
import com.rajit.samachaar.interfaces.OnArticleClickListener
import com.rajit.samachaar.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

import com.google.android.material.bottomnavigation.BottomNavigationView

@AndroidEntryPoint
class CategoryNewsViewer : Fragment(), OnArticleClickListener {

    private var _binding: FragmentCategoryNewsViewerBinding? = null
    // _binding!! is given because we know that this will be null only if used outside onCreateView()
    // and onDestroyView()
    private val binding get() = _binding!!

    private val args by navArgs<CategoryNewsViewerArgs>()
    private val mAdapter: MyNewsAdapter by lazy { MyNewsAdapter(this) }
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryNewsViewerBinding.inflate(inflater, container, false)

        binding.categoryNewsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mAdapter
        }

        mainViewModel.applyQueries("in", args.categoryName)
        mainViewModel.getTopCategoryHeadlines()

        mainViewModel.topCategoryHeadlines.observe(viewLifecycleOwner, {
            mAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArticleClick(article: Article) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsActivity(article)
        findNavController().navigate(action)
    }
}