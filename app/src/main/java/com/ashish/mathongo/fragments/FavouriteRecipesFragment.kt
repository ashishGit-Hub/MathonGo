package com.ashish.mathongo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashish.mathongo.adapters.FavouriteRecipeRvAdapter
import com.ashish.mathongo.data.viewmodels.RecipeViewModel
import com.ashish.mathongo.databinding.FragmentFavouriteRecipesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private var _binding : FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!


    private lateinit var recipesRvAdapter : FavouriteRecipeRvAdapter

    private val viewModel by viewModels<RecipeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater,container,false)
        recipesRvAdapter = FavouriteRecipeRvAdapter(::itemClick)
        return binding.root
    }

    private fun itemClick(recipeId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailsFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recipesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.recipesRv.setHasFixedSize(false)
        binding.recipesRv.adapter = recipesRvAdapter

        viewModel.getFavouriteRecipe.observe(viewLifecycleOwner){
            recipesRvAdapter.submitList(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}