package dev.tonycode.views.apvdemo

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import dev.tonycode.views.apvdemo.databinding.PredefinedFragmentBinding
import dev.tonycode.views.apvdemo.util.SimpleSeekBarChangeListener


class PredefinedFragment : Fragment(R.layout.predefined_fragment) {

    private var _vb: PredefinedFragmentBinding? = null

    /** This property is valid only between onCreateView and onDestroyView */
    private val vb get() = _vb!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _vb = PredefinedFragmentBinding.bind(view)

        vb.seekBar.setOnSeekBarChangeListener(object : SimpleSeekBarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val floatProgress = progress / 100f

                vb.arcProgressView1.progress = floatProgress
                vb.arcProgressView2.progress = floatProgress
                vb.arcProgressView3.progress = floatProgress
                vb.arcProgressView4.progress = floatProgress
                vb.arcProgressView5.progress = floatProgress
            }
        })
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }

}
