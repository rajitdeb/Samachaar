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

        newsViewModel.readLanguageAndSource.asLiveData().observe(viewLifecycleOwner, { value ->
            languagePref = value.lang
            languageIdPref = value.languageId
            sourcePref = value.source
            sourceIdPref = value.sourceId
            updateChip(value.languageId, binding.languagesChipGroup)
            updateChip(value.sourceId, binding.sourcesChipGroup)
        })

        Toast.makeText(
            requireContext(),
            "Lang: $languagePref, Source: $sourcePref",
            Toast.LENGTH_SHORT
        ).show()

        binding.apply {
            languagesChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
                val chip = group.findViewById<Chip>(selectedChipId)
                val selectedLanguage = chip.text.toString().lowercase(Locale.ROOT)
                languagePref = selectedLanguage
                languageIdPref = selectedChipId
            }

            sourcesChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
                val chip = group.findViewById<Chip>(selectedChipId)
                val selectedSource = chip.text.toString()
                Log.d("search article", "search article: chip id: $selectedChipId")
                sourcePref = selectedSource
                sourceIdPref = selectedChipId
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