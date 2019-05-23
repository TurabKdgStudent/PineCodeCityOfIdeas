package be.kdg.cityofideas.fragments.ideasFragments


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import be.kdg.cityofideas.MainActivity
import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import be.kdg.cityofideas.rest.data.Idea
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import kotlin.random.Random


class AddIdeaFragment : Fragment() {

    var ideationIndex = 0

    private lateinit var titelInput: EditText
    private lateinit var shortInput: EditText
    private lateinit var longInput: EditText
    private lateinit var submit: FloatingActionButton
    private lateinit var titel: String
    private lateinit var short: String
    private lateinit var long: String
    private lateinit var url: Uri



    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_idea, container, false)

        titelInput = view.findViewById(R.id.TitelInput)
        shortInput = view.findViewById(R.id.ShortInput)
        longInput = view.findViewById(R.id.LongInput)
        submit = view.findViewById(R.id.AddIdeaSubmit)
        url = Uri.EMPTY

        addEventHandlers()

        return view
    }

    private fun addEventHandlers(){
        submit.setOnClickListener {
            checkValidation()
            titel = titelInput.text.toString()
            short = shortInput.text.toString()
            long = longInput.text.toString()
            println(titel)

            view?.let { it1 -> MainActivity().hideSoftKeyboard(it1) }
            if (checkValidation()){
                createIdea()
                closeFragment()
            }
        }
    }


    private fun checkValidation(): Boolean {
        return when {
            titelInput.text.toString().trim().isEmpty() -> {
                titelInput.requestFocus()
                titelInput.error = "Titel kan niet leeg zijn!"
                false
            }
            shortInput.text.toString().trim().isEmpty() -> {
                shortInput.requestFocus()
                shortInput.error = "Korte beschrijving kan niet leeg zijn!"
                false
            }
            else -> true
        }
    }
    /**
     * LocalDateTime.now() is enkel vanaf API26, maar app onderstuent vanaf 21.
     * Tijdelijk suppressen we gewoon.
     **/
    @SuppressLint("NewApi")
    private fun createIdea() {
        val idea = Idea((Random.nextInt(200,500)).toString(),titel, LocalDate.now().toString(),short,long,null,null,null,null,null,null,null,null)
        val gson = Gson()
        print(gson.toJson(idea))
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val token = sharedPref.getString("token","leeg")
        val call : Call<String> = APIUtils().getAPIService().createIdea(token,ideationIndex+1,idea)

        call.enqueue(object : retrofit2.Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                println()
                println(response.body().toString() + "IDEACREATED")


                Toast.makeText(context,"Bedankt voor het creeren van uw Idee",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                println(t.printStackTrace())
                Toast.makeText(context,"Log in om een idee te kunnen toevoegen!",Toast.LENGTH_LONG).show()

            }
        })
    }

    private fun closeFragment(){
        val ideasFragment = IdeasFragment()
        ideasFragment.ideationIndex = ideationIndex
        fragmentManager!!.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
            .replace(R.id.fragment_container,ideasFragment)
            .commit()
    }
}