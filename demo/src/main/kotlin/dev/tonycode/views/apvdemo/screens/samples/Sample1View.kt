package dev.tonycode.views.apvdemo.screens.samples

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import dev.tonycode.views.apvdemo.R
import dev.tonycode.views.apvdemo.databinding.Sample1ViewBinding


class Sample1View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val vb: Sample1ViewBinding

    init {
        vb = Sample1ViewBinding.inflate(LayoutInflater.from(context), this)

        orientation = HORIZONTAL
        setBackgroundColor(ContextCompat.getColor(context, R.color.azulado))
        setPadding(context.resources.getDimensionPixelSize(R.dimen.background_padding))

        setupViews()
    }

    private fun setupViews() {
        // score #1: 4 out of 10
        vb.score1Label.text = context.getString(R.string.score_label_pattern, 1)
        vb.score1Value.text = context.getString(R.string.score_pattern, 4, 10)
        vb.score1Apv.progress = (4 / 10f)

        // score #2: 8 out of 10
        vb.score2Label.text = context.getString(R.string.score_label_pattern, 2)
        vb.score2Value.text = context.getString(R.string.score_pattern, 8, 10)
        vb.score2Apv.progress = (8 / 10f)

        // progress: 25%
        @SuppressLint("SetTextI18n")
        vb.progressValue.text = "25%"
        vb.progressApv.progress = 0.25f
    }

}
