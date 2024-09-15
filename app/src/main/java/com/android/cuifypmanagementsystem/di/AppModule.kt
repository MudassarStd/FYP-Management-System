package com.android.cuifypmanagementsystem.di

import android.content.Context
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.room.TeacherDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// AppModule.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context : Context) : MainDatabase {
        return MainDatabase.getDatabase(context)
    }

    @Provides
    fun provideTeacherDao(database: MainDatabase) : TeacherDao {
        return database.teacherDao()
    }
}
