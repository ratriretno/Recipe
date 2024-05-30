package com.ratriretno.recipe.data.model

import com.google.gson.annotations.SerializedName

data class ApiRecipe (

    @SerializedName("id")
    val id : Int,

    @SerializedName("title")
    val title : String,

//    @SerializedName("course")
//    val course: String,
//
//    @SerializedName("cuisine")
//    val cuisine : String,
//
//    @SerializedName("mainIngredient")
//    var mainIngredient  : String,
//
    @SerializedName("description")
    var description  : String,

    var mainIngredient  : String,
    var ingredients  : String,
    var prepTime : Int,
    var cookTime : Int,
    var totalTime : Int,
    var directions : String,
//
//    @SerializedName("source")
//    var source  : String,
//
//    @SerializedName("url")
    var url : String,
//
//    @SerializedName("urlHost")
//    var urlHost : String,
//
//    @SerializedName("prepTime")
//    var prepTime : Int,
//
//    @SerializedName("cookTime")
//    var cookTime : Int,
//
//    @SerializedName("totalTime")
//    var totalTime : Int,
//
//    @SerializedName("servings")
//    var servings  : Int,
//
//    @SerializedName("yield")
//    var yield  : Int,
//
//    @SerializedName("ingredients")
//    var ingredients  : String,
//
//    @SerializedName("directions")
//    var directions : String,
//
//    @SerializedName("tags")
//    var tags : String,
//
//    @SerializedName("rating")
//    var rating : String,
//
//    @SerializedName("publicUrl")
//    var publicUrl : String,
//
    @SerializedName("photoUrl")
    val photoUrl : String,
//
//    @SerializedName("private")
//    var private : String,
//
//    @SerializedName("nutritionalScoreGeneric" )
//    var nutritionalScoreGeneric : String,
//
//    @SerializedName("calories")
//    var calories : Int,
//
//    @SerializedName("fat")
//    var fat : String,
//
//    @SerializedName("cholesterol")
//    var cholesterol : String,
//
//    @SerializedName("sodium")
//    var sodium : String,
//
//    @SerializedName("sugar")
//    var sugar  : String,
//
//    @SerializedName("carbohydrate")
//    var carbohydrate  : String,
//
//    @SerializedName("fiber")
//    var fiber : String,
//
//    @SerializedName("protein")
//    var protein : String,
//
//    @SerializedName("cost")
//    var cost : String
)
