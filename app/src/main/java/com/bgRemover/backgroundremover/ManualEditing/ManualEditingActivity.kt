package com.bgRemover.backgroundremover.ManualEditing


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alexvasilkov.gestures.views.interfaces.GestureView
import com.bgRemover.backgroundremover.R
import com.bgRemover.backgroundremover.RemovedResultActivity
import com.bgRemover.backgroundremover.databinding.ActivityManualEditingBinding
import java.io.OutputStream


class ManualEditingActivity : AppCompatActivity() {
    companion object {
        var offset = 0
        var Strokewidth = 20
    }

    private lateinit var binding: ActivityManualEditingBinding

    lateinit var mDrawingView: DrawingView
    var gestureView: GestureView? = null
    val MAX_ZOOM = 4f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManualEditingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDrawingView = findViewById(R.id.main_drawing_view)
        gestureView = findViewById(R.id.gestureView)

        binding.offsetSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                binding.constraintOffsetseekbar.background =
                    resources.getDrawable(R.drawable.border_shape)
                binding.constraintWidth.background = null
                offset = i
                mDrawingView.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.widthSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                binding.constraintOffsetseekbar.background = null
//                resources.getDrawable(R.drawable.border_shape)
                binding.constraintWidth.background = resources.getDrawable(R.drawable.border_shape)
                Strokewidth = i
                mDrawingView.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        deactivateGestureView()
        clickListener()
    }

    private fun clickListener() {

        binding.editingmode.isActivated = false

        binding.btnSave.setOnClickListener {

            intent.putExtra("image","img")
            setResult(Activity.RESULT_OK, intent)
            finish()

     /*       RemovedResultActivity.isApicall=true*/
//            saveImage(DrawingView.mSourceBitmap!!, "iamge")
        }
        binding.btnBack.setOnClickListener {
           onBackPressed()
        }

        /* binding.btnErase.setOnClickListener {
             binding.btnErase.setTextColor(resources.getColor(R.color.white))
             binding.CardErase.setCardBackgroundColor(resources.getColor(R.color.appBlue))
             binding.btnEdit.setTextColor(resources.getColor(R.color.black))
             binding.CardEdit.setCardBackgroundColor(resources.getColor(R.color.white))
             binding.btnBackground.setTextColor(resources.getColor(R.color.black))
             binding.CardBackground.setCardBackgroundColor(resources.getColor(R.color.white))
         }
         binding.btnEdit.setOnClickListener {
             binding.btnEdit.setTextColor(resources.getColor(R.color.white))
             binding.CardEdit.setCardBackgroundColor(resources.getColor(R.color.appBlue))
             binding.btnErase.setTextColor(resources.getColor(R.color.black))
             binding.CardErase.setCardBackgroundColor(resources.getColor(R.color.white))
             binding.btnBackground.setTextColor(resources.getColor(R.color.black))
             binding.CardBackground.setCardBackgroundColor(resources.getColor(R.color.white))
         }
         binding.btnBackground.setOnClickListener {
             binding.btnBackground.setTextColor(resources.getColor(R.color.white))
             binding.CardBackground.setCardBackgroundColor(resources.getColor(R.color.appBlue))
             binding.btnErase.setTextColor(resources.getColor(R.color.black))
             binding.CardErase.setCardBackgroundColor(resources.getColor(R.color.white))
             binding.btnEdit.setTextColor(resources.getColor(R.color.black))
             binding.CardEdit.setCardBackgroundColor(resources.getColor(R.color.white))
         }*/

        //Editindmode tools clicklistener

        binding.editingmode.setOnClickListener {
            binding.CardEditingmode.setCardBackgroundColor(resources.getColor(R.color.appBlue))
            binding.CardZoom.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.CardUndo.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.CardRedo.setCardBackgroundColor(resources.getColor(R.color.white))

            binding.editingmode.setColorFilter(ContextCompat.getColor(this, R.color.white))
            binding.btnZoom.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.imgUndo.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.imgRedo.setColorFilter(ContextCompat.getColor(this, R.color.black))

            DrawingView.setAction(DrawingView.DrawViewAction.EDITMODE)
            binding.editingmode.isActivated = true
            binding.editingmode.isActivated = false
            deactivateGestureView()

        }
        binding.CardZoom.setOnClickListener {
            binding.CardZoom.setCardBackgroundColor(resources.getColor(R.color.appBlue))
            binding.CardEditingmode.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.CardUndo.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.CardRedo.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.editingmode.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.btnZoom.setColorFilter(ContextCompat.getColor(this, R.color.white))
            binding.imgUndo.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.imgRedo.setColorFilter(ContextCompat.getColor(this, R.color.black))
            DrawingView.setAction(DrawingView.DrawViewAction.ZOOMMODE)
            activateGestureView()
        }
        binding.imgUndo.setOnClickListener {
            mDrawingView.undo()
            mDrawingView.invalidate()
            binding.CardUndo.setCardBackgroundColor(resources.getColor(R.color.appBlue))
            binding.CardZoom.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.CardEditingmode.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.CardRedo.setCardBackgroundColor(resources.getColor(R.color.white))

            binding.editingmode.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.btnZoom.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.imgUndo.setColorFilter(ContextCompat.getColor(this, R.color.white))
            binding.imgRedo.setColorFilter(ContextCompat.getColor(this, R.color.black))


        }
        binding.imgRedo.setOnClickListener {
            binding.CardRedo.setCardBackgroundColor(resources.getColor(R.color.appBlue))
            binding.CardZoom.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.CardUndo.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.CardEditingmode.setCardBackgroundColor(resources.getColor(R.color.white))

            binding.editingmode.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.btnZoom.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.imgUndo.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.imgRedo.setColorFilter(ContextCompat.getColor(this, R.color.white))

            mDrawingView.redo()
            mDrawingView.invalidate()

        }

    }


    private fun activateGestureView() {
        gestureView!!.controller.settings
            .setMaxZoom(MAX_ZOOM)
            .setDoubleTapZoom(-1f) // Falls back to max zoom level
            .setPanEnabled(true)
            .setZoomEnabled(true)
            .setDoubleTapEnabled(true)
            .setOverscrollDistance(0f, 0f).overzoomFactor = 2f
    }

    private fun deactivateGestureView() {
        gestureView!!.controller.settings
            .setPanEnabled(false)
            .setZoomEnabled(false).isDoubleTapEnabled = false
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
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            Log.d("error", e.message!!)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mDrawingView.clearCanvas()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDrawingView.clearCanvas()
    }

}
