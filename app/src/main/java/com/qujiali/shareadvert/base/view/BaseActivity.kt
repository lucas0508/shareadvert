package com.qujiali.shareadvert.base.view

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.common.utils.AppManager
import com.qujiali.shareadvert.common.utils.RevealUtil.circularFinishReveal
import com.qujiali.shareadvert.common.utils.RevealUtil.setReveal
import com.qujiali.shareadvert.custom.dialog.DialogManage
import org.jetbrains.anko.toast


abstract class BaseActivity : AppCompatActivity() {

    private var mExitTime: Long = 0

    lateinit var mRootView: View

    var mApp: DialogManage? = null

    val loadService: LoadService<*> by lazy {
        LoadSir.getDefault().register(this) {
            reLoad()
        }
    }

    open fun initView() {}

    open fun initData() {}

    open fun reLoad() {}

    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
        mRootView = (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)
        AppManager.instance.addActivity(this)

        mApp = DialogManage(this)
        initView()
        initStatusColor()
        initData()

        if (showCreateReveal()) {
            setUpReveal(savedInstanceState)
        }
//        EventBus.getDefault().register(this)
    }


    override fun onCreateView(
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {


        return super.onCreateView(name, context, attrs)
    }

    private fun initStatusColor222() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.BLACK
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun initStatusColor() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColorInt(Color.WHITE)
            .navigationBarColorInt(Color.BLACK)
            .keyboardEnable(true)
            .autoDarkModeEnable(true)
            .init()


    }

    override fun onBackPressed() {
        val time = System.currentTimeMillis()

        if (time - mExitTime > 2000) {
            toast(getString(R.string.exit_app))
            mExitTime = time
        } else {
            AppManager.instance.exitApp(this)
        }
    }

    open fun showCreateReveal(): Boolean = true

    open fun showDestroyReveal(): Boolean = false

    fun setUpReveal(savedInstanceState: Bundle?) {
        setReveal(savedInstanceState)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                val imm =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(v!!.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = (left
                    + v.getWidth())
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false
    }

    override fun onPause() {
        super.onPause()
        if (showDestroyReveal()) {
            circularFinishReveal(mRootView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        EventBus.getDefault().unregister(this)
        AppManager.instance.removeActivity(this)
        // 必须调用该方法，防止内存泄漏
    }

    override fun finish() {
        super.finish()
        if (showDestroyReveal()) {
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
        } else {
            overridePendingTransition(
                R.anim.animo_alph_open,
                R.anim.animo_alph_close
            )
        }
    }


}