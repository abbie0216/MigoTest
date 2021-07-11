package com.ab.migotest.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ab.migotest.databinding.ItemPassBinding
import com.ab.migotest.databinding.ItemSeparatorBinding
import com.ab.migotest.model.db.vo.PassItem
import com.ab.migotest.model.enums.PassType
import com.ab.migotest.model.vo.PassListItem

class PassAdapter(
    private val onItemClick: (PassItem) -> Unit,
    private val onBtnClick: (PassItem) -> Unit
) : ListAdapter<PassListItem, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<PassListItem>() {
                override fun areItemsTheSame(
                    oldItem: PassListItem,
                    newItem: PassListItem
                ): Boolean {
                    return ((oldItem is PassListItem.Pass && newItem is PassListItem.Pass && oldItem.content.id == newItem.content.id)
                            || (oldItem is PassListItem.Separator && newItem is PassListItem.Separator && oldItem.type == newItem.type))
                }

                override fun areContentsTheSame(
                    oldItem: PassListItem,
                    newItem: PassListItem
                ): Boolean {
                    return ((oldItem is PassListItem.Pass && newItem is PassListItem.Pass && oldItem.content == newItem.content)
                            || (oldItem is PassListItem.Separator && newItem is PassListItem.Separator && oldItem.type == newItem.type))
                }
            }

        const val TYPE_PASS = 0
        const val TYPE_SEPARATOR = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PASS -> {
                val binding = ItemPassBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                PassViewHolder(binding)
            }
            else -> {
                val binding = ItemSeparatorBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                SeparatorViewHolder(binding)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PassListItem.Pass -> TYPE_PASS
            is PassListItem.Separator -> TYPE_SEPARATOR
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.also {
            when (holder) {
                is PassViewHolder -> holder.onBind(
                    (it as PassListItem.Pass).content,
                    onItemClick,
                    onBtnClick
                )
                is SeparatorViewHolder -> holder.onBind((it as PassListItem.Separator).type)
            }
        }
    }

}

class PassViewHolder(itemBinding: ItemPassBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    private val tvPassName: TextView = itemBinding.tvPassName
    private val tvPassPrice: TextView = itemBinding.tvPassPrice
    private val btnPass: Button = itemBinding.btnPass

    fun onBind(item: PassItem, onItemClick: (PassItem) -> Unit, onBtnClick: (PassItem) -> Unit) {
        tvPassName.text = "${item.value} ${item.type} PASS"
        tvPassPrice.text = "Rp ${item.price}"
        btnPass.isEnabled = !item.isActivated
        btnPass.text = if (item.isActivated) "ACTIVATED" else "BUY"
        btnPass.setOnClickListener {
            if (!item.isActivated) {
                onBtnClick.invoke(item)
            }
        }
        itemView.setOnClickListener {
            onItemClick.invoke(item)
        }
    }
}

class SeparatorViewHolder(itemBinding: ItemSeparatorBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    private val tvTitle: TextView = itemBinding.tvTitle

    fun onBind(type: PassType) {
        tvTitle.text = "$type PASS"
    }
}