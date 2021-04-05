package com.johan.reigntest.model

import com.google.gson.annotations.SerializedName

data class Hit (
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("title") var title: String?,
        @SerializedName("url") var url: String?,
        @SerializedName("author") var author: String?,
        @SerializedName("story_text") var storyText: String?,
        @SerializedName("comment_text") var commentText: String?,
        @SerializedName("story_id") var storyId: Int?,
        @SerializedName("story_title") var storyTitle: String?,
        @SerializedName("story_url") var storyUrl: String?,
        @SerializedName("created_at_i") var createdAtI: Int?
)