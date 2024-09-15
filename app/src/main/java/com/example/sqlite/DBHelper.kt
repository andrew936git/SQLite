package com.example.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

        companion object{
            private val DATABASE_VERSION = 1
            private val DATABASE_NAME = "PRODUCT_DATABASE"
            private val TABLE_NAME = "product_table"
            private val KEY_ID = "id"
            private val KEY_NAME = "name"
            private val KEY_WEIGHT = "weight"
            private val KEY_PRICE = "price"
        }
    override fun onCreate(db: SQLiteDatabase?) {
        val productTable = "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, " +
                "$KEY_WEIGHT TEXT, $KEY_PRICE TEXT)"
        db?.execSQL(productTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(product: Product){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_WEIGHT, product.weight)
        contentValues.put(KEY_PRICE, product.price)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    @SuppressLint("Recycle", "Range")
    fun readProduct(): MutableList<Product>{
        val products = mutableListOf<Product>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLException){
            db.execSQL(selectQuery)
            return products
        }
        var productId: Int
        var productName: String
        var productWeight: String
        var productPrice: String
        if (cursor.moveToFirst()){
            do {
                productId = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                productName = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                productWeight = cursor.getString(cursor.getColumnIndex(KEY_WEIGHT))
                productPrice = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                val product = Product(productId, productName, productWeight, productPrice)
                products.add(product)
            }while (cursor.moveToNext())
        }
        return products
    }

    fun updateProduct(product: Product){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_WEIGHT, product.weight)
        contentValues.put(KEY_PRICE, product.price)
        db.update(TABLE_NAME, contentValues, "id=" + product.id, null)
        db.close()
    }

    fun removeProduct(product: Product){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        db.delete(TABLE_NAME, "id=" + product.id, null)
        db.close()
    }
}