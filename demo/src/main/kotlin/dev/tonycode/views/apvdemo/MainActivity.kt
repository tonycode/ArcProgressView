package dev.tonycode.views.apvdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dev.tonycode.views.apvdemo.databinding.MainActivityBinding
import dev.tonycode.views.apvdemo.screens.customization.CustomizationFragment
import dev.tonycode.views.apvdemo.screens.samples.SamplesFragment
import dev.tonycode.views.apvdemo.screens.styled.PredefinedFragment
import dev.tonycode.views.apvdemo.util.consumed


class MainActivity : AppCompatActivity() {

    private lateinit var vb: MainActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = MainActivityBinding.inflate(layoutInflater)
        setContentView(vb.root)

        if (supportFragmentManager.fragments.isEmpty()) {
            showFragment(PredefinedFragment())
        }

        vb.bottomNav.setOnItemSelectedListener {
            if (vb.bottomNav.selectedItemId == it.itemId) return@setOnItemSelectedListener false

            when (it.itemId) {
                R.id.menu_item_predefined -> consumed {
                    showFragment(PredefinedFragment())
                }

                R.id.menu_item_samples -> consumed {
                    showFragment(SamplesFragment())
                }

                R.id.menu_item_custom -> consumed {
                    showFragment(CustomizationFragment())
                }

                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

}
