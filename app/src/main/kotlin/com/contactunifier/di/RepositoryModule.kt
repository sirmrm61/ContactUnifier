package com.contactunifier.di

import android.content.ContentResolver
import android.content.Context
import com.contactunifier.data.repository.ContactsRepositoryImpl
import com.contactunifier.domain.repository.ContactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt dependency injection module.
 * Provides singleton instances of repository and other dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideContactsRepository(
        @ApplicationContext context: Context,
        contentResolver: ContentResolver
    ): ContactsRepository {
        return ContactsRepositoryImpl(context, contentResolver)
    }
}
