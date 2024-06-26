package com.ashish.mathongo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.mathongo.data.models.RecipeApiResp
import com.ashish.mathongo.data.repo.RecipeRepo
import com.ashish.mathongo.utils.NetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo
): ViewModel() {

    private val _recipesMutableLiveData =  MutableLiveData<NetworkResult<RecipeApiResp>>()

    val recipeLiveData : LiveData<NetworkResult<RecipeApiResp>> get() = _recipesMutableLiveData

    fun getRandomRecipes(query : Map<String,String>){

        viewModelScope.launch {
            _recipesMutableLiveData.postValue(NetworkResult.Loading())
        }
    }

}