package be.kdg.cityofideas.drawer


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.kdg.cityofideas.adapters.RvPlatformsAdapter

import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.BASE_URL
import be.kdg.cityofideas.rest.data.Platform
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class PlatformFragment : Fragment() {

    private lateinit var rvPlatforms : RecyclerView
    private lateinit var requestQueue : RequestQueue
    private lateinit var adapter : RvPlatformsAdapter
    private lateinit var listener: RvPlatformsAdapter.OnPlatformSelectedListener
    private lateinit var platforms : ArrayList<Platform>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_platform, container, false)

        rvPlatforms = view.findViewById(R.id.platformRv)
        rvPlatforms.layoutManager = LinearLayoutManager(activity)

        platforms = ArrayList()
        requestQueue = Volley.newRequestQueue(this.context)


        adapter = RvPlatformsAdapter(context,platforms,listener)
        rvPlatforms.adapter = adapter

        queue()

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RvPlatformsAdapter.OnPlatformSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement onPlatformSelectedListener!")
        }
    }

    fun queue() {
        val query = "query{platforms{id name explanationShort}}"
        val jsonObject = JSONObject()
        jsonObject.put("query", query)

        val request = JsonObjectRequest(Request.Method.POST, BASE_URL,jsonObject,
            com.android.volley.Response.Listener { response ->
                // Process the json
                try {
                    val obj = response.getJSONObject("data")
                    println(obj)
                    val conn = obj.getJSONArray("platforms")

                    for (i in 0 until conn.length()) {
                        val obj2 = conn.getJSONObject(i)

                        val title = obj2.getString("name")
                        val id = obj2.getString("id")
                        val short = obj2.getString("explanationShort")

                        platforms.add(Platform(id,title,short,null,null,null))
                    }
                    println(platforms[0])
                    adapter.notifyDataSetChanged()
                }catch (e:Exception){
                    e.printStackTrace()
                    println("FAILLL")
                }

            }, com.android.volley.Response.ErrorListener{
                // Error in request
                print("FAILVOLLEY")
            })
        requestQueue.add(request)

    }


}
