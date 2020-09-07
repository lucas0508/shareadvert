package com.qujiali.shareadvert.module.index

import android.os.Bundle
import android.util.SparseArray
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseActivity
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.common.utils.checkUpdate
import com.qujiali.shareadvert.module.demand.view.HomeFragmentDemand
import com.qujiali.shareadvert.module.home.view.HomeFragment
import com.qujiali.shareadvert.module.mine.view.MineFragment
import com.qujiali.shareadvert.module.resources.view.ResourcesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class MainActivity : BaseActivity() {
    private var mLastIndex: Int = -1
    private val mFragmentSparseArray = SparseArray<Fragment>()

    // 当前显示的 fragment
    private var mCurrentFragment: Fragment? = null
    private var mLastFragment: Fragment? = null

    private lateinit var mToolbarTitles: List<String>

    override fun getLayoutId(): Int = R.layout.activity_main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        // 判断当前是recreate还是新启动
        initToolbarTitles()
        initBottomNavigation()
        if (savedInstanceState == null) {
            switchFragment(Constant.HOME)
            checkUpdate(this, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // recreate时保存当前页面位置
        outState.putInt("index", mLastIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 恢复recreate前的页面
        mLastIndex = savedInstanceState.getInt("index")
        switchFragment(mLastIndex)
    }

    private fun initToolbarTitles() {
        mToolbarTitles = arrayListOf(
            getString(R.string.navigation_home),
            getString(R.string.navigation_resources),
            getString(R.string.navigation_require),
            getString(R.string.navigation_mine)
        )
    }

    private fun initBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                   // fab_add.visibility = View.VISIBLE
                    switchFragment(Constant.HOME)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_resources -> {
                    fab_add.visibility = View.GONE
                    switchFragment(Constant.RESOURCES)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_require -> {
                    fab_add.visibility = View.GONE
                    switchFragment(Constant.REQUIRE)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_mine -> {
                    fab_add.visibility = View.GONE
                    switchFragment(Constant.MINE)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun switchFragment(index: Int) {
        val fragmentManager = supportFragmentManager
        val transaction =
            fragmentManager.beginTransaction()
        // 将当前显示的fragment和上一个需要隐藏的fragment分别加上tag, 并获取出来
        // 给fragment添加tag,这样可以通过findFragmentByTag找到存在的fragment，不会出现重复添加
        mCurrentFragment = fragmentManager.findFragmentByTag(index.toString())
        mLastFragment = fragmentManager.findFragmentByTag(mLastIndex.toString())
        // 如果位置不同
        if (index != mLastIndex) {
            if (mLastFragment != null) {
                transaction.hide(mLastFragment!!)
            }
            if (mCurrentFragment == null) {
                mCurrentFragment = getFragment(index)
                transaction.add(R.id.content, mCurrentFragment!!, index.toString())
            } else {
                transaction.show(mCurrentFragment!!)
            }
        }

        // 如果位置相同或者新启动的应用
        if (index == mLastIndex) {
            if (mCurrentFragment == null) {
                mCurrentFragment = getFragment(index)
                transaction.add(R.id.content, mCurrentFragment!!, index.toString())
            }
        }
        transaction.commit()
        mLastIndex = index
        setToolBarTitle(toolbar, mToolbarTitles[mLastIndex])
    }

    private fun getFragment(index: Int): Fragment {
        var fragment: Fragment? = mFragmentSparseArray.get(index)
        if (fragment == null) {
            when (index) {
                Constant.HOME -> fragment = HomeFragment.getInstance()
                Constant.RESOURCES -> fragment = ResourcesFragment.getInstance()
                Constant.REQUIRE -> fragment = HomeFragmentDemand.getInstance()
                Constant.MINE -> fragment = MineFragment.getInstance()
            }
            mFragmentSparseArray.put(index, fragment)
        }
        return fragment!!
    }

    private fun setToolBarTitle(toolbar: Toolbar, title: String) {
        toolbarTextView.text = title
    }

    override fun showCreateReveal(): Boolean = false
}
