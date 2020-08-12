package com.example.calencon.presentation.group.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.calencon.R
import com.example.calencon.data.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class AddContactsAdapter: RecyclerView.Adapter<AddContactsAdapter.ViewHolder>() {
    private var mData: MutableList<User> = mutableListOf()
    private var onItemClickListener: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false))
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(mData[position])
    }

    fun add(user: User) {
        mData.add(user)
        notifyItemInserted(mData.size-1)
    }

    fun isEmpty() = mData.isEmpty()

    fun setData(data: List<User>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: (User) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: User) {
            view.apply {
                contact_name_item.text = item.name
                Picasso.get()
                    .load(item.url)
                    .into(photo_img)

                setOnClickListener {
                    if (item.isSelected) {
                        setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    } else {
                        setBackgroundColor(ContextCompat.getColor(context, R.color.blue_gray))
                    }
                    onItemClickListener?.invoke(item)
                }
            }
        }
    }
}