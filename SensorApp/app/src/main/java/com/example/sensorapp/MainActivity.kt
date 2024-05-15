package com.example.sensorapp
//import co.yml.charts.axis.AxisData
//import co.yml.charts.common.model.Point
//import co.yml.charts.ui.linechart.LineChart
//import co.yml.charts.ui.linechart.model.GridLines
//import co.yml.charts.ui.linechart.model.IntersectionPoint
//import co.yml.charts.ui.linechart.model.Line
//import co.yml.charts.ui.linechart.model.LineChartData
//import co.yml.charts.ui.linechart.model.LinePlotData
//import co.yml.charts.ui.linechart.model.LineStyle
//import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
//import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
//import co.yml.charts.ui.linechart.model.ShadowUnderLine
//import androidx.lifecycle.ProcessLifecycleOwner
//import android.graphics.Color
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.github.mikephil.charting.charts.LineChart
//import com.github.mikephil.charting.components.Description
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.data.LineData
//import com.github.mikephil.charting.data.LineDataSet
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

//import androidx.compose.material.MaterialTheme
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color


//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
////import androidx.compose.material.MaterialTheme
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import io.vico.core.Chart
//import io.vico.core.Series
//import io.vico.core.data.Point
//import io.vico.core.data.XYPoint

//import kotlin.random.Random

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            SensorAppTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
//    }
//}

//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.github.voronoff22.composeforviz.charts.LineChart
//
//

//import android.graphics.Color
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity

//import kotlin.random.Random


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.sensorapp.ui.theme.SensorAppTheme
import com.github.mikephil.charting.charts.LineChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity(){

    private lateinit var sensorDBRepo: SensorDBRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sensorDatabase = Room.databaseBuilder(
                applicationContext,
                SensorDatabase::class.java, "sensor_database"
            ).build()
            sensorDBRepo = SensorDBRepo(sensorDatabase.sensorDao())

            MyApp(sensorDBRepo,applicationContext)

        }

    }
}

class SecondActivity : ComponentActivity(){

    private lateinit var sensorDBRepo: SensorDBRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sensorDatabase = Room.databaseBuilder(
                applicationContext,
                SensorDatabase::class.java, "sensor_database"
            ).build()

            sensorDBRepo = SensorDBRepo(sensorDatabase.sensorDao())

            MyApp2(sensorDBRepo,applicationContext)

        }

    }
}

// Export history & chnag of sensing interval

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp2(sensorDBRepo: SensorDBRepo,context: Context) {
    var lineChartData1 by remember { mutableStateOf<LineChartData?>(null) }
    var lineChartData2 by remember { mutableStateOf<LineChartData?>(null) }
    var lineChartData3 by remember { mutableStateOf<LineChartData?>(null) }


    var graphNum by remember { mutableStateOf(0) }
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(50.dp)
                .width(140.dp),
                onClick = {

                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)

                }) {
                Text(text = "Stop Viewing Graph")
            }
            Spacer(
                modifier = Modifier
                    .height(50.dp)
                    //.border(2.dp, Color.Black)
                    .width(25.dp)
            )
            val xList = mutableListOf<Pair<Float, String>>()
            val yList = mutableListOf<Pair<Float, String>>()
            val zList = mutableListOf<Pair<Float, String>>()

            CoroutineScope(Dispatchers.Main).launch {
                val items = sensorDBRepo.getAllData()
                if (items != null) {
                    for (item in items) {

                        val xFloat = item.x.toFloat()
                        val yFloat = item.y.toFloat()
                        val zFloat = item.z.toFloat()

                        xList.add(Pair(xFloat, item.timestamp))
                        yList.add(Pair(yFloat, item.timestamp))
                        zList.add(Pair(zFloat, item.timestamp))

                        Log.d("MainActivity2", "Timestamp: ${item.timestamp}")
                    }
                }

                if (xList.isNotEmpty() && yList.isNotEmpty() && zList.isNotEmpty()) {
                    lineChartData1 = Gen(xList)
                    lineChartData2 = Gen(yList)
                    lineChartData3 = Gen(zList)

                }
            }

            if (lineChartData1 != null && graphNum == 0) {
                Text(
                    text = "Graph for Roll value",
                    modifier = Modifier
                        .padding(4.dp)
                        //.border(3.dp, Color.Black)
                        //.fillMaxWidth()
                        .background(Color.LightGray),
                    fontSize = 15.sp,
                    //lineHeight = 116.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Blue,
                    //style = MaterialTheme.typography.titleLarge,
                    //style = MaterialTheme.typography.body1
                )

                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    lineChartData = lineChartData1!!
                )

