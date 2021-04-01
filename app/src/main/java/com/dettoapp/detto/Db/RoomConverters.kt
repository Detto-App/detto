package com.dettoapp.detto.Db

import androidx.room.TypeConverter
import com.dettoapp.detto.Models.ClassroomSettingsModel
import com.dettoapp.detto.Models.TeacherModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
    @TypeConverter
    fun fromClassroomSettingsModel(classroomSettingsModel: ClassroomSettingsModel):String{
        return Gson().toJson(classroomSettingsModel)
    }
    @TypeConverter
    fun fromClassroomSettingsString(classroomSettingsString:String):ClassroomSettingsModel{
        val type=object :TypeToken<ClassroomSettingsModel>(){}.type
        return Gson().fromJson(classroomSettingsString,type)
    }
    @TypeConverter
    fun fromHashMap(map:HashMap<Int,String>):String{
        return  Gson().toJson(map)
    }

    @TypeConverter
    fun fromHashMapString(mapString:String):HashMap<Int,String>
    {
        val type=object :TypeToken<Map<Int,String>>(){}.type
        return Gson().fromJson(mapString,type)
    }
}