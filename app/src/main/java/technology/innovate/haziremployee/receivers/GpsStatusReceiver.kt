package technology.innovate.haziremployee.receivers

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager

class GpsStatusReceiver(private var onGpsStateListener:OnGpsStateListener): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val lm = context!!.getSystemService(Service.LOCATION_SERVICE) as LocationManager
        val isEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        onGpsStatusChanged(isEnabled)
    }


    private fun onGpsStatusChanged(isEnabled: Boolean) {
        onGpsStateListener.onGPsTurnedOnOff(isEnabled)
    }

    interface OnGpsStateListener {
        fun onGPsTurnedOnOff(isGpsOn: Boolean)
    }
}