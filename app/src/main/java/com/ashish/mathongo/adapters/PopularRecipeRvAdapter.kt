package com.ashish.mathongo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.databinding.PopularRecipeRvItemBinding

class PopularRecipeRvAdapter(private val itemClick : (Int)-> Unit) : ListAdapter<Recipe, PopularRecipeRvAdapter.RecipeViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = PopularRecipeRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        recipe?.let {
            holder.bindItem(it)
        }
        holder.binding.root.setOnClickListener {
            itemClick(recipe.id)
        }
    }

    class RecipeViewHolder(val binding : PopularRecipeRvItemBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bindItem(item : Recipe){
            binding.recipeImg.load(item.image)
            binding.recipeTitleTxt.text = item.title
            binding.preparationTimeTxt.text = "Ready in ${item.readyInMinutes} min"
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(
            oldItem: Recipe,
            newItem: Recipe
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Recipe,
            newItem: Recipe
        ): Boolean {
            return oldItem == newItem
        }
    }
}