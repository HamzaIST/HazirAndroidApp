package technology.innovate.haziremployee.ui.check_out

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.droidsonroids.gif.GifDrawable
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.FragmentCheckOutBinding
import technology.innovate.haziremployee.locationUtils.GpsSettingsCheckCallback
import technology.innovate.haziremployee.locationUtils.LocationHelper
import technology.innovate.haziremployee.locationUtils.LocationUtils
import technology.innovate.haziremployee.receivers.GpsStatusReceiver
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.check_in.ActionViewModel
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import technology.innovate.haziremployee.utils.TimerHelper
import technology.innovate.haziremployee.utils.Utils
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class CheckOutFragment : Fragment(), GpsStatusReceiver.OnGpsStateListener,
    GpsSettingsCheckCallback {
    private val REQUEST_UPDATE_GPS_SETTINGS = 191
    private var lastLocation: Location? = null
    private var locationUpdatesReceived = 0
    private var locationHelper: LocationHelper? = null
    private var latitude = 0.0
    private val locationPermissionList = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private var isGPSTurnedOn = false
    private var broadcastTriggerCount = 0
    private var isLocationRefreshedOnAction: Boolean = false
    private var logAction: Int = 0
    private var longitude: Double = 0.0
    private var addressLine: String? = ""
    private lateinit var viewBinding: FragmentCheckOutBinding
    private lateinit var gpsStatusReceiver: GpsStatusReceiver
    private var timer: Timer = Timer()
    private var isShiftOver = false
    private val viewModel by activityViewModels<ActionViewModel>()


    private var resultLauncher = registerForActivityResult(StartActivityForResult()) {
        if (isRuntimePermissionGranted()) {
            locationHelper?.startLocationUpdates()
        } else createAlertForPermission()

    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted ->
            if (isGranted) {
                locationHelper?.startLocationUpdates()
            } else {
                createAlertForPermission()

            }
        }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (activity != null && isAdded) {
                val locationReceived = locationResult.lastLocation
                if (LocationUtils.isBetterLocation(locationReceived, lastLocation)) {
                    lastLocation = locationReceived
                }
                locationUpdatesReceived++
                if (locationHelper != null && lastLocation != null && ((lastLocation!!.hasAccuracy() && lastLocation!!.accuracy <= 100)) || locationUpdatesReceived > 3) {
                    stopLocationUpdates()
                    locationUpdatesReceived = 0;
                    latitude = lastLocation!!.latitude
                    longitude = lastLocation!!.longitude
                    addressLine = getStreetName(latitude, longitude)
                    viewBinding.placeName.text = addressLine
                    if (isLocationRefreshedOnAction) {
                        if (logAction == Constants.LOG_ACTION.BREAK_IN) {
                            breakIn()
                        } else if (logAction == Constants.LOG_ACTION.BREAK_OUT) {
                            breakOut()
                        } else if (logAction == Constants.LOG_ACTION.PUNCH_OUT) {
                            checkOut()
                        }
                        isLocationRefreshedOnAction = false

                    }

                }
            }

            Log.d("location", "$locationUpdatesReceived")

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        SessionManager.init(requireContext())

        viewBinding = FragmentCheckOutBinding.inflate(inflater, container, false)

        gpsStatusReceiver = GpsStatusReceiver(this)
        checkPunchInOutStatus()
        viewBinding.projectname.text=SessionManager!!.projectname.toString()
        Log.e("projectid",SessionManager.projectid.toString())

        if (SessionManager.projectname.isNullOrEmpty())
        {

            viewBinding.projectnamelayout.visibility=View.GONE
        }
        else
        {
            viewBinding.projectnamelayout.visibility=View.VISIBLE

        }

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        performTimerLogic()

        viewBinding.backImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun performTimerLogic() {
        println("SessionManager isBreakOut = ${SessionManager.isBreakOut}")
        if (SessionManager.isBreakOut == true) {
            breakOutStatusChange(true)
            breakInStatusChange(false)

            SessionManager.hours.let {
                var hour = it
                if (it.isNullOrEmpty()) {
                    hour = "00:00:00"
                }
                viewBinding.timer.text = hour

            }
        } else {
            breakInStatusChange(true)
            breakOutStatusChange(false)
        }
        runTimer()

        statusUpdate()


    }

    private fun statusUpdate() {
        if (activity != null && isAdded) {
            if (!SessionManager.isBreakStarted!!) {
                viewBinding.breakStatus.visibility = View.GONE
                viewBinding.checkIn.visibility = View.VISIBLE

                val gifFromAssets = GifDrawable(requireActivity().assets, "check_in_gif.gif")
                viewBinding.checkInGif.setImageDrawable(gifFromAssets)

                viewBinding.timeTxt.text = "at ${SessionManager.timing}"
            } else {
                viewBinding.breakStatus.visibility = View.VISIBLE
                viewBinding.checkIn.visibility = View.GONE

                if (SessionManager.isBreakOut == true) {
                    viewBinding.imageView2.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_break_out, null
                        )
                    )
                    viewBinding.statusName.setTextColor(resources.getColor(R.color.color_break_out))
                    viewBinding.statusName.text = "Break Out "

                    viewBinding.timeTxt.text = "at ${SessionManager.timing}"
                } else {
                    viewBinding.imageView2.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_break_in, null
                        )
                    )
                    viewBinding.statusName.setTextColor(resources.getColor(R.color.color_break_in))
                    viewBinding.statusName.text = "Break In "

                    viewBinding.timeTxt.text = "at ${SessionManager.timing}"
                }
            }
        }

    }

    private fun breakInStatusChange(status: Boolean) {
        if (status) {
            viewBinding.imgBreakIn.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.image_tint
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
            viewBinding.breakInTxt.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.image_tint
                )
            )
            viewBinding.breakInLayout.setOnClickListener(null)

        } else {
            viewBinding.imgBreakIn.colorFilter = null
            viewBinding.breakInTxt.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_break_in
                )
            )
            viewBinding.breakInLayout.setOnClickListener {
                if (lastLocation == null) {
                    startOrStopLocationUpdates()
                    isLocationRefreshedOnAction = true
                    logAction = Constants.LOG_ACTION.BREAK_IN
                } else if (System.currentTimeMillis() - lastLocation!!.time > 50000) {
                    startOrStopLocationUpdates()
                    isLocationRefreshedOnAction = true
                    logAction = Constants.LOG_ACTION.BREAK_IN
                } else {
                    breakIn()
                    Log.d("arun", "punched")
                }

            }

        }
    }

    private fun breakOutStatusChange(status: Boolean) {
        if (status) {
            viewBinding.imgBreakOut.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.image_tint
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
            viewBinding.breakOutTxt.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.image_tint
                )
            )
            viewBinding.breakOutLayout.setOnClickListener(null)
        } else {
            viewBinding.imgBreakOut.colorFilter = null
            viewBinding.breakOutTxt.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_break_out
                )
            )
            viewBinding.breakOutLayout.setOnClickListener {
                if (lastLocation == null) {
                    startOrStopLocationUpdates()
                    isLocationRefreshedOnAction = true
                    logAction = Constants.LOG_ACTION.BREAK_OUT
                } else if (System.currentTimeMillis() - lastLocation!!.time > 50000) {
                    startOrStopLocationUpdates()
                    isLocationRefreshedOnAction = true
                    logAction = Constants.LOG_ACTION.BREAK_OUT
                } else {
                    breakOut()
                }
            }

        }
    }


    fun getStreetName(latitude: Double, longitude: Double): String {
        var streetName: String? = null
        val geocoder = Geocoder(requireActivity())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            // Thoroughfare seems to be the street name without numbers
            streetName =
                address.getAddressLine(0) + "\n(Accurate to ${lastLocation?.accuracy} meters)"
        }
        return if (!streetName.isNullOrEmpty()) streetName else "Unknown Address Found For Location\n(Accurate to ${lastLocation?.accuracy} meters)"
    }


    private fun isRuntimePermissionGranted(): Boolean {
        var result: Boolean
        for (permission in locationPermissionList) {
            result = activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    permission
                )
            } == PackageManager.PERMISSION_GRANTED
            if (!result) return false
        }
        return true
    }

    private fun createAlertForPermission() {
        val alertDialog = AlertDialog.Builder(activity).setTitle("Info").setCancelable(false)
            .setMessage("Please allow permission to work app properly")
            .setNegativeButton("No") { dialog12: DialogInterface, which: Int ->
                dialog12.dismiss()
                findNavController().popBackStack()
            }
            .setPositiveButton("Go to Permissions") { dialog1: DialogInterface?, which: Int ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${activity?.packageName}")
                )
                if (activity != null && isAdded) {
                    resultLauncher.launch(intent)
                }
            }.create()
        alertDialog.show()
    }


    private fun initLocationHelper() {
        locationHelper = LocationHelper(activity)
        locationHelper?.setRequiredGpsPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationHelper?.init()
        locationHelper?.setLocationCallback(locationCallback)
        locationHelper!!.checkForGpsSettings(this)

    }


    override fun onResume() {
        super.onResume()

        requireActivity().registerReceiver(
            gpsStatusReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
        initLocationHelper()

        if (isShiftOver) {
            navToHome()
        }

    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(gpsStatusReceiver)
        locationHelper?.stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationHelper?.stopLocationUpdates()
        timer.cancel()
    }

    override fun onGPsTurnedOnOff(isGpsOn: Boolean) {
        broadcastTriggerCount++
        isGPSTurnedOn = isGpsOn
        if (!isGPSTurnedOn && broadcastTriggerCount > 2) {
            isLocationRefreshedOnAction = false
            broadcastTriggerCount = 0
            disableEnableViews(false)
            Toast.makeText(activity, "Please turn on GPS", Toast.LENGTH_SHORT).show()
            startOrStopLocationUpdates()

        } else if (isGpsOn && broadcastTriggerCount > 2) {
            broadcastTriggerCount = 0
            Toast.makeText(activity, "GPS is ON", Toast.LENGTH_SHORT).show()
            startOrStopLocationUpdates()

        }

    }

    private fun startOrStopLocationUpdates() {
        if (isGPSTurnedOn) {
            if (lastLocation == null || (System.currentTimeMillis() - lastLocation!!.time > 5000)) {
                locationHelper!!.startLocationUpdates()
                viewBinding.progressBar.visibility = View.VISIBLE
                disableEnableViews(false)
            } else
                stopLocationUpdates()
        } else {
            locationHelper!!.checkForGpsSettings(this)
        }
    }

    private fun stopLocationUpdates() {
        locationHelper?.stopLocationUpdates()
        viewBinding.progressBar.visibility = View.GONE
        disableEnableViews(true)
    }

    private fun checkPunchInOutStatus() {
        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        initLocationHelper()
    }


    override fun requiredGpsSettingAreUnAvailable(status: ResolvableApiException) {
        try {
            if (activity != null && isAdded) {
                startIntentSenderForResult(
                    status.resolution.intentSender, REQUEST_UPDATE_GPS_SETTINGS,
                    null,
                    0,
                    0,
                    0,
                    null
                )
            }
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun requiredGpsSettingAreAvailable() {
        isGPSTurnedOn = true
        startOrStopLocationUpdates()
        Log.d("points,", "GPS is turned on")
    }

    override fun gpsSettingsNotAvailable() {
        Toast.makeText(activity, " GPS settings Not Available", Toast.LENGTH_SHORT)
            .show()
    }

    private fun disableEnableViews(isEnable: Boolean) {
        if (activity != null && isAdded) {
            if (!isEnable) {
                viewBinding.imgBreakOut.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.image_tint
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                viewBinding.breakOutTxt.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.image_tint
                    )
                )

                viewBinding.imgBreakIn.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.image_tint
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                viewBinding.breakInTxt.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.image_tint
                    )
                )

                viewBinding.endShift.disable()


            } else {

                viewBinding.imgBreakOut.colorFilter = null
                viewBinding.breakOutTxt.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_break_out
                    )
                )


                viewBinding.imgBreakIn.colorFilter = null
                viewBinding.breakInTxt.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_break_in
                    )
                )


                viewBinding.endShift.enable()

                viewBinding.endShift.setOnClickListener {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("End Shift?")
                    builder.setMessage("Do you really want to End the Shift?")
                    builder.setPositiveButton(
                        "Yes"
                    ) { _, _ ->
                        if (lastLocation == null) {
                            startOrStopLocationUpdates()
                            isLocationRefreshedOnAction = true
                            logAction = Constants.LOG_ACTION.PUNCH_OUT
                        } else if (System.currentTimeMillis() - lastLocation!!.time > 50000) {
                            startOrStopLocationUpdates()
                            isLocationRefreshedOnAction = true
                            logAction = Constants.LOG_ACTION.PUNCH_OUT
                        } else {
                            checkOut()
                        }

                    }
                    builder.setNegativeButton("No", null)
                    builder.show()
                }

                performTimerLogic()

            }
        }
    }

    private fun runTimer() {
        timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                lifecycleScope.launch(Dispatchers.Main) {
                    if (SessionManager.isBreakOut == true || SessionManager.timing.isNullOrEmpty()) {
                        timer.cancel()
                    } else {
                        SessionManager.hours.let {
                            var hours = it
                            if (it.isNullOrEmpty()) {
                                hours = "00:00:00"
                            }
                            val timerTime =
                                TimerHelper().findTime(SessionManager.timing!!, hours!!)
                            val h1 = timerTime.split(":").toTypedArray()
                            val hour1 = h1[0].toInt()
                            if (hour1 >= 18) {
                                timer.cancel()
                                isShiftOver = true
                                navToHome()

                            }


                            viewBinding.timer.text = timerTime
                        }
                    }
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    private fun navToHome() {
        if (activity != null && isAdded) {
            SessionManager.timing = ""
            SessionManager.hours = ""
            SessionManager.isBreakOut = false
            SessionManager.isBreakStarted = false
            findNavController().popBackStack()
        }
    }

    private fun breakIn() {
        if (isValidAction()) {
            val lat_lng =
                "$latitude,$longitude"
            val currentDate = Utils.getCurrentDate()
            val currentTime = Utils.getCurrentTime()
            if (latitude == 0.0 || longitude == 0.0) {
                requireActivity().showToast("Unable to fetch location")
            } else if (currentDate == null) {
                requireActivity().showToast("Invalid Date")
            } else if (currentTime == null) {
                requireActivity().showToast("Invalid Time")
            } else {
                val breakInRequest = BreakInRequest(
                    date = currentDate,
                    time = currentTime,
                    lat_lng
                )

                viewModel.breakIn(breakInRequest)
                viewModel.breakIn.observe(this, breakInObserver)

            }
        }
    }

    private fun breakOut() {
        if (isValidAction()) {
            val lat_lng = "$latitude,$longitude"
            val currentDate = Utils.getCurrentDate()
            val currentTime = Utils.getCurrentTime()
            if (latitude == 0.0 || longitude == 0.0) {
                requireActivity().showToast("Unable to fetch location")
            } else if (currentDate == null) {
                requireActivity().showToast("Invalid Date")
            } else if (currentTime == null) {
                requireActivity().showToast("Invalid Time")
            } else {
                val breakOutRequest = BreakOutRequest(
                    date = currentDate,
                    time = currentTime,
                    lat_lng
                )

                viewModel.breakOut(breakOutRequest)
                viewModel.breakOut.observe(this, breakOutObserver)
            }
        }
    }

    private fun checkOut() {
        if (isValidAction()) {
            val lat_lng = "$latitude,$longitude"
            val currentDate = Utils.getCurrentDate()
            val currentTime = Utils.getCurrentTime()
            if (latitude == 0.0 || longitude == 0.0) {
                requireActivity().showToast("Unable to fetch location")
            } else if (currentDate == null) {
                requireActivity().showToast("Invalid Date")
            } else if (currentTime == null) {
                requireActivity().showToast("Invalid Time")
            } else {
                val checkOutRequest = CheckOutRequest(
                    date = currentDate,
                    time = currentTime,
                    settings_project_id = SessionManager.projectid.toString(),
                    lat_lng,
                    2
                )
                viewModel.checkOut(checkOutRequest)
                viewModel.checkOut.observe(this, checkOutObserver)

            }
        }
    }

    private fun isValidAction(): Boolean {
        val isValid: Boolean = if (lastLocation?.isFromMockProvider == true) {
            CustomDialog(requireActivity()).showWarningDialog("Some Fake GPS Spoof App is Running om your device.Please remove that from your device otherwise you will not be able to mark attendance\nThanks!")
            false
        } else if (!isAutoTimeZoneEnabled()) {
            CustomDialog(requireActivity()).showWarningDialog("Please enable the Automatic time zone to be able to mark attendance")
            false
        } else {
            true
        }
        return isValid
    }

    private var breakInObserver: Observer<DataState<BreakInResponse>> =
        androidx.lifecycle.Observer<DataState<BreakInResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateBreakInResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireContext().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }

    private fun validateBreakInResponse(body: BreakInResponse) {
        if (body.status == Constants.API_RESPONSE_CODE.OK) {
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val tt = sdf.format(Date())

            SessionManager.timing = tt
            SessionManager.isBreakOut = false
            SessionManager.isTimerRunning = true
            SessionManager.isBreakStarted = true

            performTimerLogic()

        } else {
            CustomDialog(requireActivity()).showInformationDialog(body.message)
        }
    }

    private var breakOutObserver: Observer<DataState<BreakOutResponse>> =
        androidx.lifecycle.Observer<DataState<BreakOutResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateBreakOutResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireContext().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }

    private fun validateBreakOutResponse(body: BreakOutResponse) {
        if (body.status == Constants.API_RESPONSE_CODE.OK) {
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val tt = sdf.format(Date())
            SessionManager.timing = tt
            SessionManager.isBreakOut = true
            SessionManager.isTimerRunning = false
            SessionManager.hours = viewBinding.timer.text.toString()
            SessionManager.isBreakStarted = true

            performTimerLogic()
        }else {
            CustomDialog(requireActivity()).showInformationDialog(body.message)
        }
    }

    private var checkOutObserver: Observer<DataState<CheckOutResponse>> =
        androidx.lifecycle.Observer<DataState<CheckOutResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateCheckOutResponse(it.item)
                    SessionManager.projectid=null
                    SessionManager.projectname=""
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireContext().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }

    private fun validateCheckOutResponse(body: CheckOutResponse) {
        if (body.status == Constants.API_RESPONSE_CODE.OK) {
            performTimerLogic()
            SessionManager.timing = ""
            SessionManager.hours = ""
            SessionManager.isBreakOut = false
            SessionManager.isBreakStarted = false
            SessionManager.isTimerRunning = false

            findNavController().popBackStack()
        } else {
            CustomDialog(requireActivity()).showInformationDialog(body.message)
        }
    }

    private fun isAutoTimeZoneEnabled() =
        Settings.Global.getInt(requireContext().contentResolver, Settings.Global.AUTO_TIME) != 0
}