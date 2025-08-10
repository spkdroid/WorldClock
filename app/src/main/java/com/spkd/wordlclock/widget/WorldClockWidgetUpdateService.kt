package com.spkd.wordlclock.widget

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Background service to update widgets periodically
 */
class WorldClockWidgetUpdateService : JobService() {
    
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("WidgetUpdateService", "Starting widget update job")
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                updateAllWidgets()
                jobFinished(params, false)
            } catch (e: Exception) {
                Log.e("WidgetUpdateService", "Error updating widgets", e)
                jobFinished(params, true) // Reschedule on error
            }
        }
        
        return true // Job is running asynchronously
    }
    
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("WidgetUpdateService", "Widget update job stopped")
        return false // Don't reschedule if stopped
    }
    
    private fun updateAllWidgets() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val componentName = ComponentName(this, WorldClockWidgetProvider::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
        
        Log.d("WidgetUpdateService", "Updating ${appWidgetIds.size} widgets")
        
        for (appWidgetId in appWidgetIds) {
            WorldClockWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId)
        }
    }
    
    companion object {
        private const val JOB_ID = 1000
        
        fun scheduleUpdates(context: Context) {
            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            
            val jobInfo = JobInfo.Builder(JOB_ID, ComponentName(context, WorldClockWidgetUpdateService::class.java))
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000) // Update every 15 minutes
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .build()
            
            jobScheduler.schedule(jobInfo)
            Log.d("WidgetUpdateService", "Scheduled periodic widget updates")
        }
        
        fun cancelUpdates(context: Context) {
            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.cancel(JOB_ID)
            Log.d("WidgetUpdateService", "Cancelled periodic widget updates")
        }
    }
}
