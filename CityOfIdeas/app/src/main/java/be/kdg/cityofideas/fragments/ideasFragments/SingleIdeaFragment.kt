package be.kdg.cityofideas.fragments.ideasFragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.*
import be.kdg.cityofideas.rest.data.Idea
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SingleIdeaFragment : Fragment() {
    var ideaIndex : Int = 0
    private lateinit var requestQueue : RequestQueue
    private lateinit var thisIdea: Idea
    private lateinit var logo : ImageView
    private lateinit var titel : TextView
    private lateinit var explanationShort : TextView
    private lateinit var explanationLong : TextView
    private lateinit var numberLikes : TextView
    private lateinit var date : TextView
    private lateinit var username : TextView
    private lateinit var sharebtn : ImageButton
    private lateinit var likebtn : ImageButton
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var circularProgressDrawable : CircularProgressDrawable


    @Inject
    lateinit var apiService : ApiService

    private val cPicasso : PicassoTrustAll = PicassoTrustAll()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_single_idea, container, false)
        requestQueue = Volley.newRequestQueue(this.context)
        initialiseViews(view)
        queue()
        addEventHandlers()
        return view
    }

    private fun initialiseViews(view: View){
        logo= view.findViewById(R.id.SingleIdeaLogo)
        titel = view.findViewById(R.id.IdeaTitel)
        numberLikes = view.findViewById(R.id.numberlikes)
        explanationShort = view.findViewById(R.id.explanationShort)
        explanationLong = view.findViewById(R.id.explanationLong)
        date = view.findViewById(R.id.IdeaPostDateText)
        username = view.findViewById(R.id.IdeaUserName)
        sharebtn = view.findViewById(R.id.shareIdeaButton)
        likebtn = view.findViewById(R.id.LikeButtonSingleIdea)
        swipeRefreshLayout = view.findViewById(R.id.refreshSwipe)
        circularProgressDrawable = CircularProgressDrawable(this.context!!)
    }
    private fun addEventHandlers(){
        sharebtn.setOnClickListener {
            val message = VOTE_URL + "Idea/${thisIdea.id}"
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,message)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent,"Share to: "))
        }
        likebtn.setOnClickListener {
            PostVote()
            updateFields()
        }
        swipeRefreshLayout.setOnRefreshListener{
            queue()
            swipeRefreshLayout.isRefreshing = false
        }
    }
    private fun updateFields(){
      //  context?.let { cPicasso.getInstance(it) }!!.load("$IMAGE_URL${thisIdea.photo}").fit().into(logo)
        circularProgressDrawable.setStyle(CircularProgressDrawable.LARGE)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(context).load("$IMAGE_URL${thisIdea.photo}").diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().placeholder(circularProgressDrawable).error(R.drawable.ic_broken_image_black_50dp).into(logo)
        titel.text = thisIdea.title
        explanationShort.text = thisIdea.explanationShort
        explanationLong.text = thisIdea.explanationLong
        numberLikes.text = thisIdea.numberOfLikes
        date.text = thisIdea.date
        username.text = thisIdea.userName

    }
    fun queue(){
        val index = ideaIndex
        val query = "query{idea(id : $index) {id title explanationShort explanationLong picture likes{id} shares{id} date account{userName}}}"
        val jsonObject = JSONObject()
        jsonObject.put("query", query)
        val request = JsonObjectRequest(Request.Method.POST, BASE_URL,jsonObject,
            Response.Listener { response ->
                // Process the json
                try {
                    val obj = response.getJSONObject("data")
                    println(obj)
                    val idea = obj.getJSONObject("idea")
                    // println(project)
                    val id = idea.getString("id")
                    val title = idea.getString("title")
                    val short = idea.getString("explanationShort")
                    val long = idea.getString("explanationLong")
                    val picture = idea.getString("picture")
                    val date = idea.getString("date")
                    val likes = idea.getJSONArray("likes").length().toString()
                    val account = idea.getJSONObject("account")
                    val username = account.getString("userName")
                    thisIdea = Idea(id,title,date,short,long,picture,null,username,null,null,likes,null,null)

                    updateFields()
                }catch (e:Exception){
                    println("FAILSINGLEIDEAFRAGMENT")
                    e.printStackTrace()
                }
            }, Response.ErrorListener{
                // Error in request
                print("FAILVOLLEY")
            })
        requestQueue.add(request)
    }
    private fun sendVote(pathId  : Int){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val token = sharedPref.getString("token","leeg")
            apiService.ideaVote(pathId,token,"").enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                    queue()
                    if (response.code() == 401){
                        Toast.makeText(context,"Log in om te kunnen stemmen!",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(context,"Bedankt voor uw stem!", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    println(t.printStackTrace())
                    Toast.makeText(context,"Log in om te kunnen stemmen!",Toast.LENGTH_LONG).show()
                }
            }
        )
    }
    private fun PostVote(){
        apiService = APIUtils().getAPIService()
        val id = thisIdea.id!!.toInt()
        println(id)
        sendVote(id)
    }
}
