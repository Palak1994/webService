package com.rotate.application

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.rotate.application.Repository.ApiFactory
import com.rotate.application.data.model.Convert
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI
import javax.net.ssl.SSLSocketFactory


class MainActivity : AppCompatActivity() {
    lateinit var dataAdapter: DataAdapter;
    private lateinit var recyclerView: RecyclerView
    var list: MutableList<BitcoinTracker> = ArrayList<BitcoinTracker>();

    companion object {
        const val WEB_SOCKET_URL = "wss://ws.blockchain.info/inv"
        const val TAG = "Coinbase"
    }

    private lateinit var webSocketClient: WebSocketClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        recyclerView = findViewById<RecyclerView>(R.id.list)
    }

    override fun onStart() {
        super.onStart()
        initWebSocket()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initWebSocket() {
        val coinbaseUri: URI? = URI(WEB_SOCKET_URL)

        createWebSocketClient(coinbaseUri)
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    private fun createWebSocketClient(coinbaseUri: URI?) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {

            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
                subscribe()
            }

            override fun onMessage(message: String?) {
                Log.d(TAG, "onMessage: $message")
                //setUpBtcPriceText(message)

                val gson = Gson()
                val bitcoin: BitcoinTracker = gson.fromJson(message, BitcoinTracker::class.java)
                val request = ApiFactory.buildService(ApiInterface::class.java)
                val call = request.getUser("BTC", "USD")
                call.enqueue(object : Callback<Convert> {
                    override fun onResponse(call: Call<Convert>, response: Response<Convert>) {
                        if (response.isSuccessful) {
                            //
                            var price =
                                ((bitcoin.x.out.get(0).value.toDouble()) * (0.00000001)) * (response.body()?.USD
                                    ?: "40596.10").toDouble()
                            Log.i("convert", price.toString() ?: "12121")
                            bitcoin.x.out.get(0).value = price.toString()
                            if (list.size < 5) {
                                list.add(bitcoin)
                            } else {
                                list.sortByDescending { it.x.out.get(0).value }
                                if (bitcoin.x.out.get(0).value > list.get(0).x.out.get(0).value) {
                                    list.removeAt(0)
                                    list.add(bitcoin)
                                }
                            }
                            runOnUiThread {
                                var linearLayoutManager: LinearLayoutManager =
                                    LinearLayoutManager(
                                        this@MainActivity,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                recyclerView.layoutManager = linearLayoutManager
                                dataAdapter = DataAdapter(this@MainActivity, list)
                                recyclerView.adapter = dataAdapter
                            }
                        }
                    }

                    override fun onFailure(call: Call<Convert>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })


            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose" + reason)
                // unsubscribe()
            }

            override fun onError(ex: Exception?) {
                Log.e("createWebSocketClient", "onError: ${ex?.message}")
            }

        }
    }

    private fun subscribe() {
        webSocketClient.send(
            "{\n" +
                    "    \"op\": \"unconfirmed_sub\"" +
                    "}"
        )
    }

    override fun onPause() {
        super.onPause()
        webSocketClient.close()
    }

}