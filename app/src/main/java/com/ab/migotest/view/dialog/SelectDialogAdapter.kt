package com.ab.migotest.view.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ab.migotest.R
import com.ab.migotest.databinding.ItemDialogSelectBinding

class SelectDialogAdapter(
    private val dataList: ArrayList<String> = arrayListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var currentPosition = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemDialogSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectDialogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SelectDialogViewHolder -> {
                holder.selectItemText.text = dataList[position]

                if (position == currentPosition) {
                    holder.selectItemText.setTextColor(
                        holder.selectItemText.context.getColor(R.color.black)
                    )
                } else {
                    holder.selectItemText.setTextColor(
                        holder.selectItemText.context.getColor(R.color.gray)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class SelectDialogViewHolder(itemBinding: ItemDialogSelectBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val selectItemText: TextView = itemBinding.tvSelectItem
    }

    fun updateCurrentPosition(position: Int) {
        currentPosition = position
    }

}