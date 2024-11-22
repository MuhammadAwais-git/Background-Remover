package com.bgRemover.backgroundremover.Background.pixabay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bgRemover.backgroundremover.MVVM.Hit

import com.bgRemover.backgroundremover.R
import com.bumptech.glide.Glide

class pixabayAdapter(private val context: Context, private val picList: ArrayList<Hit>,
                     private var mlistener: onItemClickListener) :
    RecyclerView.Adapter<pixabayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_imageview, viewGroup, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = picList[position]

            Glide.with(context)
                .load(model.largeImageURL)
                .into(holder.backgroundimg)

        /*    holder.backgroundimg.setImageResource(model.ImageUrl)*/

        holder.backgroundimg.setOnClickListener(View.OnClickListener {
            mlistener.onitemclick(model)
        })
    }

    override fun getItemCount(): Int {
        return picList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var backgroundimg: ImageView

        init {
            backgroundimg = view.findViewById<View>(R.id.imgbackground) as ImageView
        }
    }

    interface onItemClickListener {
        fun onitemclick(model: Hit)
    }
}