//                    Spacer(
//                        modifier = Modifier
//                            .height(50.dp)
//                            //.border(2.dp, Color.Black)
//                            .width(25.dp)
//                    )
            }
            if (lineChartData2 != null && graphNum == 1) {
                Text(
                    text = "Graph for Pitch value",
                    modifier = Modifier
                        .padding(4.dp)
                        //.border(3.dp, Color.Black)
                        //.fillMaxWidth()
                        .background(Color.LightGray),
                    fontSize = 15.sp,
                    //lineHeight = 116.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Blue,
                    //style = MaterialTheme.typography.titleLarge,
                    //style = MaterialTheme.typography.body1
                )

                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    lineChartData = lineChartData2!!
                )
            }
            if (lineChartData3 != null && graphNum == 2) {
                Text(
                    text = "Graph for Yaw value",
                    modifier = Modifier
                        .padding(4.dp)
                        //.border(3.dp, Color.Black)
                        //.fillMaxWidth()
                        .background(Color.LightGray),
                    fontSize = 15.sp,
                    //lineHeight = 116.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Blue,
                    //style = MaterialTheme.typography.titleLarge,
                    //style = MaterialTheme.typography.body1
                )

                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    lineChartData = lineChartData3!!
                )
            }
            Spacer(
                modifier = Modifier
                    .height(50.dp)
                    //.border(2.dp, Color.Black)
                    .width(25.dp)
            )
            Button(modifier = Modifier
                //.fillMaxWidth()
                .padding(5.dp)
                .height(50.dp)
                .width(180.dp),
                onClick = { graphNum = (graphNum + 1) % 3 }) {
                Text(text = "Switch to next graph")
            }
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(sensorDBRepo: SensorDBRepo,context: Context) {
    var xValue by remember { mutableStateOf(0.0) }
    var yValue by remember { mutableStateOf(0.0) }
    var zValue by remember { mutableStateOf(0.0) }
    var veiwingGraph by remember { mutableStateOf(false) }


    val ctx = LocalContext.current
    val sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val sensorStatus = remember {
        mutableStateOf("")
    }

    val aceleroSensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }


        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    sensorStatus.value =
                        "X: ${event.values[0]}, Y: ${event.values[1]}, Z: ${event.values[2]}"
                }
            }
        }
    }

    sensorManager.registerListener(

        aceleroSensorEventListener,

        accelerometer,
        SensorManager.SENSOR_DELAY_NORMAL
        //1000000
    )

    CoroutineScope(Dispatchers.Main).launch {
        while (true) {
            if (sensorStatus.value.isNotEmpty()) {
                val calendar = Calendar.getInstance()
                val timestamp = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SS",
                    Locale.getDefault()
                ).format(calendar.time)

                val parts = sensorStatus.value.split(",")

                val xValue = parts[0].substringAfter("X:").trim().toDouble()
                val yValue = parts[1].substringAfter("Y:").trim().toDouble()
                val zValue = parts[2].substringAfter("Z:").trim().toDouble()
                val sensorData = SensorData(
                    timestamp = timestamp,
                    x = xValue,
                    y = yValue,
                    z = zValue
                )
                sensorDBRepo.insertSensorData(sensorData)
                Log.d("MainActivity", "Val Add: ${timestamp}")
            }
            delay(1000)
        }
    }


    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(50.dp)
                .width(140.dp),
                onClick = { veiwingGraph = !veiwingGraph
                    val intent = Intent(context, SecondActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }) {
                Text(text = if (veiwingGraph) "Stop Viewing Graph" else "View Graph")
            }
            Spacer(
                modifier = Modifier
                    .height(50.dp)
                    //.border(2.dp, Color.Black)
                    .width(25.dp)
            )

            //if (!veiwingGraph) {

                Text(
                    text = "Orientation Angles Value",
                    modifier = Modifier
                        .padding(4.dp),
                    //.border(3.dp, Color.Black)
                    //.fillMaxWidth()
                    //.background(Color.Gray),
                    fontSize = 30.sp,
                    //lineHeight = 116.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Magenta,
                    style = MaterialTheme.typography.titleLarge,
                    //style = MaterialTheme.typography.body1
                    //style = MaterialTheme.typography.
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (sensorStatus.value.isNotEmpty()) {
                    val parts = sensorStatus.value.split(",")
                    xValue = parts[0].substringAfter("X:").trim().toDouble()
                    yValue = parts[1].substringAfter("Y:").trim().toDouble()
                    zValue = parts[2].substringAfter("Z:").trim().toDouble()
                }

                Text(
                    text = "Roll: ${xValue}",
                    modifier = Modifier
                        .padding(4.dp)
                        //.border(3.dp, Color.Black)
                        //.fillMaxWidth()
                        .background(Color.LightGray),
                    fontSize = 25.sp,
                    //lineHeight = 116.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Blue,
                    style = MaterialTheme.typography.titleLarge,
                    //style = MaterialTheme.typography.body1
                )
                Spacer(
                    modifier = Modifier
                        .height(40.dp)
                        //.border(2.dp, Color.Black)
                        .width(25.dp)
                )
                Text(
                    text = "Pitch: ${yValue}",
                    modifier = Modifier
                        .padding(4.dp)
                        //.border(3.dp, Color.Black)
                        //.fillMaxWidth()
                        .background(Color.LightGray),
                    fontSize = 25.sp,
                    //lineHeight = 116.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Blue,
                    style = MaterialTheme.typography.titleLarge,
                    //style = MaterialTheme.typography.body1
                )
                Spacer(
                    modifier = Modifier
                        .height(40.dp)
                        //.border(2.dp, Color.Black)
                        .width(25.dp)
                )
                Text(
                    text = "Yaw: ${zValue}",
                    modifier = Modifier
                        .padding(4.dp)
                        //.border(3.dp, Color.Black)
                        //.fillMaxWidth()
                        .background(Color.LightGray),
                    fontSize = 25.sp,
                    //lineHeight = 116.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Blue,
                    style = MaterialTheme.typography.titleLarge,
                    //style = MaterialTheme.typography.body1
                )
                Spacer(
                    modifier = Modifier
                        .height(50.dp)
                        //.border(2.dp, Color.Black)
                        .width(25.dp)
                )
            }

    }
}

//@Composable
fun Gen(pointsData: List<Pair<Float, String>>) :LineChartData{
    // Convert string timestamps to float values for x-axis
    val xValues = pointsData.map { it.first }
    val timestamps = pointsData.map { it.second }

    val xAxisData = AxisData.Builder()
        .axisStepSize(150.dp)
        .backgroundColor(Color.Cyan)
        .steps(pointsData.size - 1)
        .labelData { i -> timestamps[i].toString() } // Use timestamps as labels
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(pointsData.size - 1)
        .backgroundColor(Color.Red)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i -> xValues[i].toString() } // Use x-values as labels
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData.mapIndexed { index, point ->
                        Point(index.toFloat(), point.first) // Use index as x-value
                    },
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        yAxisData = yAxisData,
        xAxisData = xAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )
    return lineChartData

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SensorAppTheme {
        Greeting("Android")
    }
}
