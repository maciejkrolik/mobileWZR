package pl.expert.mobilewzr.widget

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import pl.expert.mobilewzr.R
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.util.CalendarUtils
import kotlin.math.roundToInt

class TimetableGrid : View {

    private var subjectSelectListener: ((Int) -> Unit)? = null

    private var subjects = listOf<Subject>()
    private val subjectRects = mutableListOf<RectF>()

    private val screenDensity = resources.displayMetrics.density
    private val times = arrayListOf(
        "08.00",
        "08.45",
        "09.45",
        "10.30",
        "11.30",
        "12.15",
        "13.30",
        "14.15",
        "15.15",
        "16.00",
        "17.00",
        "17.45",
        "18.45",
        "19.30",
        "20.15"
    )
    private val breakTimes = arrayListOf(
        "09.30",
        "11.15",
        "13.00",
        "15.00",
        "16.45",
        "18.30",
        "20.15"
    )
    private val startTime = CalendarUtils.getMinutesFromTimeString(times.first())

    private val blackPaint = Paint().apply {
        textSize = 15 * screenDensity
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    private val redPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 2 * screenDensity
        isAntiAlias = true
    }
    private val whiteTextPaint = TextPaint().apply {
        color = Color.WHITE
        textSize = 12 * screenDensity
        isAntiAlias = true
    }
    private val subjectPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.colorAccentGradient)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, 800.toPx().toInt())
    }

    override fun onDraw(canvas: Canvas) {
        this.subjectRects.clear()

        val oneSixthWide = width / 6F
        val oneTwelfthWide = width / 12F

        // Draw subject times with line dividers
        for (time in times) {
            val yPosition = getYPosition(time)
            canvas.drawLine(oneSixthWide, yPosition, width.toFloat(), yPosition, blackPaint)
            canvas.drawText(time, oneTwelfthWide, yPosition + 5.toPx(), blackPaint)
        }

        // Draw days titles
        canvas.drawText(
            context.getString(R.string.monday_short),
            oneTwelfthWide * 3,
            20.toPx(),
            blackPaint
        )
        canvas.drawText(
            context.getString(R.string.tuesday_short),
            oneTwelfthWide * 5,
            20.toPx(),
            blackPaint
        )
        canvas.drawText(
            context.getString(R.string.wednesday_short),
            oneTwelfthWide * 7,
            20.toPx(),
            blackPaint
        )
        canvas.drawText(
            context.getString(R.string.thursday_short),
            oneTwelfthWide * 9,
            20.toPx(),
            blackPaint
        )
        canvas.drawText(
            context.getString(R.string.friday_short),
            oneTwelfthWide * 11,
            20.toPx(),
            blackPaint
        )

        // Draw break dividers
        for (time in breakTimes) {
            val yPosition = getYPosition(time)
            canvas.drawLine(oneSixthWide, yPosition, width.toFloat(), yPosition, blackPaint)
        }

        // Draw allSubjects rectangles
        for (subject in subjects) {
            val dayOfTheWeek = CalendarUtils.getDayOfWeek(subject.startDate)

            if (dayOfTheWeek >= 5) break

            val leftMargin = when (dayOfTheWeek) {
                0 -> oneSixthWide
                1 -> oneSixthWide * 2
                2 -> oneSixthWide * 3
                3 -> oneSixthWide * 4
                4 -> oneSixthWide * 5
                else -> oneSixthWide
            }

            val rightMargin = when (dayOfTheWeek) {
                0 -> oneSixthWide * 2
                1 -> oneSixthWide * 3
                2 -> oneSixthWide * 4
                3 -> oneSixthWide * 5
                4 -> oneSixthWide * 6
                else -> oneSixthWide * 2
            }

            val rect = RectF(
                leftMargin + 1.toPx(),
                getYPosition(subject.startTime),
                rightMargin - 1.toPx(),
                getYPosition(subject.endTime)
            )

            canvas.drawRoundRect(rect, 15F, 15F, subjectPaint)
            subjectRects.add(rect)

            val subjectTitle = subject.title
            val subjectShortLocation = subject.location.substringBefore(",")

            val staticLayout = StaticLayout
                .Builder
                .obtain(
                    "$subjectTitle\n$subjectShortLocation}",
                    0,
                    subjectTitle.length + subjectShortLocation.length + 1,
                    whiteTextPaint,
                    rect.width().roundToInt() - 6.toPx().toInt()
                )
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setMaxLines(5)
                .setEllipsize(TextUtils.TruncateAt.END)
                .build()

            canvas.save()
            canvas.translate(rect.left + 3.toPx(), rect.top + 3.toPx())
            staticLayout.draw(canvas)
            canvas.restore()
        }

        // Draw current time line
        if (CalendarUtils.getMinutesFromTimeString(CalendarUtils.getCurrentTime()) >= CalendarUtils.eightAmInMinutes) {
            val currentTimeYPosition = getYPosition(CalendarUtils.getCurrentTime())
            canvas.drawLine(
                oneSixthWide - 5.toPx(),
                currentTimeYPosition,
                width.toFloat(),
                currentTimeYPosition,
                redPaint
            )
            canvas.drawOval(
                oneSixthWide - 9.toPx(),
                currentTimeYPosition - 3.toPx(),
                oneSixthWide - 3.toPx(),
                currentTimeYPosition + 3.toPx(),
                redPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                for ((index, rect) in subjectRects.withIndex().reversed()) {
                    if (rect.contains(event.x, event.y)) {
                        broadcastSubject(index)
                        break
                    }
                }
            }
        }

        return false
    }

    fun setSubjects(subjects: List<Subject>) {
        this.subjects = subjects
        invalidate()
    }

    fun setListener(subjectIndex: (Int) -> Unit) {
        this.subjectSelectListener = subjectIndex
    }

    private fun getYPosition(timeString: String): Float {
        return (CalendarUtils.getMinutesFromTimeString(timeString) - startTime + 35).toPx()
    }

    private fun Int.toPx(): Float {
        return this * screenDensity
    }

    private fun broadcastSubject(subjectIndex: Int) {
        this.subjectSelectListener?.let { function ->
            function(subjectIndex)
        }
    }

}