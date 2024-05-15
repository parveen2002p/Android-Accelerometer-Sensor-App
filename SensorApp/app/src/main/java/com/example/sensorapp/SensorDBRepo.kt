package com.example.sensorapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SensorDBRepo(private val sensorDao : SensorDao) {

    suspend fun insertSensorData(sensorData: SensorData) {
        withContext(Dispatchers.IO) {
            sensorDao.insert(sensorData)
        }
    }
    suspend fun getAllData(): List<SensorData>? {
        return withContext(Dispatchers.IO) {
            sensorDao.getAllData()
        }
    }
}
