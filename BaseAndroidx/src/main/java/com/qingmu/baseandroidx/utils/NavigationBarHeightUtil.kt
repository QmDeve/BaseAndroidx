package com.qingmu.baseandroidx.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

internal class NavigationBarHeightUtil {
    fun getRealNavigationBarHeight(view: View): Int {
        val insets = ViewCompat.getRootWindowInsets(view)
            ?.getInsets(WindowInsetsCompat.Type.navigationBars())
        return insets?.bottom ?: getNavigationBarHeight()
    }

    private fun getNavigationBarHeight(): Int {
        return 0
    }
}