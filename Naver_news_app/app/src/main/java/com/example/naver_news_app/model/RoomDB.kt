package com.example.naver_news_app.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[ArticleEntity::class], version=1)
abstract class RoomDB : RoomDatabase() {
    abstract fun articleDao(): ArticleDAO

    companion object {
        private var instance: RoomDB? = null

        @Synchronized
        fun getInstance(context: Context): RoomDB? {
            if(instance == null){
                synchronized(RoomDB::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDB::class.java,
                        "Article.db"
                    )
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
        
    }
}