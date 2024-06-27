package com.ashish.mathongo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.models.SimilarRecipe
import com.ashish.mathongo.databinding.RecipeRvItemBinding

class SimilarRecipeRvAdapter(private val itemClick : (Int)-> Unit) : ListAdapter<SimilarRecipe, SimilarRecipeRvAdapter.SimilarRecipeViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarRecipeViewHolder {
        val view = RecipeRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SimilarRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimilarRecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        recipe?.let {rec->
            holder.bindItem(rec)
        }
        holder.binding.root.setOnClickListener {
            itemClick(recipe.id)
        }
    }

    class SimilarRecipeViewHolder(val binding : RecipeRvItemBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bindItem(item : SimilarRecipe){
            binding.recipeImg.load(item.sourceUrl)
            binding.recipeTitleTxt.text = item.title
            binding.preparationTimeTxt.text = "Ready in ${item.readyInMinutes} min"
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<SimilarRecipe>() {
        override fun areItemsTheSame(
            oldItem: SimilarRecipe,
            newItem: SimilarRecipe
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SimilarRecipe,
            newItem: SimilarRecipe
        ): Boolean {
            return oldItem == newItem
        }
    }
}