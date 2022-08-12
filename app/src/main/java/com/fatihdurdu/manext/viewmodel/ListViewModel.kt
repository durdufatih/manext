package com.fatihdurdu.manext.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatihdurdu.manext.repository.ListAllImageRepository
import com.fatihdurdu.manext.viewmodel.state.ListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val listAllImageRepository: ListAllImageRepository) :
    ViewModel() {

    private var _listState = MutableStateFlow<ListState>(ListState.Empty)
    val allListState: StateFlow<ListState> = _listState

    init {
        getAllInPlayMatches()
    }

    private fun getAllInPlayMatches() {
        _listState.value = ListState.Loading

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val listResponse = listAllImageRepository.getAllImages()
                _listState.value = ListState.Success(listResponse)
            } catch (exception: HttpException) {
                _listState.value = ListState.Error("Something went wrong")
            } catch (exception: IOException) {
                _listState.value = ListState.Error("No internet connection")
            }
        }
    }
}