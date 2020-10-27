package com.example.calencon.presentation.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.example.calencon.R
import kotlinx.android.synthetic.main.base_choice_dialog.*

class BaseChoiceDialog(private val assetId: Int = R.drawable.ic_calendar,
                       private val title: String, private val message: CharSequence, private val positiveButton: String,
                       private val negativeButton: String? = null, private val enableCloseDialog: Boolean = false,
                       context: Context, private val listener: DialogListener) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_choice_dialog)

        icon_dialog.setImageResource(assetId)
        dialog_title.text = title
        dialog_message.text = message

        positive_button.apply {
            text = positiveButton
            setOnClickListener {
                listener.onPositiveClickListener()
            }
        }

        negative_button.apply {
            negativeButton?.let {
                visibility = View.VISIBLE
                text = it
                setOnClickListener {
                    listener.onNegativeClickListener()
                }
            } ?: kotlin.run {
                visibility = View.GONE
            }
        }

        if(enableCloseDialog) {
            close_dialog.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    this@BaseChoiceDialog.dismiss()
                }
            }
        }


        val lp = WindowManager.LayoutParams()

        lp.apply {

            this@BaseChoiceDialog.window?.let {
                copyFrom(it.attributes)
            }
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        this.window?.let {
            it.setBackgroundDrawableResource(android.R.color.transparent)
            it.attributes = lp
        }

    }

    interface DialogListener {
        fun onNegativeClickListener()
        fun onPositiveClickListener()
    }
}