package com.allfreeapps.theballgame.di

//import com.allfreeapps.theballgame.viewModels.SettingsViewModel
import android.content.Context
import com.allfreeapps.theballgame.service.AppDatabase
import com.allfreeapps.theballgame.service.dao.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {
//    @Provides
//    @Singleton
//    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
//        return androidx.datastore.preferences.core.PreferenceDataStoreFactory.create(
//            corruptionHandler = androidx.datastore.core.handlers.ReplaceFileCorruptionHandler(
//                produceNewData = { emptyPreferences() }
//            ),
//            migrations = listOf(androidx.datastore.migrations.SharedPreferencesMigration(context, YOUR_SHARED_PREFS_NAME_IF_MIGRATING)), // Optional
//            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
//            produceFile = { context.preferencesDataStoreFile("app_settings") }
//        )
//    }


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    @Provides
    fun provideScoreDao(appDatabase: AppDatabase): ScoreDao {
        return appDatabase.scoreDao()
    }
}