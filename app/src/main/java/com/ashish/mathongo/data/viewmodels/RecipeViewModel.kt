package com.ashish.mathongo.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.ashish.mathongo.data.localdb.RecipeEntity
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.models.RecipeApiResp
import com.ashish.mathongo.data.models.SearchResp
import com.ashish.mathongo.data.models.SimilarRecipe
import com.ashish.mathongo.data.repo.RecipeRepo
import com.ashish.mathongo.utils.NetworkManager
import com.ashish.mathongo.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val networkManager: NetworkManager
) : ViewModel() {

    private val _recipesMutableLiveData = MutableLiveData<NetworkResult<RecipeApiResp>>()

    val recipeLiveData: LiveData<NetworkResult<RecipeApiResp>> get() = _recipesMutableLiveData

    fun getRandomRecipes(query: Map<String, String>) {
        if (networkManager.internetConnected) {
            _recipesMutableLiveData.postValue(NetworkResult.Loading())
            viewModelScope.launch {
                try {
                    val response = recipeRepo.getRandomRecipes(query)
                    if (response.isSuccessful && response.body() != null){
                        _recipesMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
                    }else{
                        _recipesMutableLiveData.postValue(NetworkResult.Error("Something went wrong! Please try again"))
                    }
                }catch (e : Exception){
                    _recipesMutableLiveData.postValue(NetworkResult.Error(e.localizedMessage))
                }
            }
        }
    }

    private val _recipesInfoMutableLiveData = MutableLiveData<NetworkResult<Recipe>>()

    val recipeInfoLiveData: LiveData<NetworkResult<Recipe>> get() = _recipesInfoMutableLiveData

    fun getRecipeInfo(recipeId : Int, query: Map<String, String>) {
        if (networkManager.internetConnected) {
            _recipesInfoMutableLiveData.postValue(NetworkResult.Loading())
            viewModelScope.launch {
                try {
                    val response = recipeRepo.getRecipeInfo(recipeId,query)
                    if (response.isSuccessful && response.body() != null){
                        _recipesInfoMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
                    }else{
                        _recipesInfoMutableLiveData.postValue(NetworkResult.Error("Something went wrong! Please try again"))
                    }
                }catch (e : Exception){
                    _recipesInfoMutableLiveData.postValue(NetworkResult.Error(e.localizedMessage))
                }
            }
        }
    }
   private val _searchRecipesMutableLiveData = MutableLiveData<NetworkResult<SearchResp>>()

    val searchRecipeLiveData: LiveData<NetworkResult<SearchResp>> get() = _searchRecipesMutableLiveData

    fun searchRecipes(query: Map<String, String>) {
        if (networkManager.internetConnected) {
            _searchRecipesMutableLiveData.postValue(NetworkResult.Loading())
            viewModelScope.launch {
                try {
                    val response = recipeRepo.searchRecipes(query)
                    if (response.isSuccessful && response.body() != null){
                        _searchRecipesMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
                    }else{
                        _searchRecipesMutableLiveData.postValue(NetworkResult.Error("Something went wrong! Please try again"))
                    }
                }catch (e : Exception){
                    _searchRecipesMutableLiveData.postValue(NetworkResult.Error(e.localizedMessage))
                }
            }
        }
    }



    private val _similarRecipesMutableLiveData = MutableLiveData<NetworkResult<List<SimilarRecipe>>>()

    val similarRecipeLiveData: LiveData<NetworkResult<List<SimilarRecipe>>> get() = _similarRecipesMutableLiveData

    fun getSimilarRecipe(recipeId: Int, query: Map<String, String>) {
        if (networkManager.internetConnected) {
            _similarRecipesMutableLiveData.postValue(NetworkResult.Loading())
            viewModelScope.launch {
                try {
                    val response = recipeRepo.getSimilarRecipe(recipeId,query)
                    if (response.isSuccessful && response.body() != null){
                        _similarRecipesMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
                    }else{
                        _similarRecipesMutableLiveData.postValue(NetworkResult.Error("Something went wrong! Please try again"))
                    }
                }catch (e : Exception){
                    _similarRecipesMutableLiveData.postValue(NetworkResult.Error(e.localizedMessage))
                }
            }
        }
    }


    fun insertFavouriteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
        recipeRepo.insertFavouriteRecipe(recipe)
        }
    }

    val getFavouriteRecipe : LiveData<List<RecipeEntity>> = recipeRepo.getFavouriteRecipe

    fun deleteFavouriteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeRepo.deleteFavouriteRecipe(recipe)
        }
    }

    fun isFavoriteRecipe(recipeId: Int): LiveData<Boolean> = liveData {
        emit(recipeRepo.isFavoriteRecipe(recipeId))
    }


}