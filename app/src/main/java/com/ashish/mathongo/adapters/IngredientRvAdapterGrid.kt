package com.ashish.mathongo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ashish.mathongo.data.models.Ingredient
import com.ashish.mathongo.databinding.IngredientsRvItemBinding
import com.ashish.mathongo.databinding.IngredientsRvItemGridBinding

class IngredientRvAdapterGrid :
    ListAdapter<Ingredient, IngredientRvAdapterGrid.IngredientViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view =
            IngredientsRvItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = getItem(position)
        ingredient?.let {
            holder.bindItem(it)
        }
    }

    class IngredientViewHolder(private val binding: IngredientsRvItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItem(item: Ingredient) {
            binding.img.load("https://img.spoonacular.com/ingredients_100x100/" + item.image)
            binding.nameTxt.text = item.name
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(
            oldItem: Ingredient,
            newItem: Ingredient
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Ingredient,
            newItem: Ingredient
        ): Boolean {
            return oldItem == newItem
        }
    }
}