package com.qujiali.shareadvert.base.repository

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class BaseRepository {
    private val mCompositeDisposable by lazy { CompositeDisposable() }

    fun addSubscribe(disposable: Disposable) = mCompositeDisposable.add(disposable)

    fun unSubscribe() = mCompositeDisposable.dispose()
}