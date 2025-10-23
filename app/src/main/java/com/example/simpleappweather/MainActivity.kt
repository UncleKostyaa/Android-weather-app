package com.example.simpleappweather

import android.graphics.fonts.Font
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpleappweather.ui.theme.SimpleAppWeatherTheme
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

@Composable
fun WeatherApp() {

    val apiKey = BuildConfig.WEATHER_API_KEY

    val client = WeatherAPIClient(apiKey)
    val aPis: APIsController = client.apIs

    val response = aPis.getRealtimeWeatherAsync("London","ru", object: APICallBack<CurrentJsonResponse>{
        override fun onSuccess(context: HttpContext, response: CurrentJsonResponse){

        }
        override fun onFailure(context: HttpContext,response: Throwable){

        }

    })

    Column(verticalArrangement = Arrangement.Center) {
        Text(
            text = response.toString(),

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
