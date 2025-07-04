package com.example.firstappxml.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.firstappxml.ListFragmentDirections
import com.example.firstappxml.databinding.RecyclerRowBinding
import com.example.firstappxml.model.Recipe

class RecipeAdapter(val recipeList: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    class RecipeViewHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return RecipeViewHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.binding.recyclerViewTextView.text = recipeList[position].name
        holder.itemView.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToRecipeFragment("old", id = recipeList[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }
}