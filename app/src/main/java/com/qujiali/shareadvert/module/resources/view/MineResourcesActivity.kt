package com.qujiali.shareadvert.module.resources.view

import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.appbar.AppBarLayout
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.common.utils.ColorUtil
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import com.qujiali.shareadvert.module.resources.viewmodel.ResourcesViewModel
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.fragment_resources.mSrlRefresh
import kotlinx.android.synthetic.main.layout_toolbar.*

class MineResourcesActivity : BaseLifeCycleActivity<ResourcesViewModel>() {

    private var mCurrentPage = 1

    private lateinit var mAdapter: MineResourcesAdapter
    private lateinit var mLinearSnapHelper: LinearSnapHelper

    override fun getLayoutId(): Int = R.layout.activity_mine_resources


    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "我的资源"
        initRefresh()
        mRvArticle.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLinearSnapHelper = LinearSnapHelper()
        mLinearSnapHelper.attachToRecyclerView(mRvArticle)
        mAdapter = MineResourcesAdapter(R.layout.item_mine_resources, null)
        mRvArticle.adapter = mAdapter
        mAdapter.setOnLoadMoreListener({ onLoadMoreData() }, mRvArticle)
        mAdapter.setEnableLoadMore(true)
        iv_release.setOnClickListener {
            com.qujiali.shareadvert.common.utils.startActivity<PublishResourcesActivity>(
                this
            )
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            com.qujiali.shareadvert.common.utils.startActivity<MineResourcesDetailActivity>(
                this) {
                putExtra(Constant.KEY_RESOURCESID, mAdapter.getItem(position)!!.resourceId)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mSrlRefresh.isRefreshing=true
        onRefreshData()
    }

    override fun initDataObserver() {
        mViewModel.resourcesLt.observe(this, Observer {
            it?.let {
                addData(it)
            }
        })
    }

    private fun addData(articleList: List<ResourcesResponse>) {
        Logger.d("数据返回：(${articleList})")
        // 返回列表为空显示加载完毕
        if (articleList.isEmpty()) {
            if(mAdapter.data.size==0){
                mAdapter.data.clear()
                mSrlRefresh.isRefreshing = false
                mAdapter.notifyDataSetChanged()
                mAdapter.setEmptyView(R.layout.recycler_nomore_data)
            }else{
                mAdapter.loadMoreEnd()
            }

            return
        }
        // 如果是下拉刷新状态，直接设置数据
        if (mSrlRefresh.isRefreshing) {
            mSrlRefresh.isRefreshing = false
            mAdapter.setNewData(articleList)
            mAdapter.loadMoreComplete()
            return
        }

        // 初始化状态直接加载数据

        mAdapter.addData(articleList)
        mAdapter.loadMoreComplete()

    }

    private fun initRefresh() {
        // 设置下拉刷新的loading颜色
        mSrlRefresh.setProgressBackgroundColorSchemeColor(ColorUtil.getColor(this))
        mSrlRefresh.setColorSchemeColors(Color.WHITE)
        mSrlRefresh.setOnRefreshListener {
            onRefreshData()
        }
    }

    private fun onRefreshData() {
        mCurrentPage = 1
        mViewModel.loadResourcesLt(mCurrentPage)
    }

    private fun onLoadMoreData() {
        mViewModel.loadResourcesLt(++mCurrentPage)
    }

    override fun showCreateReveal(): Boolean = true

    override fun showDestroyReveal(): Boolean = false

    override fun onBackPressed() = finish()
}