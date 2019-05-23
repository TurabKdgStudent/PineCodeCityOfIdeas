package be.kdg.cityofideas.identity.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import be.kdg.cityofideas.MainActivity

import be.kdg.cityofideas.R
import be.kdg.cityofideas.identity.Register
import be.kdg.cityofideas.identity.User
import be.kdg.cityofideas.rest.APIUtils
import be.kdg.cityofideas.rest.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class RegisterFragment : Fragment() {

    private lateinit var login : TextView
    private lateinit var email: AutoCompleteTextView
    private lateinit var pwd: EditText
    private lateinit var confirmPwd: EditText
    private lateinit var registerbtn: Button

    @Inject
    lateinit var apiService : ApiService

    private lateinit var register : Register

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register, container, false)

        initialiseViews(view)
        addEventHandlers()

        return view
    }

    private fun initialiseViews(view : View){

        login = view.findViewById(R.id.goToLogin)
        email = view.findViewById(R.id.email)
        pwd = view.findViewById(R.id.password)
        confirmPwd = view.findViewById(R.id.confirmpassword)
        registerbtn = view.findViewById(R.id.RegisterButton)
    }

    private fun addEventHandlers(){
        login.setOnClickListener{
            fragmentManager!!.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                .replace(R.id.fragment_container,LoginFragment()).addToBackStack("").commit()
        }
        registerbtn.setOnClickListener {
            register()
            view?.let { it1 -> MainActivity().hideSoftKeyboard(it1) }
        }

    }

    fun register(){
        register = Register(email.text.toString(),pwd.text.toString(),confirmPwd.text.toString())
        apiService = APIUtils().getAPIService()

        apiService.register(register).enqueue(object : Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context,"REGISTERCALLBACKFAIL", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(context,"Succes! Log in om verder te gaan!", Toast.LENGTH_LONG).show()
                    fragmentManager!!.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                        .replace(R.id.fragment_container,LoginFragment()).addToBackStack("").commit()
                } else{
                    Toast.makeText(context,"Er is een fout opgetreden. Gelieve later opnieuw te proberen.",Toast.LENGTH_LONG).show()
                }
            }
        })
    }


}
