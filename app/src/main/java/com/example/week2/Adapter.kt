package com.example.week2

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.week2.data.Doc

class Adapter(var context: Context, var list: ArrayList<Doc>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val doc: Doc = list[p1]
        val url_thumbnail: String? = (doc.multimedia.find { it.subtype == "thumbnail" })?.url

        when(p0){
            is ViewHolder ->{
                GlideApp.with(context).load("https://static01.nyt.com/$url_thumbnail").into(p0.thumbnail)

                p0.headline.text = doc.headline.main

                p0.linear.setOnClickListener {
                    val builder = CustomTabsIntent.Builder()
                    val customTabsIntent = builder.build()
                    builder.addDefaultShareMenuItem()
                    customTabsIntent.launchUrl(context, Uri.parse(doc.webUrl))
                }
            }
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        var headline: TextView = view.findViewById(R.id.headline)
        var linear: LinearLayout = view.findViewById(R.id.linear)
    }
}