package com.ashish.mathongo.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashish.mathongo.Constants.TAG
import com.ashish.mathongo.adapters.SearchRvAdapter
import com.ashish.mathongo.data.viewmodels.RecipeViewModel
import com.ashish.mathongo.databinding.FragmentSearchBinding
import com.ashish.mathongo.utils.Extensions.toast
import com.ashish.mathongo.utils.LoadingDialog
import com.ashish.mathongo.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RecipeViewModel>()

    private lateinit var searchRecipeRvAdapter: SearchRvAdapter

    @Inject
    lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        searchRecipeRvAdapter = SearchRvAdapter(::searchedItemClick)
        return binding.root
    }

    private fun searchedItemClick(recipeId: Int) {
        val action =
            SearchFragmentDirections.actionSearchFragmentToSearchedRecipeDetailsFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchRecipeInputField.setStartIconOnLongClickListener {
            findNavController().popBackStack()
        }
        binding.searchRecipeRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchRecipeRv.setHasFixedSize(false)
        binding.searchRecipeRv.adapter = searchRecipeRvAdapter

        binding.searchRecipeInputField.editText?.addTextChangedListener(
            afterTextChanged = {
                if (it?.isNotEmpty() == true) {
                    viewModel.searchRecipes(
                        mapOf(
                            "query" to it.toString(),
                            "number" to "15",
                            "limitLicense" to "true"
                        )
                    )
                } else {
                    searchRecipeRvAdapter.submitList(null)
                }
            }
        )

        viewModel.searchRecipeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    toast(it.message)
                    Log.d(TAG, it.message.toString())
                }

                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    searchRecipeRvAdapter.submitList(it.data?.results)
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}