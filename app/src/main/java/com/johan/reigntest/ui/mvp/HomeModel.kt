package com.johan.reigntest.ui.mvp

import android.util.Log
import com.johan.reigntest.api.HomeApi
import com.johan.reigntest.model.Hit
import com.johan.reigntest.model.Hits
import com.johan.reigntest.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeModel(private val homeApi: HomeApi) : HomeContract.Model {
    companion object {
        private val TAG = HomeModel::class.java.simpleName
    }

    override fun getHits(presenter: HomeContract.Presenter?, query: String?, page: Int?, hitsPerPage: Int?) {
        homeApi.getHits(query, page, hitsPerPage).enqueue(object : Callback<Hits?> {
            override fun onResponse(call: Call<Hits?>, response: Response<Hits?>) {
                response.raw().cacheResponse?.let { Log.d(TAG, "Cache Response") }
                response.raw().networkResponse?.let { Log.d(TAG, "Server Response") }

                if (response.isSuccessful) {
                    val hitsRemoved = SessionManager.instance?.getHitsRemoved()

                    presenter?.showHits(response.body()?.hits?.filter {
                                !(hitsRemoved?.contains(it.createdAtI.toString()) ?: false)
                            } as ArrayList<Hit>,
                            response.body()?.nbPages ?: 0
                    )
                } else {
                    Log.e(TAG, "Error: ${response.message()}")

                    if (SessionManager.instance?.hasNetwork() == false)
                        presenter?.showError("No internet connection")
                    else presenter?.showError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Hits?>, t: Throwable) {
                Log.e(TAG, "Error: ${t.localizedMessage}")

                if (SessionManager.instance?.hasNetwork() == false)
                    presenter?.showError("No internet connection")
                else presenter?.showError("Error: ${t.localizedMessage}")
            }
        })
    }

}