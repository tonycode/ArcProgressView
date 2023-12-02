package dev.tonycode.views.apvdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dev.tonycode.views.apvdemo.databinding.MainActivityBinding


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
                R.id.menu_item_predefined -> {
                    showFragment(PredefinedFragment())
                    true
                }

                R.id.menu_item_custom -> {
                    showFragment(CustomizationFragment())
                    true
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
