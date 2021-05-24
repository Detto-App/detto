package com.dettoapp.detto.Db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dettoapp.detto.Models.RubricsModel

@Dao
interface RubricsDAO {
    @Insert
    suspend fun insertRubrics(rubricsModel: RubricsModel)

    @Query("SELECT * FROM rubrics_table WHERE cid = :cid")
    suspend fun getRubrics(cid:String):RubricsModel
}