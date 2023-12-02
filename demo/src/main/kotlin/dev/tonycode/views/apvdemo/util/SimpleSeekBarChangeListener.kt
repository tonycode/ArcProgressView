package dev.tonycode.views.apvdemo.util

import android.widget.SeekBar


open class SimpleSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        // nothing
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // nothing
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // nothing
    }

}
