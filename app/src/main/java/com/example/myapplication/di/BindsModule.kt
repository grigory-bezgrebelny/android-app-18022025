package com.example.myapplication.di

import com.example.myapplication.datastore.DefaultGenderDatastore
import com.example.myapplication.datastore.GenderDatastore
import com.example.myapplication.repo.DefaultGenderRepo
import com.example.myapplication.repo.GenderRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {

    @Binds
    fun bindsGenderRepo(genderRepo: DefaultGenderRepo): GenderRepo

    @Binds
    fun bindsGenderDatastore(genderDatastore: DefaultGenderDatastore): GenderDatastore
}
