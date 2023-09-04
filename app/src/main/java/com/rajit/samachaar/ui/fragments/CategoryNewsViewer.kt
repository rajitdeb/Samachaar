package com.rajit.samachaar.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rajit.samachaar.adapter.MyNewsAdapter
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.databinding.FragmentCategoryNewsViewerBinding
import com.rajit.samachaar.interfaces.OnArticleClickListener
import com.rajit.samachaar.viewmodel.MainViewModel
import com.rajit.samachaar.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryNewsViewer : Fragment(), OnArticleClickListener {

    private var _binding: FragmentCategoryNewsViewerBinding? = null

    // _binding!! is given because we know that this will be null only if used outside onCreateView()
    // and onDestroyView()
    private val binding get() = _binding!!

    private val args by navArgs<CategoryNewsViewerArgs>()
    private val mAdapter: MyNewsAdapter by lazy { MyNewsAdapter(this) }
    private val mainViewModel by viewModels<MainViewModel>()
    private val newsViewModel by viewModels<NewsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryNewsViewerBinding.inflate(inflater, container, false)

        mAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.categoryNewsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mAdapter
        }

        // Replaced lifecycleScope.launchWhenResumed with lifecycle.repeatOnLifecycle
        // for performance efficiency
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                newsViewModel.readCountryAndCode.collect {
                    mainViewModel.getTopCategoryHeadlines(it.countryCode, args.categoryName)
                        .observe(viewLifecycleOwner) { pagingData ->
                            mAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                        }
                }
            }
        }

        mAdapter.addLoadStateListener { loadState ->
            binding.apply {
                categoryNewsRv.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                errorTv.isVisible = loadState.source.refresh is LoadState.Error
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                btnRetry.setOnClickListener {

                    // Replaced lifecycleScope.launchWhenResumed with lifecycle.repeatOnLifecycle
                    // for performance efficiency
                    lifecycleScope.launch {
                        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                            newsViewModel.readCountryAndCode.collect {
                                mainViewModel.getTopCategoryHeadlines(
                                    it.countryCode,
                                    args.categoryName
                                ).observe(viewLifecycleOwner) { pagingData ->
                                        mAdapter.submitData(
                                            viewLifecycleOwner.lifecycle,
                                            pagingData
                                        )
                                }
                            }
                        }
                    }

                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArticleClick(article: Article?, category: String) {

        if (article?.title != null && article.url != null) {
            val action = CategoryNewsViewerDirections.actionCategoryNewsViewerToDetailsActivity(
                article = article, categoryName = args.categoryName
            )
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Article Not Available", Toast.LENGTH_SHORT).show()
        }
    }
}