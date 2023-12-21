package dev.tonycode.views.apvdemo

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import dev.tonycode.views.apvdemo.databinding.CustomizationFragmentBinding
import dev.tonycode.views.apvdemo.util.SimpleSeekBarChangeListener
import org.dmfs.android.colorpicker.ColorPickerDialogFragment
import org.dmfs.android.colorpicker.ColorPickerDialogFragment.ColorDialogResultListener
import org.dmfs.android.colorpicker.palettes.RandomPalette


class CustomizationFragment : Fragment(R.layout.customization_fragment), ColorDialogResultListener {

    private var _vb: CustomizationFragmentBinding? = null

    /** This property is valid only between onCreateView and onDestroyView */
    private val vb get() = _vb!!

    private val trackPalettes = (0..5).map {
        RandomPalette(PALETTE_PREFIX_TRACK + it, "Random 9", 9)
    }

    private val progressPalettes = (0..5).map {
        RandomPalette(PALETTE_PREFIX_PROGRESS + it, "Random 9", 9)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _vb = CustomizationFragmentBinding.bind(view)

        vb.startAngleStepper.value = vb.arcProgressView.startAngle.toInt()

        vb.sweepAngleStepper.value = vb.arcProgressView.sweepAngle.toInt()

        vb.trackWidthStepper.value = vb.arcProgressView.trackWidth.toInt()

        vb.trackColorView.setBackgroundColor(vb.arcProgressView.trackColor)

        vb.arcProgressView.progress = 0.3f
        vb.progressSeekBar.progress = (vb.arcProgressView.progress * 100).toInt()

        vb.progressWidthStepper.value = vb.arcProgressView.progressWidth.toInt()

        vb.progressColorView.setBackgroundColor(vb.arcProgressView.progressColor)

        vb.roundCornersSwitch.isChecked = vb.arcProgressView.roundCorners


        vb.startAngleStepper.onChangeListener = {
            vb.arcProgressView.startAngle = it.toFloat()
        }

        vb.sweepAngleStepper.onChangeListener = {
            vb.arcProgressView.sweepAngle = it.toFloat()
        }

        vb.trackWidthStepper.onChangeListener = {
            vb.arcProgressView.trackWidth = it.toFloat()
        }

        vb.trackColorView.setOnClickListener {
            val df = ColorPickerDialogFragment()

            df.setPalettes(*trackPalettes.toTypedArray())
            df.show(childFragmentManager, null)
        }

        vb.progressSeekBar.setOnSeekBarChangeListener(object : SimpleSeekBarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                vb.arcProgressView.progress = (progress / 100f)
            }
        })

        vb.progressWidthStepper.onChangeListener = {
            vb.arcProgressView.progressWidth = it.toFloat()
        }

        vb.progressColorView.setOnClickListener {
            val df = ColorPickerDialogFragment()

            df.setPalettes(*progressPalettes.toTypedArray())
            df.show(childFragmentManager, null)
        }

        vb.roundCornersSwitch.setOnCheckedChangeListener { _, isChecked ->
            vb.arcProgressView.roundCorners = isChecked
        }
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }

    //region ColorDialogResultListener impl
    override fun onColorChanged(color: Int, paletteId: String, colorName: String?, paletteName: String?) {
        if (paletteId.startsWith(PALETTE_PREFIX_TRACK)) {
            vb.arcProgressView.trackColor = color
            vb.trackColorView.setBackgroundColor(color)
        } else {
            vb.arcProgressView.progressColor = color
            vb.progressColorView.setBackgroundColor(color)
        }
    }

    override fun onColorDialogCancelled() { /* do nothing */ }
    //endregion


    companion object {
        private const val PALETTE_PREFIX_TRACK = "palette_track_"
        private const val PALETTE_PREFIX_PROGRESS = "palette_progress_"
    }

}
