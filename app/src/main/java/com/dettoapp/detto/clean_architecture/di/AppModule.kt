package com.dettoapp.detto.clean_architecture.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.dettoapp.detto.APIs.CreateClassroomAPI
import com.dettoapp.detto.APIs.RegistrationAPI
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.clean_architecture.common.PreferenceManager
import com.dettoapp.detto.clean_architecture.data.repository.LoginSignUpComposeRepositoryImpl
import com.dettoapp.detto.clean_architecture.domain.repository.LoginSignUpComposeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        userDetailsSharedPreference: PreferenceManager.UserDetailsSharedPreference,
        registrationAPI: RegistrationAPI,
        createClassroomAPI: CreateClassroomAPI,
        databaseDetto: DatabaseDetto
    ): LoginSignUpComposeRepository {
        return LoginSignUpComposeRepositoryImpl(userDetailsSharedPreference, registrationAPI,createClassroomAPI,databaseDetto.classroomDAO)
    }

    @Provides
    @Singleton
    fun provideRegistrationAPI(): RegistrationAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_DETTO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RegistrationAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCreateClassroomAPI(): CreateClassroomAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_DETTO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CreateClassroomAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideDettoDatabase(app: Application): DatabaseDetto {
        return Room.databaseBuilder(
            app,
            DatabaseDetto::class.java,
            DatabaseDetto.DATABASE_NAME
        ).build()
    }

}