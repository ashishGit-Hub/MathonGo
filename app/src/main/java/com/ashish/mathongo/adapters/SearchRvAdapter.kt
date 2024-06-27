package com.ashish.mathongo.adapters

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ashish.mathongo.data.models.SearchedItem
import com.ashish.mathongo.databinding.SearchRvItemBinding

class SearchRvAdapter(private val itemClick : (Int) -> Unit) : ListAdapter<SearchedItem, SearchRvAdapter.SearchedItemViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedItemViewHolder {
        val view = SearchRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchedItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchedItemViewHolder, position: Int) {
        val searchedItem = getItem(position)
        searchedItem?.let {
            holder.bindItem(it)
        }
        holder.binding.root.setOnClickListener {
            itemClick(searchedItem.id)
        }
    }

    class SearchedItemViewHolder(val binding : SearchRvItemBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bindItem(item : SearchedItem){
            binding.recipeTitleTxt.text = item.title
        }


        // Function to bold all occurrences of a specific word in a string
        private fun getBoldSpannableString(fullText: String, wordToBold: String): SpannableString {
            val spannableString = SpannableString(fullText)
            var startIndex = fullText.indexOf(wordToBold)

            while (startIndex != -1) {
                val endIndex = startIndex + wordToBold.length
                val boldSpan = StyleSpan(Typeface.BOLD)
                spannableString.setSpan(boldSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                startIndex = fullText.indexOf(wordToBold, endIndex)
            }

            return spannableString
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<SearchedItem>() {
        override fun areItemsTheSame(
            oldItem: SearchedItem,
            newItem: SearchedItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SearchedItem,
            newItem: SearchedItem
        ): Boolean {
            return oldItem == newItem
        }
    }

}