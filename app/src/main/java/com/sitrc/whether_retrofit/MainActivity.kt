package com.sitrc.whether_retrofit

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // Private variables for view reference

    private var weatherData: TextView? = null
    private var latitude:EditText? = null
    private var longitude:EditText? = null
    private var img_groot:ImageView ? = null

    // To store Temperature value in Celsius
    private var temperature=0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Attaching the views
        weatherData = findViewById(R.id.whether_text)
        latitude=findViewById(R.id.latitude)
        longitude=findViewById(R.id.longitude)
        img_groot=findViewById(R.id.img_groot)

        findViewById<View>(R.id.button).setOnClickListener { getCurrentData() }
    }

    internal fun getCurrentData() {

        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(WeatherService::class.java)

        var lat = latitude?.text.toString()
        var lon = longitude?.text.toString()



        val call = service.getCurrentWeatherData(lat, lon, AppId)


        call.enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    temperature=weatherResponse.main!!.temp.toDouble()
                    temperature -= 273.15

                    if(temperature<26.00){
                        //cold
                        img_groot?.setImageResource(R.drawable.groot__cold)
                    }
                    else if(temperature>30.00){
                        //hot
                        img_groot?.setImageResource(R.drawable.groot__hot)
                    }
                    else{
                        //normal
                        img_groot?.setImageResource(R.drawable.groot)
                    }


                    val stringBuilder = "Country: " +
                            weatherResponse.sys!!.country +
                            "\n" +
                            "Temperature: " +
                            temperature +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main!!.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main!!.temp_max +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main!!.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main!!.pressure

                    weatherData!!.text = stringBuilder
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                weatherData!!.text = t.message
            }
        })
    }

    companion object {

        var BaseUrl = "http://api.openweathermap.org/"
        var AppId = "2e65127e909e178d0af311a81f39948c"

    }

}

// Africa - 8,34
// Antarctica - 82, 135
// India - 20,78