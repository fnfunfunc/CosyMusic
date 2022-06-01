package com.musicapp.cosymusic.activity

import android.app.Activity

/**
 * @author Eternal Epoch
 * @date 2022/5/26 21:18
 */
object ActivityCollector {

    private val activityList = mutableListOf<Activity>()

    fun addActivity(activity: Activity){
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity){
        activityList.remove(activity)
    }

    fun getCurrentActivity(): Activity {
        return if(activityList.size == 0)
            Activity()
        else activityList[activityList.size - 1]
    }

    fun finishAll(){
        for(activity in activityList){
            if(!activity.isFinishing){
                activity.finish()
            }
            activityList.clear()
        }
    }

}