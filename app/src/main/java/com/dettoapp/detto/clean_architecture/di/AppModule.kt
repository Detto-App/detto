package com.dettoapp.detto.clean_architecture.di

import android.content.Context
import com.dettoapp.detto.clean_architecture.common.PreferenceManager
import com.dettoapp.detto.clean_architecture.data.repository.LoginSignUpComposeRepositoryImpl
import com.dettoapp.detto.clean_architecture.domain.repository.LoginSignUpComposeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun userDetailsSharedPreference(
        @ApplicationContext
        context: Context
    ): PreferenceManager.UserDetailsSharedPreference {
        return PreferenceManager.UserDetailsSharedPreference(context)
    }

    @Provides
    @Singleton
    fun provideLoginSignUpComposeRepository(
        userDetailsSharedPreference: PreferenceManager.UserDetailsSharedPreference
    ): LoginSignUpComposeRepository {
        return LoginSignUpComposeRepositoryImpl(userDetailsSharedPreference)
    }
}