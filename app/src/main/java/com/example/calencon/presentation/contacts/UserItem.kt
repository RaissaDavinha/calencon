package com.example.calencon.presentation.contacts

import com.example.calencon.R
import com.example.calencon.data.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class UserItem(val mUser: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.recycler_view_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.contact_name_item.text = mUser.name
        Picasso.get()
            .load(mUser.url)
            .into(viewHolder.itemView.photo_img)
    }
}