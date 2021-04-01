package com.dettoapp.detto.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel

@Database(entities = [Classroom::class,ProjectModel::class],version = 1)
@TypeConverters(RoomConverters::class)
abstract class DatabaseDetto:RoomDatabase() {

    abstract val classroomDAO : ClassroomDAO
    abstract val projectDAO :ProjectDAO

    companion object{
        private var INSTANCE : DatabaseDetto?=null
        fun getInstance(context: Context): DatabaseDetto {
            synchronized(this){
                var instance: DatabaseDetto?=
                    INSTANCE
                if(instance==null){
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseDetto::class.java,
                        "classroom_data_database"
                    ).build()
                }
                return instance
            }
        }
    }
}