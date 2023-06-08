package com.hera.coinlistapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.color.HarmonizedColorsOptions
import com.hera.coinlistapp.R
import com.hera.coinlistapp.databinding.ItemBinding
import com.hera.coinlistapp.response.ResponseCoinsList

import com.hera.coinlistapp.utils.Constants
import com.hera.coinlistapp.utils.Constants.animationDuration
import com.hera.coinlistapp.utils.roundToThreeDecimals
import com.hera.coinlistapp.utils.roundToTwoDecimals
import com.hera.coinlistapp.utils.toDoubletoFloat
import javax.inject.Inject

class CryptosAdapter @Inject() constructor() : RecyclerView.Adapter<CryptosAdapter.ViewHolder>() {

    private lateinit var binding: ItemBinding
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseCoinsList.ResponseCoinsMarketsItem) {
            binding.apply {
                tvName.text = item.id
                tvSymbol.text = item.symbol?.uppercase()
                tvPrice.text = "€${item.currentPrice?.roundToThreeDecimals()}"
                imgCrypto.load(item.image) {
                    crossfade(true)
                    crossfade(500)
                    placeholder(R.drawable.baseline_currency_bitcoin_24)
                    error(R.drawable.baseline_currency_bitcoin_24)
                }

                lineChart.gradientFillColors =
                    intArrayOf(
                        Color.parseColor("#2a9085"),
                        Color.TRANSPARENT
                    )

                lineChart.animation.duration = Constants.animationDuration
                val listData = item.sparklineIn7d?.price.toDoubletoFloat()
                lineChart.animate(listData)

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }

    private var onItemClickListener:((ResponseCoinsList.ResponseCoinsMarketsItem)-> Unit)?=null
    fun setOnItemClickListener (listener:(ResponseCoinsList.ResponseCoinsMarketsItem)->Unit){
        onItemClickListener=listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<ResponseCoinsList.ResponseCoinsMarketsItem>() {
        override fun areItemsTheSame(oldItem: ResponseCoinsList.ResponseCoinsMarketsItem, newItem: ResponseCoinsList.ResponseCoinsMarketsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem:ResponseCoinsList.ResponseCoinsMarketsItem, newItem: ResponseCoinsList.ResponseCoinsMarketsItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}