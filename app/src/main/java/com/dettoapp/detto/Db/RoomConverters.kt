package com.dettoapp.detto.Db

import androidx.room.TypeConverter
import com.dettoapp.detto.Models.TeacherModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

//import com.google.gson.reflect.TypeToken

class RoomConverters {
    @TypeConverter
    fun fromTeacherModel(teacherModel: TeacherModel):String{
     return Gson().toJson(teacherModel)
    }
    @TypeConverter
  fun fromTeacherString(teacherString:String):TeacherModel{
      val type = object: TypeToken<TeacherModel>(){}.type
      return Gson().fromJson(teacherString,type)
  }


}