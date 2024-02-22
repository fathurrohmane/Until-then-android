package com.elkusnandi.untilthen.widget.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.elkusnandi.data.countdown.source.CountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WidgetConfigViewModel @Inject constructor(repository: CountdownRepository) :
    ViewModel() {

    val countdowns = repository.getAllCountdown()
        .cachedIn(
            viewModelScope
        )

}