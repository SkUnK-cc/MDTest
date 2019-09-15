package com.example.hp.mdtest

import android.animation.ValueAnimator
import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.hp.mdtest.utils.LogUtils

class MyAppBarBehavior: AppBarLayout.Behavior {

    val ALPHA_VIEWGROUP = "floating_alpha_layout"
    private var alphaViewGroup :ViewGroup?=null
    val TOOLBAR = "toolbar"
    private var toolbar: Toolbar?=null
    val TABLAYOUT = "tablayout"
    private var tabLayout: ViewGroup?=null

    private var scrollCriticalY: Int = 0
    private var scrollRawY :Float = 0F

    private var mOffsetAnimator: ValueAnimator?= null

    constructor()
    constructor(context: Context, attrs:AttributeSet):super(context,attrs)


    override fun onLayoutChild(parent: CoordinatorLayout, abl: AppBarLayout?, layoutDirection: Int): Boolean {
        var handled =  super.onLayoutChild(parent, abl, layoutDirection)
        alphaViewGroup = parent.findViewWithTag(ALPHA_VIEWGROUP)
        toolbar = parent.findViewWithTag(TOOLBAR)
        tabLayout = parent.findViewWithTag(TABLAYOUT)
        if(scrollCriticalY==0)
            scrollCriticalY = toolbar?.height!! + alphaViewGroup?.height!!+tabLayout?.height!!
        LogUtils.e("toolbar.height: ${toolbar?.height}")
        LogUtils.e("alphaViewGroup.height: ${alphaViewGroup?.height}")
        LogUtils.e("scrollCriticalY: $scrollCriticalY")
        return handled
    }
    /**
     * 只有down事件是在 NestedScrollView范围内开始的情况下，这个方法才会被调用，
     * @param target NestedScrollView
     */
    override fun onStartNestedScroll(parent: CoordinatorLayout, child: AppBarLayout, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        var cla = target.javaClass
        LogUtils.e(cla.simpleName)
//        LogUtils.e("top: ${target.top}")
        LogUtils.e("y: ${target.y}")
        if(scrollRawY==0F)scrollRawY = target.y
        return true
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        LogUtils.e("y: ${target.y}")
        //dy:鼠标往上走是正，往下走是负
        if(dy>0 && target.y<=scrollCriticalY){  //到达临界值，固定 y，不消耗偏移量
            target.y = scrollCriticalY.toFloat()
            target.invalidate()
            return
        }
        LogUtils.e("scrollY: ${(target as NestedScrollView).scrollY}")
        if(dy<0 && target.y>=scrollRawY){
            target.y = scrollRawY
            target.invalidate()
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, velocityX: Float, velocityY: Float): Boolean {
//        //鼠标向下拉，velocityY为负
//        LogUtils.e("velocityY: $velocityY")
//        if(velocityY>0 && target.y<=scrollCriticalY){  //到达临界值，固定 y，不消耗偏移量
//            target.y = scrollCriticalY.toFloat()
//            target.invalidate()
//            return false
//        }
//        LogUtils.e("scrollY: ${(target as NestedScrollView).scrollY}")
//        if(velocityY<0 && target.y>=scrollRawY){
//            target.y = scrollRawY
//            target.invalidate()
//            return false
//        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    override fun onNestedFling(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    }
    private fun animateScroll(target: NestedScrollView,velocityY: Float, duration: Int, consumed: Boolean) {
        val currentOffset = target.getScrollY()
        val endOffset = 0
        if (mOffsetAnimator == null) {
            mOffsetAnimator = ValueAnimator()
            mOffsetAnimator?.addUpdateListener { animation ->
                if (animation.animatedValue is Int) {
                    target.scrollTo(0, animation.animatedValue as Int)
                }
            }
        } else {
            mOffsetAnimator?.cancel()
        }
        mOffsetAnimator?.duration = Math.min(duration, 600).toLong()

        if (velocityY >= 0) {
            mOffsetAnimator?.setIntValues(currentOffset, endOffset)
            mOffsetAnimator?.start()
        } else {
            if (!consumed) {
                mOffsetAnimator?.setIntValues(currentOffset, 0)
                mOffsetAnimator?.start()
            }
        }
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, abl: AppBarLayout, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }
}