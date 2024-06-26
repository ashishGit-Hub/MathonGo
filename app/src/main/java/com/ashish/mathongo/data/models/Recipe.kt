package com.ashish.mathongo.data.models

import com.ashish.mathongo.data.models.analyzedinstructions.AnalyzedInstructions

data class Recipe(
    val analyzedInstructions: List<AnalyzedInstructions>, //change list Data type String to Object
//    val cheap: Boolean,
//    val creditsText: String,
//    val cuisines: List<String>,
//    val dairyFree: Boolean,
//    val diets: List<String>,
//    val dishTypes: List<String>,
    val extendedIngredients: List<Ingredient> = listOf(),
//    val gaps: String,
//    val glutenFree: Boolean,
//    val healthScore: Double,
    val id: Int,
    val image: String,
//    val imageType: String,
    val instructions: String,
//    val ketogenic: Boolean,
//    val license: String,
//    val lowFodmap: Boolean,
//    val occasions: List<String>,
    val pricePerServing: Double = 0.0,
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
//    val sourceName: String,
//    val sourceUrl: String,
//    val spoonacularScore: Double,
//    val spoonacularSourceUrl: String,
    val summary: String,
//    val sustainable: Boolean,
    val title: String,
//    val vegan: Boolean,
//    val vegetarian: Boolean,
//    val veryHealthy: Boolean,
//    val veryPopular: Boolean,
//    val weightWatcherSmartPoints: Int,
//    val whole30: Boolean,
//    val winePairing: WinePairing
)