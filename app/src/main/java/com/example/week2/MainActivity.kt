package com.example.week2

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.week2.data.AllNews
import com.example.week2.data.Doc
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    var allnews: AllNews? = null
    var listDoc: ArrayList<Doc>? = null
    var adapter: Adapter? = null
    var query: String? = ""

    private val PAGE = "page"
    private val NY_BEGIN_DATE = "begin_date"
    val NY_NEWS_DESK = "fq"
    private val NY_SORT_ORDER = "sort"
    private val QUERY = "q"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar = this.supportActionBar!!
        actionBar.title = "News Article Search"

        if(isNetworkAvailable()){
            Toast.makeText(applicationContext , "Đã kết nối" , Toast.LENGTH_LONG).show()
        }
        else
            Toast.makeText(applicationContext , "Lỗi kết nối" , Toast.LENGTH_LONG).show()

        btn_search.setOnClickListener{
            search(keyword.text.toString(), 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.filter) {
            val transaction = supportFragmentManager.beginTransaction()
            val dialogFiler = DialogFilter()
            dialogFiler.show(transaction , "dialogFilter")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun search(keyword: String, page: Int) {
        val retrofit = Retrofit().getRetrofit().create(GetData::class.java)
        val call = retrofit.getDataSearch(createQuery(keyword , page), createNewDesks())

        call.enqueue(object : Callback<AllNews>{
            override fun onFailure(call: Call<AllNews>, t: Throwable) {
                Toast.makeText(applicationContext, "Lỗi", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<AllNews>, response: Response<AllNews>) {
                Toast.makeText(applicationContext, "Thành công", Toast.LENGTH_LONG).show()
                allnews = response.body()!!
                listDoc = allnews!!.response.docs as ArrayList<Doc>
                this@MainActivity.adapter = Adapter(this@MainActivity, listDoc!!)
                val staggeredGridLayoutManager = StaggeredGridLayoutManager(4 , LinearLayout.VERTICAL)
                findViewById<RecyclerView>(R.id.recyler_view).layoutManager = staggeredGridLayoutManager
                findViewById<RecyclerView>(R.id.recyler_view).addItemDecoration(DividerItemDecoration(applicationContext, LinearLayout.VERTICAL))
                findViewById<RecyclerView>(R.id.recyler_view).adapter = adapter

                adapter?.notifyItemRangeInserted(adapter!!.itemCount, listDoc!!.size - 1)
                adapter?.notifyDataSetChanged()
            }

        })
    }

    private fun createNewDesks(): ArrayList<String> {
        val share = getSharedPreferences("Filter" , Context.MODE_PRIVATE)
        val checkA : Boolean = share.getBoolean("SelectA" , false)
        val checkF : Boolean= share.getBoolean("SelectF" , false)
        val checkS : Boolean= share.getBoolean("SelectS" , false)
        val arrayFilter = arrayListOf<String>()
        if(checkF) arrayFilter.add("fashion & style")
        if(checkA) arrayFilter.add("arts")
        if(checkS) arrayFilter.add("sport")
        return arrayFilter
    }

    private fun createQuery(keyword: Any, page: Any): Map<String, String> {
        val share = getSharedPreferences("Filter" , Context.MODE_PRIVATE)

        val date = share?.getString("Date" , "")
        val sort : Int = share.getInt("Sort" , 0)

        val params = HashMap<String,String>()

        if(!date.equals("")){
            Log.d("srtDate" , date)
            val da = date?.split("/")
            Log.d("srtDate" , da.toString())
            val strDate = "${da!![2]}${da[1]}${da[0]}"
            Log.d("srtDate" , strDate)
            params[NY_BEGIN_DATE] = strDate
        }
        if(sort >= 0){
            params[NY_SORT_ORDER] = when(sort) {
                1 -> "newest"
                else -> "oldest"
            }
        }
        params[QUERY] = keyword as String
        params[PAGE] = page.toString()

        Log.d("paramss" , params.toString())

        return params
    }

    fun reloadData() {
        Log.d("query" , "query: $query")
        listDoc!!.clear()
        search(query!! , 0)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}
