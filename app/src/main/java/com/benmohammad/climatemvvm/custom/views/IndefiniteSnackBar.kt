package com.benmohammad.climatemvvm.custom.views

import android.view.View
import com.benmohammad.climatemvvm.R
import com.google.android.material.snackbar.Snackbar

object IndefiniteSnackBar {

    private var snackbar: Snackbar? = null

    fun show(view: View,text: String, action: () -> Unit) {
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(view.context.getString(R.string.retry)) {
                show()
            }
        }
    }
    fun hide() {
        snackbar?.dismiss()
    }
}