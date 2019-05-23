package be.kdg.cityofideas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import be.kdg.cityofideas.R
import be.kdg.cityofideas.rest.IMAGE_URL
import be.kdg.cityofideas.rest.PicassoTrustAll

class RvPlatformsAdapter(context: Context?,
                         private val platforms: ArrayList<be.kdg.cityofideas.rest.data.Platform>,
                         private val listener: RvPlatformsAdapter.OnPlatformSelectedListener)
: androidx.recyclerview.widget.RecyclerView.Adapter<RvPlatformsAdapter.MyViewHolder>() {


    class MyViewHolder(view : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val logo : ImageView =view.findViewById(R.id.platformLogo)
        val titel : TextView = view.findViewById(R.id.platformtitle)
        val explanationShort : TextView = view.findViewById(R.id.platformExpl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvPlatformsAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_plarform_item,parent,false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = platforms.size


    override fun onBindViewHolder(holder: RvPlatformsAdapter.MyViewHolder, position: Int) {
        val platform = platforms[position]
        holder.titel.text = platform.name
        holder.explanationShort.text = platform.explanationShort
        holder.itemView.setOnClickListener {
            listener.onPlatformSelected(platform.id!!.toInt())
        }
    }

    interface OnPlatformSelectedListener{
        fun onPlatformSelected(platformIndex : Int)
    }
}