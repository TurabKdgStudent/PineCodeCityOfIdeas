package be.kdg.cityofideas.fragments.ideasFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.kdg.cityofideas.adapters.RvIdeasAdapter

import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.BASE_URL
import be.kdg.cityofideas.rest.data.Idea
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject


class IdeasFragment : Fragment() {

    private lateinit var listener : RvIdeasAdapter.OnIdeaSelectedListener
    private lateinit var rvIdeas : RecyclerView
    var ideationIndex : Int = 0
    private lateinit var ideas : ArrayList<Idea>
    private lateinit var requestQueue : RequestQueue
    private lateinit var adapter : RvIdeasAdapter
    private var ideaId : String = ""

    private lateinit var addIdea: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_ideas, container, false)
        ideas = ArrayList()
        requestQueue = Volley.newRequestQueue(this.context)
        queue()

        addIdea = view.findViewById(R.id.addIdeaButton)
        addIdeaHandler()

        rvIdeas = view.findViewById(R.id.rv_Ideas)
        rvIdeas.layoutManager = LinearLayoutManager(activity)

        adapter = RvIdeasAdapter(context,ideaId,listener,ideationIndex,ideas)
        rvIdeas.adapter = adapter

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RvIdeasAdapter.OnIdeaSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnIdeaInteractionListener")
        }
    }

    private fun addIdeaHandler(){
        val addIdeaFragment = AddIdeaFragment()
        addIdeaFragment.ideationIndex = ideationIndex
        addIdea.setOnClickListener {
            fragmentManager!!.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                .replace(R.id.fragment_container,addIdeaFragment)
                .addToBackStack("").commit()
        }
    }

    fun queue(){
        val index = ideationIndex
        val query = "query{ideation(id : $index) {ideas{id title explanationShort picture likes{id} shares{id} maincomments{id} }}}"
        val jsonObject = JSONObject()
        jsonObject.put("query", query)

        // Volley post request with parameters
        val request = JsonObjectRequest(Request.Method.POST,BASE_URL,jsonObject,
            Response.Listener { response ->
                // Process the json
                try {
                    val obj = response.getJSONObject("data")
                    println(obj)
                    val conn1 = obj.getJSONObject("ideation").getJSONArray("ideas")
                    println(conn1)
                    for (i in 0 until conn1.length()) {
                        val obj2 = conn1.getJSONObject(i)

                        val id = obj2.getString("id")
                        val title = obj2.getString("title")
                        val explanationShort = obj2.getString("explanationShort")
                        val picture = obj2.getString("picture")
                        val likes = obj2.getJSONArray("likes").length().toString()
                        val shares = obj2.getJSONArray("shares").length().toString()
                        val comments = obj2.getJSONArray("maincomments").length().toString()
                        ideas.add(Idea(id,title,null,explanationShort,null,picture,null,null,null,null,likes,comments,shares))
                        ideaId = id
                        adapter.notifyDataSetChanged()

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
