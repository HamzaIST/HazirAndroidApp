package technology.innovate.haziremployee.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TimerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}