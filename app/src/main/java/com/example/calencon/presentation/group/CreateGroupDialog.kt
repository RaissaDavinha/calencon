package com.example.calencon.presentation.group

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import com.example.calencon.R
import kotlinx.android.synthetic.main.dialog_create_group.*

class CreateGroupDialog(context: Context, private val listener: OnOptionClickListener) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_create_group)

        val lp = WindowManager.LayoutParams()

        lp.apply {
            this@CreateGroupDialog.window?.let {
                copyFrom(it.attributes)
            }
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        this.window?.let {
            it.setBackgroundDrawableResource(android.R.color.transparent)
            it.attributes = lp
        }

        select_photo_button.setOnClickListener { listener.onSelectPhoto() }
        create_button.setOnClickListener {
            if (input_user_name.text != null && input_user_name.text?.length!! > 3) {
                listener.onDoneClicked(input_user_name.text.toString())
            } else {
                Toast.makeText(context, R.string.min_group_name_size, Toast.LENGTH_SHORT).show()
            }
        }
        cancel_button.setOnClickListener { listener.onCloseClicked( ) }
    }

    interface OnOptionClickListener {
        fun onDoneClicked(groupName: String)
        fun onCloseClicked()
        fun onSelectPhoto()
    }
}