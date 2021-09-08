package com.rajit.samachaar.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajit.samachaar.R
import com.rajit.samachaar.adapter.CategoryAdapter
import com.rajit.samachaar.adapter.MyNewsAdapter
import com.rajit.samachaar.adapter.NewsLoadStateAdapter
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.data.network.model.Category
import com.rajit.samachaar.databinding.FragmentHomeBinding
import com.rajit.samachaar.interfaces.OnArticleClickListener
import com.rajit.samachaar.interfaces.OnCategoryClickListener
import com.rajit.samachaar.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnArticleClickListener, OnCategoryClickListener {

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var listOfCategory: List<Category>

    private val mainViewModel by viewModels<MainViewModel>()
    private val mAdapter: MyNewsAdapter by lazy { MyNewsAdapter(this) }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        listOfCategory= listOf(
            Category("Business", R.drawable.business),
            Category("Entertainment", R.drawable.entertainment),
            Category("Health", R.drawable.health),
            Category("Sports", R.drawable.sports),
            Category("Science", R.drawable.science),
            Category("Technology", R.drawable.technology)
        )

        binding.countryFab.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_countryBottomSheet)
        }

        categoryAdapter = CategoryAdapter(this)

        binding.categoryRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            categoryAdapter.setData(listOfCategory)
            adapter = categoryAdapter
        }

        binding.topHeadlinesRv.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter.withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter { mAdapter.retry() },
                footer = NewsLoadStateAdapter { mAdapter.retry() }
            )
            setHasFixedSize(true)
        }

        mainViewModel.applyQueries("in")
        mainViewModel.getTopHeadlines()

        mainViewModel.topHeadlines.observe(viewLifecycleOwner, {
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

    override fun onClick(category: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToCategoryNewsViewer(category)
        findNavController().navigate(action)
    }
}