package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var progressColor: Int = Color.BLUE
    private var progressBackgroundColor: Int = Color.GRAY

    private val valueAnimator = ValueAnimator.ofFloat()

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Clicked -> buttonState = ButtonState.Loading
            ButtonState.Completed -> downloadProgress = 0f
            ButtonState.Loading -> trackProgress(0f)
        }
        invalidate()
    }

    private var downloadProgress: Float by Delegates.observable(0f){ p, old, new ->
        invalidate()
    }

    private var basePaint: Paint = Paint()

    private lateinit var rect: Rect

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)
        progressColor = typedArray.getColor(R.styleable.LoadingButton_progressColor, Color.BLUE)
        progressBackgroundColor =
            typedArray.getColor(R.styleable.LoadingButton_progressBackgroundColor, Color.GRAY)
        typedArray.recycle()

        isClickable = true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        basePaint.color = progressBackgroundColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), basePaint)

        basePaint.color = progressColor
        canvas.drawRect(0f, 0f, width * downloadProgress, height.toFloat(), basePaint)

        when (buttonState) {
            ButtonState.Completed -> canvas.drawText("Download", 0f, 0f, basePaint)
            ButtonState.Loading -> canvas.drawText("Loading", 0f, 0f, basePaint)
        }
    }

    fun trackProgress(progress: Float){
        downloadProgress = progress
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect = Rect(0, 0, w, h)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}