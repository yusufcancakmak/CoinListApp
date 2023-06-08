package com.hera.coinlistapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hera.coinlistapp.repository.ApiRepository
import com.hera.coinlistapp.response.ResponseCoinsList
import com.hera.coinlistapp.response.ResponseDetails
import com.hera.coinlistapp.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val apiRepository: ApiRepository):ViewModel() {

    private val _coinList= MutableLiveData<DataStatus<List<ResponseCoinsList.ResponseCoinsMarketsItem>>>()

    val coinList:LiveData<DataStatus<List<ResponseCoinsList.ResponseCoinsMarketsItem>>> get() = _coinList

    fun getCoinList(vs_currency:String)=viewModelScope.launch {
        apiRepository.getCoinList(vs_currency).collect{
            _coinList.value=it
        }
    }

    private val _coinDetailList= MutableLiveData<DataStatus<ResponseDetails>>()

    val coinDetailList:LiveData<DataStatus<ResponseDetails>> get() = _coinDetailList

    fun getCoinDetailList(id:String)=viewModelScope.launch {
        apiRepository.getCoinDetails(id).collect{
            _coinDetailList.value=it
        }
    }

}