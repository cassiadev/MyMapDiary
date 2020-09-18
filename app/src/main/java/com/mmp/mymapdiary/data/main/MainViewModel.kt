package com.mmp.mymapdiary.data.main

import android.text.Editable
import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.mmp.mymapdiary.CreateUserMutation
import com.mmp.mymapdiary.R
import com.mmp.mymapdiary.UserQuery
import kotlinx.android.synthetic.main.free_try_fragment.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

fun callOkHttpApolloClient(): ApolloClient? {
    val baseUrl =
        "http://ec2-18-208-222-183.compute-1.amazonaws.com:4002/graphql"
//        "https://5ahxbvmodj.execute-api.us-east-1.amazonaws.com/dev/graphql"
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
        val userQuery = UserQuery.builder().build()

        callOkHttpApolloClient()?.query(userQuery)
            ?.enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.e(TAG, e.toString())
                }

                override fun onResponse(response: Response<UserQuery.Data>) {
                    Log.d(TAG, response.data.toString())
                    val user = response.data?.user()
                    _apolloText.postValue(
                        "id: " + user?.id() + "\n"
                                + "email: " + user?.email() + "\n"
                                + "create_date: " + user?.c_date()
                    )
                }
            })
    }

    fun mutateGraphQLData() {
        val createUserMutation = CreateUserMutation.builder()
            .build()

        callOkHttpApolloClient()?.mutate(createUserMutation)
            ?.enqueue(object : ApolloCall.Callback<CreateUserMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.e(TAG, e.toString())
                }

                override fun onResponse(response: Response<CreateUserMutation.Data>) {
                    Log.d(TAG, response.data.toString())
                }
            })
    }
}
