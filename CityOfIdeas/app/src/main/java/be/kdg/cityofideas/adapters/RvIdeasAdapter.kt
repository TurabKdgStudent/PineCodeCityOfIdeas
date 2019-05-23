package be.kdg.cityofideas.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.kdg.cityofideas.R
import be.kdg.cityofideas.fragments.ideasFragments.IdeasFragment
import be.kdg.cityofideas.rest.IMAGE_URL
import be.kdg.cityofideas.rest.PicassoTrustAll

class RvIdeasAdapter(
    context: Context?,
    private val ideaId : String,
    private val listener: OnIdeaSelectedListener,
    private val ideationIndex: Int,
    private val ideas: ArrayList<be.kdg.cityofideas.rest.data.Idea>)
    : RecyclerView.Adapter<RvIdeasAdapter.MyViewHolder>() {

    private val cPicasso : PicassoTrustAll = PicassoTrustAll()
    private val context : Context = context!!


    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val logo: ImageView = view.findViewById(R.id.idea_logo)
        val titel: TextView = view.findViewById(R.id.IdeaTitelText)
        val numberLikes: TextView = view.findViewById(R.id.numberLikes)
        val numberComments: TextView = view.findViewById(R.id.numberComments)
        val explanation : TextView = view.findViewById(R.id.explanationShort)
        val cardview: androidx.cardview.widget.CardView = view.findViewById(R.id.cardViewIdeas)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvIdeasAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_ideas_card_item, parent, false)

        return MyViewHolder(view)
    }




    override fun onBindViewHolder(holder: RvIdeasAdapter.MyViewHolder, position: Int) {
        var idea = ideas[position]
        holder.titel.text = idea.title
        holder.numberComments.text = idea.numberOfComments
        holder.numberLikes.text = idea.numberOfLikes
        holder.explanation.text = idea.explanationShort
        if (idea.photo!!.isEmpty()){
            holder.logo.setImageResource(R.mipmap.pinecodelogo2)
        }else{
            cPicasso.getInstance(context)!!.load("$IMAGE_URL${idea.photo}").fit().into(holder.logo)
        }
        cPicasso.getInstance(context)!!.load("$IMAGE_URL${idea.photo}").fit().into(holder.logo)
        holder.cardview.setOnClickListener {
            listener.onIdeaSelected(idea.id!!.toInt())
        }

    }
    override fun getItemCount(): Int = ideas.size

    interface OnIdeaSelectedListener {
        fun onIdeaSelected(ideaIndex: Int)
    }


}