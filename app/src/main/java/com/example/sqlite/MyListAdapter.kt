package com.example.sqlite

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdapter(context: Context, productList: MutableList<Product>):
ArrayAdapter<Product>(context, R.layout.list_item, productList){


    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val product = getItem(position)
        var view = convertView
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val idTextView = view?.findViewById<TextView>(R.id.idTextView)
        val nameTextView = view?.findViewById<TextView>(R.id.nameTextView)
        val weightTextView = view?.findViewById<TextView>(R.id.weightTextView)
        val priceTextView = view?.findViewById<TextView>(R.id.priceTextView)
        idTextView?.text = "${product?.id}"
        nameTextView?.text = "Наименование: ${product?.name}"
        weightTextView?.text = "Вес: ${product?.weight}"
        priceTextView?.text = " Цена: ${product?.price}"

        return view!!
    }
}