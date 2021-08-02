package com.example.localchat.base


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseFragment< T : ViewDataBinding, N : BaseNavigator,  V : BaseViewModel<N>> :
    Fragment()
    , BaseNavigator {

     var mViewDataBinding: T ?= null
     var mViewModel: V? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getContentView(), container, false)
        this.mViewModel = if (mViewModel == null) setViewModel() else mViewModel
        mViewDataBinding?.setVariable(getBindingVariable(), mViewModel)
        mViewModel?.navigator = getNavigator()
        return mViewDataBinding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(savedInstanceState)
    }


    abstract fun getContentView(): Int

    abstract fun setViewModel(): V?

    abstract fun getBindingVariable(): Int

    abstract fun getNavigator(): N

    abstract fun initViews(savedInstanceState: Bundle?)



    override fun startLoading() {
        (activity as? BaseActivity<*, *, *>)?.startLoading()
    }

    override fun finishLoading() {
        (activity as? BaseActivity<*, *, *>)?.finishLoading()
    }


    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    open fun onBackPressed(): Boolean {
        finishLoading()
        return false
    }

}