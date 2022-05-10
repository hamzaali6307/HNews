package com.phonecheck.hnews.utills

import android.view.View
import com.google.android.material.snackbar.Snackbar


internal fun View.isVisible(isVisible: Boolean) {
    if (isVisible) visible() else gone()
}

internal fun View.OnClickListener.initMultipleViewsClickListener(vararg view: View) {
    for (v in view) {
        v.setOnClickListener (this)
    }
}

private fun View.visible() {
    visibility = View.VISIBLE
}

private fun View.gone() {
    visibility = View.GONE
}

fun showSnackBar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}
