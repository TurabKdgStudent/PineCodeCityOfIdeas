package be.kdg.cityofideas.identity.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import be.kdg.cityofideas.identity.Login

import be.kdg.cityofideas.R
import be.kdg.cityofideas.drawer.PlatformFragment
import be.kdg.cityofideas.rest.APIUtils
import be.kdg.cityofideas.rest.ApiService
import com.auth0.android.jwt.JWT
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class LoginFragment : Fragment() {

    private lateinit var register : TextView
    private lateinit var email: AutoCompleteTextView
    private lateinit var pwd: EditText
    private lateinit var loginbtn: Button
    var username = ""

    @Inject
    lateinit var apiService : ApiService

    private lateinit var login : Login
    private lateinit var token : String
    private lateinit var navigationView: NavigationView
    private lateinit var navLoginBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        initialiseViews(view)
        addEventHandlers()

        return view
    }

    private fun initialiseViews(view : View){
        register = view.findViewById(R.id.RegisterTextView)
        email = view.findViewById(R.id.email)
        pwd = view.findViewById(R.id.password)
        loginbtn = view.findViewById(R.id.LoginButton)
        navigationView = activity!!.findViewById(R.id.nav_view)
        navLoginBtn = navigationView.findViewById(R.id.nav_login)
    }

    private fun addEventHandlers(){
        register.setOnClickListener{
            fragmentManager!!.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                .replace(R.id.fragment_container,RegisterFragment()).addToBackStack("").commit()
        }
        loginbtn.setOnClickListener {
            login()
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            navLoginBtn.text = resources.getString(R.string.Log_out)
            with (sharedPref!!.edit()) {
                putString("logbutton", navLoginBtn.text.toString())
                apply()
            }
        }
    }

    fun login(){
        login = Login(email.text.toString(),pwd.text.toString())
        apiService = APIUtils().getAPIService()

        apiService.login(login).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context,"LOGINCALLBACKFAIL",Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    token = response.body().toString()
                    println(token)
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                    with (sharedPref.edit()) {
                        putString("token", "Bearer $token")
                        apply()
                    }
                    println(sharedPref.getString("token","leeg"))
                    val parsedJwt = JWT(token)
                    val subscriptionMetaData = parsedJwt.getClaim("sub")
                    val parsedValue = subscriptionMetaData.asString()
                    println(parsedValue)
                    username = parsedValue!!
                    sharedPref.edit().putString("username",username).apply()
                    activity!!.usernameNavhead.text = sharedPref.getString("username","")
                    Toast.makeText(context,"U bent ingelogd, welkom $parsedValue!",Toast.LENGTH_LONG).show()
                    fragmentManager!!.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                        .replace(R.id.fragment_container, PlatformFragment()).addToBackStack("").commit()
                }else{
                    Toast.makeText(context,"Foutieve inloggegevens!",Toast.LENGTH_LONG).show()

                }
            }

        })

    }



}
