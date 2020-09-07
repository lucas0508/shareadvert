package com.qujiali.shareadvert.base.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.common.utils.ChangeThemeEvent
import com.qujiali.shareadvert.common.utils.ColorUtil
import com.qujiali.shareadvert.custom.dialog.DialogManage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*


abstract class BaseFragment<VM : BaseViewModel<*>> : Fragment() {
    protected lateinit var loadService: LoadService<*>
    var mApp: DialogManage? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(getLayoutId(), null)
        loadService = LoadSir.getDefault().register(rootView) { reLoad() }
        EventBus.getDefault().register(this)
        mApp = DialogManage(requireContext())
        return loadService.loadLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initStatusColor()
        initView()
        initData()
    }

    abstract fun initView()

    open fun initData() {}

    // 重新加载
    open fun reLoad() = initData()


    abstract fun getLayoutId(): Int

    private fun initStatusColor() {
//        ImmersionBar.with(this)
//            .statusBarDarkFont(true, 0.2f)
//            .navigationBarColor(R.color.holo_green_dark)
//            .init()

        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColorInt(Color.WHITE)
            .navigationBarColorInt(Color.BLACK)
            .autoDarkModeEnable(true)
            .init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun changeThemeEvent(event: ChangeThemeEvent) {
    }
}