package com.johan.reigntest.ui

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johan.reigntest.R
import com.johan.reigntest.databinding.ActivityHomeBinding
import com.johan.reigntest.di.App
import com.johan.reigntest.di.component.DaggerHomeComponent
import com.johan.reigntest.di.module.HomeModule
import com.johan.reigntest.model.Hit
import com.johan.reigntest.ui.adapter.HomeClick
import com.johan.reigntest.ui.adapter.HomeHitsAdapter
import com.johan.reigntest.ui.mvp.HomeContract
import com.johan.reigntest.util.SessionManager
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.View, HomeClick {
    private lateinit var bind: ActivityHomeBinding
    private lateinit var adapter: HomeHitsAdapter

    private var hitsRemoved: MutableSet<String>? = null

    private val perPage = 20
    private val query = "mobile"

    private var isLoading = false
    private var page = 0
    private var lastPage = 0

    private var backgroundSwipe: ColorDrawable? = null
    private var iconSwipe: Drawable? = null

    @Inject
    lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initComponent()

        hitsRemoved = SessionManager.instance?.getHitsRemoved() ?: mutableSetOf()

        bind.rvHits.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val divider = DividerItemDecoration(bind.rvHits.context, LinearLayoutManager.VERTICAL)

        ResourcesCompat.getDrawable(resources, R.drawable.line_divider, null)?.let {
            divider.setDrawable(it)
            bind.rvHits.addItemDecoration(divider)
        }
        ResourcesCompat.getDrawable(resources, R.drawable.ic_trash, null)?.let {
            iconSwipe = it
        }

        backgroundSwipe = ColorDrawable(Color.RED)

        bind.swipeRefresh.setProgressViewOffset(
                false,
                bind.swipeRefresh.progressViewStartOffset,
                bind.swipeRefresh.progressViewEndOffset
        )

        bind.swipeRefresh.setColorSchemeColors(getColor(R.color.purple))
        bind.swipeRefresh.setOnRefreshListener {
            page = 0
            isLoading = true
            presenter.getHits(query, page, perPage)
        }

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20

                val iconMargin: Int = itemView.height / 4
                val iconHeight: Int = itemView.height / 2
                val iconWidth: Int = (iconHeight * (iconSwipe?.intrinsicWidth ?: 0)) / (iconSwipe?.intrinsicHeight ?: 0)

                val iconTop: Int = itemView.top + iconMargin
                val iconBottom: Int = iconTop + iconHeight

                if (dX < 0) {
                    val iconRight = itemView.right - iconMargin
                    val iconLeft: Int = iconRight - iconWidth

                    iconSwipe?.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                    backgroundSwipe?.setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top, itemView.right, itemView.bottom)
                } else {
                    iconSwipe?.setBounds(0, 0, 0, 0)
                    backgroundSwipe?.setBounds(0, 0, 0, 0)
                }

                backgroundSwipe?.draw(c)
                iconSwipe?.draw(c)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                adapter.getId(viewHolder.adapterPosition)?.let { hitsRemoved?.add(it.toString()) }
                adapter.delete(viewHolder.adapterPosition)

                SessionManager.instance?.setHitsRemoved(hitsRemoved)

                Toast.makeText(baseContext, getString(R.string.item_removed), Toast.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(bind.rvHits)

        bind.nsvContent.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val v: View = bind.nsvContent.getChildAt(bind.nsvContent.childCount - 1)
            val diff = v.bottom - (bind.nsvContent.height + scrollY)

            if (diff == 0 && !isLoading && page <= lastPage) {
                isLoading = true
                bind.pbLoader.visibility = View.VISIBLE

                presenter.getHits(query, page, perPage)
            }
        }

        isLoading = true
        presenter.getHits(query, page, perPage)
    }

    private fun initComponent() {
        DaggerHomeComponent.builder()
            .appComponent(App[this].component())
            .homeModule(HomeModule(this))
            .build()
            .inject(this)
    }

    override fun showHits(hits: ArrayList<Hit>?, nbPages: Int) {
        bind.swipeRefresh.isRefreshing = false

        if (page == 0) {
            lastPage = nbPages - 1
            adapter = HomeHitsAdapter(this, hits, this)
            bind.rvHits.adapter = adapter
        } else
            adapter.pagination(hits)

        if (page >= lastPage)
            bind.pbLoader.visibility = View.GONE

        isLoading = false
        page++
    }

    override fun showError(message: String?) {
        bind.swipeRefresh.isRefreshing = false
        isLoading = false
        bind.pbLoader.visibility = View.INVISIBLE

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun hitClick(hit: Hit) {
        val detailIntent = Intent(this, DetailActivity::class.java)
        detailIntent.putExtra("url", hit.storyUrl)
        startActivity(detailIntent)
    }
}