package com.example.firstappxml.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.firstappxml.adaptor.RecipeAdapter
import com.example.firstappxml.databinding.FragmentListBinding
import com.example.firstappxml.model.Recipe
import com.example.firstappxml.roomdb.RecipeDAO
import com.example.firstappxml.roomdb.RecipeDb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : RecipeDb
    private lateinit var recipeDAO: RecipeDAO
    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(), RecipeDb::class.java, RecipeDb.DATABASE_NAME).build()
        recipeDAO = db.recipeDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener{
            addNewRecipe(it)
        }
        binding.recipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        getInfos()
    }

    private fun getInfos() {
        mDisposable.add(
            recipeDAO.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(recipe: List<Recipe>) {
       val adapter = RecipeAdapter(recipe)
        binding.recipeRecyclerView.adapter = adapter
    }

    fun addNewRecipe(view: View)
    {
        val action = ListFragmentDirections.actionListFragmentToRecipeFragment("new", 0)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}