package com.ashish.mathongo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ashish.mathongo.data.models.analyzedinstructions.Equipment
import com.ashish.mathongo.databinding.IngredientsRvItemBinding

class EquipmentRvAdapter : ListAdapter<Equipment, EquipmentRvAdapter.EquipmentViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        val view = IngredientsRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EquipmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        val equipment = getItem(position)
        equipment?.let {
            holder.bindItem(it)
        }
    }

    class EquipmentViewHolder(private val binding : IngredientsRvItemBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bindItem(item : Equipment){
            binding.img.load(item.image)
            binding.nameTxt.text = item.name
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Equipment>() {
        override fun areItemsTheSame(
            oldItem: Equipment,
            newItem: Equipment
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Equipment,
            newItem: Equipment
        ): Boolean {
            return oldItem == newItem
        }
    }
}