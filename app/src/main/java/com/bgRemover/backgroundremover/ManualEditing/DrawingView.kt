package com.bgRemover.backgroundremover.ManualEditing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.bgRemover.backgroundremover.ManualEditing.ManualEditingActivity.Companion.offset
import com.bgRemover.backgroundremover.R
import com.bgRemover.backgroundremover.RemovedResultActivity

class DrawingView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mDrawPath: Path? = null
    private var mBackgroundPaint: Paint? = null
    private var mDrawPaint: Paint? = null
    private var mDrawCanvas: Canvas? = null
    private var mCanvasBitmap: Bitmap? = null
    var touchX = 5f
    var touchY = 5f
    private val mPaths = ArrayList<Path?>()
    private val mPaints = ArrayList<Paint?>()
    private val mUndonePaths = ArrayList<Path?>()
    private val mUndonePaints = ArrayList<Paint?>()

    // Set default values
    private var mBackgroundColor = -0x1
    private var mPaintColor = -0x9a0000
    private var mCursorBitmap: Bitmap? = null
    private var mCursorCanvas: Canvas? = null
    private var mSourceCanvas: Canvas? = null

    private var zoomScale = 1.0f


    enum class DrawViewAction {
        EDITMODE, ZOOMMODE
    }

    private fun init() {
        mDrawPath = Path()
        mCursorCanvas = Canvas()
        mSourceCanvas = Canvas()
        mDrawPaint = Paint()
        mBackgroundPaint = Paint()
        val rawBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.circle_cursor)
        mCursorBitmap =
            Bitmap.createBitmap(rawBitmap.width, rawBitmap.height, Bitmap.Config.ARGB_8888)
        mCursorCanvas!!.setBitmap(mCursorBitmap)
        mCursorCanvas!!.drawBitmap(rawBitmap, 0f, 0f, null)
        initPaint()
    }

    private fun initPaint() {
        mDrawPaint = Paint()
        mDrawPaint!!.color = mPaintColor
        mDrawPaint!!.alpha = 0
        mDrawPaint!!.isAntiAlias = true
        mDrawPaint!!.strokeWidth = ManualEditingActivity.Strokewidth.toFloat()
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mDrawPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        setBackImage()
    }


    private fun setBackImage() {

        if (RemovedResultActivity.removedImgBitmap!!.width >= 1080 &&
            RemovedResultActivity.removedImgBitmap!!.height >= 2000)
        {
            Log.d("TAG", "setBackImage: greater")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 0.2).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 0.2).toInt()), true)

            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)
        }else if (RemovedResultActivity.removedImgBitmap!!.width >= 1000 &&
            RemovedResultActivity.removedImgBitmap!!.height >=  1120)
        {
            Log.d("TAG", "setBackImage: WLess Hgreater")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 0.6).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 0.6).toInt()), true)

            /*       mSourceBitmap!!.density = DisplayMetrics.DENSITY_280*/
            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)
        }else if (RemovedResultActivity.removedImgBitmap!!.width <= 1080 &&
            RemovedResultActivity.removedImgBitmap!!.height >= 1200)
        {
            Log.d("TAG", "setBackImage: WLess Hgreater1")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 0.6).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 0.6).toInt()), true)

            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)
        } else if (RemovedResultActivity.removedImgBitmap!!.width <= 300 &&
            RemovedResultActivity.removedImgBitmap!!.height <= 250
        ) {
            Log.d("TAG", "setBackImage:  Less2")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 1.8).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 1.8).toInt()), true)

            /*mSourceBitmap!!.density = DisplayMetrics.DENSITY_XXHIGH*/
            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)

        }

        else if (RemovedResultActivity.removedImgBitmap!!.width <= 460 &&
            RemovedResultActivity.removedImgBitmap!!.height <= 614
        ) {
            Log.d("TAG", "setBackImage:  Less1")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 3.2).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 3.2).toInt()), true)

            /* mSourceBitmap!!.density = DisplayMetrics.DENSITY_XXHIGH*/
            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)

        }

        else {
            Log.d("TAG", "setBackImage: less")
            val rawBitmap = RemovedResultActivity.removedImgBitmap
            mSourceBitmap = Bitmap.createBitmap(rawBitmap!!.width, rawBitmap.height, Bitmap.Config.ARGB_8888)

            /*         mSourceBitmap!!.density = DisplayMetrics.DENSITY_XHIGH*/
            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(rawBitmap, -50f, 0f, null)
        }

        mDrawPaint!!.alpha = 0
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mDrawPaint!!.strokeWidth =  ManualEditingActivity.Strokewidth.toFloat()
        mDrawPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    }

    private fun drawBackground(canvas: Canvas) {
        mBackgroundPaint!!.color = mBackgroundColor
        mBackgroundPaint!!.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, this.width.toFloat(), this.height.toFloat(), mBackgroundPaint!!)
    }

    private fun drawPaths(canvas: Canvas) {
        var i = 0
        for (p in mPaths) {
//            canvas.drawPath(p, mPaints.get(i));
            mSourceCanvas!!.drawPath(p!!, mPaints[i]!!)
            i++
            invalidate()
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaledWidth = mSourceBitmap!!.width * zoomScale
        val scaledHeight = mSourceBitmap!!.height * zoomScale

        val centerX = (width - scaledWidth) / 2f
        val centerY = (height - scaledHeight) / 2f

        canvas.drawBitmap(mSourceBitmap!!, centerX+50, centerY-20, null)

//        canvas.drawBitmap(mSourceBitmap!!, 50f, 0f, null)
        mDrawPaint!!.color = mPaintColor
        mDrawPaint!!.alpha = 0
        mDrawPaint!!.isAntiAlias = true

        canvas.drawCircle(touchX+50, touchY  - offset, 25f, mDrawPaint!!)
        mSourceCanvas!!.drawPath(mDrawPath!!, mDrawPaint!!)
        canvas.drawBitmap(mCursorBitmap!!, touchX- 40 , touchY- 70 , null)
        mSourceCanvas!!.save()
        drawPaths(canvas)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mDrawCanvas = Canvas(mCanvasBitmap!!)
//        mSourceCanvas = Canvas(mCanvasBitmap!!)  n
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (currentAction != DrawViewAction.ZOOMMODE) {
            touchX = event.x
            touchY = event.y

            when (event.action) {
                MotionEvent.ACTION_MOVE -> mDrawPath!!.lineTo(touchX,  touchY - offset)
                MotionEvent.ACTION_DOWN -> mDrawPath!!.moveTo(touchX ,  touchY - offset)
                MotionEvent.ACTION_UP -> { mDrawPath!!.lineTo(touchX,  touchY  - offset)
                    mPaths.add(mDrawPath)
                    mPaints.add(mDrawPaint)
                    mDrawPath = Path()
                    initPaint()
                }
                else -> return false
            }
        }

        invalidate()
        return true
    }

    fun clearCanvas() {
        mPaths.clear()
        mPaints.clear()
        mUndonePaths.clear()
        mUndonePaints.clear()
        mDrawCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
        setBackImage()
        invalidate()
    }

    fun setPaintColor(color: Int) {
        mPaintColor = color
        //        mDrawPaint.setColor(mPaintColor);
    }

    fun setPaintStrokeWidth(strokeWidth: Int) {
        ManualEditingActivity.Strokewidth = strokeWidth
        mDrawPaint!!.strokeWidth =  ManualEditingActivity.Strokewidth.toFloat()
    }

    override fun setBackgroundColor(color: Int) {
        mBackgroundColor = color
        mBackgroundPaint!!.color = mBackgroundColor
        invalidate()
    }

    //        drawBackground(mSourceCanvas);
    val bitmap: Bitmap?
        get() {
//        drawBackground(mSourceCanvas);
            drawPaths(mSourceCanvas!!)
            return mCanvasBitmap
        }

    fun undo() {
        if (mPaths.size > 0) {
            mUndonePaths.add(mPaths.removeAt(mPaths.size - 1))
            mUndonePaints.add(mPaints.removeAt(mPaints.size - 1))
            invalidate()
        }
        //        draw(mSourceCanvas);
        setBackImage()
        invalidate()
    }

    fun redo() {
        if (mUndonePaths.size > 0) {
            mPaths.add(mUndonePaths.removeAt(mUndonePaths.size - 1))
            mPaints.add(mUndonePaints.removeAt(mUndonePaints.size - 1))
            invalidate()
        }
    }

    companion object {
        //backimg
        var mSourceBitmap: Bitmap? = null
        var currentAction: DrawViewAction? = null
        fun setAction(newAction: DrawViewAction?) {
            currentAction = newAction
        }
    }

    init {
        init()
    }

}
/*
class DrawingView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mDrawPath: Path? = null
    private var mBackgroundPaint: Paint? = null
    private var mDrawPaint: Paint? = null
    private var mDrawCanvas: Canvas? = null
    private var mCanvasBitmap: Bitmap? = null
    var touchX = 500f
    var touchY = 500f
    private val mPaths = ArrayList<Path?>()
    private val mPaints = ArrayList<Paint?>()
    private val mUndonePaths = ArrayList<Path?>()
    private val mUndonePaints = ArrayList<Paint?>()

    // Set default values
    private var mBackgroundColor = -0x1
    private var mPaintColor = -0x9a0000
    private var mCursorBitmap: Bitmap? = null
    private var mCursorCanvas: Canvas? = null
    private var mSourceCanvas: Canvas? = null

    enum class DrawViewAction {
        EDITMODE, ZOOMMODE
    }

    private var zoomScale = 1.0f
    private var maxZoomScale = 3.0f
    private var minZoomScale = 0.5f

    private fun init() {
        mDrawPath = Path()
        mCursorCanvas = Canvas()
        mSourceCanvas = Canvas()
        mDrawPaint = Paint()
        mBackgroundPaint = Paint()
        val rawBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.circle_cursor)
        mCursorBitmap =
            Bitmap.createBitmap(rawBitmap.width, rawBitmap.height, Bitmap.Config.ARGB_8888)
        mCursorCanvas!!.setBitmap(mCursorBitmap)
        mCursorCanvas!!.drawBitmap(rawBitmap, 0f, 0f, null)
        initPaint()
    }

    private fun initPaint() {
        mDrawPaint = Paint()
        mDrawPaint!!.color = mPaintColor
        mDrawPaint!!.alpha = 0
        mDrawPaint!!.isAntiAlias = true
        mDrawPaint!!.strokeWidth = ManualEditingActivity.Strokewidth.toFloat()
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mDrawPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        setBackImage()
    }


    private fun setBackImage() {

        if (RemovedResultActivity.removedImgBitmap!!.width >= 1080 &&
            RemovedResultActivity.removedImgBitmap!!.height >= 2000)
        {
            Log.d("TAG", "setBackImage: greater")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 0.2).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 0.2).toInt()), true)

            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)
        }else if (RemovedResultActivity.removedImgBitmap!!.width >= 1000 &&
            RemovedResultActivity.removedImgBitmap!!.height >=  1120)
        {
            Log.d("TAG", "setBackImage: WLess Hgreater")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 0.6).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 0.6).toInt()), true)

            mSourceBitmap!!.density = DisplayMetrics.DENSITY_280
            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)
        }else if (RemovedResultActivity.removedImgBitmap!!.width <= 1080 &&
            RemovedResultActivity.removedImgBitmap!!.height >= 1200)
        {
            Log.d("TAG", "setBackImage: WLess Hgreater1")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 0.6).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 0.6).toInt()), true)

            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)
        } else if (RemovedResultActivity.removedImgBitmap!!.width <= 300 &&
            RemovedResultActivity.removedImgBitmap!!.height <= 250
        ) {
            Log.d("TAG", "setBackImage:  Less2")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 1.8).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 1.8).toInt()), true)

            mSourceBitmap!!.density = DisplayMetrics.DENSITY_XXHIGH
            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)

        }

        else if (RemovedResultActivity.removedImgBitmap!!.width <= 460 &&
            RemovedResultActivity.removedImgBitmap!!.height <= 614
        ) {
            Log.d("TAG", "setBackImage:  Less1")
            mSourceBitmap = Bitmap.createScaledBitmap(
                RemovedResultActivity.removedImgBitmap!!,
                ((RemovedResultActivity.removedImgBitmap!!.width * 3.2).toInt()),
                ((RemovedResultActivity.removedImgBitmap!!.height * 3.2).toInt()), true)

            mSourceBitmap!!.density = DisplayMetrics.DENSITY_XXHIGH
            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(mSourceBitmap!!, 0f, 0f, null)

        }

        else {
            Log.d("TAG", "setBackImage: less")
            val rawBitmap = RemovedResultActivity.removedImgBitmap
            mSourceBitmap = Bitmap.createBitmap(rawBitmap!!.width, rawBitmap.height, Bitmap.Config.ARGB_8888)

            mSourceBitmap!!.density = DisplayMetrics.DENSITY_XHIGH
            mSourceCanvas!!.setBitmap(mSourceBitmap)
            mSourceCanvas!!.drawBitmap(rawBitmap, -50f, 0f, null)
        }

        mDrawPaint!!.alpha = 0
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mDrawPaint!!.strokeWidth =  ManualEditingActivity.Strokewidth.toFloat()
        mDrawPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        */
