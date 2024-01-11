package com.enesas.coroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.enesas.coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        var view = binding.root
        setContentView(view)

        /*
        GlobalScope.launch { // GlobalScope tüm uygulama boyunca olacağı için tercih edilmiyor.
            delay(3000L)
            Log.d(TAG,"Coroutine'den geldim. Hello from thread ${Thread.currentThread().name}")
        }
        Log.d(TAG,"Hello from thread ${Thread.currentThread().name}")

         */

        /*
        GlobalScope.launch {// Aynı coroutine scope içinde 2 tane suspend fun çağırdık. O yüzden 6 saniye sonra logcat'te sonuç gelecek.
            val networkCallAnswer = doNetworkCall()
            val networkCallAnswer2 = doNetworkCall2()

            Log.d(TAG,networkCallAnswer)
            Log.d(TAG,networkCallAnswer2)
        }

         */

        /*
        CoroutineScope(Dispatchers.Main).launch { // Ama async kullanıp await fonksiyonu ile ikiside 3 saniyede gelmiş olur.

            val networkCallAnswer = async { doNetworkCall() }
            val networkCallAnswer2 = async { doNetworkCall2() }

            Log.d(TAG, "Answer1 is ${networkCallAnswer.await()}")
            Log.d(TAG, "Answer2 is ${networkCallAnswer2.await()}")
        }

         */

        /*
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG,"Coroutine şu thread içinde başladı: ${Thread.currentThread().name}")
            val networkCallAnswer = doNetworkCall()
            withContext(Dispatchers.Main){// Dispatcher'ı değiştirmek için kullanıyoruz.
                Log.d(TAG,"Coroutine ardından ${Thread.currentThread().name} geçti ")
                binding.textView.text = networkCallAnswer
            }
        }

         */

        /*
        Log.d(TAG,"Before runBlocking")
        runBlocking {// Bulunduğu Dispatcher'ı blokluyor.
            Log.d(TAG,"Start runBlocking")
            delay(5000L)
            Log.d(TAG,"End runBlocking")
        }
        Log.d(TAG,"After runBlocking")

         */

        /*
        Log.d(TAG,"Before runBlocking")
        runBlocking {
            launch(Dispatchers.IO) {
                delay(3000L)
                Log.d(TAG,"Finish IO coroutine") // Bu örnekte ise 3s + 3s = 6s değil. Direkt 3s sonra 2 launch scopeta aynı anda çalıştırılıyor.
            }
            launch(Dispatchers.IO) {
                delay(3000L)
                Log.d(TAG,"Finish IO coroutine2")
            }
            Log.d(TAG,"Start runBlocking")
            delay(5000L)
            Log.d(TAG,"End runBlocking")
        }
        Log.d(TAG,"After runBlocking")

         */

        /*
        val job = CoroutineScope(Dispatchers.Default).launch{
            repeat(5){
                Log.d(TAG,"Coroutine hala çalışıyor....")
                delay(1000L)
            }
        }


        runBlocking {
            job.join()
            Log.d(TAG,"Main Thread devam ediyor...")

        }

         */
        /*
        val job = CoroutineScope(Dispatchers.Default).launch{
            repeat(5){
                Log.d(TAG,"Coroutine hala çalışıyor....")
                delay(1000L)
            }
        }

        runBlocking {
            delay(2000L)
            job.cancel()
            Log.d(TAG,"Job cancel edildi...")

        }

        */
        /*
        binding.button.setOnClickListener {
            lifecycleScope.launch {// lifecyclescope sayesinde diğer aktiviteye geçip bunu finish() yaptığımda burada çalışan coroutine duruyor.
                while(true){
                    delay(1000L)
                    Log.d(TAG,"Main activity çalışıyor...")
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                delay(5000L)
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivity(intent)
                finish()

            }
        }

         */

        /* NORMALDE RETROFIT'TEN BU ŞEKİLDE VERİ ÇEKERİZ.
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommentsAPI::class.java)

        api.getComments().enqueue(object : Callback<List<Comments>>{
            override fun onResponse(
                call: Call<List<Comments>>,
                response: Response<List<Comments>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        for (comment in it){
                            Log.d(TAG,comment.toString())
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Comments>>, t: Throwable) {
                Log.d(TAG,"Error: $t")
            }

        })

         */

        /*
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommentsAPI::class.java)

        // Böyle daha kolay ve temiz çağırabiliriz.
        CoroutineScope(Dispatchers.IO).launch {
            val comments = api.getComments().await()
            for (comment in comments){
                Log.d(TAG,comment.toString())
            }
        }
        // Yada böyle.
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getComments().awaitResponse()
            if (response.isSuccessful){
                for (comment in response.body()!!){
                    Log.d(TAG,comment.toString())
                }
            }
        }
        // Yada direkt @GET fonksiyonunu suspend yaparak ve hiç await() kullanmaya gerek kalmadan yapabiliriz.
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getSuspendComments()
            if (response.isSuccessful){
                for (comment in response.body()!!){
                    Log.d(TAG,comment.toString())
                }
            }
        }


         */



    }
    val BASE_URL = "https://jsonplaceholder.typicode.com/"


    suspend fun doNetworkCall() : String{
        delay(3000L)
        return "This is the answer"
    }

    suspend fun doNetworkCall2() : String{
        delay(3000L)
        return "This is the answer2"
    }


}