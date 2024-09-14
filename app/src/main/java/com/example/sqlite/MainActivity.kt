package com.example.sqlite

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private val db = DBHelper(this)
    val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = "Потребительская корзина"
        setSupportActionBar(toolbar)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val weightEditText = findViewById<EditText>(R.id.weightEditText)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val listView = findViewById<ListView>(R.id.listView)
        saveButton.setOnClickListener{
            val name = nameEditText.text.toString()
            val weight = weightEditText.text.toString()
            val price = priceEditText.text.toString()

            val product = Product(name, weight, price)
            productList.add(product)

            val adapter = MyListAdapter(this, productList)
            listView.adapter = adapter
            adapter.notifyDataSetChanged()

            db.addProduct(product)

            Toast.makeText(this,"Данные добавлены в базу данных", Toast.LENGTH_LONG).show()

            nameEditText.text.clear()
            priceEditText.text.clear()
            weightEditText.text.clear()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.exit_menu -> {
                Toast.makeText(
                    applicationContext,
                    "Программа завершена",
                    Toast.LENGTH_LONG
                ).show()
                finishAffinity()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}