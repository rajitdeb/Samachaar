package com.rajit.samachaar.ui.fragments

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajit.samachaar.R
import com.rajit.samachaar.adapter.MyNewsAdapter
import com.rajit.samachaar.adapter.NewsLoadStateAdapter
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.databinding.FragmentSearchArticlesBinding
import com.rajit.samachaar.interfaces.OnArticleClickListener
import com.rajit.samachaar.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchArticlesFragment : Fragment(), OnArticleClickListener, SearchView.OnQueryTextListener {

    private var _binding: FragmentSearchArticlesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()
    private val mAdapter: MyNewsAdapter by lazy { MyNewsAdapter(this) }

    private var searchView: SearchView? = null

    private var search_query: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchArticlesBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.searchArticlesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter.withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter { mAdapter.retry() },
                footer = NewsLoadStateAdapter { mAdapter.retry() }
            )
            setHasFixedSize(true)
        }

        mAdapter.addLoadStateListener { loadStates ->
            binding.apply {
                progressBar.isVisible = loadStates.source.refresh is LoadState.Loading
                searchArticlesRv.isVisible = loadStates.source.refresh is LoadState.NotLoading
                errorTv.isVisible = loadStates.source.refresh is LoadState.Error
                btnRetry.isVisible = loadStates.source.refresh is LoadState.Error
                btnRetry.setOnClickListener {
                    Log.d("Search Query","Search Query: $search_query")
                    search_query?.let { query -> searchArticle(query) }
                }
            }
        }

        return binding.root
    }

    fun searchArticle(searchQuery: String) {
        mainViewModel.searchArticle(searchQuery).observe(viewLifecycleOwner, {
            mAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.search_articles_btn)
        searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArticleClick(article: Article, category: String) {
        val action = SearchArticlesFragmentDirections
            .actionSearchArticlesFragmentToDetailsActivity(article = article, "")

        findNavController().navigate(action)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchArticle(query)
            search_query = query
            searchView?.clearFocus()
        }else {
            Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}