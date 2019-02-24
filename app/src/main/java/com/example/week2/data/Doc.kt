package com.example.week2.data

import com.google.gson.annotations.SerializedName

data class Doc(
    @SerializedName("web_url")
    val webUrl: String,
    val snippet: String,
    @SerializedName("lead_paragraph")
    val leadParagraph: String,
    val blog: Blog,
    val source: String,
    val multimedia: List<Multimedia>,
    val headline: Headline,
    val keywords: List<Any>,
    @SerializedName("pub_date")
    val pubDate: String,
    @SerializedName("document_type")
    val documentType: String,
    @SerializedName("news_desk")
    val newsDesk: String,
    @SerializedName("section_name")
    val sectionName: String,
    val subsectoinName: String,
    val byline: Byline,
    @SerializedName("type_of_material")
    val typeOfMaterial: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("word_count")
    val wordCount: Int,
    val score: Int,
    val uri: String,
    @SerializedName("slideshow_credits")
    val slideshowCredits: String
)