package com.ashish.mathongo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashish.mathongo.Constants.TAG
import com.ashish.mathongo.R
import com.ashish.mathongo.adapters.PopularRecipeRvAdapter
import com.ashish.mathongo.adapters.RecipeRvAdapter
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.viewmodels.RecipeViewModel
import com.ashish.mathongo.databinding.FragmentAllRecipesBinding
import com.ashish.mathongo.utils.Extensions.toast
import com.ashish.mathongo.utils.LoadingDialog
import com.ashish.mathongo.utils.NetworkResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllRecipesFragment : Fragment() {

    private var _binding : FragmentAllRecipesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RecipeViewModel>()
    private lateinit var popularRecipesRvAdapter : PopularRecipeRvAdapter
    private lateinit var recipesRvAdapter : RecipeRvAdapter

    private lateinit var auth : FirebaseAuth

    @Inject
    lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        viewModel.getRandomRecipes(mapOf(
            "number" to "20",
            "limitLicense" to "true",
            "includeNutrition" to "false"
        ))
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentAllRecipesBinding.inflate(inflater,container,false)
        popularRecipesRvAdapter = PopularRecipeRvAdapter()

        recipesRvAdapter = RecipeRvAdapter(::recipeItemClick)

        auth.currentUser?.displayName?.let {username ->
            binding.username.text = getString(R.string.hey)+" "+username.split(" ").first()

        }

        Log.d(TAG,auth.currentUser?.displayName.toString())


        return binding.root
    }
    private fun recipeItemClick(id : Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailsFragment(id)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.popularRecipesRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.popularRecipesRv.setHasFixedSize(false)
        binding.popularRecipesRv.adapter = popularRecipesRvAdapter

        binding.allRecipesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.allRecipesRv.setHasFixedSize(false)
        binding.allRecipesRv.adapter = recipesRvAdapter


        viewModel.recipeLiveData.observe(viewLifecycleOwner){
            loadingDialog.dismiss()
            when(it){
                is NetworkResult.Error ->{
                    toast(it.message)
                    Log.d(TAG,it.message.toString())
                }
                is NetworkResult.Loading -> loadingDialog.startLoading()
                is NetworkResult.Success -> {
                    popularRecipesRvAdapter.submitList(it.data?.recipes?.subList(0,5))
                    recipesRvAdapter.submitList(it.data?.recipes)
                }
            }
        }

        // Click Listener
        binding.searchView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}