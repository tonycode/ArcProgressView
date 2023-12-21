package dev.tonycode.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.toRectF
import kotlin.math.abs
import kotlin.math.max


/**
 * A view that displays progress as an Arc
 *
 * Consists of:
 * - track
 * - progress that occupy none, a part of track, full track (according to 0% .. 100%)
 */
class ArcProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //region public props
    /**
     * The start angle for both track and progress.
     *
     * - uom: degrees (-360..360). Zero value corresponds to 3 o'clock.
     */
    var startAngle: Float = DEFAULT_START_ANGLE
        set(value) {
            field = normalizeAngle(value)
            invalidate()
        }

    /**
     * track end
     *
     * @throws IllegalArgumentException if value is negative
     */
    var sweepAngle: Float = DEFAULT_SWEEP_ANGLE
        set(value) {
            if (value < -ANGLE_EPS) throw IllegalArgumentException("sweepAngle can't be negative")
            field = normalizeAngle(abs(value))
            invalidate()
        }

    /**
     * uom: pixels
     */
    var trackWidth: Float = DEFAULT_TRACK_WIDTH_PX
        set(value) {
            field = value
            trackPaint.strokeWidth = value
            updateViewRect()
        }

    @ColorInt
    var trackColor: Int = DEFAULT_TRACK_COLOR
        set(value) {
            if (field == value) return
            field = value
            trackPaint.color = value
            invalidate()
        }

    /**
     * from `0f` (0%) to `1f` (100%)
     */
    @FloatRange(from = 0.0, to = 1.0)
    var progress: Float = DEFAULT_PROGRESS
        set(value) {
            field = value
            invalidate()
        }

    /**
     * uom: pixels
     */
    var progressWidth: Float = DEFAULT_PROGRESS_WIDTH_PX
        set(value) {
            field = value
            progressPaint.strokeWidth = value
            updateViewRect()
        }

    @ColorInt
    var progressColor: Int = DEFAULT_PROGRESS_COLOR
        set(value) {
            if (field == value) return
            field = value
            progressPaint.color = value
            invalidate()
        }

    /**
     * Whether the corners of track and progress lines are round or not
     */
    var roundCorners: Boolean = DEFAULT_ROUND_CORNERS
        set(value) {
            if (field == value) return
            field = value
            updateRoundCorners(value)
            invalidate()
        }
    //endregion

    private val trackPaint = Paint()
    private val progressPaint = Paint()

    private val viewRect = RectF()


    init {
        context.obtainStyledAttributes(attrs, R.styleable.ArcProgressView, defStyleAttr, 0).let { ta ->
            applyAttrs(ta)
            ta.recycle()
        }

        trackPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = trackWidth
            color = trackColor
            isAntiAlias = true
            isDither = true
        }
        progressPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = progressWidth
            color = progressColor
            isAntiAlias = true
            isDither = true
        }
        updateRoundCorners(roundCorners)
    }

    private fun applyAttrs(ta: TypedArray) {
        for (i in 0 until ta.indexCount) {
            when (val attrIdx = ta.getIndex(i)) {
                R.styleable.ArcProgressView_apv_startAngle ->
                    startAngle = ta.getFloat(attrIdx, DEFAULT_START_ANGLE)
                R.styleable.ArcProgressView_apv_sweepAngle ->
                    sweepAngle = ta.getFloat(attrIdx, DEFAULT_SWEEP_ANGLE)
                R.styleable.ArcProgressView_apv_trackWidth ->
                    trackWidth = ta.getDimension(attrIdx, DEFAULT_TRACK_WIDTH_PX)
                R.styleable.ArcProgressView_apv_trackColor ->
                    trackColor = ta.getColor(attrIdx, DEFAULT_TRACK_COLOR)
                R.styleable.ArcProgressView_apv_progress ->
                    progress = ta.getFloat(attrIdx, DEFAULT_PROGRESS)
                R.styleable.ArcProgressView_apv_progressWidth ->
                    progressWidth = ta.getDimension(attrIdx, DEFAULT_PROGRESS_WIDTH_PX)
                R.styleable.ArcProgressView_apv_progressColor ->
                    progressColor = ta.getColor(attrIdx, DEFAULT_PROGRESS_COLOR)
                R.styleable.ArcProgressView_apv_roundCorners ->
                    roundCorners = ta.getBoolean(attrIdx, DEFAULT_ROUND_CORNERS)
            }
        }
    }

    /**
     * Make given angle (in degrees) fit -360..360
     */
    private fun normalizeAngle(src: Float): Float {
        val remainder = src.mod(360f)
        return if (abs(remainder) < ANGLE_EPS && abs(src) > ANGLE_EPS) 360f
        else remainder
    }

    private fun updateRoundCorners(areRound: Boolean) {
        val cap = if (areRound) Paint.Cap.ROUND else Paint.Cap.BUTT
        trackPaint.strokeCap = cap
        progressPaint.strokeCap = cap
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateViewRect(w, h)
    }

    /**
     * Update [viewRect] by applying insets to whole view size
     *
     * These insets are required for stroke widths to make painting fit the whole view
     */
    private fun updateViewRect(w: Int, h: Int) {
        val maxStrokeWidth = max(trackWidth, progressWidth)
        val halfMaxStrokeWidth = maxStrokeWidth * 0.5f

        viewRect.set(
            Rect(0, 0, w, h)
                .toRectF()
                .apply { inset(halfMaxStrokeWidth, halfMaxStrokeWidth) }
        )

        invalidate()
    }

    private fun updateViewRect() {
        updateViewRect(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // track
        canvas.drawArc(viewRect, startAngle, sweepAngle, false, trackPaint)

        // progress
        canvas.drawArc(viewRect, startAngle, progress * sweepAngle, false, progressPaint)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState() ?: Bundle()

        val savedState = ApvSavedState(superState)
        savedState.startAngle = startAngle
        savedState.sweepAngle = sweepAngle
        savedState.trackWidth = trackWidth
        savedState.trackColor = trackColor
        savedState.progress = progress
        savedState.progressWidth = progressWidth
        savedState.progressColor = progressColor
        savedState.roundCorners = roundCorners

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is ApvSavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        startAngle = state.startAngle
        sweepAngle = state.sweepAngle
        trackWidth = state.trackWidth
        trackColor = state.trackColor
        progress = state.progress
        progressWidth = state.progressWidth
        progressColor = state.progressColor
        roundCorners = state.roundCorners

        super.onRestoreInstanceState(state.superState)
    }


    /**
     * Internal saved state
     */
    internal class ApvSavedState : BaseSavedState {
        var startAngle: Float = DEFAULT_START_ANGLE
        var sweepAngle: Float = DEFAULT_SWEEP_ANGLE
        var trackWidth: Float = DEFAULT_TRACK_WIDTH_PX
        var trackColor: Int = DEFAULT_TRACK_COLOR
        var progress: Float = DEFAULT_PROGRESS
        var progressWidth: Float = DEFAULT_PROGRESS_WIDTH_PX
        var progressColor: Int = DEFAULT_PROGRESS_COLOR
        var roundCorners: Boolean = DEFAULT_ROUND_CORNERS

        /**
         * Called from [ArcProgressView.onSaveInstanceState]
         */
        constructor(superState: Parcelable) : super(superState)

        /**
         * Called from [CREATOR]
         */
        private constructor(parcel: Parcel) : super(parcel) {
            startAngle = parcel.readFloat()
            sweepAngle = parcel.readFloat()
            trackWidth = parcel.readFloat()
            trackColor = parcel.readInt()
            progress = parcel.readFloat()
            progressWidth = parcel.readFloat()
            progressColor = parcel.readInt()
            roundCorners = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                parcel.readBoolean()
            } else {
                (parcel.readByte() == 1.toByte())
            }
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(startAngle)
            out.writeFloat(sweepAngle)
            out.writeFloat(trackWidth)
            out.writeInt(trackColor)
            out.writeFloat(progress)
            out.writeFloat(progressWidth)
            out.writeInt(progressColor)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                out.writeBoolean(roundCorners)
            } else {
                out.writeByte(1)
            }
        }

        override fun toString(): String {
            return "ApvSavedState(" +
                "startAngle=$startAngle, sweepAngle=$sweepAngle, " +
                "trackWidth=$trackWidth, trackColor=$trackColor, " +
                "progress=$progress, progressWidth=$progressWidth, progressColor=$progressColor, " +
                "roundCorners=$roundCorners" +
                ")"
        }

        companion object CREATOR : Parcelable.Creator<ApvSavedState> {
            override fun createFromParcel(parcel: Parcel): ApvSavedState {
                return ApvSavedState(parcel)
            }

            override fun newArray(size: Int): Array<ApvSavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    companion object {
        private const val ANGLE_EPS = 0.001  // proximity to treat angles equal
        private const val DEFAULT_START_ANGLE = -180f  // from 9 o'clock
        private const val DEFAULT_SWEEP_ANGLE = 180f  // to 3 o'clock
        private const val DEFAULT_TRACK_WIDTH_PX: Float = 8f
        private const val DEFAULT_TRACK_COLOR: Int = 0xFF3F51B5.toInt()
        private const val DEFAULT_PROGRESS = 0f  // 0f (0%) .. 1f (100%)
        private const val DEFAULT_PROGRESS_WIDTH_PX: Float = 16f
        private const val DEFAULT_PROGRESS_COLOR: Int = 0xFF002984.toInt()
        private const val DEFAULT_ROUND_CORNERS: Boolean = true
    }

}
