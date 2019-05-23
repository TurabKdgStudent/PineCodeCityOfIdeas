package be.kdg.cityofideas.fragments.projectPageFragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.kdg.cityofideas.adapters.RvSurveyAdapter

import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.BASE_URL
import be.kdg.cityofideas.rest.data.Survey
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class SurveysFragment : Fragment() {

    private lateinit var listener :RvSurveyAdapter.OnSurveySelectedListener
    private lateinit var rvSurvey : RecyclerView
    var projectIndex : Int = 0
    private lateinit var requestQueue : RequestQueue
    private lateinit var adapter : RvSurveyAdapter
    private lateinit var surveys : ArrayList<be.kdg.cityofideas.rest.data.Survey>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_surveys, container, false)

        rvSurvey = view.findViewById(R.id.rv_Surveys)
        rvSurvey.layoutManager = LinearLayoutManager(activity)
        surveys = ArrayList()
        requestQueue = Volley.newRequestQueue(this.context)
        queue()
        adapter = RvSurveyAdapter(context,surveys,listener,projectIndex)
        rvSurvey.adapter = adapter

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RvSurveyAdapter.OnSurveySelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement onSurveySelectedListener!")
        }
    }


    fun queue(){
        val index = projectIndex
        val query = "query{project(id: $index){phases{surveys{id phase{name} title about questions{id}} logo}}}"
        val jsonObject = JSONObject()
        jsonObject.put("query", query)

        // Volley post request with parameters
        val request = JsonObjectRequest(Request.Method.POST, BASE_URL,jsonObject,
            Response.Listener { response ->
                // Process the json
                try {
                    val obj = response.getJSONObject("data")
                    println(obj)
                    val conn1 = obj.getJSONObject("project").getJSONArray("phases")
                    println(conn1)
                    for (i in 0 until conn1.length()) {
                        val obj2 = conn1.getJSONObject(i)

                        val logo = obj2.getString("logo")
                        val conn2 = obj2.getJSONArray("surveys")
                        println(conn2)
                        for (j in 0 until conn2.length()) {
                            val obj3 = conn2.getJSONObject(j)

                            val id = obj3.getString("id")
                            val title = obj3.getString("title")
                            val about = obj3.getString("about")
                            val questions = obj3.getJSONArray("questions").length().toString()
                            val phase = obj3.getJSONObject("phase").getString("name")

                            surveys.add(Survey(id,null,title,about,null,questions,phase,logo))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }catch (e:Exception){
                    println("SurveyFail")
                    e.printStackTrace()
                }
            }, Response.ErrorListener{
                // Error in request
                print("FAILVOLLEY")
            })
        requestQueue.add(request)
    }

}
