package com.mmp.mymapdiary.data.main

import android.text.Editable
import android.text.Selection
import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.mmp.mymapdiary.CreateMapMutation
import com.mmp.mymapdiary.SelectAllUsersQuery
import com.mmp.mymapdiary.SelectAllMapsQuery
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

fun callOkHttpApolloClient(): ApolloClient? {
    val baseUrl =
//        "http://ec2-18-208-222-183.compute-1.amazonaws.com:4002/graphql"
        "https://5ahxbvmodj.execute-api.us-east-1.amazonaws.com/dev/graphql"
    val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .pingInterval(30, TimeUnit.SECONDS)
        .build()

    return ApolloClient.builder()
        .serverUrl(baseUrl)
        .okHttpClient(okHttpClient)
        .build()
}

class MainViewModel : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _viewModelText: MutableLiveData<String> = MutableLiveData()
    private val _viewModelCounter: MutableLiveData<Int> = MutableLiveData()
    private val _regexInputText: MutableLiveData<Editable> = MutableLiveData()
    private val _regexResultText: MutableLiveData<String> = MutableLiveData()
    private val _apolloText: MutableLiveData<String> = MutableLiveData()

    val viewModelText: LiveData<String> = _viewModelText
    val viewModelCounter: LiveData<Int> = _viewModelCounter  //= ObservableInt(0)
    val regexInputText: LiveData<Editable> = _regexInputText
    val regexResultText: LiveData<String> = _regexResultText
    val apolloText: LiveData<String> = _apolloText

    val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)

    init {
        _viewModelText.value = "Start"
        _viewModelCounter.value = 0
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    fun increase() {
        _viewModelText.value = "Increased"
        _viewModelCounter.value = (viewModelCounter).value?.plus(1)
//        viewModelCounter.set(viewModelCounter.get() + 1)
    }

    fun decrease() {
        _viewModelText.value = "Decreased"
        _viewModelCounter.value = (viewModelCounter.value)?.minus(1)
//        viewModelCounter.set(viewModelCounter.get() - 1)
    }

    fun applyRegex(resultText: String) {
//        val testerRegex = """[^0-9가-힣\u30A1-\u30F6\u30FB\u3000]""".toRegex()
//        val regexText = testerRegex.replace(resultText, "?")
        _regexResultText.value = resultText
    }

    fun callInGraphQLData() {
        val selectAllMaps = SelectAllMapsQuery.builder().build()

        callOkHttpApolloClient()?.query(selectAllMaps)
            ?.enqueue(object : ApolloCall.Callback<SelectAllMapsQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.e(TAG, e.toString())
                }

                override fun onResponse(response: Response<SelectAllMapsQuery.Data>) {
                    Log.d(TAG, response.data.toString())
                    val map = response.data?.maps()?.get(5)
                    _apolloText.postValue(
                        "userId: " + map?.userId() + "\n"
                                + "title: " + map?.title()+ "\n"
                                + "create_date: " + map?.c_date()
                    )
                }
            })
    }

    fun mutateGraphQLData() {
        val createUser = CreateMapMutation.builder().build()

        callOkHttpApolloClient()?.mutate(createUser)
            ?.enqueue(object : ApolloCall.Callback<CreateMapMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.e(TAG, e.toString())
                }

                override fun onResponse(response: Response<CreateMapMutation.Data>) {
                    Log.d(TAG, response.data.toString())
                }
            })
    }
}
