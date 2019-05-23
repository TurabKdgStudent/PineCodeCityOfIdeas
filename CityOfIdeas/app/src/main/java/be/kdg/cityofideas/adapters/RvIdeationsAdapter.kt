package be.kdg.cityofideas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.IMAGE_URL
import be.kdg.cityofideas.rest.PicassoTrustAll

class RvIdeationsAdapter(context: Context?,
                         private val listener: OnIdeationSelectedListener,
                         private val projectIndex : Int,
                         private val ideations: ArrayList<be.kdg.cityofideas.rest.data.Ideation>)
    : RecyclerView.Adapter<RvIdeationsAdapter.MyViewHolder>() {

    private val context : Context = context!!
    private val cPicasso : PicassoTrustAll = PicassoTrustAll()


    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val logo: ImageView = view.findViewById(R.id.ideation_logo)
        val titel: TextView = view.findViewById(R.id.ideation_title)
        val explanation: TextView = view.findViewById(R.id.explanation)
        val numberLikes: TextView = view.findViewById(R.id.numberLikes)
        val numberIdeas: TextView = view.findViewById(R.id.numberIdeas)
        val cardview: androidx.cardview.widget.CardView = view.findViewById(R.id.cardViewIdeations)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_ideations_card_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RvIdeationsAdapter.MyViewHolder, position: Int) {
        val ideation = ideations[position]
        cPicasso.getInstance(context)!!.load("$IMAGE_URL${ideation.picture}").fit().into(holder.logo)
        holder.titel.text = ideation.question
        holder.explanation.text = ideation.about
        holder.numberLikes.text = ideation.numberOfLikes
        holder.numberIdeas.text = ideation.numberOfIdeas
        holder.cardview.setOnClickListener {
            listener.onIdeationSelected(position)
        }

    }
    override fun getItemCount(): Int = ideations.size


    interface OnIdeationSelectedListener {
        fun onIdeationSelected(ideationIndex: Int) }

}






