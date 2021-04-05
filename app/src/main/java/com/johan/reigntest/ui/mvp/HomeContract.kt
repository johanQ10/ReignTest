package com.johan.reigntest.ui.mvp

import com.johan.reigntest.model.Hit

interface HomeContract {
    interface View {
        fun showHits(hits: ArrayList<Hit>?, nbPages: Int)
        fun showError(message: String?)
    }

    interface Presenter {
        fun showHits(hits: ArrayList<Hit>?, nbPages: Int)
        fun showError(message: String?)
        fun getHits(query: String?, page: Int?, hitsPerPage: Int?)
    }

    interface Model {
        fun getHits(presenter: Presenter?, query: String?, page: Int?, hitsPerPage: Int?)
    }
}