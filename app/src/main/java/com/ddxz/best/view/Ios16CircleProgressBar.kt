package com.ddxz.best.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.IntRange
import com.ddxz.best.R

class CircleProgressBar : View {
    // 画圆环的画笔
    private var mRingPaint: Paint? = null

    // 圆环颜色
    private var mRingColor = 0
    private var mRingBgColor = 0

    // 圆环半径
    private var mRingRadius = 0f

    // 圆环宽度
    private var mStrokeWidth = 0f

    // 透明圆宽度，必须比圆环宽度大
    private var mDotTransparentWidth = 0f
    private var mXCenter = 0
    private var mYCenter = 0

    // 当前进度
    private var mProgress = 0
    private var mClockwise = false
    // 左边显示一个点
    private var isShowDot = false
    // 环形为虚线
    private var isDash = false
    private val mRingOval = RectF()
    private val mDotTransparent = RectF()
    private val mode1 = PorterDuffXfermode(PorterDuff.Mode.SRC)
    lateinit var dashPathEffect: DashPathEffect

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttrs(context, attrs)
        init()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typeArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, 0, 0)
            mDotTransparentWidth = typeArray.getDimension(R.styleable.CircleProgressBar_dotTransparentWidth, 10f)
            mStrokeWidth = typeArray.getDimension(R.styleable.CircleProgressBar_strokeWidth, 10f)
            mRingColor = typeArray.getColor(R.styleable.CircleProgressBar_ringColor, -0x1)
            mRingBgColor = typeArray.getColor(R.styleable.CircleProgressBar_ringBgColor, 0x33FFFFFF)
            mProgress = typeArray.getInteger(R.styleable.CircleProgressBar_progress, 0)
            mClockwise = typeArray.getBoolean(R.styleable.CircleProgressBar_clockwise, true)
            isShowDot = typeArray.getBoolean(R.styleable.CircleProgressBar_isShowDot, false)
            isDash = typeArray.getBoolean(R.styleable.CircleProgressBar_isDash, false)
            typeArray.recycle()
        }
    }

    private fun init() {
//        setLayerType(View.LAYER_TYPE_SOFTWARE, mRingPaint);
        mRingPaint = Paint()
        mRingPaint!!.isAntiAlias = true
        mRingPaint!!.style = Paint.Style.STROKE
        mRingPaint!!.strokeWidth = mStrokeWidth
        if (isDash) {
            mRingPaint!!.strokeCap = Paint.Cap.BUTT
        } else {
            mRingPaint!!.strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRingRadius = Math.min(width, height) / 2f - mStrokeWidth / 2f
        mXCenter = w / 2
        mYCenter = h / 2
        mRingOval.left = mXCenter - mRingRadius
        mRingOval.top = mYCenter - mRingRadius
        mRingOval.right = mXCenter + mRingRadius
        mRingOval.bottom = mYCenter + mRingRadius
        mDotTransparent.left = 0f
        mDotTransparent.top = mYCenter - mStrokeWidth
        mDotTransparent.right = mStrokeWidth * 2
        mDotTransparent.bottom = mYCenter + mStrokeWidth
        val a: Int
        val b: Int
        if (mXCenter > mYCenter) {
            a = mXCenter
            b = mYCenter
        } else {
            a = mYCenter
            b = mXCenter
        }
        val l = 2 * Math.PI * b + (4 * (a - b))
        val dash2 = l / 12 / 10
        val dash1 = l / 12 - dash2
        Log.d("riki", "dash1 $dash1 dash2 $dash2")
        dashPathEffect = DashPathEffect(floatArrayOf(dash1.toFloat(), dash2.toFloat()), 0f)
    }

    override fun onDraw(canvas: Canvas) {
        var saved = -1
        if (isShowDot) {
            saved = canvas.saveLayer(null, null)
        }
        mRingPaint!!.color = mRingBgColor
        mRingPaint!!.style = Paint.Style.STROKE
        if (isDash) {
            mRingPaint!!.pathEffect = dashPathEffect
        }
        canvas.drawArc(mRingOval, -90f, 360f, false, mRingPaint!!)
        if (mProgress > 0) {
            mRingPaint!!.color = mRingColor
            if (mClockwise) {
                canvas.drawArc(mRingOval, -90f, mProgress.toFloat() / MAX_PROGRESS * 360, false, mRingPaint!!)
            }
            else {
                canvas.drawArc(mRingOval, -90f, -(mProgress.toFloat() / MAX_PROGRESS * 360), false, mRingPaint!!)
            }
        }
        if (isDash) {
            mRingPaint!!.pathEffect = null
        }
        if (isShowDot) {
            mRingPaint!!.color = Color.TRANSPARENT
            mRingPaint!!.style = Paint.Style.FILL
            mRingPaint!!.xfermode = mode1
            canvas.drawCircle(mStrokeWidth / 2, mYCenter.toFloat(), mDotTransparentWidth, mRingPaint!!)
            canvas.restoreToCount(saved)
            mRingPaint!!.xfermode = null
            mRingPaint!!.color = mRingColor
            canvas.drawCircle(mStrokeWidth / 2, mYCenter.toFloat(), mStrokeWidth, mRingPaint!!)
        }

//        if (isShowArc) {
////            saved = canvas.saveLayer(null, null)
//            mRingPaint!!.color = Color.TRANSPARENT
//            mRingPaint!!.style = Paint.Style.FILL
//            mRingPaint!!.xfermode = mode1
//            for (i in 0..12) {
//                canvas.drawArc(0f, 0f, width.toFloat(), height.toFloat(), -80f + (i * (360 / 12)), 5f, true, mRingPaint!!)
//            }
//            mRingPaint!!.xfermode = null
////            canvas.restoreToCount(saved)
//        }
    }

    fun setProgress(@IntRange(from = 0, to = 100) progress: Int) {
        mProgress = progress
        postInvalidate()
    }

    fun setRingColor(ringColor: Int) {
        mRingColor = ringColor
        postInvalidate()
    }

    companion object {
        const val MAX_PROGRESS = 100
    }
}