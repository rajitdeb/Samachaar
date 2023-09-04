package com.rajit.samachaar.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.rajit.samachaar.R
import com.rajit.samachaar.databinding.FilterBottomSheetBinding
import com.rajit.samachaar.util.Constants
import com.rajit.samachaar.viewmodel.MainViewModel
import com.rajit.samachaar.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SourceAndLanguageBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var languagePref: String = Constants.DEFAULT_LANGUAGE_PREFERENCE_VALUE
    private var languageIdPref: Int = 0
    private var sourcePref: String = Constants.DEFAULT_SOURCE_PREFERENCE_VALUE
    private var sourceIdPref: Int = 0

    private val newsViewModel by viewModels<NewsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FilterBottomSheetBinding.inflate(inflater, container, false)

        newsViewModel.readLanguageAndSource.asLiveData().observe(viewLifecycleOwner) { value ->
            languagePref = value.lang
            languageIdPref = value.languageId
            sourcePref = value.source
            sourceIdPref = value.sourceId
            updateChip(value.languageId, binding.languagesChipGroup)
            updateChip(value.sourceId, binding.sourcesChipGroup)
        }

        binding.apply {
            languagesChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
                val chip = group.findViewById<Chip>(selectedChipId[0])
                val selectedLanguage = chip.text.toString().lowercase(Locale.ROOT)
                languagePref = selectedLanguage
                languageIdPref = selectedChipId[0]
            }

            sourcesChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->

                val chip = group.findViewById<Chip>(selectedChipId[0])
                val selectedSource = chip.text.toString()
                sourcePref = selectedSource
                sourceIdPref = selectedChipId[0]
            }

            submitBtn.setOnClickListener {
                newsViewModel.saveLanguageAndSource(
                    languagePref,
                    languageIdPref,
                    sourcePref,
                    sourceIdPref
                )

                findNavController().navigate(R.id.action_sourceAndLanguageBottomSheet_to_search_articles_fragment)
            }

        }

        return binding.root
    }

    private fun updateChip(resourceId: Int, chipGroup: ChipGroup) {
        if (resourceId != 0) {
            try {
                chipGroup.findViewById<Chip>(resourceId).isChecked = true
            } catch (e: Exception) {
                Log.d("ChipGroup Error", "updateChip: $e")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}