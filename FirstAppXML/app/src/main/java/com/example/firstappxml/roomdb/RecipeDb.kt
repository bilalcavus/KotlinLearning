package com.example.firstappxml.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.firstappxml.model.Recipe


@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDAO

    companion object {
        const val DATABASE_NAME = "recipe_db"
    }
}