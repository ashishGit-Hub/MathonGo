package com.ashish.mathongo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ashish.mathongo.data.localdb.RecipeEntity
import com.ashish.mathongo.databinding.RecipeRvItemBinding

class FavouriteRecipeRvAdapter(private val itemClick: (Int) -> Unit) :
    ListAdapter<RecipeEntity, FavouriteRecipeRvAdapter.RecipeViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = RecipeRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        recipe?.let { rec ->
            holder.bindItem(rec)
        }
        holder.binding.root.setOnClickListener {
            itemClick(recipe.id)
        }
    }

    class RecipeViewHolder(val binding: RecipeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItem(item: RecipeEntity) {
            binding.recipeImg.load(item.image)
            binding.recipeTitleTxt.text = item.title
            binding.preparationTimeTxt.text = "Ready in ${item.readyInMinutes} min"
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<RecipeEntity>() {
        override fun areItemsTheSame(
            oldItem: RecipeEntity,
            newItem: RecipeEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecipeEntity,
            newItem: RecipeEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}