package com.ratriretno.recipe.data.network

import android.provider.ContactsContract.Profile
import com.ratriretno.recipe.data.model.ApiRecipe
import retrofit2.http.GET
import retrofit2.http.Url


interface ApiInterface {

    @GET("recipes")
    suspend fun getRecipes(): List <ApiRecipe>

}