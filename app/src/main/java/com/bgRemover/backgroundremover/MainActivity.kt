package com.bgRemover.backgroundremover

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.Handler
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.provider.OpenableColumns
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bgRemover.backgroundremover.MyUtils.MyUtils
import com.bgRemover.backgroundremover.Retrofit.DataViewModel
import com.bgRemover.backgroundremover.Retrofit.MainRepository
import com.bgRemover.backgroundremover.Retrofit.MyViewModelFactory
import com.bgRemover.backgroundremover.databinding.ActivityMainBinding
import com.facebook.shimmer.ShimmerFrameLayout
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        var urlRemovedImage: String? = null
        var link: String? = null
        var origionalFile: String = ""
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: DataViewModel
    lateinit var toggle: ActionBarDrawerToggle
    private var mShimmerViewContainer: ShimmerFrameLayout? = null
    private var mShimmerViewContainer1: ShimmerFrameLayout? = null


    //    var weburlBgRemover = "https://www.remove.bg/upload"
    var weburlBgRemover = "https://www.slazzer.com/upload"
    private var mUploadMessage: ValueCallback<Uri>? = null
    var uploadMessage: ValueCallback<Array<Uri>>? = null
    val REQUEST_SELECT_FILE = 100

    //    val REQUEST_SELECT_FILE1 = 1001
    private val FILECHOOSER_RESULTCODE = 1
    var isGalleryButtonPress: String = "Gallery"
    var filePath: String = ""
    private var linkCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressbar.visibility = View.VISIBLE
        DrawerListener()
        /*    mShimmerViewContainer = findViewById(R.id.shimmer_container)
            mShimmerViewContainer!!.startShimmer()
            mShimmerViewContainer!!.visibility = View.VISIBLE
            binding.shimmerContainer1.startShimmer()
            binding.shimmerContainer1.visibility = View.VISIBLE*/

        viewModel =
            ViewModelProvider(this, MyViewModelFactory(MainRepository()))[DataViewModel::class.java]
//        viewModel.mLiveData.observe(this, Observer { mList ->
//            Log.d("TAG", "onCreate: data ${mList.url}")
//            urlRemovedImage=mList.url
//            Log.d("TAG", "onCreate: url ${urlRemovedImage}")
//
//        })

        clicklistener()
