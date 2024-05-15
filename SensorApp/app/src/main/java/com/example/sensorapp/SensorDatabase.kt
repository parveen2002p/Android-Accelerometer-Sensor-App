package com.example.sensorapp

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [SensorData::class], version = 1)
abstract class SensorDatabase : RoomDatabase() {
    abstract fun sensorDao(): SensorDao
}
