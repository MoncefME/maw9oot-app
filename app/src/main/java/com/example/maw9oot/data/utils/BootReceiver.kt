package com.example.maw9oot.data.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.maw9oot.data.local.DataStoreManager
import com.example.maw9oot.data.repository.PrayerTimesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var prayerTimesRepository: PrayerTimesRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Re-scheduling alarms after boot")

            val pendingResult = goAsync()
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                try {
                    rescheduleAllAlarms(context, dataStoreManager, prayerTimesRepository)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
