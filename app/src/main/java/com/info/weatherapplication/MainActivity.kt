package com.info.weatherapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    var city:String = "Gazipur,bd";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherTask().execute();

        fun input(){
            val editText = findViewById<EditText>(R.id.getCity);
            city = editText.text.toString();
        }

        val button = findViewById<Button>(R.id.searchBtn);
        button.setOnClickListener{
            input();
            weatherTask().execute();
        }
    }


    inner class weatherTask() : AsyncTask<String, Void, String>()
    {
        override fun onPreExecute()
        {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.progressLoader).visibility = View.VISIBLE;
            findViewById<ConstraintLayout>(R.id.mainView).visibility = View.GONE;
            findViewById<TextView>(R.id.viewMessege).visibility = View.GONE;
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?
            try{
                response = URL("https://api.weatherapi.com/v1/current.json?key=bf4391c96e9843d79a7214902222311&q=$city")
                    .readText(Charsets.UTF_8)
            }
            catch(e: Exception){
                response = null;
            }
            return response
        }

        override fun onPostExecute(response_data: String?) {
            super.onPostExecute(response_data)
            try{
                val jetJson = JSONObject(response_data);
                val main = jetJson.getJSONObject("current");
                val temp = main.getString("temp_c");
                val currentforecast = main.getJSONObject("condition");
                val conditionText = currentforecast.getString("text");

                findViewById<TextView>(R.id.temp).text = temp+" °C";
                findViewById<TextView>(R.id.currenntMessage).text = conditionText;
                findViewById<ProgressBar>(R.id.progressLoader).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.mainView).visibility = View.VISIBLE

            }
            catch(e: Exception){
                findViewById<ProgressBar>(R.id.progressLoader).visibility = View.GONE
                findViewById<TextView>(R.id.viewMessege).visibility = View.VISIBLE
            }
        }
    }


}