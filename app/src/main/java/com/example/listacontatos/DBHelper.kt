package com.example.listacontatos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE $TABLE_NAME ("
                + "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME_COl TEXT, " +
                "$NUMERO_COl INTEGER, " +
                "$EMAIL_COl TEXT)")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addName(name: String, numero: Int, email: String) {
        val values = ContentValues().apply {
            put(NAME_COl, name)
            put(NUMERO_COl, numero)
            put(EMAIL_COl, email)
        }

        val db = writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getName(): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun searchByName(name: String): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $NAME_COl LIKE ?", arrayOf("%$name%"))
    }

    fun updateName(oldName: String, newName: String, newNumero: Int, newEmail: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(NAME_COl, newName)
            put(NUMERO_COl, newNumero)
            put(EMAIL_COl, newEmail)
        }
        return db.update(TABLE_NAME, values, "$NAME_COl = ?", arrayOf(oldName))
    }

    fun deleteName(name: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$NAME_COl = ?", arrayOf(name))
    }

    companion object {
        private const val DATABASE_NAME = "Dispositivos_Moveis"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "tb_contato"
        const val ID_COL = "id"
        const val NAME_COl = "name"
        const val NUMERO_COl = "numero"
        const val EMAIL_COl = "email"
    }
}
