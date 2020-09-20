package org.d3if1071.helloworld.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if1071.helloworld.api.ApiStatus
import org.d3if1071.helloworld.api.Covid19Api
import org.d3if1071.helloworld.models.Harian
import java.lang.Exception

class MainViewModel : ViewModel() {
    private val data = MutableLiveData<List<Harian>>()
    private val status = MutableLiveData<ApiStatus>()
    private val entries = MutableLiveData<List<Entry>>()
    private val entriesSembuh = MutableLiveData<List<Entry>>()


    //data status
    fun getStatus(): LiveData<ApiStatus> = status

    fun getData(): LiveData<List<Harian>> = data

    //method untuk getEntries
    fun getEntries(): LiveData<List<Entry>> = entries

    //method untuk getEntriesSembuh
    fun getEntriesSembuh(): LiveData<List<Entry>> = entriesSembuh


    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                requestData()
            }
        }
    }

    // jadi ini method untuk memasukan dari list harian ke list untuk chart nya
    private fun getEntry(data: List<Harian>): List<Entry> {
        val result = ArrayList<Entry>()
        var index = 1f;
        for (harian in data) {
            result.add(Entry(index, harian.jumlahPositif.value.toFloat()))
            index += 1
        }
        return result
    }

    // Challenge sembuh : data untuk dari list Harian masuk ke List untuk char sembuh
    private fun getEntrySembuh(data: List<Harian>): List<Entry> {
        val result = ArrayList<Entry>()
        var index = 1f;
        for (harian in data) {
            result.add(Entry(index, harian.jumlah_sembuh.value.toFloat()))
            index += 1
        }
        return result
    }


    //request data pake log
    private suspend fun requestData() {
        try {

            //status dari loading yang ada
            status.postValue(ApiStatus.LOADING)
            val result = Covid19Api.service.getData()
            //data dari list harian adalah result dari update dan class models harian
            data.postValue(result.update.harian)
            Log.d("REQUEST", "Data Size: ${result.update.harian.size}")

            //entries data chart
            entries.postValue(getEntry(result.update.harian))

            //entries data chart
            entriesSembuh.postValue(getEntrySembuh(result.update.harian))

            status.postValue(ApiStatus.SUCCESS)

        } catch (e: Exception) {
            //terjadi jika ada masalah ketika pengambilan data
            status.postValue(ApiStatus.FAILED)
            Log.d("REQUEST", e.message.toString())

        }
    }

}