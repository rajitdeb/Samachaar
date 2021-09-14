package com.rajit.samachaar.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
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
import com.rajit.samachaar.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchArticlesFragment : Fragment(), OnArticleClickListener, SearchView.OnQueryTextListener {

    private var _binding: FragmentSearchArticlesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()
    private val newsViewModel by viewModels<NewsViewModel>()
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
//                    Log.d("Search Query", "Search Query: $search_query")
//                    if (sourcePref != null) {
//
//                        search_query?.let { query ->
//                            searchArticle(
//                                query,
//                                languagePref,
//                                sourcePref
//                            )
//                        }
//                    } else {
//                        search_query?.let { query -> searchArticle(query, languagePref) }
//                    }
                }
            }
        }

        binding.filterBtn.setOnClickListener {
            findNavController().navigate(R.id.action_search_articles_fragment_to_sourceAndLanguageBottomSheet)
            // go to filter bottom sheet
        }

        return binding.root
    }

    fun searchArticle(searchQuery: String, language: String, source: String) {
        val languageVar = checkLanguageAbv(language)
        val sourceVar = checkSourceAbv(source)
        Log.d("search article", "search article: $languageVar, $sourceVar")
        if (!sourceVar.contains("all")) {
            mainViewModel.searchArticle(searchQuery, languageVar, sourceVar)
                .observe(viewLifecycleOwner, {
                    mAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                })
        } else {
            mainViewModel.searchArticle(searchQuery, languageVar).observe(viewLifecycleOwner, {
                mAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            })
        }
    }

    private fun checkSourceAbv(source: String): String {
        Toast.makeText(requireContext(), "Source: $source", Toast.LENGTH_SHORT).show()
            return when {
                source.contains("Times of India") -> "the-times-of-india"
                source.contains("Google News - In") -> "google-news-in"
                source.contains("BBC") -> "bbc-news"
                source.contains("Lenta") -> "lenta"
                source.contains("Focus") -> "focus"
                source.contains("Al-Jazeera") -> "al-jazeera-english"
                source.contains("ABC") -> "abc-news"
                source.contains("Aftenposten") -> "aftenposten"
                source.contains("Bloomberg") -> "bloomberg"
                source.contains("Business Inside") -> "business-insider"
                source.contains("CNN") -> "cnn"
                source.contains("National Geographic") -> "national-geographic"
                source.contains("IGN") -> "ign"
                source.contains("Google News - Fr") -> "google-news-fr"
                source.contains("Google News - Au") -> "google-news-au"
                source.contains("Google News - Ca") -> "google-news-ca"
                source.contains("Four Four Two - UK") -> "four-four-two"
                source.contains("Google News - Il") -> "google-news-il"
                source.contains("The Jerusalem Post") -> "the-jerusalem-post"
                else -> "all"
            }
    }

    private fun checkLanguageAbv(language: String): String {
        return when {
            language.contains("en") -> "en"
            language.contains("ru") -> "ru"
            language.contains("es") -> "es"
            language.contains("fr") -> "fr"
            language.contains("de") -> "de"
            else -> "en"
        }
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
        if (query != null) {
//            searchArticle(query, "en", "BBC")
            newsViewModel.readLanguageAndSource.asLiveData().observe(viewLifecycleOwner, {
                Log.d("searchArticle: Checking Preferences", "searchArticle: checking Preferences: ${it.lang}, ${it.source}")
                searchArticle(query, it.lang, it.source)
            })
            search_query = query
            searchView?.clearFocus()
        } else {
            Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT)
                .show()
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}