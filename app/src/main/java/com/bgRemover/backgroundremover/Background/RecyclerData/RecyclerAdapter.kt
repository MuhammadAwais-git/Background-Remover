package com.bgRemover.backgroundremover.Background.RecyclerData

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bgRemover.backgroundremover.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener


class RecyclerAdapter(private val context: Context, private val stickerPathList: Array<String>,
                      private var mlistener: onItemClickListener) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_imageview, viewGroup, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = stickerPathList[position]
        Log.d("TAG", "onBindViewHolder:model $model")
        Log.d("TAG", "onBindViewHolder:stickerPathList ${stickerPathList}")
/*

        Glide.with(context).load(model.ImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {

                    Log.d("TAG", "onLoadFailed: ")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    Log.d("TAG", "onResourceReady: ")

                    return false
                }
            })
            .into(holder.backgroundimg)
*/

        if (model=="gallery"){
            Glide.with(context)
                .load(R.drawable.gallery_icon)
                .into(holder.backgroundimg)
        }else if (model=="moreBackground"){
            Glide.with(context)
                .load(R.drawable.more_icon)
                .into(holder.backgroundimg)
        }else{
            Glide.with(context)
                .load(model)
                .into(holder.backgroundimg)
        }



        /*    holder.backgroundimg.setImageResource(model.ImageUrl)*/

        holder.backgroundimg.setOnClickListener(View.OnClickListener {
            mlistener.onitemclick(model)
        })


       // Glide.with(context).load(RemovedResultActivity.origionalpath).into(holder.backgroundimg)

    }

    override fun getItemCount(): Int {
        return stickerPathList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var backgroundimg: ImageView

        init {
            backgroundimg = view.findViewById<View>(R.id.imgbackground) as ImageView
        }
    }

    interface onItemClickListener {
        fun onitemclick(url: String)
    }
}