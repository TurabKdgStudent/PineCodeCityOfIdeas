package be.kdg.cityofideas.fragments.projectPageFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.BASE_URL
import be.kdg.cityofideas.rest.IMAGE_URL
import be.kdg.cityofideas.rest.data.Phase
import be.kdg.cityofideas.rest.PicassoTrustAll
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


  class DetailFragment : Fragment() {
    private lateinit var projectpic : ImageView
    private lateinit var projectTitle : TextView
    private lateinit var explanation : TextView
    var projectIndex : Int = 0
      val index = 0

      //REST
    private lateinit var phases : ArrayList<Phase>
    private lateinit var requestQueue : RequestQueue
      private lateinit var thisproject : be.kdg.cityofideas.rest.data.Project
      private val cPicasso : PicassoTrustAll = PicassoTrustAll()




      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_detail, container, false)

        phases = ArrayList()
          thisproject = be.kdg.cityofideas.rest.data.Project(null,null,null,null,null,null)
        requestQueue = Volley.newRequestQueue(this.context)

        projectTitle = view.findViewById(R.id.ProjectTitel)
        explanation = view.findViewById(R.id.projectText)
        projectpic = view.findViewById(R.id.ProjectDetailPic)

          queue()
        return view
    }


    private fun updateFields() {
        projectTitle.text = thisproject.title
        explanation.text = thisproject.about
        context?.let { cPicasso.getInstance(it) }!!.load("$IMAGE_URL${thisproject.picture}").fit().into(projectpic)
    }

    fun queue(){
        val index = projectIndex
        val query = "query{project(id: $index ){id title about picture phases{name}}}"
        val jsonObject = JSONObject()
          jsonObject.put("query", query)

          // Volley post request with parameters
                val request = JsonObjectRequest(Request.Method.POST, BASE_URL,jsonObject,
                    Response.Listener { response ->
                  // Process the json
                  try {
                      val obj = response.getJSONObject("data")
                      println(obj)
                      val project = obj.getJSONObject("project")
                      val id = project.getString("id")
                      val title = project.getString("title")
                      val about = project.getString("about")
                      val picture = project.getString("picture")
                      println(id)
                      thisproject = be.kdg.cityofideas.rest.data.Project(id,title,about,picture,null,null)
                      updateFields()

                  }catch (e:Exception){
                      println("FAILDETAILFRAGMENT")
                      e.printStackTrace()
                  }

              }, Response.ErrorListener{
                  // Error in request
                  print("FAILVOLLEY")
              })
        requestQueue.add(request)
    }
}
