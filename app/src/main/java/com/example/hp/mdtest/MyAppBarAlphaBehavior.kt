package com.example.hp.mdtest

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.hp.mdtest.utils.LogUtils

class MyAppBarAlphaBehavior : AppBarLayout.Behavior{

    val ALPHA_VIEWGROUP = "floating_alpha_layout"
    private var alphaViewGroup :ViewGroup?=null
    val TOOLBAR = "toolbar"
    private var toolbar: Toolbar?=null
    val TABLAYOUT = "tablayout"
    private var tabLayout: ViewGroup?=null
    val NESTED_SCROLL = "nestedscrollview"
    private var nestedScrollView : NestedScrollView?=null

    private var scrollViewCriticalY: Int = 0
    private var scrollRawY :Float = 0F

    constructor()
    constructor(context: Context, attrs:AttributeSet):super(context,attrs)


    override fun onLayoutChild(parent: CoordinatorLayout, abl: AppBarLayout?, layoutDirection: Int): Boolean {
        var handled =  super.onLayoutChild(parent, abl, layoutDirection)
        alphaViewGroup = parent.findViewWithTag(ALPHA_VIEWGROUP)
        toolbar = parent.findViewWithTag(TOOLBAR)
        tabLayout = parent.findViewWithTag(TABLAYOUT)
        nestedScrollView = parent.findViewWithTag(NESTED_SCROLL)
        if(scrollViewCriticalY==0)
            scrollViewCriticalY = toolbar?.height!!+tabLayout?.height!!

        LogUtils.e("toolbar.height: ${toolbar?.height}")
        LogUtils.e("alphaViewGroup.height: ${alphaViewGroup?.height}")
        LogUtils.e("scrollViewCriticalY: $scrollViewCriticalY")
        return handled
    }

    override fun onStartNestedScroll(parent: CoordinatorLayout, child: AppBarLayout, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        if(scrollRawY ==0F)scrollRawY=nestedScrollView!!.y
        LogUtils.e("scrollRawY: $scrollRawY")
        return true
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        var curY = nestedScrollView!!.y
        var alpha = (scrollRawY-curY)/(scrollRawY-scrollViewCriticalY)
        LogUtils.e("alpha: $alpha")
        LogUtils.e("curY: $curY")
        alphaViewGroup?.alpha = alpha
        alphaViewGroup?.invalidate()
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }
}