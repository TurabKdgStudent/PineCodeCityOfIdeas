package be.kdg.cityofideas.drawer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.kdg.cityofideas.adapters.RvProjectsAdapter
import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.*
import be.kdg.cityofideas.rest.data.Phase
import be.kdg.cityofideas.rest.data.Project
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

@Suppress("NAME_SHADOWING")
class ProjectsFragment : Fragment() {

    private lateinit var rvProjects: androidx.recyclerview.widget.RecyclerView
    private lateinit var listener :RvProjectsAdapter.OnProjectSelectedListener
    private lateinit var projects : ArrayList<Project>
    private lateinit var phases : ArrayList<Phase>
    private lateinit var requestQueue : RequestQueue
    private lateinit var adapter : RvProjectsAdapter
    var platformIndex = 0



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {
        val view = inflater.inflate(R.layout.fragment_projecten, container, false)

        rvProjects = view.findViewById(R.id.rv_Projects)
        rvProjects.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)

        projects = ArrayList()
        phases = ArrayList()
        requestQueue = Volley.newRequestQueue(this.context)

        queue()

        adapter = RvProjectsAdapter(
            context,projects ,listener)
        rvProjects.adapter = adapter

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RvProjectsAdapter.OnProjectSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement onTeamSelectedListener!")
        }
    }

    fun queue(){
        val index = platformIndex+1
        val query = "query{platform(id: $index){projects{id title about picture isActive phases{id}}}}"
    //    val query = "query{platform(id: 1){projects{id title about picture isActive phases{id}}}}"
        val jsonObject = JSONObject()
        jsonObject.put("query", query)

        // Volley post request with parameters
        val request = JsonObjectRequest(Request.Method.POST, BASE_URL,jsonObject,
            com.android.volley.Response.Listener { response ->
                // Process the json
                try {
                    val obj = response.getJSONObject("data")
                    println(obj)
                    val conn = obj.getJSONObject("platform").getJSONArray("projects")

                    for (i in 0 until conn.length()) {
                        val obj2 = conn.getJSONObject(i)

                        val title = obj2.getString("title")
                        val id = obj2.getString("id")
                        val pic = obj2.getString("picture")
                        val bool = obj2.getString("isActive")

                        println(title)

                        val phase = obj2.getJSONArray("phases")
                        for (i in 0 until phase.length()){
                            val phaseObj = phase.getJSONObject(i)

                            val phaseId = phaseObj.getString("id")
                            println(phaseId)
                            phases.add(Phase(phaseId,null,null,null,null,null,null,null))
                        }
                        println(phases.size)

                        val status : String
                        status = if (bool == "true"){
                            "Actief"
                        } else "Gesloten"

                        projects.add(Project(id,title,null,pic,status,phases))
                    }
                    println(projects[0])
                    adapter.notifyDataSetChanged()
                }catch (e:Exception){
                    println("FAILLL")
                }

            }, com.android.volley.Response.ErrorListener{
                // Error in request
                print("FAILVOLLEY")
            })
        requestQueue.add(request)
    }
    }

