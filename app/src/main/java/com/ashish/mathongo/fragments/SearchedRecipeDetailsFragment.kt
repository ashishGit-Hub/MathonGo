package com.ashish.mathongo.fragments

import android.annotation.SuppressLint
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
import com.ashish.mathongo.Constants.TAG
import com.ashish.mathongo.R
import com.ashish.mathongo.adapters.EquipmentRvAdapter
import com.ashish.mathongo.adapters.IngredientRvAdapterGrid
import com.ashish.mathongo.adapters.SimilarRecipeRvAdapter
import com.ashish.mathongo.data.localdb.RecipeEntity
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.models.analyzedinstructions.Equipment
import com.ashish.mathongo.data.viewmodels.RecipeViewModel
import com.ashish.mathongo.databinding.FragmentSearchedRecipeDetailsBinding
import com.ashish.mathongo.utils.Extensions.gone
import com.ashish.mathongo.utils.Extensions.toast
import com.ashish.mathongo.utils.Extensions.visible
import com.ashish.mathongo.utils.LoadingDialog
import com.ashish.mathongo.utils.NetworkResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchedRecipeDetailsFragment : BottomSheetDialogFragment() {

    lateinit var recipe: Recipe

    private val args by navArgs<SearchedRecipeDetailsFragmentArgs>()

    private var _binding: FragmentSearchedRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    private var isFavourite: Boolean = false

    private val viewModel by viewModels<RecipeViewModel>()

    @Inject
    lateinit var loadingDialog: LoadingDialog

    private lateinit var ingredientsRvAdapter: IngredientRvAdapterGrid

    private lateinit var equipmentsRvAdapter: EquipmentRvAdapter
    private lateinit var similarRecipesRvAdapter: SimilarRecipeRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ingredientsRvAdapter = IngredientRvAdapterGrid()
        viewModel.getRecipeInfo(
            args.recipeId,
            mapOf(
                "includeNutrition" to "true",
                "addWinePairing" to "false",
                "addTasteData" to "false"
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchedRecipeDetailsBinding.inflate(inflater, container, false)
        equipmentsRvAdapter = EquipmentRvAdapter()
        similarRecipesRvAdapter = SimilarRecipeRvAdapter { }

        viewModel.isFavoriteRecipe(args.recipeId).observe(viewLifecycleOwner){
            isFavourite = it
            if(isFavourite){
                binding.favouriteBtn.setImageResource(R.drawable.heart_filled)
            }else{

                binding.favouriteBtn.setImageResource(R.drawable.heart)
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Set Recycler View Setting
        binding.recipesIngredientsRv.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recipesIngredientsRv.setHasFixedSize(false)
        binding.recipesIngredientsRv.adapter = ingredientsRvAdapter


        binding.recipesEquipmentsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recipesEquipmentsRv.setHasFixedSize(false)
        binding.recipesEquipmentsRv.adapter = equipmentsRvAdapter

        binding.recipesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.recipesRv.setHasFixedSize(false)
        binding.recipesRv.adapter = similarRecipesRvAdapter


        // All Click Listeners
        binding.favouriteBtn.setOnClickListener {
            if (this::recipe.isInitialized) {
                if (isFavourite) {
                    viewModel.deleteFavouriteRecipe(
                        RecipeEntity(
                            id = recipe.id,
                            title = recipe.title,
                            image = recipe.image,
                            readyInMinutes = recipe.readyInMinutes
                        )
                    )
                    binding.favouriteBtn.setImageResource(R.drawable.heart)
                } else {
                    viewModel.insertFavouriteRecipe(
                        RecipeEntity(
                            id = recipe.id,
                            title = recipe.title,
                            image = recipe.image,
                            readyInMinutes = recipe.readyInMinutes
                        )
                    )

                    binding.favouriteBtn.setImageResource(R.drawable.heart_filled)
                }
                isFavourite = !isFavourite
            }
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.getIngredientBtn.setOnClickListener {
            binding.recipeDetailsLayout.gone()
            binding.ingredientLayout.visible()
            binding.ingredientBody.visible()
            if (this::recipe.isInitialized) {
                ingredientsRvAdapter.submitList(recipe.extendedIngredients)
            }

            binding.fullRecipeTopLayout.gone()
            binding.fullRecipeBody.gone()
            binding.getSimilarRecipeBtn.gone()
            binding.similarTopLayout.gone()
            binding.recipesRv.gone()
        }

        binding.ingredientCollapsedImg.setOnClickListener {
            binding.recipeDetailsLayout.visible()
            binding.ingredientLayout.gone()
            binding.ingredientBody.gone()
            binding.fullRecipeTopLayout.gone()
            binding.fullRecipeBody.gone()
            binding.getSimilarRecipeBtn.gone()
            binding.similarTopLayout.gone()
            binding.recipesRv.gone()
            binding.msgTxt.gone()
        }

        binding.getFullRecipeBtn.setOnClickListener {
            binding.ingredientBody.gone()

            binding.fullRecipeTopLayout.visible()
            binding.fullRecipeBody.visible()
            binding.getSimilarRecipeBtn.visible()
            val equipment = mutableListOf<Equipment>()

            recipe.analyzedInstructions.forEach { instruction ->
                instruction.steps.forEach { step ->
                    if (step.equipment.isNotEmpty()) {
                        equipment.addAll(step.equipment)
                        Log.d(TAG, step.equipment.toString())
                    }
                }
            }
            Log.d(TAG, equipment.toString())
            equipmentsRvAdapter.submitList(equipment.distinct())
        }

        binding.fullRecipeCollapsedImg.setOnClickListener {
            binding.ingredientLayout.visible()
            binding.ingredientBody.visible()
            binding.fullRecipeTopLayout.gone()
            binding.fullRecipeBody.gone()
            binding.getSimilarRecipeBtn.gone()
            binding.similarTopLayout.gone()
            binding.recipesRv.gone()
            binding.msgTxt.gone()
        }

        binding.getSimilarRecipeBtn.setOnClickListener {

            viewModel.getSimilarRecipe(
                args.recipeId, mapOf(
                    "number" to "10",
                    "limitLicense" to "true"
                )
            )
            binding.fullRecipeBody.gone()
            binding.getSimilarRecipeBtn.gone()
            binding.similarTopLayout.visible()
            binding.recipesRv.visible()
        }

        binding.similarRecipeCollapsedImg.setOnClickListener {
            binding.fullRecipeBody.visible()
            binding.getSimilarRecipeBtn.visible()
            binding.similarTopLayout.gone()
            binding.recipesRv.gone()
            binding.msgTxt.gone()
        }


        viewModel.recipeInfoLiveData.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            when (it) {
                is NetworkResult.Error -> {
                    toast(it.message)
                    Log.d(TAG, it.message.toString())
                }

                is NetworkResult.Loading -> loadingDialog.startLoading()
                is NetworkResult.Success -> {
                    it.data?.let { recipe ->
                        this.recipe = recipe
                        binding.recipeImg.load(recipe.image)
                        binding.recipeTitleTxt.text = recipe.title
                        recipe.readyInMinutes.let { binding.preparationTimeTxt.text = "${recipe.readyInMinutes} min" }
                        recipe.servings.let { binding.servingsTxt.text = "${recipe.servings}" }
                        recipe.pricePerServing.let {
                            binding.pricePerServingTxt.text = "${recipe.pricePerServing}"
                        }
                    }
                }
            }
        }

        viewModel.similarRecipeLiveData.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            when (it) {
                is NetworkResult.Error -> {
                    toast(it.message)
                    binding.msgTxt.visible()
                }

                is NetworkResult.Loading -> loadingDialog.startLoading()
                is NetworkResult.Success -> {
                    similarRecipesRvAdapter.submitList(it.data)
                    if (it.data?.size == 0) {
                        binding.msgTxt.visible()
                    } else {
                        binding.msgTxt.gone()
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