//        loadimage()
        /*   binding.CardTestimg1.setOnClickListener {
               val fileUri = Uri.parse("android.resource://com.bgRemover.backgroundremover/" + R.drawable.testimages)
               Log.d("TAG", "onCreate: fileUri ${fileUri}")
               Log.d("TAG", "onCreate: fileUri11 ${fileUri.path}")
               val newfile = getFilePath(this, fileUri)
               Log.d("TAG", "onCreate: newfile  ${newfile}")
              *//* val intent = Intent(this, RemovedResultActivity::class.java)
            intent.putExtra("ImageURL", fileUri)
            startActivity(intent)*//*


        }*/

        val mWebSettings: WebSettings = binding.bgWebView.settings
        mWebSettings.javaScriptEnabled = true
        mWebSettings.setSupportZoom(false)
        mWebSettings.allowFileAccess = true
        mWebSettings.allowContentAccess = true

        binding.bgWebView.settings.javaScriptEnabled = true
        binding.bgWebView.isHorizontalScrollBarEnabled = false
        binding.bgWebView.settings.loadWithOverviewMode = true
        binding.bgWebView.settings.useWideViewPort = true
        binding.bgWebView.settings.allowFileAccess = true
        binding.bgWebView.settings.allowContentAccess = true
        binding.bgWebView.settings.allowFileAccessFromFileURLs = true
        binding.bgWebView.settings.allowUniversalAccessFromFileURLs = true
        binding.bgWebView.settings.domStorageEnabled = true
        binding.bgWebView.settings.setSupportZoom(true)
        binding.bgWebView.settings.builtInZoomControls = true
        binding.bgWebView.settings.displayZoomControls = true

        binding.bgWebView.webViewClient = CruiseWebViewClient()
        binding.bgWebView.loadUrl(weburlBgRemover)

        binding.bgWebView.setWebChromeClient(object : WebChromeClient() {

            override fun onProgressChanged(window: WebView?, newProgress: Int) {
                super.onCloseWindow(window)
                Log.i("setWebChromeClient", "onProgressChanged" + newProgress.toString())

                if (newProgress == 100) {
                    binding.progressbar.visibility = View.GONE

                }
            }


            override fun onShowFileChooser(
                mWebView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (uploadMessage != null) {
                    uploadMessage!!.onReceiveValue(null)
                    uploadMessage = null
                }
                uploadMessage = filePathCallback
                try {
                    if (isGalleryButtonPress == "Gallery") {

                        val intent1 = fileChooserParams.createIntent()
                        startActivityForResult(intent1, REQUEST_SELECT_FILE)
                    } else if (isGalleryButtonPress == "TestImg") {
                        /*  val intent = Intent(this@MainActivity, EmptyViewActivity::class.java)
                          startActivityForResult(intent, REQUEST_SELECT_FILE1)
                          Log.d("TAG", "clicklistener: filePath $filePath")*/
                        val intent = Intent(this@MainActivity, EmptyViewActivity::class.java)
                        intent.putExtra("filePath", filePath)
                        startActivityForResult(intent, REQUEST_SELECT_FILE)
                    } else {
                        val intent = Intent(this@MainActivity, camreaActivity::class.java)
                        startActivityForResult(intent, REQUEST_SELECT_FILE)
                    }

                    /* val intent= Intent(this@FileProcessingActivity, ConvertingActivity::class.java)
                     intent.putExtra("filePath",filePath)
                     startActivityForResult(intent, REQUEST_SELECT_FILE)*/

                } catch (e: ActivityNotFoundException) {
                    uploadMessage = null
                    Toast.makeText(
                        this@MainActivity,
                        "Cannot Open File Chooser",
                        Toast.LENGTH_LONG
                    ).show()
                    return false
                }
                return true
            }

        })


    }

    private inner class CruiseWebViewClient : WebViewClient() {
        override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
            super.onScaleChanged(view, oldScale, newScale)
            Log.i("setWebChromeClient", "onScaleChanged")
        }

        override fun shouldOverrideUrlLoading(webview: WebView?, url: String?): Boolean {
            webview!!.loadUrl(url!!)
            // binding.pb.visibility = View.VISIBLE
            Log.i("setWebChromeClient", "shouldOverrideUrlLoading")
            return true
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            Log.i("setWebChromeClient", "onReceivedError")
            Toast.makeText(
                this@MainActivity, "Internet Issue Use a Stable Internet",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onPageFinished(webview: WebView?, url: String?) {
            super.onPageFinished(webview, url)
            Log.i("setWebChromeClient", "onPageFinished")
            Log.i("setWebChromeClient", "onPageFinished url: " + url.toString())

        }

    }

    private fun getUrlFromWeb() {
//        Handler().postDelayed({
//                binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")
        Handler().postDelayed({
//                var divElement = document.querySelector('.my-3.w-100');


            val id = ".my-3.w-100"
            binding.bgWebView.evaluateJavascript(
                """
            (function() {
                 var downloadButton = document.querySelector('.downloadButton a');
                        if (downloadButton) {
                            downloadButton.click();
                            return downloadButton.getAttribute('href');
                        }
                        return null;
                    })()
            """.trimIndent()
            ) { result ->
                Log.d("setWebChromeClient", "result : $result ")
                if (result != null && result != "null") {

                    link = result.trim('"')

                    println("Extracted link: $link")
                    Log.i("setWebChromeClient", "Video URL: $link")
//                            downloadimg(link)
//                        downloadAndSaveImage(link!!,"bg_image.jpg")
                    urlRemovedImage = link
                    if (urlRemovedImage!!.isNotEmpty()) {

                        val intent = Intent(this, RemovedResultActivity::class.java)
                        intent.putExtra("urlRemovedImage", "https://www.slazzer.com$link")
                        startActivity(intent)
                        binding.progressbar.visibility = View.GONE
                    }

                } else {
                    Log.i("setWebChromeClient", "Video URL:Failed ")
                    getUrlFromWeb()
                    linkCount++
                    Log.d("setWebChromeClient", "getUrlFromWeb:linkCount  ${linkCount}")
                    if (linkCount == 6) {
                        Toast.makeText(
                            this,
                            "SomeThing went wrong!! Please try again  ",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressbar.visibility = View.GONE
                        linkCount = 0
                    }
                }
            }

        }, 15000)

//        }, 2000)
    }


    private fun downloadAndSaveImage(link: String, filename: String) {
        val request = DownloadManager.Request(Uri.parse(link))

        request.setTitle("Image Download")
        request.setDescription("Downloading an image")
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
        )
        request.setVisibleInDownloadsUi(true)
        val storageDir = getExternalFilesDir(null)
        val file = File(storageDir, filename)

        if (file.exists()) {
            file.delete()
        }

        request.setDestinationUri(Uri.fromFile(file))

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {

                    Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show()
                    context?.unregisterReceiver(this)
                }
            }
        }

        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun getImg() {

        val filename = "bg_image.jpg"
        val imageFile = File(getExternalFilesDir(null), filename)
        if (imageFile.exists()) {
            val imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
//            val imageView = findViewById<ImageView>(R.id.miImageView)
//            imageView.setImageBitmap(imageBitmap)
        } else {

            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null) return

                val selectedImageUri: Uri = intent?.data!!
//                val imageUri: String? = intent.data?.path
                if (isGalleryButtonPress == "Gallery") {
                    origionalFile = getFilePath(this, selectedImageUri)!!
                } else if (isGalleryButtonPress == "TestImg") {
                    origionalFile = filePath
                }


//                val intent = Intent(this, RemovedResultActivity::class.java)
//                intent.putExtra("ImageURL", newfile)
//                startActivity(intent)
                Log.d("TAG", "onActivityResult: Path ${selectedImageUri}")
                Log.d("TAG", "onActivityResult: imageUri ${origionalFile}")
                uploadMessage!!.onReceiveValue(
                    WebChromeClient.FileChooserParams.parseResult(
                        resultCode,
                        intent
                    )
                )
                getUrlFromWeb()
                uploadMessage = null
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            val result =
                if (intent == null || resultCode != AppCompatActivity.RESULT_OK) null else intent.data
            mUploadMessage!!.onReceiveValue(result)
            mUploadMessage = null
        } else Toast.makeText(
            this,
            "Failed to Upload Image",
            Toast.LENGTH_LONG
        ).show()
    }


    /*  @RequiresApi(Build.VERSION_CODES.N)
      override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(requestCode, resultCode, data)
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              if (requestCode == REQUEST_SELECT_FILE) {
                  if (uploadMessage == null) return
                  uploadMessage!!.onReceiveValue(
                      WebChromeClient.FileChooserParams.parseResult(
                          resultCode,
                          intent
                      )
                  )
                  uploadMessage = null
              }
          } else if (requestCode == FILECHOOSER_RESULTCODE)
          {
              if (null == mUploadMessage) return
              // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
              // Use RESULT_OK only if you're implementing WebView inside an Activity
              val result =
                  if (intent == null || resultCode != RESULT_OK) null else intent.data
              mUploadMessage!!.onReceiveValue(result)
              mUploadMessage = null
          } else Toast.makeText(
              this,
              "Failed to Upload Image",
              Toast.LENGTH_LONG
          ).show()
          if (requestCode == 1 && resultCode === RESULT_OK) {
              val imageUri: Uri? = data?.data
              val newfile = getFilePath(this, imageUri!!)
  //            val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
  //            val picturepath = BitmapFactory.decodeStream(imageStream)
  //            getalldata(File(newfile))
              val intent = Intent(this, RemovedResultActivity::class.java)
              intent.putExtra("ImageURL", newfile)
              startActivity(intent)
          } else if (requestCode == 2 && resultCode == RESULT_OK) {

  *//*
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val uri = getImageUri(this, imageBitmap)
            Log.d("TAG", "onActivityResult: origionalpath $uri")
            val newfile = uri?.let { getFilePath(this, it) }
            val intent = Intent(this, RemovedResultActivity::class.java)
            intent.putExtra("ImageURL", newfile)
            startActivity(intent)*//*


            val uri: Uri? = data?.data
            try {
                val bitmap = Images.Media.getBitmap(contentResolver, uri)
                binding.imgFrombroswer.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data
            val newfile = getFilePath(this, imageUri!!)
//            val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
//            val picturepath = BitmapFactory.decodeStream(imageStream)
//            binding.imgFrombroswer.setImageBitmap(picturepath)
//            getalldata(File(newfile))
            val intent = Intent(this, RemovedResultActivity::class.java)
            intent.putExtra("ImageURL", newfile)
            startActivity(intent)
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }

    }

*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    imagefrombrowse()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            102 -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")
//                    imagefromgallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 95, bytes)
        val path =
            Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    /* private fun getalldata(file: File) {
         viewModel.getData(file)

     }
 */
    @SuppressLint("ResourceType")
    private fun clicklistener() {

        if (!MyUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "You need an active internet", Toast.LENGTH_LONG).show()
        } else {
            binding.constraintDotedbroswer.setOnClickListener {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                        requestPermissions(permissions, 101);

                    } else {
                        imagefrombrowse()
                    }
                } else {
                    imagefrombrowse()
                }
            }

            binding.btnImgfromcam.setOnClickListener {
//            startActivity(Intent(this, camreaActivity::class.java))
//            imagefromcam()
                /* if (uploadMessage != null) {
                 uploadMessage!!.onReceiveValue(null)
                 uploadMessage = null
             }*/
//            uploadMessage = filePathCallback

                isGalleryButtonPress = "Camera"
                binding.progressbar.visibility = View.VISIBLE
                binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")
            }

            binding.btnImgfromgallery.setOnClickListener {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                        //show popup to request runtime permission
                        requestPermissions(permissions, 102)
                    } else {
                        //permission already granted
//                    imagefromgallery();
//                        binding.bgWebView.loadUrl("javascript:document.querySelector('button[type=\"button\"]').click();")
                        binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")
                        binding.progressbar.visibility = View.VISIBLE
                    }
                } else {
                    //system OS is < Marshmallow
//                imagefromgallery();
                    binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")
                    binding.progressbar.visibility = View.VISIBLE
//                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                startActivityForResult(pickPhoto, 101)
                }

