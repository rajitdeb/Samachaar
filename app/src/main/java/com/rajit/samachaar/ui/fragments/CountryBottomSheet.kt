package com.rajit.samachaar.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rajit.samachaar.R
import com.rajit.samachaar.data.local.entity.Country
import com.rajit.samachaar.databinding.CountryBottomSheetBinding
import com.rajit.samachaar.util.Constants
import com.rajit.samachaar.viewmodel.MainViewModel
import com.rajit.samachaar.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountryBottomSheet : BottomSheetDialogFragment() {

    private var _binding: CountryBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()
    private val newsViewModel by viewModels<NewsViewModel>()

    private var country = Constants.DEFAULT_COUNTRY_PREFERENCES
    private var countryCode = Constants.DEFAULT_COUNTRY_CODE_PREFERENCES

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = CountryBottomSheetBinding.inflate(inflater, container, false)

        val countryList = mutableListOf<String>()
        var countryModelList = emptyList<Country>()

        lifecycleScope.launch {
            mainViewModel.getAllCountriesList().observe(viewLifecycleOwner) {
                countryModelList = it
                for (country in it) {
                    countryList.add(country.countryName)
                }
            }
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.bottom_sheet_list_item, countryList)
        (binding.countrySelector.editText as AutoCompleteTextView).setAdapter(adapter)

        lifecycleScope.launch {
            newsViewModel.getCountryAndCode().observe(viewLifecycleOwner) {
                country = it.countryName
                countryCode = it.countryCode

                (binding.countrySelector.editText as AutoCompleteTextView).setText(country, false)

                Toast.makeText(
                    requireContext(),
                    "BottomSheet: $country, $countryCode",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

//        newsViewModel.readCountryAndCode.asLiveData().observe(viewLifecycleOwner) {
//            country = it.countryName
//            countryCode = it.countryCode
//
//            (binding.countrySelector.editText as AutoCompleteTextView).setText(country, false)
//
//            Toast.makeText(
//                requireContext(),
//                "BottomSheet: $country, $countryCode",
//                Toast.LENGTH_SHORT
//            ).show()
//        }

        (binding.countrySelector.editText as AutoCompleteTextView)
            .setOnItemClickListener { _, _, position, _ ->
                val selectedCountry = countryModelList[position]
                country = selectedCountry.countryName
                countryCode = selectedCountry.countryCode
            }

        binding.submitBtn.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Saving: country: $country, code: $countryCode",
                Toast.LENGTH_SHORT
            ).show()
            newsViewModel.saveCountryAndCode(country, countryCode)
            findNavController().navigate(R.id.action_countryBottomSheet_to_home_fragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}