/*   val displayMetrics = resources.displayMetrics
           val screenWidth = displayMetrics.widthPixels
           val screenHeight = displayMetrics.heightPixels

           val scalingFactor = calculateScalingFactor(screenWidth, screenHeight)

           mSourceBitmap = Bitmap.createScaledBitmap(
               RemovedResultActivity.removedImgBitmap!!,
               (RemovedResultActivity.removedImgBitmap!!.width * scalingFactor).toInt(),
               (RemovedResultActivity.removedImgBitmap!!.height * scalingFactor).toInt(),
               true
           )*//*


    }
    private fun calculateScalingFactor(screenWidth: Int, screenHeight: Int): Float {
        // Adjust these constants as needed for your application
        val maxScreenWidth = 1080
        val maxScreenHeight = 1920

        val scalingFactor = when {
            screenWidth >= maxScreenWidth && screenHeight >= maxScreenHeight -> 0.2f
            screenWidth >= 1000 && screenHeight >= 1120 -> 0.6f
            screenWidth <= 1080 && screenHeight >= 1200 -> 0.6f
            screenWidth <= 300 && screenHeight <= 250 -> 1.8f
            screenWidth <= 460 && screenHeight <= 614 -> 3.2f
            else -> 1.0f // Default scaling factor
        }

        return scalingFactor
    }
    private fun drawBackground(canvas: Canvas) {
        mBackgroundPaint!!.color = mBackgroundColor
        mBackgroundPaint!!.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, this.width.toFloat(), this.height.toFloat(), mBackgroundPaint!!)
    }

    private fun drawPaths(canvas: Canvas) {
        var i = 0
        for (p in mPaths) {
//            canvas.drawPath(p, mPaints.get(i));
            mSourceCanvas!!.drawPath(p!!, mPaints[i]!!)
            i++
            invalidate()
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

             val scaledWidth = mSourceBitmap!!.width * zoomScale
             val scaledHeight = mSourceBitmap!!.height * zoomScale

             val centerX = (width - scaledWidth) / 2f
             val centerY = (height - scaledHeight) / 2f

//             canvas.drawBitmap(mSourceBitmap!!, null, RectF(centerX+50, centerY, centerX + scaledWidth, centerY + scaledHeight), null)
        canvas.drawBitmap(mSourceBitmap!!, centerX, centerY, null)
//        canvas.drawBitmap(mSourceBitmap!!, 50f, 0f, null)


        mDrawPaint!!.color = mPaintColor
        mDrawPaint!!.alpha = 0
        mDrawPaint!!.isAntiAlias = true

        canvas.drawCircle(touchX+50, touchY  - offset+180, 25f, mDrawPaint!!)
        mSourceCanvas!!.drawPath(mDrawPath!!, mDrawPaint!!)
        canvas.drawBitmap(mCursorBitmap!!, touchX , touchY , null)
        mSourceCanvas!!.save()
        drawPaths(canvas)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mDrawCanvas = Canvas(mCanvasBitmap!!)
//        mSourceCanvas = Canvas(mCanvasBitmap!!)  n


    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (currentAction != DrawViewAction.ZOOMMODE) {
            touchX = event.x
            touchY = event.y

            when (event.action) {
                MotionEvent.ACTION_MOVE -> mDrawPath!!.lineTo(touchX- 50,  touchY - offset)
                MotionEvent.ACTION_DOWN -> mDrawPath!!.moveTo(touchX -50,  touchY - offset)
                MotionEvent.ACTION_UP -> { mDrawPath!!.lineTo(touchX- 50,  touchY- offset  )
                    mPaths.add(mDrawPath)
                    mPaints.add(mDrawPaint)
                    mDrawPath = Path()
                    initPaint()
                }
                else -> return false
            }
        }

        invalidate()
        return true
    }

    fun clearCanvas() {
        mPaths.clear()
        mPaints.clear()
        mUndonePaths.clear()
        mUndonePaints.clear()
        mDrawCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
        setBackImage()
        invalidate()
    }

    fun setPaintColor(color: Int) {
        mPaintColor = color
        //        mDrawPaint.setColor(mPaintColor);
    }

    fun setPaintStrokeWidth(strokeWidth: Int) {
        ManualEditingActivity.Strokewidth = strokeWidth
        mDrawPaint!!.strokeWidth =  ManualEditingActivity.Strokewidth.toFloat()
    }

    override fun setBackgroundColor(color: Int) {
        mBackgroundColor = color
        mBackgroundPaint!!.color = mBackgroundColor
        invalidate()
    }

    //        drawBackground(mSourceCanvas);
    val bitmap: Bitmap?
        get() {
//        drawBackground(mSourceCanvas);
            drawPaths(mSourceCanvas!!)
            return mCanvasBitmap
        }

    fun undo() {
        if (mPaths.size > 0) {
            mUndonePaths.add(mPaths.removeAt(mPaths.size - 1))
            mUndonePaints.add(mPaints.removeAt(mPaints.size - 1))
            invalidate()
        }
        //        draw(mSourceCanvas);
        setBackImage()
        invalidate()
    }

    fun redo() {
        if (mUndonePaths.size > 0) {
            mPaths.add(mUndonePaths.removeAt(mUndonePaths.size - 1))
            mPaints.add(mUndonePaints.removeAt(mUndonePaints.size - 1))
            invalidate()
        }
    }

    companion object {
        //backimg
        var mSourceBitmap: Bitmap? = null
        var currentAction: DrawViewAction? = null
        fun setAction(newAction: DrawViewAction?) {
            currentAction = newAction
        }
    }

    init {
        init()
    }

}
*/
