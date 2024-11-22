package com.bgRemover.backgroundremover

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bgRemover.backgroundremover.Background.BackGroundsActivity
import com.bgRemover.backgroundremover.ManualEditing.DrawingView
import com.bgRemover.backgroundremover.ManualEditing.ManualEditingActivity
import com.bgRemover.backgroundremover.MyUtils.MyUtils
import com.bgRemover.backgroundremover.Retrofit.DataViewModel
import com.bgRemover.backgroundremover.Retrofit.MainRepository
import com.bgRemover.backgroundremover.Retrofit.MyViewModelFactory
import com.bgRemover.backgroundremover.databinding.ActivityRemovedResultBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.facebook.shimmer.ShimmerFrameLayout
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class RemovedResultActivity : AppCompatActivity() {
    companion object {
        var urlRemovedImage: String? = null
        var origionalpath: String? = null
        var removedImgBitmap: Bitmap? = null
        var mfinalBitmap: Bitmap? = null

    }

    lateinit var binding: ActivityRemovedResultBinding
    private lateinit var viewModel: DataViewModel
    private var mShimmerViewContainer: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemovedResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container)

        origionalpath = intent.getStringExtra("ImageURL").toString()
        Log.d("TAG", "onCreate: origionalpath $origionalpath ")
        urlRemovedImage = intent.getStringExtra("urlRemovedImage")

        if (!MyUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "You need an active internet", Toast.LENGTH_LONG).show()
        } else {
            mShimmerViewContainer!!.startShimmer()
            mShimmerViewContainer!!.visibility = View.VISIBLE
            viewModel = ViewModelProvider(
                this,
                MyViewModelFactory(MainRepository())
            )[DataViewModel::class.java]
            viewModel.getData(File(origionalpath))
            viewModel.mLiveData.observe(this, Observer { mList ->
//                  urlRemovedImage = mList.url

//                  setImagetoURL(urlRemovedImage)
            })
        }

        clickListener()
        Handler().postDelayed({
            if (urlRemovedImage!!.isNotEmpty())
                setImagetoURL(urlRemovedImage)

        }, 2000)
    }


    private fun setImagetoURL(urlRemovedImage: String?) {
        Glide.with(this).asBitmap().load(urlRemovedImage)
            /*  .diskCacheStrategy(DiskCacheStrategy.NONE)
              .skipMemoryCache(true)*/
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>,
                    isFirstResource: Boolean
                ): Boolean {
                    mShimmerViewContainer!!.stopShimmer()
                    mShimmerViewContainer!!.visibility = View.INVISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    mShimmerViewContainer!!.stopShimmer()
                    mShimmerViewContainer!!.visibility = View.INVISIBLE

                    return false
                }
            })
            .into(binding.imgRemoved)
        binding.imgRemoved.isDrawingCacheEnabled = true
    }

    private fun clickListener() {

        binding.btnBackground.setOnClickListener {
            if (removedImgBitmap == null) {
                Toast.makeText(this, "Wait for loading complete", Toast.LENGTH_SHORT).show()
            } else {

                val intent = Intent(this, BackGroundsActivity::class.java)
                intent.putExtra("urlRemovedImage", removedImgBitmap)
                startActivity(intent)
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.txtRemoveimg.setOnClickListener {
            binding.txtRemoveimg.setTextColor(resources.getColor(R.color.appBlue))
            binding.removeimgView.visibility = View.VISIBLE
            binding.txtOrigional.setTextColor(resources.getColor(R.color.black))
            binding.OrigionalView.visibility = View.GONE
            binding.constraintRemoveimage.background = resources.getDrawable(R.drawable.bg)

            Glide.with(this).load(removedImgBitmap).into(binding.imgRemoved)
        }
        binding.txtOrigional.setOnClickListener {
            removedImgBitmap = binding.imgRemoved.drawingCache
            if (removedImgBitmap == null) {
                Toast.makeText(this, "Wait for loading complete", Toast.LENGTH_SHORT).show()
            } else {
                binding.txtRemoveimg.setTextColor(resources.getColor(R.color.black))
                binding.removeimgView.visibility = View.INVISIBLE
                binding.centerLt.background = null
                binding.constraintRemoveimage.background = null
                binding.txtOrigional.setTextColor(resources.getColor(R.color.appBlue))
                binding.OrigionalView.visibility = View.VISIBLE
                Glide.with(this).load(MainActivity.origionalFile).into(binding.imgRemoved)
            }
        }
        binding.btnSave.setOnClickListener {
//            removedImgBitmap = getBitmapFromURL(urlRemovedImage)

            removedImgBitmap = binding.imgRemoved.drawingCache
            if (removedImgBitmap != null) {
                saveMediaToStorage(removedImgBitmap!!)
            } else {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnManualEditing.setOnClickListener {
            removedImgBitmap = binding.imgRemoved.drawingCache
            Log.d("TAG", "onResourceReady:  wd ${removedImgBitmap!!.width}")
            Log.d("TAG", "onResourceReady:  He ${removedImgBitmap!!.height}")
            if (removedImgBitmap == null) {
                Toast.makeText(this, "Wait for loading complete", Toast.LENGTH_SHORT).show()
            } else {

                val intent = Intent(this, ManualEditingActivity::class.java)
                startActivityForResult(intent, 2)
            }
        }


    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.png"
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                }
                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
                Toast.makeText(this, "Successfully Downloaded", Toast.LENGTH_SHORT).show()
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
            Toast.makeText(this, "Successfully Downloaded", Toast.LENGTH_SHORT).show()
        }
        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, it)
        }
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    override fun onPause() {
        Log.d("TAG", "onPause: ")
        mShimmerViewContainer!!.stopShimmer()
        mShimmerViewContainer!!.visibility = View.INVISIBLE
        super.onPause()
    }

    override fun onResume() {
        Log.e("TAG", "onResume: ")
        super.onResume()
    }

    override fun onBackPressed() {
        removedImgBitmap = null
        binding.imgRemoved.setImageBitmap(null)
        binding.imgRemoved.setImageDrawable(null)
        binding.imgRemoved.setImageResource(0)
        removedImgBitmap = null
        Glide.with(this).clear(binding.imgRemoved)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                val result = data!!.getStringExtra("image")
                Log.d("zzzzzzzz", "onActivityResult: $result")
                binding.imgRemoved.setImageBitmap(null)
                binding.imgRemoved.setImageDrawable(null)
                binding.imgRemoved.invalidate()
                mfinalBitmap = DrawingView.mSourceBitmap
                removedImgBitmap = mfinalBitmap
                binding.imgRemoved.setImageBitmap(mfinalBitmap)
            }
        }
    }
}