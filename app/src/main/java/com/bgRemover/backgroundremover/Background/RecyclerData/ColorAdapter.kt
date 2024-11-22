package com.bgRemover.backgroundremover.Background.RecyclerData

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bgRemover.backgroundremover.R

class ColorAdapter(val mColorlist:List<Int>,
                   private var mlistener: onItemClickListener):RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_imageviewcolor, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mColorlist[position]
        holder.backgroundcolorimg.setBackgroundColor(model)

        holder.backgroundcolorimg.setOnClickListener(View.OnClickListener {
            mlistener.oncoloritemclick(model)
        })
    }

    override fun getItemCount(): Int {
      return mColorlist.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var backgroundcolorimg: View

        init {
            backgroundcolorimg = view.findViewById<View>(R.id.imgbackgroundcolor)
        }
    }

    interface onItemClickListener {
        fun oncoloritemclick(model: Int)
    }
}