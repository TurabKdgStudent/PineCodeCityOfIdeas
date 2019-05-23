package be.kdg.cityofideas.drawer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.*
import be.kdg.cityofideas.rest.data.Idea
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class QRFragment : Fragment(){


    private lateinit var btnScan: Button
    var index = ""
    private lateinit var requestQueue : RequestQueue
    private lateinit var thisIdea: Idea

    private lateinit var qrScanIntegrator: IntentIntegrator

    private lateinit var logo : ImageView
    private lateinit var titel : TextView
    private lateinit var explanationShort : TextView
    private lateinit var numberLikes : TextView
    private lateinit var date : TextView
    private lateinit var username : TextView
    private lateinit var btnVote : Button
    private lateinit var btnShare : ImageButton
    private lateinit var btnLike : ImageButton
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    @Inject lateinit var apiService : ApiService
    private val cPicasso : PicassoTrustAll = PicassoTrustAll()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qrscanner, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestQueue = Volley.newRequestQueue(this.context)



        logo= view.findViewById(R.id.SingleIdeaLogo)
        titel = view.findViewById(R.id.IdeaTitel)
        numberLikes = view.findViewById(R.id.numberlikes)
        explanationShort = view.findViewById(R.id.explanationShort)
        date = view.findViewById(R.id.IdeaPostDateText)
        username = view.findViewById(R.id.IdeaUserName)
        btnVote = view.findViewById(R.id.Vote)
        btnShare = view.findViewById(R.id.shareIdeaButtonQr)
        btnLike = view.findViewById(R.id.LikeButton)
        swipeRefreshLayout = view.findViewById(R.id.refreshSwipe)


        btnVote.visibility = View.INVISIBLE
        btnShare.visibility = View.INVISIBLE
        btnLike.visibility = View.INVISIBLE


        btnScan = view.findViewById(R.id.Scan)

        addEventHandlers()

        qrScanIntegrator = IntentIntegrator.forSupportFragment(this)
        qrScanIntegrator.setOrientationLocked(true)

        qrScanIntegrator.setPrompt(getString(R.string.scan_a_code))
        qrScanIntegrator.setCameraId(0)  // Use a specific camera of the device
        qrScanIntegrator.setBeepEnabled(false)
        qrScanIntegrator.setBarcodeImageEnabled(true)

        super.onViewCreated(view, savedInstanceState)

    }
    private fun addEventHandlers(){
        btnScan.setOnClickListener { performAction() }

        btnLike.setOnClickListener {
            postVote()
            updateFields()
        }

        btnShare.setOnClickListener {
            val message = VOTE_URL + "Idea/${thisIdea.id}"
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,message)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent,"Share to: "))
        }

        swipeRefreshLayout.setOnRefreshListener{
            queue()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun performAction() {
        qrScanIntegrator.initiateScan()

    }

    private fun updateFields(){
        context?.let { cPicasso.getInstance(it) }!!.load("$IMAGE_URL${thisIdea.photo}").fit().into(logo)
        titel.text = thisIdea.title
        explanationShort.text = thisIdea.explanationShort
        numberLikes.text = thisIdea.numberOfLikes
        date.text = thisIdea.date
        username.text = thisIdea.userName

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                Toast.makeText(activity, R.string.result_not_found, Toast.LENGTH_LONG).show()
            } else {
                // If QRCode contains data.
                try {
                    // Converting the data to json format
                    val obj = JSONObject(result.contents)
                    index = obj.getString("id")
                    queue()
                    btnShare.visibility = View.VISIBLE
                    btnLike.visibility = View.VISIBLE




                } catch (e: JSONException) {
                    e.printStackTrace()

                    // Data not in the expected format. So, whole object as toast message.
                    Toast.makeText(activity, result.contents, Toast.LENGTH_LONG).show()
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun queue(){
        val index = index
        val query = "query{idea(id : $index) {id title explanationShort picture likes{id} date account{userName}}}"
        val jsonObject = JSONObject()
        jsonObject.put("query", query)

        val request = JsonObjectRequest(Request.Method.POST,BASE_URL,jsonObject,
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
                    val picture = idea.getString("picture")
                    val date = idea.getString("date")
                    val likes = idea.getJSONArray("likes").length().toString()

                    val account = idea.getJSONObject("account")
                    val username = account.getString("userName")

                    thisIdea = Idea(id,title,date,short,null,picture,null,username,null,null,likes,null,null)

                    updateFields()

                }catch (e:Exception){
                    println("FAILQRFRAGMENT")
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
            object : Callback<String>{
                override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                        println(response.body().toString())
                    queue()
                    Toast.makeText(context,"Bedankt voor uw stem!",Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    println(t.printStackTrace())
                    Toast.makeText(context,"Log in om te kunnen stemmen!",Toast.LENGTH_LONG).show()

                }
            }
        )
    }
    private fun postVote(){
        apiService = APIUtils().getAPIService()
        val id = thisIdea.id!!.toInt()
        println(id)
        sendVote(id)

    }
}