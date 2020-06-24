package com.benmohammad.climatemvvm.base

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment: Fragment() {

    fun showSnackBar(view: View, message: String) =
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

    fun toggleVisibility(
        progress: Progress,
        shouldHide: Boolean = false,
        reverse: Boolean = false
    ) = when(progress.isLoading) {
        true -> if(!reverse) View.VISIBLE else {
            if(shouldHide) View.INVISIBLE else View.GONE
        }
        false -> if(!reverse) {
            if(shouldHide) View.VISIBLE else View.GONE
        } else {
            View.VISIBLE
        }
    }
}