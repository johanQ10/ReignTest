package com.johan.reigntest.model

import com.google.gson.annotations.SerializedName

data class Hits (
        @SerializedName("hits") var hits: ArrayList<Hit>?,
        @SerializedName("nbHits") var nbHits: Int?,
        @SerializedName("page") var page: Int?,
        @SerializedName("nbPages") var nbPages: Int?,
        @SerializedName("hitsPerPage") var hitsPerPage: Int?,
        @SerializedName("exhaustiveNbHits") var exhaustiveNbHits: Boolean?,
        @SerializedName("query") var query: String?,
        @SerializedName("params") var params: String?,
        @SerializedName("processingTimeMS") var processingTimeMS: Int?
)