package com.example.calencon.presentation.group

import com.example.calencon.R
import com.example.calencon.data.Group
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class GroupItem(val group: Group): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.recycler_view_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.contact_name_item.text = group.name
        Picasso.get()
            .load(group.url)
            .into(viewHolder.itemView.photo_img)
    }
}