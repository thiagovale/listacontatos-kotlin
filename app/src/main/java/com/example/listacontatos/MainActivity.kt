package com.example.listacontatos

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    lateinit var edtName: EditText
    lateinit var edtNumero: EditText
    lateinit var edtEmail: EditText

    lateinit var txtName: TextView
    lateinit var txtNumero: TextView
    lateinit var txtEmail: TextView

    lateinit var btAdicionar: Button
    lateinit var btAtualizar: Button
    lateinit var btDeletar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtName = findViewById(R.id.enterName)
        edtNumero = findViewById(R.id.enterNumero)
        edtEmail = findViewById(R.id.enterEmail)

        txtName = findViewById(R.id.Name)
        txtNumero = findViewById(R.id.Numero)
        txtEmail = findViewById(R.id.Email)

        btAdicionar = findViewById(R.id.addName)
        btAtualizar = findViewById(R.id.btAtualizar)
        btDeletar = findViewById(R.id.btDeletar)

        btAdicionar.setOnClickListener { addName() }
        btAtualizar.setOnClickListener { atualizarContato() }
        btDeletar.setOnClickListener { deletarContato() }

        printAllNames()
    }

    private fun addName() {
        val db = DBHelper(this)
        val name = edtName.text.toString()
        val numero = edtNumero.text.toString().toIntOrNull() ?: return
        val email = edtEmail.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty()) {
            db.addName(name, numero, email)
            Toast.makeText(this, "$name added to database", Toast.LENGTH_LONG).show()
            clearFields()
            printAllNames()
        }
    }

    private fun atualizarContato() {
        val db = DBHelper(this)
        val oldName = edtName.text.toString()
        val newName = edtName.text.toString()
        val newNumero = edtNumero.text.toString().toIntOrNull() ?: return
        val newEmail = edtEmail.text.toString()

        if (oldName.isNotEmpty() && newName.isNotEmpty()) {
            val result = db.updateName(oldName, newName, newNumero, newEmail)
            if (result > 0) {
                Toast.makeText(this, "$oldName updated", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Contact not found", Toast.LENGTH_LONG).show()
            }
            printAllNames()
        }
    }

    private fun deletarContato() {
        val db = DBHelper(this)
        val name = edtName.text.toString()

        if (name.isNotEmpty()) {
            val result = db.deleteName(name)
            if (result > 0) {
                Toast.makeText(this, "$name deleted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Contact not found", Toast.LENGTH_LONG).show()
            }
            printAllNames()
        }
    }

    private fun clearFields() {
        edtName.text.clear()
        edtNumero.text.clear()
        edtEmail.text.clear()
    }

    private fun printAllNames() {
        val db = DBHelper(this)
        val cursor = db.getName()
        displayCursorData(cursor)
    }

    @SuppressLint("Range")
    private fun displayCursorData(cursor: Cursor?) {
        txtName.text = "Nome\n\n"
        txtNumero.text = "Numero\n\n"
        txtEmail.text = "Email\n\n"

        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    txtName.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
                    txtNumero.append(cursor.getString(cursor.getColumnIndex(DBHelper.NUMERO_COl)) + "\n")
                    txtEmail.append(cursor.getString(cursor.getColumnIndex(DBHelper.EMAIL_COl)) + "\n")
                } while (cursor.moveToNext())
            }
        }
        cursor?.close()
    }
}

