package com.ashish.mathongo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ashish.mathongo.Constants.TAG
import com.ashish.mathongo.R
import com.ashish.mathongo.adapters.EquipmentRvAdapter
import com.ashish.mathongo.adapters.IngredientRvAdapter
import com.ashish.mathongo.data.localdb.RecipeEntity
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.models.analyzedinstructions.Equipment
import com.ashish.mathongo.data.viewmodels.RecipeViewModel
import com.ashish.mathongo.databinding.FragmentRecipeDetailsBinding
import com.ashish.mathongo.utils.Extensions.toast
import com.ashish.mathongo.utils.LoadingDialog
import com.ashish.mathongo.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {

    private val navArgs by navArgs<RecipeDetailsFragmentArgs>()

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientRvAdapter: IngredientRvAdapter
    private lateinit var equipmentsRvAdapter: EquipmentRvAdapter

    private val viewModel by viewModels<RecipeViewModel>()

    @Inject
    lateinit var loadingDialog: LoadingDialog

    private var isFavourite : Boolean = false
    private var recipe : Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getRecipeInfo(
            navArgs.recipeId, mapOf(
                "includeNutrition" to "true",
                "addWinePairing" to "false",
                "addTasteData" to "false"
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)

        viewModel.isFavoriteRecipe(navArgs.recipeId).observe(viewLifecycleOwner){
            isFavourite = it
            if(isFavourite){
                binding.favouriteBtn.setImageResource(R.drawable.heart_filled)
            }else{

                binding.favouriteBtn.setImageResource(R.drawable.heart)
            }
        }

        ingredientRvAdapter = IngredientRvAdapter()
        equipmentsRvAdapter = EquipmentRvAdapter()

        binding.recipesIngredientsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recipesIngredientsRv.setHasFixedSize(false)
        binding.recipesIngredientsRv.adapter = ingredientRvAdapter

        binding.recipesEquipmentsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recipesEquipmentsRv.setHasFixedSize(false)
        binding.recipesEquipmentsRv.adapter = equipmentsRvAdapter

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.favouriteBtn.setOnClickListener {
            if (recipe != null){
                if(isFavourite){
                    viewModel.deleteFavouriteRecipe(RecipeEntity(id = recipe!!.id, title = recipe!!.title, image = recipe!!.image, readyInMinutes = recipe!!.readyInMinutes))
                    binding.favouriteBtn.setImageResource(R.drawable.heart)
                }else{
                    viewModel.insertFavouriteRecipe(RecipeEntity(id = recipe!!.id, title = recipe!!.title, image = recipe!!.image, readyInMinutes = recipe!!.readyInMinutes))

                    binding.favouriteBtn.setImageResource(R.drawable.heart_filled)
                }
                isFavourite = !isFavourite
            }else{
                toast("Something went wrong")
            }
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
                        recipe.readyInMinutes.let { binding.preparationTimeTxt.text = "$it min" }
                        recipe.servings.let { binding.servingsTxt.text = "${recipe.servings}" }
                        recipe.pricePerServing.let {
                            binding.pricePerServingTxt.text = "${recipe.pricePerServing}"
                        }
                        binding.quickSummaryTxt.text = Html.fromHtml(recipe.summary, Html.FROM_HTML_MODE_COMPACT)
                        binding.instructionsTxt.text = Html.fromHtml(recipe.instructions, Html.FROM_HTML_MODE_COMPACT)
                        ingredientRvAdapter.submitList(recipe.extendedIngredients)
                        val equipment = mutableListOf<Equipment>()

                        recipe.analyzedInstructions.forEach { instruction ->
                            instruction.steps.forEach { step ->
                                if (step.equipment.isNotEmpty()) {
                                    equipment.addAll(step.equipment)
                                }
                            }
                        }

                        equipmentsRvAdapter.submitList(equipment.distinct())

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