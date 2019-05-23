package be.kdg.cityofideas.fragments.projectPageFragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.kdg.cityofideas.adapters.RvIdeationsAdapter

import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.BASE_URL
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class IdeationsFragment : Fragment() {

    private lateinit var listener :RvIdeationsAdapter.OnIdeationSelectedListener
    private lateinit var rvIdeations : RecyclerView
    var projectIndex : Int = 0
    private lateinit var ideations : ArrayList<be.kdg.cityofideas.rest.data.Ideation>
    private lateinit var requestQueue : RequestQueue
    private lateinit var adapter : RvIdeationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_ideations, container, false)

        ideations = ArrayList()
        requestQueue = Volley.newRequestQueue(this.context)
        queue()
        rvIdeations = view.findViewById(R.id.rv_Ideations)
        rvIdeations.layoutManager = LinearLayoutManager(activity)
        adapter = RvIdeationsAdapter(context ,listener,projectIndex,ideations)

        rvIdeations.adapter = adapter

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RvIdeationsAdapter.OnIdeationSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement onIdeationSelectedListener!")
        }
    }

    fun queue(){
        val index = projectIndex+1
        val query = "query{project(id: $index){phases{ideations{question about picture likes{id} shares{id} ideas{id}}}}}"
        val jsonObject = JSONObject()
        jsonObject.put("query", query)

        // Volley post request with parameters
        val request = JsonObjectRequest(Request.Method.POST, BASE_URL,jsonObject,
            Response.Listener { response ->
                // Process the json
                try {
                    val obj = response.getJSONObject("data")
                    println(obj)
                    val phasesArray = obj.getJSONObject("project").getJSONArray("phases")
                    println(phasesArray)
                    for (i in 0 until phasesArray.length()) {
                        val obj2 = phasesArray.getJSONObject(i)

                        val ideationArray = obj2.getJSONArray("ideations")
                        println(ideationArray)
                        for (j in 0 until ideationArray.length()) {
                            val ideation = ideationArray.getJSONObject(j)

                            val question = ideation.getString("question")
                            val about = ideation.getString("about")
                            val picture = ideation.getString("picture")
                            val likes = ideation.getJSONArray("likes").length().toString()
                            val shares = ideation.getJSONArray("shares").length().toString()
                            val ideas = ideation.getJSONArray("ideas").length().toString()

                            ideations.add(
                                be.kdg.cityofideas.rest.data.Ideation(
                                    null,
                                    question,
                                    about,
                                    picture,
                                    null,
                                    null,
                                    likes,
                                    ideas,
                                    shares
                                )
                            )
                        }
                    }

                    adapter.notifyDataSetChanged()
                }catch (e:Exception){
                    println("FAILLL")
                    e.printStackTrace()
                }

            }, Response.ErrorListener{
                // Error in request
                print("FAILVOLLEY")
            })
        requestQueue.add(request)
    }

}
