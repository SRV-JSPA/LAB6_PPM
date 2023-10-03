package com.example.lab6_ppm

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab6_ppm.ui.theme.LAB6_PPMTheme
import org.json.JSONObject
import retrofit2.Call

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB6_PPMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    getCities()
                }
            }
        }
    }
}

interface TeleportApi {
    @GET("/api/urban_areas")
    fun getCiudades(): Call<ciudades>
}

fun getCities(): List<ciudades> {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.teleport.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val API = retrofit.create(TeleportApi::class.java)
    val ListCiudades = mutableListOf<ciudades>()

    try {
        val call = API.getCiudades()
        val response = call.execute()

        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                val json = JSONObject(responseBody.toString())
                val curies = json.getJSONObject("curies")
                val uaItemArray = curies.getJSONArray("ua:item")

                for (i in 0 until uaItemArray.length()) {
                    val cityData = uaItemArray.getJSONObject(i)
                    val name = cityData.getString("name")
                    val slug = cityData.getString("slug")
                    val city = ciudades(name, slug)
                    ListCiudades.add(city)
                }
            }
        }
    } catch (e: Exception) {
        println("error de conexion")
    }

    return ListCiudades
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
    LAB6_PPMTheme {
        getCities()
    }
}