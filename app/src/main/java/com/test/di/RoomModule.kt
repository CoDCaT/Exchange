package com.test.di

import android.content.Context
import androidx.room.Room
import com.test.room.RoomDataBaseImpl
import dagger.Module
import dagger.Provides

@Module
class RoomModule {

    @Provides
    fun provideRoomDatabase(context: Context): RoomDataBaseImpl {
        return Room
            .databaseBuilder(context, RoomDataBaseImpl::class.java, "test.db")
            .build()
    }

}