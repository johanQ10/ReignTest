package com.johan.reigntest.ui.mvp

import com.johan.reigntest.model.Hit

class HomePresenter(private val view: HomeContract.View?, private val model: HomeContract.Model?) : HomeContract.Presenter {
    override fun showHits(hits: ArrayList<Hit>?, nbPages: Int) {
        view?.showHits(hits, nbPages)
    }

    override fun showError(message: String?) {
        view?.showError(message)
    }

    override fun getHits(query: String?, page: Int?, hitsPerPage: Int?) {
        model?.getHits(this, query, page, hitsPerPage)
    }

}