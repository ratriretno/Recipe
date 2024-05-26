package com.ratriretno.recipe.di

import android.content.Context
import androidx.room.Room
import com.ratriretno.recipe.common.Const
import com.ratriretno.recipe.data.local.DefaultRecipeRepository
import com.ratriretno.recipe.data.local.RecipeDao
import com.ratriretno.recipe.data.local.RecipeDatabase
import com.ratriretno.recipe.data.local.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindItemRepository(repository: DefaultRecipeRepository): RecipeRepository

}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class DataSourceModule {
//
//    @Singleton
//    @Binds
//    abstract fun bindNetworkDataSource(dataSource: TaskNetworkDataSource): NetworkDataSource
//}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RecipeDatabase::class.java,
            provideDbName()

        ).build()
    }

    @DbName
    @Provides
    fun provideDbName(): String = Const.DB_NAME

    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao = database.recipeDao()

}