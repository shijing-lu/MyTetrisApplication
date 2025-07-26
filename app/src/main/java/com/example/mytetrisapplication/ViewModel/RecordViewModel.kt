package com.example.mytetrisapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.content.Context
import com.example.mytetrisapplication.Entity.GameRecord
import com.example.mytetrisapplication.GameRecordRepository
import kotlinx.coroutines.flow.collectLatest

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
    
    private var repository: GameRecordRepository? = null
    
    private fun getRepository(context: Context): GameRecordRepository {
        if (repository == null) {
            repository = GameRecordRepository(context)
        }
        return repository!!
    }
    
    fun handleEvent(event: RecordEvent, context: Context) {
        when (event) {
            is RecordEvent.LoadRecords -> loadRecordsFromDatabase(context)
            is RecordEvent.SortByScore -> {
                _recordState.update { it.copy(sortByScore = true) }
                loadRecordsFromDatabase(context)
            }
            is RecordEvent.SortByDate -> {
                _recordState.update { it.copy(sortByScore = false) }
                loadRecordsFromDatabase(context)
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
    
    private fun loadRecordsFromDatabase(context: Context) {
        viewModelScope.launch {
            try {
                val repository = getRepository(context)
                
                val currentState = _recordState.value
                val flow = if (currentState.sortByScore) {
                    repository.getAllRecordsSortedByScore()
                } else {
                    repository.getAllRecordsSortedByDate()
                }
                
                flow.collectLatest { records ->
                    _recordState.update { it.copy(records = records) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun saveNote(context: Context, note: String) {
        val currentState = _recordState.value
        if (currentState.editingIndex >= 0 && currentState.editingIndex < currentState.records.size) {
            val recordToUpdate = currentState.records[currentState.editingIndex]
            val updatedRecord = recordToUpdate.copy(note = note)
            
            viewModelScope.launch {
                try {
                    val repository = getRepository(context)
                    repository.updateRecord(updatedRecord)
                    
                    _recordState.update { 
                        it.copy(
                            editingNote = null,
                            editingIndex = -1
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    private fun deleteRecord(context: Context, index: Int) {
        val currentState = _recordState.value
        if (index >= 0 && index < currentState.records.size) {
            val recordToDelete = currentState.records[index]
            
            viewModelScope.launch {
                try {
                    val repository = getRepository(context)
                    repository.deleteRecord(recordToDelete)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    fun getSortedRecords(): List<GameRecord> {
        return _recordState.value.records
    }
} 