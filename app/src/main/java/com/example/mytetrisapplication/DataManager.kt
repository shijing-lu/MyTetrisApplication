package com.example.mytetrisapplication

import android.content.Context
import com.example.mytetrisapplication.Entity.GameRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun saveRecord(context: Context, record: GameRecord) {
    val prefs = context.getSharedPreferences("records", Context.MODE_PRIVATE)
    val gson = Gson()
    val list = loadRecords(context).toMutableList()
    list.add(0, record)
    prefs.edit().putString("list", gson.toJson(list)).apply()
}

fun loadRecords(context: Context): List<GameRecord> {
    val prefs = context.getSharedPreferences("records", Context.MODE_PRIVATE)
    val json = prefs.getString("list", null) ?: return emptyList()
    val type = object : TypeToken<List<GameRecord>>(){}.type
    return Gson().fromJson(json, type)
} 