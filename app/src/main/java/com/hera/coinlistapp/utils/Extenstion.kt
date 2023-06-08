package com.hera.coinlistapp.utils

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

private val formatTwo=DecimalFormat("##.##")
private val formatThree=DecimalFormat("##.##")

fun Double.roundToTwoDecimals()= formatTwo.format(this).toString()
fun Double.roundToThreeDecimals()= formatThree.format(this).toString()

fun List<Double?>?.toDoubletoFloat():List<Pair<String,Float>>{
    return this!!.map {
        val f=it!!.toFloat()
        val s=it.toString()
        Pair(s,f)
    }


}
fun RecyclerView.initRecyclerView(layoutManager: LinearLayoutManager,adapter:RecyclerView.Adapter<*>){
    this.adapter=adapter
    this.layoutManager=layoutManager

}

fun View.isVisible(isShowLoading:Boolean, container : View){
    if (isShowLoading){
        this.visibility=View.VISIBLE
        container.visibility=View.GONE
    }else{
        this.visibility=View.GONE
        container.visibility=View.VISIBLE
    }
}