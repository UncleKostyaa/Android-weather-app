package com.example.simpleappweather

import android.graphics.fonts.Font
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.simpleappweather.ui.theme.SimpleAppWeatherTheme
import com.google.gson.JsonObject
import com.weatherapi.api.Configuration
import com.weatherapi.api.WeatherAPIClient
import com.weatherapi.api.controllers.APIsController
import com.weatherapi.api.http.client.APICallBack
import com.weatherapi.api.http.client.HttpContext
import com.weatherapi.api.models.CurrentJsonResponse

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleAppWeatherTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar (
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            ),
                            title = {
                                Text(
                                    text = "Weather App",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(top = 16.dp),

                                )
                            }

                        )
                    }

                    ) { innerPadding ->
                    Surface(modifier = Modifier
                        .padding(innerPadding)) {
                        WeatherApp()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(modifier: Modifier = Modifier) {

    val apiKey = BuildConfig.WEATHER_API_KEY
    val client = WeatherAPIClient(apiKey)
    val aPis: APIsController = client.apIs


    var weatherStatus by remember { mutableStateOf("Loading...") }
    var iconURL by remember { mutableStateOf<String?>(null) }
    var temperatureStatus by remember { mutableDoubleStateOf(0.0) }
    val cityList = listOf<String>("Vilnius", "Kiev", "London","New York", "Berlin")
    var citySelected by remember { mutableStateOf(cityList[0]) }
    var expanded by remember { mutableStateOf(false) }

    val response = aPis.getRealtimeWeatherAsync(
        citySelected,
        "ru",
        object: APICallBack<CurrentJsonResponse> {
        override fun onSuccess(context: HttpContext, response: CurrentJsonResponse) {
            weatherStatus = response.current.condition.text
            iconURL = "https:${response.current.condition.icon}"
            temperatureStatus = response.current.tempC

        }
        override fun onFailure(context: HttpContext,response: Throwable) {
            weatherStatus = "Error of loading weather"
        }

    })

    Row (
        modifier = modifier
            .padding(start = 12.dp, top = 24.dp),

    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {expanded = !expanded}
        ) {
            OutlinedTextField(
                value = citySelected,
                onValueChange = {},
                label = {Text(text = "Choose city...")},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                modifier = modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu (
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                cityList.forEach { city ->
                    DropdownMenuItem(
                        text = {Text(city)},
                        onClick = {
                            citySelected = city
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 54.dp)
            .fillMaxSize()

    ) {
        iconURL?.let{
            AsyncImage(
                model = it,
                contentDescription = weatherStatus,
                modifier = modifier.size(128.dp)
            )
        }
        Text(
            text =" + ${temperatureStatus} °C",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = modifier
        )
        Text(
            text = weatherStatus,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = modifier
                .padding(top = 24.dp)

        )

    }
}

@Preview(showBackground = true)
@Composable
fun WeatherAppPreview() {
    SimpleAppWeatherTheme {
        WeatherApp()
    }

}
