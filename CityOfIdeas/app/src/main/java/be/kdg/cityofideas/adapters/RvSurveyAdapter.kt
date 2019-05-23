package be.kdg.cityofideas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.kdg.cityofideas.R

class RvSurveyAdapter(val context : Context?,
                      private val surveys: ArrayList<be.kdg.cityofideas.rest.data.Survey>,
                      private val listener: OnSurveySelectedListener,
                      private val projectIndex : Int)
    : RecyclerView.Adapter<RvSurveyAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val titel: TextView = view.findViewById(R.id.survey_title)
        val questions: TextView = view.findViewById(R.id.aantalQuestionsText)
        val surveyPhase : TextView = view.findViewById(R.id.surveyPhaseText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RvSurveyAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_surveys_rv_item, parent, false)

        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: RvSurveyAdapter.MyViewHolder, position: Int) {

        val survey = surveys[position]
        holder.titel.text = survey.title
        holder.questions.text = survey.numberOfQuestions
        holder.surveyPhase.text = survey.phase
        holder.itemView.setOnClickListener {
            listener.onSurveySelected(survey.id!!.toInt(),projectIndex)
        }
    }
    override fun getItemCount(): Int = surveys.size

    interface OnSurveySelectedListener {
        fun onSurveySelected(surveyIndex: Int,projectIndex: Int)
    }
}