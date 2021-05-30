package com.dettoapp.detto.Db

import androidx.room.TypeConverter
import com.dettoapp.detto.Models.ClassroomSettingsModel
import com.dettoapp.detto.Models.MarksModel
import com.dettoapp.detto.Models.TeacherModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//import com.google.gson.reflect.TypeToken

class RoomConverters {
    @TypeConverter
    fun fromTeacherModel(teacherModel: TeacherModel): String {
        return Gson().toJson(teacherModel)
    }

    @TypeConverter
    fun fromTeacherString(teacherString: String): TeacherModel {
        val type = object : TypeToken<TeacherModel>() {}.type
        return Gson().fromJson(teacherString, type)
    }

    @TypeConverter
    fun fromClassroomSettingsModel(classroomSettingsModel: ClassroomSettingsModel): String {
        return Gson().toJson(classroomSettingsModel)
    }

    @TypeConverter
    fun fromClassroomSettingsString(classroomSettingsString: String): ClassroomSettingsModel {
        val type = object : TypeToken<ClassroomSettingsModel>() {}.type
        return Gson().fromJson(classroomSettingsString, type)
    }

    @TypeConverter
    fun fromMarksModel(marksModel: ArrayList<MarksModel>): String {
        return Gson().toJson(marksModel)
    }

    @TypeConverter
    fun fromMarksModelString(marksString: String): ArrayList<MarksModel> {
        val type = object : TypeToken<ArrayList<MarksModel>>() {}.type
        return Gson().fromJson(marksString, type)
    }

    @TypeConverter
    fun fromHashSet(set: HashSet<String>): String {
        return Gson().toJson(set)
    }

    @TypeConverter
    fun fromHashSetString(setString: String): HashSet<String> {
        val type = object : TypeToken<Set<String>>() {}.type
        return Gson().fromJson(setString, type)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromArrayListString(listString: String): ArrayList<String> {
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(listString, type)
    }

    @TypeConverter
    fun fromHashMap(map: HashMap<String, String>): String {
        return Gson().toJson(map.values)
    }

    @TypeConverter
    fun fromHashMapString(mapString: String): HashMap<String, String> {
        val type = object : TypeToken<HashMap<String, String>>() {}.type
        return Gson().fromJson(mapString, type)
    }

}