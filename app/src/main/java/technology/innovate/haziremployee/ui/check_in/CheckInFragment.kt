package technology.innovate.haziremployee.ui.check_in

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.config.Constants.API_RESPONSE_CODE.*
import technology.innovate.haziremployee.databinding.FragmentCheckInBinding
import technology.innovate.haziremployee.locationUtils.GpsSettingsCheckCallback
import technology.innovate.haziremployee.locationUtils.LocationHelper
import technology.innovate.haziremployee.locationUtils.LocationUtils
import technology.innovate.haziremployee.receivers.GpsStatusReceiver
import technology.innovate.haziremployee.rest.entity.CheckInRequest
import technology.innovate.haziremployee.rest.entity.CheckInResponse
import technology.innovate.haziremployee.rest.entity.projectlistmodel.Data
import technology.innovate.haziremployee.rest.entity.projectlistmodel.Projectlistmodel
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.applyjobform.filterjoblist.DesigbationlistAdaptor
import technology.innovate.haziremployee.ui.jobpost.Jobpostlistviewmodel
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import technology.innovate.haziremployee.utils.Utils
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class CheckInFragment :
    Fragment(),
    OnMapReadyCallback,
    GpsStatusReceiver.OnGpsStateListener, GpsSettingsCheckCallback {
    private val REQUEST_UPDATE_GPS_SETTINGS = 191
    private val viewModel by activityViewModels<ActionViewModel>()
    private lateinit var addNewJobPostAdaptor: ProjectListAdaptornew
    private var userPosMap: GoogleMap? = null
    private var lastLocation: Location? = null
    private var locationUpdatesReceived = 0
    private var locationHelper: LocationHelper? = null
    private val marker = MarkerOptions()
    private var latitude = 0.0
    private val locationPermissionList = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private var isGPSTurnedOn = false
    private var broadcastTriggerCount = 0
    private var isLocationRefreshedOnPunch: Boolean = false
    private var longitude: Double = 0.0
    private var addressLine: String? = ""
    private  var designationid:Int=0
    private lateinit var viewBinding: FragmentCheckInBinding
    private lateinit var gpsStatusReceiver: GpsStatusReceiver
    private var isPunchInOutDetailAvailable: Boolean = false
    private val jobpostlistviewmodel: Jobpostlistviewmodel by viewModels()

    var arrayList: ArrayList<Data>? = null
    var dialog: Dialog? = null


    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (isRuntimePermissionGranted()) {

            viewBinding.googleMap.getMapAsync(this)
            locationHelper?.startLocationUpdates()
        } else createAlertForPermission()

    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted ->
            if (isGranted) {
                viewBinding.googleMap.getMapAsync(this)
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
                    locationUpdatesReceived = 0
                    latitude = lastLocation!!.latitude
                    longitude = lastLocation!!.longitude
                    addressLine = getStreetName(latitude, longitude)
                    addMarkerOnMap(latitude, longitude)
                    viewBinding.placeName.text = addressLine
                    if (isLocationRefreshedOnPunch) {
                        isLocationRefreshedOnPunch = false
                        punchInOutAttendance()
                    }

                }
            }


        }

    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setStatusBarTranslucent(true)

        SessionManager.init(requireActivity())
        viewBinding = FragmentCheckInBinding.inflate(inflater, container, false)
        viewBinding.googleMap.onCreate(savedInstanceState)

        arrayList= ArrayList()

        gpsStatusReceiver = GpsStatusReceiver(this)
        setListenerOnViews()
        checkPunchInOutStatus()
        loadproject()
        search()
        setupRecyclerView()

        viewBinding.searchview.doOnTextChanged { text, start, before, count ->

            viewBinding.projecterror.visibility=View.GONE
            Log.e("textdata","start"+start+"before"+before)
            if (text!=null)
            {
                viewBinding.recyclerView.visibility=View.VISIBLE
                val newList = addNewJobPostAdaptor.differ.currentList.filter { it.title?.contains(text,true) == true

                }
                Log.e("newlist", newList.toString() )
                addNewJobPostAdaptor.differ.submitList(newList)
                addNewJobPostAdaptor.notifyDataSetChanged()
                if(text.isNullOrBlank() || addNewJobPostAdaptor.differ.currentList.isEmpty())
                    addNewJobPostAdaptor.differ.submitList(viewModel.storePickupList)
                    viewBinding.recyclerView.isVisible = !text.isNullOrBlank()

                if (newList.isEmpty())
                {
                    addNewJobPostAdaptor.notifyDataSetChanged()
                    viewBinding.recyclerView.visibility=View.GONE
                }
            }
            else
            {

            }

        }
        return viewBinding.root
    }

    private fun setListenerOnViews() {

        viewBinding.shiftBt.setOnClickListener {
            if (System.currentTimeMillis() - lastLocation!!.time > 50000) {
                startOrStopLocationUpdates()
                isLocationRefreshedOnPunch = true
            } else {
                punchInOutAttendance()
                Log.d("arun", "punched")
            }
        }

        viewBinding.toolBar.setNavigationOnClickListener {
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


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        userPosMap = googleMap
        MapsInitializer.initialize(requireActivity())

    }

    fun getStreetName(latitude: Double, longitude: Double): String? {
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
            streetName =
                address.getAddressLine(0) + "\n(Accurate to ${lastLocation?.accuracy} meters)"
        }
        return if (!streetName.isNullOrEmpty()) streetName else "Unknown Address Found For Location\n(Accurate to ${lastLocation?.accuracy} meters)"
    }

    private fun isAutoTimeZoneEnabled() =
        Settings.Global.getInt(requireContext().contentResolver, Settings.Global.AUTO_TIME) != 0

    private fun punchInOutAttendance() {
        if (activity != null && isAdded) {
            if (lastLocation?.isFromMockProvider == true) {
                CustomDialog(requireActivity()).showWarningDialog("Some Fake GPS Spoof App is Running om your device.Please remove that from your device otherwise you will not be able to mark attendance\nThanks!")
            } else if (!isAutoTimeZoneEnabled()) {
                CustomDialog(requireActivity()).showWarningDialog("Please enable the Automatic time zone to be able to mark attendance")
            } else {
                val currentDate = Utils.getCurrentDate()
                val currentTime = Utils.getCurrentTime()
                if (latitude == 0.0 || longitude == 0.0) {
                    requireActivity().showToast("Unable to fetch location")
                } else if (currentDate == null) {
                    requireActivity().showToast("Invalid Date")
                } else if (currentTime == null) {
                    requireActivity().showToast("Invalid Time")
                } else {
                    val versionName = getAppVersion(requireActivity())

                    if (viewBinding.searchview.text.isNotEmpty())
                    {
                        if (viewBinding.searchview.text.trim().toString()==SessionManager.projectname)
                        {
                            val checkInRequest = CheckInRequest(
                                mode = 1,
                                date = currentDate,
                                time = currentTime,
                                lat_long = "$latitude,$longitude",
                                settings_project_id = designationid,
                                app_version = versionName

                            )
                            viewBinding.projecterror.visibility=View.GONE

                            viewModel.checkIn(checkInRequest)
                            viewModel.checkIn.observe(viewLifecycleOwner, checkInObserver)


                        }
                        else
                        {
                            viewBinding.projecterror.visibility=View.VISIBLE
                        }
                        Log.e("projectserch",viewBinding.searchview.text.trim().toString())
                    }
                    else
                    {
                        viewBinding.projecterror.visibility=View.GONE
                        val checkInRequest = CheckInRequest(
                            mode = 1,
                            date = currentDate,
                            time = currentTime,
                            lat_long = "$latitude,$longitude",
                            settings_project_id = designationid,
                            app_version = versionName

                        )

                    viewModel.checkIn(checkInRequest)
                    viewModel.checkIn.observe(viewLifecycleOwner, checkInObserver)
                    }



                }
            }
        }
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
                activity?.let {
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

    fun addMarkerOnMap(latitude: Double, longitude: Double) {
        userPosMap?.clear()
        marker.position(LatLng(latitude, longitude))
        userPosMap?.addMarker(
            marker
        )?.showInfoWindow()
        val cameraPosition = CameraPosition.Builder().target(
            LatLng(latitude, longitude)
        ).zoom(15f).build()
        userPosMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        super.onResume()
        viewBinding.googleMap.onResume()

        requireActivity().registerReceiver(
            gpsStatusReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
        if (isPunchInOutDetailAvailable)
            initLocationHelper()


    }

    override fun onPause() {
        super.onPause()
        viewBinding.googleMap.onPause()
        requireActivity().unregisterReceiver(gpsStatusReceiver)
        locationHelper?.stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.googleMap.onDestroy()
        locationHelper?.stopLocationUpdates()
    }

    override fun onGPsTurnedOnOff(isGpsOn: Boolean) {
        broadcastTriggerCount++
        isGPSTurnedOn = isGpsOn
        if (!isGPSTurnedOn && broadcastTriggerCount > 2) {
            isLocationRefreshedOnPunch = false
            broadcastTriggerCount = 0
            viewBinding.shiftBt.visibility = View.GONE
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
                viewBinding.shiftBt.visibility = View.GONE
            } else
                stopLocationUpdates()
        } else {
            locationHelper!!.checkForGpsSettings(this)
        }
    }

    private fun stopLocationUpdates() {
        locationHelper?.stopLocationUpdates()
        viewBinding.progressBar.visibility = View.GONE
        viewBinding.shiftBt.visibility = View.VISIBLE

        val gifFromAssets = GifDrawable(requireActivity().assets, "start_shift.gif")
        viewBinding.gif.setImageDrawable(gifFromAssets)
    }

    private fun checkPunchInOutStatus() {
        if (SessionManager.timing?.isNotEmpty() == true) {
            isPunchInOutDetailAvailable = false
            viewBinding.progressBar.visibility = View.GONE
        } else {
            isPunchInOutDetailAvailable = true
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            if (isRuntimePermissionGranted()) {
                viewBinding.googleMap.getMapAsync(this)
            }
            initLocationHelper()
        }

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
        CustomDialog(requireActivity()).showInformationDialog("GPS settings Un available")
    }

    override fun requiredGpsSettingAreAvailable() {
        isGPSTurnedOn = true
        startOrStopLocationUpdates()
        Log.d("points,", "GPS is turned on")
    }


    override fun gpsSettingsNotAvailable() {
        CustomDialog(requireActivity()).showInformationDialog("GPS settings Not available")
    }

    private fun getAppVersion(context: Context): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }

    private var checkInObserver: Observer<DataState<CheckInResponse>> =
        androidx.lifecycle.Observer<DataState<CheckInResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireActivity().showProgress()
                }
                is DataState.Success -> {
                    requireActivity().dismissProgress()
                    validateResponse(it.item)
                }
                is DataState.Error -> {
                    requireActivity().dismissProgress()
                    requireActivity().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireActivity().dismissProgress()
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

    private fun validateResponse(body: CheckInResponse) {

        if (activity != null && isAdded) {
            if (body.status == OK) {
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val nowTime = sdf.format(Date())

                SessionManager.timing = nowTime
                SessionManager.isTimerRunning = true

                val direction = CheckInFragmentDirections.checkInToCheckOut()
                findNavController().navigate(direction)


            } else {
                CustomDialog(requireActivity()).showInformationDialog(body.message)
            }

        }
    }

    ///
    private fun loadproject() {
        jobpostlistviewmodel.projectlist()
//        jobpostlistviewmodel.departmentRequestTypes.observe(this, designationlistobserver)

        jobpostlistviewmodel.projectlistResponse.observe(
            requireActivity()
        ) {
            when (it) {
                is DataState.Loading -> {
                    requireActivity().showProgress()
                }
                is DataState.Success -> {
                    requireActivity().dismissProgress()
                    Log.e("designationdata",it.item.toString())
                    designationTypesData(it.item)
                }
                is DataState.Error -> {
                    requireActivity().dismissProgress()
                    context?.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireActivity().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (this as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }


    }


    fun search()
    {
        jobpostlistviewmodel.projectlist()
//        jobpostlistviewmodel.departmentRequestTypes.observe(this, designationlistobserver)

        jobpostlistviewmodel.projectlistResponse.observe(requireActivity()) {
            when (it)
            {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    Log.e("department",it.item.toString())

                    viewModel.storePickupList=it.item.data
                    addNewJobPostAdaptor.differ.submitList(it.item.data)
                    viewBinding.recyclerView.visibility=View.GONE

                    addNewJobPostAdaptor.onRowClickListener {
                        //viewModel.storePickupList= listOf(it)
                        viewBinding.searchview.setText(it.title)
                        search()

                       addNewJobPostAdaptor.notifyDataSetChanged()
                        designationid=it.id

                        SessionManager.projectid=it.id.toString()
                        SessionManager.projectname=it.title
//                        selectedquestion!!.add(
//                            technology.dubaileading.maccessemployee.rest.entity.prerequiistquestionmodel.Data(
//                                it.id,
//                                it.question
//                            )
//                        )

                    }


                }
                is DataState.Error -> {
                    requireActivity().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {

                }
            }
        }

    }
    private fun setupRecyclerView() {
        addNewJobPostAdaptor = ProjectListAdaptornew(requireContext())

        viewBinding.recyclerView.adapter = addNewJobPostAdaptor
    }
    @SuppressLint("SetTextI18n")
    private fun designationTypesData(
        response: Projectlistmodel,
    ) {

        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.data != null && response.data.isNotEmpty()) {
                val spinnerAdapter = ProjectlistAdaptor(requireActivity(), response.data)
                viewBinding.designationspinner.apply {
                    adapter = spinnerAdapter
                    onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                //    leaveBalanceTextView.text=spinnerAdapter.result[p2-1]!!.title
                                //   leaveTypesSpinner.textView.setText(spinnerAdapter.result[p2]!!.title)

                                try {
                                    designationid=spinnerAdapter.result[p2-1]?.id!!
                                    Log.e("data",spinnerAdapter.result[p2-1]?.id.toString())
                                    SessionManager.projectid=spinnerAdapter.result[p2-1]?.id.toString()
                                    SessionManager.projectname=spinnerAdapter.result[p2-1]?.title.toString()
                                }
                                catch (ex:java.lang.Exception)
                                {
                                    ex.printStackTrace()
                                }


                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }
                        }
                }

            } else {
                val equipmentsSpinnerAdapter =
                    DesigbationlistAdaptor(requireActivity(), Collections.emptyList())
                viewBinding.designationspinner.apply {
                    adapter = equipmentsSpinnerAdapter
                }
            }

        } else {
            CustomDialog(requireActivity()).showInformationDialog(response.message)
        }

    }


    ///////
}