package com.bgRemover.backgroundremover.Background


import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bgRemover.backgroundremover.Background.RecyclerData.ColorAdapter
import com.bgRemover.backgroundremover.Background.RecyclerData.DataClass
import com.bgRemover.backgroundremover.Background.RecyclerData.RecyclerAdapter
import com.bgRemover.backgroundremover.Background.pixabay.pixabayAdapter
import com.bgRemover.backgroundremover.Dialog.moreBackgroundDialog
import com.bgRemover.backgroundremover.MVVM.Hit
import com.bgRemover.backgroundremover.MVVMpixabay.DataViewModelPixabay
import com.bgRemover.backgroundremover.MVVMpixabay.MainRepositoryPixabay
import com.bgRemover.backgroundremover.MVVMpixabay.MyViewModelFactoryPixabay
import com.bgRemover.backgroundremover.MyUtils.MyUtils
import com.bgRemover.backgroundremover.R
import com.bgRemover.backgroundremover.RemovedResultActivity
import com.bgRemover.backgroundremover.databinding.ActivityBackGroundsBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class BackGroundsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBackGroundsBinding
    private lateinit var imgList: ArrayList<DataClass>
    var blurBitmap: Bitmap? = null
    private lateinit var viewModel: DataViewModelPixabay

    var blurRadius: Int = 20
    private lateinit var mColorAdapter: ColorAdapter
    private var rotationAngle = 0

    var adapter: RecyclerAdapter? = null
    var urlRemovedImage: String? = null
    private lateinit var imgListpixabay: ArrayList<Hit>

    var madapter: pixabayAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackGroundsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactoryPixabay(MainRepositoryPixabay())
        )[DataViewModelPixabay::class.java]

