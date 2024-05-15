package com.example.sensorapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SensorDao {
    @Insert
    suspend fun insert(sensorData: SensorData)

    @Query("SELECT * FROM sensor_data")
    suspend fun getAllData(): List<SensorData>?
}