//            imagefromgallery()
            }

            binding.imgTestimg1.setOnClickListener {
                isGalleryButtonPress = "TestImg"

                val file = File(this.filesDir, "my_image1.jpg")
                val inputStream: InputStream = this.resources.openRawResource(R.drawable.try_img1)
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                filePath = file.path
                Log.d("TAG", "clicklistener: filePath $filePath")

                /*     val intent = Intent(this@MainActivity, EmptyViewActivity::class.java)
                 intent.putExtra("filePath", filePath)
                 startActivityForResult(intent, REQUEST_SELECT_FILE)*/
                binding.progressbar.visibility = View.VISIBLE
                binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")

                /*   val intent = Intent(this, RemovedResultActivity::class.java)
               intent.putExtra("ImageURL", filePath)
               startActivity(intent)*/
            }
            binding.imgTestimg2.setOnClickListener {
                isGalleryButtonPress = "TestImg"
                val file = File(this.filesDir, "my_image2.jpg")
                val inputStream: InputStream = this.resources.openRawResource(R.drawable.try_img2)
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                filePath = file.path
                Log.d("TAG", "clicklistener: filePath $filePath")
                binding.progressbar.visibility = View.VISIBLE
                binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")

                /* val intent = Intent(this@MainActivity, EmptyViewActivity::class.java)
             intent.putExtra("filePath", filePath)
             startActivityForResult(intent, REQUEST_SELECT_FILE1)*/
            }
            binding.imgTestimg3.setOnClickListener {
                isGalleryButtonPress = "TestImg"
                val file = File(this.filesDir, "my_imag3.jpg")
                val inputStream: InputStream = this.resources.openRawResource(R.drawable.try_img3)
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                filePath = file.path

                binding.progressbar.visibility = View.VISIBLE
                binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")
                /*val intent = Intent(this, RemovedResultActivity::class.java)
                intent.putExtra("ImageURL", filePath)
                startActivity(intent)*/
            }
            binding.imgTestimg4.setOnClickListener {
                isGalleryButtonPress = "TestImg"
                val file = File(this.filesDir, "my_imag4.jpg")
                val inputStream: InputStream = this.resources.openRawResource(R.drawable.try_img4)
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                filePath = file.path
                Log.d("TAG", "clicklistener: filePath $filePath")

                binding.progressbar.visibility = View.VISIBLE
                binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")
            }
        }
    }

    private fun imagefrombrowse() {
        /* val intent = Intent()
         intent.type = "image/*"
         intent.action = Intent.ACTION_GET_CONTENT
         startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)*/
         */
        isGalleryButtonPress = "Gallery"
        binding.progressbar.visibility = View.VISIBLE
        binding.bgWebView.loadUrl("javascript:document.getElementsByClassName('btn user_image_upload')[0].click();")

    }


    private fun imagefromcam() {
        /*      val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
              try {
                  startActivityForResult(takePictureIntent, 2)
              } catch (e: ActivityNotFoundException) {
              }*/

        /*   val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
           var photoFile: File? = null
           try {
               photoFile = createImageFile()
           } catch (ex: IOException) {
               // Error occurred while creating the File
               Log.d("mylog", "Exception while creating file: " + ex.toString())
           }
           // Continue only if the File was successfully created
           // Continue only if the File was successfully created
           if (photoFile != null) {
               Log.d("mylog", "Photofile not null")
               val photoURI = FileProvider.getUriForFile(
                   this,
                   "com.bgRemover.backgroundremover",
                   photoFile
               )
               takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
               startActivityForResult(takePictureIntent, 2)
           }
   */

    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "PNG" + timeStamp + "_"
        val storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".png",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = image.absolutePath
        Log.d("mylog", "Path: $mCurrentPhotoPath")
        return image
    }


    private fun imagefromgallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(pickPhoto, 3)
        } catch (e: ActivityNotFoundException) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getFilePath(context: Context, contentUri: Uri): String? {
        try {
            val filePathColumn = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
            )

            val returnCursor = contentUri.let {
                context.contentResolver.query(
                    it,
                    filePathColumn,
                    null,
                    null,
                    null
                )
            }
            if (returnCursor != null) {
                returnCursor.moveToFirst()
                val nameIndex = returnCursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                val name = returnCursor.getString(nameIndex)
                val file = File(context.cacheDir, name)
                val inputStream = context.contentResolver.openInputStream(contentUri)
                val outputStream = FileOutputStream(file)
                var read: Int
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable = inputStream!!.available()
                val bufferSize = Integer.min(bytesAvailable, maxBufferSize)
                val buffers = ByteArray(bufferSize)
                while (inputStream.read(buffers).also { read = it } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                inputStream.close()
                outputStream.close()
                return file.absolutePath
            } else {
                Log.d("", "returnCursor is null")
                return null
            }
        } catch (e: Exception) {
            Log.d("", "exception caught at getFilePath(): $e")
            return null
        }
    }

    private fun DrawerListener() {
        binding.imgDrawer.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@MainActivity, drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            binding.drawer.constraintHome.setOnClickListener {
                closeDrawer()
            }
            binding.drawer.constraintPrivacypolicy.setOnClickListener {

            }
            binding.drawer.constraintMoreapps.setOnClickListener {
            }
            binding.drawer.constraintRateus.setOnClickListener {
            }
            binding.drawer.constraintHelp.setOnClickListener {
            }
            binding.drawer.constraintExit.setOnClickListener {
                finishAffinity()
            }


        }
    }

    private fun closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }

}