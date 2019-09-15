package com.example.hp.mdtest.utils

import android.util.Log
import com.example.hp.mdtest.BuildConfig

object LogUtils {
    var className:String = ""
    var methodName:String = ""
    var lineNumber:Int = 0

    private fun isDebuggable():Boolean{
        return BuildConfig.DEBUG
    }

    private fun createLog(log:String):String{
        var buffer = StringBuffer()
        return buffer.append("===$methodName($className:$lineNumber)===:$log").toString()
    }

    private fun getMethodName(elements: Array<StackTraceElement>){
        className = elements[1].fileName
        methodName = elements[1].methodName
        lineNumber = elements[1].lineNumber
    }

    fun e(message:String){
        if(!isDebuggable())return
        getMethodName(Throwable().stackTrace)
        Log.e(className, createLog(message))
    }
    fun d(message:String){
        if(!isDebuggable())return
        getMethodName(Throwable().stackTrace)
        Log.d(className, createLog(message))
    }
    fun i(message:String){
        if(!isDebuggable())return
        getMethodName(Throwable().stackTrace)
        Log.i(className, createLog(message))
    }
    fun v(message:String){
        if(!isDebuggable())return
        getMethodName(Throwable().stackTrace)
        Log.v(className, createLog(message))
    }
    fun w(message:String){
        if(!isDebuggable())return
        getMethodName(Throwable().stackTrace)
        Log.w(className, createLog(message))
    }


}