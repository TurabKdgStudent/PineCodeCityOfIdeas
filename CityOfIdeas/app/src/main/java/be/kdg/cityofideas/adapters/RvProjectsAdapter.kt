package be.kdg.cityofideas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.IMAGE_URL
import be.kdg.cityofideas.rest.PicassoTrustAll
import com.bumptech.glide.Glide


class RvProjectsAdapter(
    context: Context?,
    private val projects: ArrayList<be.kdg.cityofideas.rest.data.Project>,
    private val listener: OnProjectSelectedListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<RvProjectsAdapter.MyViewHolder>() {


    private val context : Context = context!!
    private val cPicasso : PicassoTrustAll = PicassoTrustAll()
    private val circularProgressDrawable = CircularProgressDrawable(context!!)




    class MyViewHolder(view : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val logo :ImageView =view.findViewById(R.id.card_image)
        val titel : TextView = view.findViewById(R.id.card_titel)
        val status : TextView = view.findViewById(R.id.card_status_input)
        val cardview : androidx.cardview.widget.CardView = view.findViewById(R.id.cardViewProjects)


    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RvProjectsAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_projects_card_item,parent,false)

        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: RvProjectsAdapter.MyViewHolder, position: Int) {
        val project = projects[position]
        /*cPicasso.getInstance(context)!!.load("$IMAGE_URL${project.picture}").fit()
            .into(holder.logo)*/
        circularProgressDrawable.setStyle(CircularProgressDrawable.LARGE)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(context).load("$IMAGE_URL${project.picture}").centerCrop().placeholder(circularProgressDrawable).error(R.mipmap.i2).centerCrop().into(holder.logo)
        holder.titel.text = project.title
        holder.status.text = project.status
        holder.cardview.setOnClickListener {
            listener.onProjectSelected(project.id!!.toInt())
        }

    }

    override fun getItemCount(): Int = projects.size

    interface OnProjectSelectedListener{
        fun onProjectSelected(projectIndex : Int)
    }
}