package com.example.mytetrisapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.content.Context
import com.example.mytetrisapplication.GameRecord
import com.example.mytetrisapplication.loadRecords
import com.example.mytetrisapplication.saveRecord

data class RecordState(
    val records: List<GameRecord> = emptyList(),
    val sortByScore: Boolean = true,
    val editingNote: String? = null,
    val editingIndex: Int = -1
)
sealed class RecordEvent {
    object LoadRecords : RecordEvent()
    object SortByScore : RecordEvent()
    object SortByDate : RecordEvent()
    data class StartEditNote(val note: String, val index: Int) : RecordEvent()
    object CancelEditNote : RecordEvent()
    data class SaveNote(val note: String) : RecordEvent()
    data class DeleteRecord(val index: Int) : RecordEvent()
}

class RecordViewModel : ViewModel() {
    
    private val _recordState = MutableStateFlow(RecordState())
    val recordState: StateFlow<RecordState> = _recordState.asStateFlow()
    
    fun handleEvent(event: RecordEvent, context: Context) {
        when (event) {
            is RecordEvent.LoadRecords -> loadRecordsFromStorage(context)
            is RecordEvent.SortByScore -> {
                _recordState.update { it.copy(sortByScore = true) }
            }
            is RecordEvent.SortByDate -> {
                _recordState.update { it.copy(sortByScore = false) }
            }
            is RecordEvent.StartEditNote -> {
                _recordState.update { 
                    it.copy(
                        editingNote = event.note,
                        editingIndex = event.index
                    )
                }
            }
            is RecordEvent.CancelEditNote -> {
                _recordState.update { 
                    it.copy(
                        editingNote = null,
                        editingIndex = -1
                    )
                }
            }
            is RecordEvent.SaveNote -> {
                saveNote(context, event.note)
            }
            is RecordEvent.DeleteRecord -> {
                deleteRecord(context, event.index)
            }
        }
    }
    
    private fun loadRecordsFromStorage(context: Context) {
        viewModelScope.launch {
            val records = loadRecords(context)
            _recordState.update { it.copy(records = records) }
        }
    }
    
    private fun saveNote(context: Context, note: String) {
        val currentState = _recordState.value
        if (currentState.editingIndex >= 0 && currentState.editingIndex < currentState.records.size) {
            val newList = currentState.records.toMutableList()
            val old = newList[currentState.editingIndex]
            newList[currentState.editingIndex] = old.copy(note = note)
            
            viewModelScope.launch {
                saveRecord(context, newList[currentState.editingIndex])
                loadRecordsFromStorage(context)
                _recordState.update { 
                    it.copy(
                        editingNote = null,
                        editingIndex = -1
                    )
                }
            }
        }
    }
    
    private fun deleteRecord(context: Context, index: Int) {
        val currentState = _recordState.value
        if (index >= 0 && index < currentState.records.size) {
            val newList = currentState.records.toMutableList()
            newList.removeAt(index)
            
            viewModelScope.launch {
                // 重新保存所有记录
                val prefs = context.getSharedPreferences("records", Context.MODE_PRIVATE)
                val gson = com.google.gson.Gson()
                prefs.edit().putString("list", gson.toJson(newList)).apply()
                loadRecordsFromStorage(context)
            }
        }
    }
    
    fun getSortedRecords(): List<GameRecord> {
        val currentState = _recordState.value
        return if (currentState.sortByScore) {
            currentState.records.sortedByDescending { it.score }
        } else {
            currentState.records.sortedByDescending { it.date }
        }
    }
} 