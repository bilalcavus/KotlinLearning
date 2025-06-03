package com.example.firstappxml.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.room.Room
import com.example.firstappxml.databinding.FragmentRecipeBinding
import com.example.firstappxml.model.Recipe
import com.example.firstappxml.roomdb.RecipeDAO
import com.example.firstappxml.roomdb.RecipeDb
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.IOException


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImage: Uri? = null
    private var selectedBitMap: Bitmap? = null
    private val mDisposable = CompositeDisposable()
    private var selectedRecipe: Recipe? = null

    private lateinit var db : RecipeDb
    private lateinit var recipeDAO: RecipeDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
        db = Room.databaseBuilder(requireContext(), RecipeDb::class.java, RecipeDb.DATABASE_NAME).build()
        recipeDAO = db.recipeDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setOnClickListener{
            selectImage(it)
        }
        binding.save.setOnClickListener{
            saveRecipe(it)
        }
        binding.delete.setOnClickListener{
            deleteRecipe(it)
        }

        arguments?.let {
            var info = RecipeFragmentArgs.fromBundle(it).info
            if (info == "new"){
                binding.delete.isEnabled = false
                binding.save.isEnabled = true
                binding.editTextText.setText("")
                binding.editTextText2.setText("")
            }
            else {
                binding.delete.isEnabled = true
                binding.save.isEnabled = false
                val id = RecipeFragmentArgs.fromBundle(it).id
                mDisposable.add(
                    recipeDAO.findById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )

            }
        }
    }

    private fun handleResponse(recipe: Recipe){
        val bitmap = BitmapFactory.decodeByteArray(recipe.image, 0, recipe.image.size)
        binding.imageView.setImageBitmap(bitmap)
        selectedRecipe = recipe
        binding.editTextText.setText(recipe.name)
        binding.editTextText2.setText(recipe.ingredients)
    }

    fun saveRecipe(view: View){
        val recipeName = binding.editTextText.text.toString()
        val recipeIngredients = binding.editTextText2.text.toString()

        if (selectedBitMap != null) {
            val littleBitmap = createLittleBitmap(selectedBitMap!!, 300)
            val outputStream = ByteArrayOutputStream()
            littleBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteArray = outputStream.toByteArray()

            val recipe = Recipe(recipeName, recipeIngredients, byteArray)

            //RxJava
            mDisposable.add(recipeDAO.insert(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponseForInsert))

        }
    }

    private fun handleResponseForInsert() {
        val action = RecipeFragmentDirections.actionRecipeFragmentToListFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun deleteRecipe(view: View){

        if (selectedRecipe != null) {
            mDisposable.add(
                recipeDAO.delete(recipe =  selectedRecipe!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseForInsert)
            )
        }

    }

    fun selectImage(view: View){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            if (
                ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view, "Galeriye erişilsin mi?", Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin Ver",
                        View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                    ).show()

                }
                else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
            else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }

        else {
            if (
                ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view, "Galeriye erişilsin mi?", Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin Ver",
                        View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    ).show()

                }
                else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }
    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    selectedImage = intent.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, selectedImage!!)
                            selectedBitMap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(selectedBitMap)
                        }
                        else {
                            selectedBitMap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
                            binding.imageView.setImageBitmap(selectedBitMap)
                        }
                    }
                    catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Resim yüklenemedi", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(requireContext(), "İzin verilmedi", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createLittleBitmap(userSelectedBitmap: Bitmap, maxSize: Int) : Bitmap {
        var width = userSelectedBitmap.width
        var height = userSelectedBitmap.height

        val bitmapRatio: Double = width.toDouble() / height.toDouble()

        if (bitmapRatio > 1 )
        {
            width = maxSize
            val newHeight = width / bitmapRatio
            height = newHeight.toInt()
        } else {
            height = maxSize
            val newWidth = height * bitmapRatio
            width = newWidth.toInt()
        }

        return Bitmap.createScaledBitmap(userSelectedBitmap, width, height, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}