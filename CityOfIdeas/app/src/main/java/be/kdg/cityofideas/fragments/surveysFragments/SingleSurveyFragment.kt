package be.kdg.cityofideas.fragments.surveysFragments


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import be.kdg.cityofideas.MainActivity
import be.kdg.cityofideas.R
import be.kdg.cityofideas.fragments.ProjectPageFragment
import be.kdg.cityofideas.fragments.projectPageFragments.SurveysFragment
import be.kdg.cityofideas.rest.APIUtils
import be.kdg.cityofideas.rest.BASE_URL
import be.kdg.cityofideas.rest.data.Answer
import be.kdg.cityofideas.rest.data.Question
import be.kdg.cityofideas.rest.data.QuestionChoice
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SingleSurveyFragment : Fragment() {

    var surveyindex: Int = 0
    private lateinit var questions: ArrayList<Question>
    private lateinit var choices: ArrayList<QuestionChoice>
    private lateinit var requestQueue: RequestQueue
    private var allViewInstance: MutableList<View> = ArrayList()
    private var numberOfQuestions: Int = 0
    private lateinit var questionArray: JSONArray
    private lateinit var choiceArray: JSONArray
    private lateinit var viewProductLayout: LinearLayout
    internal var jsonObject = JSONObject()
    private var optionsObj: JsonObject? = null
    private var postArray: JsonArray? = null
    private var dropdownObj: JsonObject? = null
    private var spinnerObj: JsonObject? = null
    private var textObj: JsonObject? = null
    private var radioObj: JsonObject? = null

    private lateinit var buttonSurvey : Button
    private var title : String = ""
    private lateinit var confirmation : TextView
    private lateinit var surveyTitel : TextView

    private lateinit var singleChoices: ArrayList<QuestionChoice>
    private lateinit var dropChoices: ArrayList<QuestionChoice>
    private lateinit var multiChoices: ArrayList<QuestionChoice>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_survey, container, false)
        viewProductLayout = view.findViewById<View>(R.id.customOptionLL) as LinearLayout

        requestQueue = Volley.newRequestQueue(context)
        questions = ArrayList()
        choices = ArrayList()
        singleChoices = ArrayList()
        dropChoices = ArrayList()
        multiChoices = ArrayList()
        questionArray = JSONArray()
        choiceArray = JSONArray()
        initialiseViews(view)
        queue()

        surveyTitel.text = title

        return view
    }

    private fun addEventHandlers(){
        buttonSurvey.setOnClickListener {
            view?.let { it1 ->
                getDataFromDynamicViews(it1)
                MainActivity().hideSoftKeyboard(it1)
            }
            sendSurvey()
            fragmentManager!!.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                .replace(R.id.fragment_container,ProjectPageFragment()).commit()
        }
    }

    private fun initialiseViews(v: View?){
        buttonSurvey = v!!.findViewById(R.id.SendSurvey)
        confirmation = v.findViewById(R.id.confirmationSurvey)
        confirmation.visibility = TextView.INVISIBLE
        surveyTitel = v.findViewById(R.id.surveyTitel)
    }


    private fun queue() {
        val index = surveyindex+1
        val query = "query{survey(id : $index){title questions{id type text choices{id text}}}}"
        val jsonObject = JSONObject()
        jsonObject.put("query", query)
        val request = JsonObjectRequest(Request.Method.POST, BASE_URL, jsonObject,
            Response.Listener { response ->
                // Process the json
                try {
                    val obj = response.getJSONObject("data")
                    println(obj)
                    val survey = obj.getJSONObject("survey")
                    // println(project)
                    val title = survey.getString("title")
                    surveyTitel.text = title
                    questionArray = survey.getJSONArray("questions")
                    numberOfQuestions = questionArray.length()
                    for (i in 0 until questionArray.length()) {
                        val question = questionArray.getJSONObject(i)

                        val id = question.getString("id")
                        val type = question.getString("type")
                        val text = question.getString("text")



                        choiceArray = question.getJSONArray("choices")
                        for (j in 0 until choiceArray.length()) {
                            val choice = choiceArray.getJSONObject(j)

                            val choiceId = choice.getString("id")
                            val choiceText = choice.getString("text")
                            choices.add(QuestionChoice(choiceId, choiceText,type))


                        }

                        questions.add(Question(id, text, type, choices))

                    }
                    showSurvey()
                } catch (e: Exception) {
                    println("FAILSINGLESURVEYFRAGMENT")
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                // Error in request
                print("FAILVOLLEY")
            })
        requestQueue.add(request)
        addEventHandlers()



    }

    @SuppressLint("NewApi")
    private fun showSurvey() {
        try {
            for (i in 0 until questions.size) {
                val singleQuestion = questions[i]
                val questionTitle = TextView(context)
                questionTitle.textSize = 18f
                questionTitle.setPadding(0, 15, 0, 15)
                questionTitle.text = questions[i].text
                viewProductLayout.addView(questionTitle)

                /****Spinner****/

                val spinnerOptions = ArrayList<String>()
                if (singleQuestion.type == "DROPDOWN") {
                    dropChoices = choices.filter { choice -> choice.questionType == "DROPDOWN"} as ArrayList<QuestionChoice>
                    println(dropChoices)
                    for (j in 0 until dropChoices.size) {
                        val choiceText = dropChoices[j].text
                        spinnerOptions.add(choiceText!!)
                    }
                    var spinnerArrayAdapter: ArrayAdapter<String>?
                    spinnerArrayAdapter = ArrayAdapter(context, R.layout.spinner_row, spinnerOptions)
                    val spinner1 = Spinner(this.context)
                    allViewInstance.add(spinner1)
                    spinner1.adapter = spinnerArrayAdapter
                    spinner1.setSelection(0, true)
                    spinner1.setPopupBackgroundResource( R.color.colorPrimaryDark)

                    spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                        override fun onItemSelected(
                            parentView: AdapterView<*>,
                            selectedItemView: View,
                            position: Int,
                            id: Long
                        ) {
                            selectedItemView.setBackgroundResource(R.color.colorPrimaryDark)
                        }
                    }
                    viewProductLayout.addView(spinner1)
                }

                if (singleQuestion.type == "SINGLE_CHOICE") {
                    singleChoices = choices.filter { choice -> choice.questionType == "SINGLE_CHOICE"} as ArrayList<QuestionChoice>
                    println(singleChoices)
                    for (j in 0 until singleChoices.size) {
                        val choiceText = singleChoices[j].text
                        spinnerOptions.add(choiceText!!)
                    }
                    var spinnerArrayAdapter: ArrayAdapter<String>?
                    spinnerArrayAdapter = ArrayAdapter(context, R.layout.spinner_row, spinnerOptions)
                    val spinner2 = Spinner(this.context)
                    allViewInstance.add(spinner2)
                    spinner2.adapter = spinnerArrayAdapter
                    spinner2.setSelection(0, true)
                    spinner2.setPopupBackgroundResource( R.color.colorPrimaryDark)


                    spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                        override fun onItemSelected(
                            parentView: AdapterView<*>,
                            selectedItemView: View,
                            position: Int,
                            id: Long
                        ) {
                            selectedItemView.setBackgroundResource(R.color.colorPrimaryDark)
                        }
                    }
                    viewProductLayout.addView(spinner2)
                }

                /****Radio****/
                if (singleQuestion.type == "MULTIPLE_CHOICE"){
                    multiChoices = choices.filter { choice -> choice.questionType == "MULTIPLE_CHOICE" } as ArrayList<QuestionChoice>
                    val params = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    params.topMargin = 3
                    params.bottomMargin = 3

                    val rg = RadioGroup(context) //create the RadioGroup
                    allViewInstance.add(rg)
                    for (j in 0 until multiChoices.size) {
                        val cb = RadioButton(context)
                        rg.addView(cb, params)
                        if (j == 0){
                            cb.isChecked = true
                        }
                        cb.layoutParams = params
                        cb.tag = multiChoices[j].id
                        cb.setBackgroundResource(R.color.colorPrimaryDark)
                        val optionName = multiChoices[j].text
                        cb.text = optionName
                    }
                    viewProductLayout.addView(rg, params)
                }

                /****TextField****/

                if (singleQuestion.type == "OPEN") {
                    val til = TextInputLayout(context)
                    til.hint = getString(R.string.hint)
                    val et = TextInputEditText(context)
                    til.addView(et)
                    allViewInstance.add(et)
                    viewProductLayout.addView(til)
                }
                if (singleQuestion.type == "MAILADRES") {
                    val til = TextInputLayout(context)
                    til.hint = getString(R.string.hint)
                    val et = TextInputEditText(context)
                    til.addView(et)
                    allViewInstance.add(et)
                    viewProductLayout.addView(til)
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun getDataFromDynamicViews(v: View){
        try {
            optionsObj = JsonObject()
            dropdownObj = JsonObject()
            spinnerObj = JsonObject()
            textObj = JsonObject()
            radioObj = JsonObject()
            postArray = JsonArray()
            for (i in 0 until questionArray.length()) {
                val singleQuestion = questions[i]
                if (singleQuestion.type == "DROPDOWN" ) {
                    val spinner : Spinner = allViewInstance[i] as Spinner
                    val choiceText = dropChoices[spinner.selectedItemPosition].id
                    Log.d("choice_text", choiceText+ "")
                    dropdownObj!!.addProperty("questionId",singleQuestion.id)
                    dropdownObj!!.addProperty("text",choiceText)
                    postArray!!.add(dropdownObj)
                }
                if (singleQuestion.type == "SINGLE_CHOICE" ) {
                    val spinner : Spinner = allViewInstance[i] as Spinner
                    val choiceText = singleChoices[spinner.selectedItemPosition].id
                    Log.d("choice_text", choiceText+ "")
                    spinnerObj!!.addProperty("questionId",singleQuestion.id)
                    spinnerObj!!.addProperty("text",choiceText)
                    postArray!!.add(spinnerObj)
                }
                if (singleQuestion.type == "MULTIPLE_CHOICE") {
                    val radioGroup = allViewInstance[i] as RadioGroup
                    val selectedCheckBox = v.findViewById<View>(radioGroup.checkedRadioButtonId) as RadioButton
                    radioObj!!.addProperty("questionId",singleQuestion.id)
                    radioObj!!.addProperty("text",selectedCheckBox.tag.toString())
                    postArray!!.add(radioObj)
                }
                if (singleQuestion.type == "OPEN" || singleQuestion.type == "MAILADRES") {
                    val textView = allViewInstance[i] as TextView
                    Log.d("choice_text", textView.text.toString())
                    textObj!!.addProperty("questionId",singleQuestion.id)
                    textObj!!.addProperty("text",textView.text.toString())
                    postArray!!.add(textObj)
                }
                }
            Log.d("optionsObj", optionsObj!!.toString() + "")
            } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun sendSurvey() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val token = sharedPref.getString("token","leeg")
        println(postArray)
        val call : Call<List<Answer>> = APIUtils().getAPIService().sendSurvey(token,surveyindex,postArray)

        call.enqueue(object : retrofit2.Callback<List<Answer>>{
            override fun onResponse(call: Call<List<Answer>>, response: retrofit2.Response<List<Answer>>) {
                println(response.body().toString())
                Toast.makeText(context,"Bedankt voor het invullen van de vragenlijst!",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<List<Answer>>, t: Throwable) {
                println(t.printStackTrace())
                Toast.makeText(context,"Log in om een vragenlijst te kunnen invullen!",Toast.LENGTH_LONG).show()
            }
        })
    }

}
