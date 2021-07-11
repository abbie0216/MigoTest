package com.ab.migotest.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ab.migotest.R
import com.ab.migotest.databinding.DialogPassDetailBinding
import com.ab.migotest.view.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseDialogFragment : DialogFragment() {

    open var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            mainViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        if (isFullLayout()) {
            val window = dialog?.window
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } else {
            val window = dialog?.window
            val widthPixels = (resources.displayMetrics.widthPixels * 0.8).toInt()
            window?.setLayout(widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    abstract fun isFullLayout(): Boolean
}