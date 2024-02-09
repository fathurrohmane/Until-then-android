package com.elkusnandi.untilthen.countdown.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.elkusnandi.data.countdown.model.Countdown
import com.elkusnandi.data.countdown.source.CountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountdownViewModel @Inject constructor(private val repository: CountdownRepository) :
    ViewModel() {

    val countdowns = repository.getAllCountdown()
        .cachedIn(
            viewModelScope
        )

    fun addCountdown(countdown: Countdown) {
        viewModelScope.launch {
            repository.insertCountdown(countdown)
        }
    }

    fun deleteCountdown(countdown: Countdown) {
        viewModelScope.launch {
            repository.deleteCountdown(countdown)
        }
    }

    fun deleteAllCountdowns() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

}