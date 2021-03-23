package com.dettoapp.detto.TeacherActivity.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Classroom::class],version = 1)
abstract class ClassroomDatabase:RoomDatabase() {

    abstract val classroomDAO :ClassroomDAO

    companion object{
        private var INSTANCE :ClassroomDatabase?=null
        fun getInstance(context: Context):ClassroomDatabase{
            synchronized(this){
                var instance:ClassroomDatabase?= INSTANCE
                if(instance==null){
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        ClassroomDatabase::class.java,
                        "classroom_data_database"
                    ).build()
                }
                return instance
            }
        }
    }
}