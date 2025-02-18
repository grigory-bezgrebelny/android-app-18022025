package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemAgeBinding

class AgeAdapter : RecyclerView.Adapter<AgeAdapter.ViewHolder>() {

    private val _differ = AsyncListDiffer(this, getDiffCallback())

    var dataList: List<AgeUiItem>
        get() = _differ.currentList
        set(value) {
            _differ.submitList(value)
        }

    override fun getItemCount(): Int = _differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAgeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = _differ.currentList.getOrNull(position) ?: return
        holder.bindView(item)
    }

    private fun getDiffCallback() = object : DiffUtil.ItemCallback<AgeUiItem>() {
        override fun areItemsTheSame(oldItem: AgeUiItem, newItem: AgeUiItem): Boolean =
                oldItem::class == newItem::class && oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: AgeUiItem, newItem: AgeUiItem): Boolean =
                oldItem::class == newItem::class && oldItem == newItem
    }

    class ViewHolder(private val viewBinding: ItemAgeBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        lateinit var model: AgeUiItem
            private set

        init {
            viewBinding.root.setOnClickListener { model.onClick() }
        }

        @SuppressLint("SetTextI18n")
        fun bindView(data: AgeUiItem) {
            model = data

            viewBinding.tv.text = data.value.toString()
            viewBinding.iv.isVisible = data.selected
        }
    }
}
