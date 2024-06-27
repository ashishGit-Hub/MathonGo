package com.ashish.mathongo.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ashish.mathongo.Constants
import com.ashish.mathongo.Constants.TAG
import com.ashish.mathongo.R
import com.ashish.mathongo.adapters.EquipmentRvAdapter
import com.ashish.mathongo.adapters.IngredientRvAdapter
import com.ashish.mathongo.adapters.IngredientRvAdapterGrid
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.models.analyzedinstructions.Equipment
import com.ashish.mathongo.data.viewmodels.RecipeViewModel
import com.ashish.mathongo.databinding.FragmentSearchedRecipeDetailsBinding
import com.ashish.mathongo.utils.Extensions.toast
import com.ashish.mathongo.utils.LoadingDialog
import com.ashish.mathongo.utils.NetworkResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchedRecipeDetailsFragment : BottomSheetDialogFragment() {

    lateinit var recipe : Recipe

    private val args by navArgs<SearchedRecipeDetailsFragmentArgs>()

    private var _binding : FragmentSearchedRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    private var isFavourite : Boolean = false

    private val viewModel by viewModels<RecipeViewModel>()

    @Inject
    lateinit var loadingDialog : LoadingDialog

    private lateinit var ingredientsRvAdapter : IngredientRvAdapterGrid

    private lateinit var equipmentsRvAdapter: EquipmentRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ingredientsRvAdapter = IngredientRvAdapterGrid()
        viewModel.getRecipeInfo(args.recipeId,
            mapOf(
            "includeNutrition" to "true",
            "addWinePairing" to "false",
            "addTasteData" to "false"
        ))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchedRecipeDetailsBinding.inflate(inflater,container,false)
        equipmentsRvAdapter = EquipmentRvAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Set Recycler View Setting
        binding.recipesIngredientsRv.layoutManager = GridLayoutManager(requireContext(),3)
        binding.recipesIngredientsRv.setHasFixedSize(false)
        binding.recipesIngredientsRv.adapter = ingredientsRvAdapter


        binding.recipesEquipmentsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recipesEquipmentsRv.setHasFixedSize(false)
        binding.recipesEquipmentsRv.adapter = equipmentsRvAdapter



        // All Click Listeners
        binding.favouriteBtn.setOnClickListener {
            if(isFavourite){
                binding.favouriteBtn.setImageResource(R.drawable.heart_filled)
            }else{
                binding.favouriteBtn.setImageResource(R.drawable.heart)
            }
            isFavourite = !isFavourite
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.getIngredientBtn.setOnClickListener {
            binding.recipeDetailsLayout.visibility = View.GONE
            binding.ingredientLayout.visibility = View.VISIBLE
            binding.ingredientBody.visibility = View.VISIBLE
            if (this::recipe.isInitialized){
                ingredientsRvAdapter.submitList(recipe.extendedIngredients)
            }

            binding.fullRecipeLayout.visibility = View.GONE
        }

        binding.ingredientCollapsedImg.setOnClickListener {
            binding.recipeDetailsLayout.visibility = View.VISIBLE
            binding.ingredientLayout.visibility = View.GONE
            binding.fullRecipeLayout.visibility = View.GONE
        }

        binding.getFullRecipeBtn.setOnClickListener {
            binding.ingredientBody.visibility = View.GONE
            binding.fullRecipeLayout.visibility = View.VISIBLE
            val equipment = mutableListOf<Equipment>()

            recipe.analyzedInstructions.forEach { instruction ->
                instruction.steps.forEach { step ->
                    if (step.equipment.isNotEmpty()) {
                        equipment.addAll(step.equipment)
                        Log.d(TAG,step.equipment.toString())
                    }
                }
            }
            Log.d(TAG,equipment.toString())
            equipmentsRvAdapter.submitList(equipment.distinct())
        }

        binding.fullRecipeCollapsedImg.setOnClickListener {
            binding.ingredientLayout.visibility = View.VISIBLE
            binding.ingredientBody.visibility = View.VISIBLE
            binding.fullRecipeLayout.visibility = View.GONE
        }




        viewModel.recipeInfoLiveData.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            when (it) {
                is NetworkResult.Error -> {
                    toast(it.message)
                    Log.d(Constants.TAG, it.message.toString())
                }

                is NetworkResult.Loading -> loadingDialog.startLoading()
                is NetworkResult.Success -> {
                    it.data?.let { recipe ->
                        this.recipe = recipe
                        binding.recipeImg.load(recipe.image)
                        binding.recipeTitleTxt.text = recipe.title
                        recipe.readyInMinutes.let { binding.preparationTimeTxt.text = "$it min" }
                        recipe.servings.let { binding.servingsTxt.text = "${recipe.servings}" }
                        recipe.pricePerServing.let {
                            binding.pricePerServingTxt.text = "${recipe.pricePerServing}"
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}