//        binding.imgbgremove.setImageBitmap(RemovedResultActivity.removedImgBitmap)
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactoryPixabay(MainRepositoryPixabay())
        )[DataViewModelPixabay::class.java]
        viewModel.getData("Nature")
        viewModel.mLiveData.observe(this) { mList ->
            Log.d("TAG", "onCreate: list ${mList.hits}")
            imgListpixabay = mList.hits as ArrayList<Hit>
            // setpixabayAdapter()
        }
        urlRemovedImage = intent.getStringExtra("urlRemovedImage")
        Glide.with(this).load(urlRemovedImage).into(binding.imgbgremove)
        blurBitmap = BitmapFactory.decodeResource(resources, R.drawable.img22)

        binding.imgRotate.setOnClickListener {
            rotationAngle += 45
            if (rotationAngle > 180) {
                rotationAngle = -135
            }
            binding.imgbgremove.rotation = rotationAngle.toFloat()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val prog: Int = progress / 4
                blurRadius = if (prog == 0) {
                    1
                } else {
                    prog
                }
                createBlurImage(blurRadius)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        setColorAdapter()
        setRecyclerAdapter()
        clicklistener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3 && resultCode == RESULT_OK) {

            val selectedImageUri: Uri? = data?.data
            blurBitmap = BitmapFactory.decodeStream(
                this.contentResolver.openInputStream(selectedImageUri!!),
                null,
                null
            )

            binding.imgBlurbackgrounds.setImageBitmap(blurBitmap)
            binding.imgBlurbackgrounds.setColorFilter(Color.TRANSPARENT)
            createBlurImage(blurRadius)

        }
    }

    private fun imagefromgallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(pickPhoto, 3)
        } catch (e: ActivityNotFoundException) {

        }
    }

    private fun clicklistener() {
        binding.btnSave.setOnClickListener {

            val view = findViewById<ConstraintLayout>(R.id.constraint_mergeimage)
            loadBitmapFromView(view)
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadBitmapFromView(v: View) {
        val b = Bitmap.createBitmap(
            v.width,
            v.height,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        saveImage(b, "img")

    }

    private fun createBlurImage(blurRadius: Int) {
        val mBlurRadius = blurRadius
        CoroutineScope(Dispatchers.Main).launch {
            val blurredBitmap =
                MyBlurBuilder.applyBlur(this@BackGroundsActivity, blurBitmap, mBlurRadius.toFloat())
            if (blurredBitmap != null) {
                binding.imgBlurbackgrounds.setImageBitmap(blurredBitmap)
            }
        }
    }


    private fun setRecyclerAdapter() {
//        imgList = ArrayList()

        val stickerPathList = arrayOf(
            "gallery",
            "https://cdn.pixabay.com/photo/2023/11/08/23/23/common-kingfisher-8376008_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/11/05/15/24/autumn-8367628_1280.jpg",
            "https://cdn.pixabay.com/photo/2016/09/10/10/11/tree-1658813_1280.png",
            "https://cdn.pixabay.com/photo/2023/11/08/23/23/common-kingfisher-8376008_1280.jpg",
            "https://cdn.pixabay.com/photo/2020/02/06/01/52/frame-4822807_1280.png",
            "https://cdn.pixabay.com/photo/2023/11/08/23/23/common-kingfisher-8376008_1280.jpg",
            "https://cdn.pixabay.com/photo/2016/09/10/10/11/tree-1658813_1280.png",
            "https://cdn.pixabay.com/photo/2016/09/10/10/11/tree-1658813_1280.png",
            "https://cdn.pixabay.com/photo/2016/09/10/10/11/tree-1658813_1280.png",
            "https://cdn.pixabay.com/photo/2023/11/08/23/23/common-kingfisher-8376008_1280.jpg",
            "https://cdn.pixabay.com/photo/2020/02/06/01/52/frame-4822807_1280.png",
            "https://cdn.pixabay.com/photo/2023/11/08/23/23/common-kingfisher-8376008_1280.jpg",
            "moreBackground"
        )


//string url
        /* imgList.add(DataClass("https://www.google.com/imgres?imgurl=https%3A%2F%2Fcdn.pixabay.com%2Fphoto%2F2018%2F04%2F05%2F14%2F09%2Fsunflowers-3292932__340.jpg&imgrefurl=https%3A%2F%2Fpixabay.com%2Fimages%2Fsearch%2Fbackground%2F&tbnid=1JhUQY6qFWUaFM&vet=12ahUKEwiW_O6JvOT8AhUXTKQEHdLSAQ0QMygLegUIARD3AQ..i&docid=LAIZRd0gOwrSbM&w=544&h=340&q=pixabay%20backgeounds&ved=2ahUKEwiW_O6JvOT8AhUXTKQEHdLSAQ0QMygLegUIARD3AQ"))
         imgList.add(DataClass("https://drive.google.com/uc?export=download&id=1N6wvMGOEsdjAXmN8mz5Wrmvm_2KWc7r-"))
         imgList.add(DataClass("https://drive.google.com/uc?export=download&id=1S4ftiketEJMo7yGzFfY5sHNk-1QtDSPw"))
         imgList.add(DataClass("https://drive.google.com/uc?export=download&id=1N6wvMGOEsdjAXmN8mz5Wrmvm_2KWc7r-"))

         //drawable
 */
//        imgList.add(DataClass(R.drawable.img44))
//        imgList.add(DataClass(R.drawable.img55))
//        imgList.add(DataClass(R.drawable.img66))
//        imgList.add(DataClass(R.drawable.img77))

        binding.bgimgRecycler.layoutManager =
            LinearLayoutManager(this.applicationContext, RecyclerView.HORIZONTAL, false)
        adapter =
            RecyclerAdapter(this, stickerPathList, object : RecyclerAdapter.onItemClickListener {
                override fun onitemclick(url: String) {
                    if (url == "gallery") {
                        imagefromgallery()
                    } else if (url == "moreBackground") {
                        Toast.makeText(this@BackGroundsActivity, "more", Toast.LENGTH_SHORT).show()
                        val morebgDialog = moreBackgroundDialog(
                            this@BackGroundsActivity,
                            this@BackGroundsActivity,
                            this@BackGroundsActivity,
                            object : pixabayAdapter.onItemClickListener {
                                override fun onitemclick(model: Hit) {
                                    Log.d("TAG", "onitemclick: ${model}")
                                    val bitmap = getBitmapFromURL(model.largeImageURL)
                                    blurBitmap = bitmap
                                    binding.imgBlurbackgrounds.setImageBitmap(bitmap)

                                    createBlurImage(blurRadius)
                                }
                            })
                        morebgDialog.show()
                    } else {

                        CoroutineScope(Dispatchers.Main).launch {
                            binding.imgBlurbackgrounds.setColorFilter(Color.TRANSPARENT)
                            // val url = URL(imgList[position].ImageUrl)
                            val bitmap = getBitmapFromURL(url)
//                            Glide.with(this@BackGroundsActivity).load(url).into(binding.imgBlurbackgrounds)
//                     blurBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                            blurBitmap = bitmap
//                            binding.imgBlurbackgrounds.setImageBitmap(bitmap)

//                    blurBitmap = BitmapFactory.decodeResource(resources, i)
//                            createBlurImageGlide(blurRadius)
                            createBlurImage(blurRadius)
//                            Log.d("TAG", "onitemclick:1111111 $bitmap")
                            Log.d("TAG", "onitemclick:urll $url")
                            Log.d("TAG", "onitemclick: 2222222 $blurBitmap")
                        }
                    }
                }
            })
        binding.bgimgRecycler.adapter = adapter
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            Log.d("TAG", "getBitmapFromURL: e $e")
            Log.d("TAG", "getBitmapFromURL: messa ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun setColorAdapter() {
        mColorAdapter =
            ColorAdapter(MyUtils.colorList(this), object : ColorAdapter.onItemClickListener {
                override fun oncoloritemclick(model: Int) {
                    binding.imgBlurbackgrounds.setColorFilter(model)
                }
            })
        binding.bgimgColorrecycler.apply {
            layoutManager =
                LinearLayoutManager(this@BackGroundsActivity, RecyclerView.HORIZONTAL, false)
            adapter = mColorAdapter
        }
    }

    private fun saveImage(finalBitmap: Bitmap, image_name: String) {

        val fos: OutputStream
        try {
            val resolver = contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$image_name.png")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = resolver.openOutputStream(imageUri!!)!!
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            Toast.makeText(this, "Image save successfully", Toast.LENGTH_SHORT).show()
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            Log.d("error", e.message!!)
        }
    }

}