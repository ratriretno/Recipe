package com.ratriretno.recipe.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipes")
data class LocalRecipe(

    @PrimaryKey
    val id : Int,
    val title : String,
    var description  : String,
    val photoUrl : String,
    var mainIngredient  : String,
    var ingredients  : String,
    var prepTime : Int,
    var cookTime : Int,
    var totalTime : Int,
    var directions : String,
    var isFavorite : Boolean,

//    @SerializedName("course")
//    val course: String,
//
//    @SerializedName("cuisine")
//    val cuisine : String,
//


//    @SerializedName("source")
//    var source  : String,
//
//    @SerializedName("url")
    var url : String,
//
//    @SerializedName("urlHost")
//    var urlHost : String,

//    @SerializedName("servings")
//    var servings  : Int,
//
//    @SerializedName("yield")
//    var yield  : Int,
//


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
