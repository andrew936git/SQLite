package com.example.sqlite

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private val db = DBHelper(this)
    private var productList = mutableListOf<Product>()
    private var listAdapter: MyListAdapter? = null
    private lateinit var listView: ListView
    private lateinit var nameEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var idEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var changeButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)
        init()

    }

    private fun init(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = "Потребительская корзина"
        setSupportActionBar(toolbar)

        nameEditText = findViewById(R.id.nameEditText)
        weightEditText = findViewById(R.id.weightEditText)
        priceEditText = findViewById(R.id.priceEditText)
        idEditText = findViewById(R.id.idEditText)
        saveButton = findViewById(R.id.saveButton)
        changeButton = findViewById(R.id.changeButton)
        deleteButton = findViewById(R.id.deleteButton)

        viewDataAdapter()

    }

    override fun onResume() {
        super.onResume()

        saveButton.setOnClickListener {
            saveRecord()
        }

        changeButton.setOnClickListener {
            changeRecord()
        }

        deleteButton.setOnClickListener {
            deleteRecord()
        }
    }

    private fun deleteRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val choseDeleteID = dialogView.findViewById<EditText>(R.id.deleteET)
        dialogBuilder.setTitle("Удалить запись")
        dialogBuilder.setMessage("Введите ID")
        dialogBuilder.setPositiveButton("Удалить"){_, _ ->
            val deleteID = choseDeleteID.text.toString()
            if (deleteID.trim() != "") {
                val product = Product(Integer.parseInt(deleteID),"", "", "")
                db.removeProduct(product)
                viewDataAdapter()
                Toast.makeText(this,
                    "Продукт удален",
                    Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена"){_, _ ->}
        dialogBuilder.create().show()
    }

    private fun changeRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val updateIdET = dialogView.findViewById<EditText>(R.id.updateIdET)
        val updateNameET = dialogView.findViewById<EditText>(R.id.updateNameET)
        val updateWeightET = dialogView.findViewById<EditText>(R.id.updateWeightET)
        val updatePriceET = dialogView.findViewById<EditText>(R.id.updatePriceET)

        dialogBuilder.setTitle("Обновить запись")
        dialogBuilder.setMessage("Введите данные ниже")
        dialogBuilder.setPositiveButton("Обновить"){_, _ ->
            val updateID = updateIdET.text.toString()
            val updateName = updateNameET.text.toString()
            val updateWeight = updateWeightET.text.toString()
            val updatePrice = updatePriceET.text.toString()

            if (updateID.trim() != ""
                && updateName.trim() != ""
                && updateWeight.trim() != ""
                && updatePrice.trim() != "") {
                val product = Product(
                    Integer.parseInt(updateID),
                    updateName,
                    updateWeight,
                    updatePrice)
                db.updateProduct(product)
                viewDataAdapter()
                Toast.makeText(this,
                    "Данные обновлены",
                    Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена"){_, _ ->
        }
        dialogBuilder.create().show()
    }

    private fun saveRecord(){
        val id = idEditText.text.toString()
        val name = nameEditText.text.toString()
        val weight = weightEditText.text.toString()
        val price = priceEditText.text.toString()
        if (id.trim() != "" && name.trim() != "" && weight.trim() != "" && price.trim() != "") {
            val product = Product(Integer.parseInt(id), name, weight, price)
            db.addProduct(product)

            Toast.makeText(this, "Данные добавлены в базу данных", Toast.LENGTH_LONG).show()

            idEditText.text.clear()
            nameEditText.text.clear()
            priceEditText.text.clear()
            weightEditText.text.clear()
            viewDataAdapter()
        }
    }

    private fun viewDataAdapter(){
        productList = db.readProduct()
        listAdapter = MyListAdapter(this, productList)
        listView.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
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