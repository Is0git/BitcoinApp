package com.is0git.bitcoin.ui.fragments.home_fragment

import android.animation.ObjectAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.PopupMenu
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.is0git.bitcoin.R
import com.is0git.bitcoin.databinding.HomeFragmentLayoutBinding
import com.is0git.bitcoin.services.UpdateTimeService
import com.is0git.bitcoin.utils.*
import com.is0git.bitcoin.viewmodels.home.HomeViewModel
import com.is0git.bitcoin.viewmodels.home.HomeViewModel.Companion.HAS_BPI_LIST_ANIMATION_PLAYED
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_app_bar_motion_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


const val HOME_FRAGMENT_TAG = "HOME_FRAGMENT_TAG"

@AndroidEntryPoint
class HomeFragment : Fragment(), MotionLayout.TransitionListener,
    UpdateTimeService.OnTimeUpdateListener, PopupMenu.OnMenuItemClickListener {

    lateinit var binding: HomeFragmentLayoutBinding
    val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var bpiListAdapter: BpiListAdapter
    lateinit var homeMotionLayout: MotionLayout
    private lateinit var updateTimeService: UpdateTimeService
    private var isChangingHint: Boolean = false
    private var timeUpdateServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(HOME_FRAGMENT_TAG, "time update service disconnected")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(HOME_FRAGMENT_TAG, "time update service connected")
            updateTimeService = (service as UpdateTimeService.UpdateServiceBinder).getService()
            updateTimeService.time = homeViewModel.bpiLiveData.value?.bpi?.time?.updated
            updateTimeService.onTimeUpdateListener = this@HomeFragment
        }

    }
    private val exchangeObjectAnimator: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(binding.homeAppBarLayout.swapIcon, "rotation", 180f).apply {
            repeatMode = ObjectAnimator.REVERSE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)
        homeMotionLayout = binding.appBar.findViewById(R.id.home_app_bar_layout)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreViewStates()
        setObservers()
        setAdapters()
        setActions()
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        Intent(
            requireContext(),
            UpdateTimeService::class.java
        ).also { intent ->
            requireActivity().bindService(
                intent,
                timeUpdateServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unbindService(timeUpdateServiceConnection)
    }

    private fun restoreViewStates() {
        val motionState =
            homeViewModel.savedStateHandle.get<Int?>(HomeViewModel.MOTION_TRANSITION_ID)
        view?.post {
            if (motionState != null) {
                if (motionState == R.id.end) {
                    homeMotionLayout.transitionToEnd()
                } else homeMotionLayout.transitionToStart()
            }
        }
        binding.homeAppBarLayout.apply {
            currencyTextField.editText?.text =
                homeViewModel.savedStateHandle.get(HomeViewModel.TO_BITCOIN_EDIT_TEXT)
            bitcoinTextField.editText?.text =
                homeViewModel.savedStateHandle.get(HomeViewModel.FROM_BITCOIN_EDIT_TEXT)
            val isBitCoinEdiTextFocused =
                homeViewModel.savedStateHandle.get<Boolean>(HomeViewModel.IS_BITCOIN_EDIT_FOCUSED)
            if (isBitCoinEdiTextFocused != null && isBitCoinEdiTextFocused) bitcoinTextField?.editText?.requestFocus()
            val isCurrencyEdiTextFocused =
                homeViewModel.savedStateHandle.get<Boolean>(HomeViewModel.IS_CURRENCY_EDIT_FOCUSED)
            Log.d(
                HOME_FRAGMENT_TAG,
                "BIT: $isBitCoinEdiTextFocused, CURRENCYT: $isCurrencyEdiTextFocused"
            )
            if (isCurrencyEdiTextFocused != null && isCurrencyEdiTextFocused) currencyTextField?.editText?.requestFocus()
        }
    }

    private fun setActions() {
        binding.homeAppBarLayout.apply {
            currencyTextField.setEndIconOnClickListener {
                createMenu()
            }
            calculatorButton.setOnClickListener {
                homeMotionLayout.transitionToEnd()
            }
            backText.setOnClickListener {
                homeMotionLayout.transitionToStart()
            }
            refreshButton.clicks()
                .buffer(Channel.RENDEZVOUS)
                .onEach { homeViewModel.getBpi() }
                .launchIn(lifecycleScope)
            bitcoinTextField.editText!!.apply {
                addChannelTextChangedListener()
                    .buffer(Channel.CONFLATED)
                    .onEach {
                        try {
                            if (hasFocus() && !isChangingHint) {
                                if (homeViewModel.selectedCurrencyCode.value != null) {
                                    val mValue = if (it.isNullOrEmpty()) "0" else it.toString()
                                    homeViewModel.convertFromBitcoin(
                                        mValue,
                                        homeViewModel.selectedCurrencyCode.value!!
                                    )
                                    rotateSwapIcon()
                                }
                            }
                        } catch (ex: Exception) {
                            handleConversionException(ex)
                        }
                    }
                    .launchIn(lifecycleScope)
            }
            currencyTextField.editText!!.apply {
                addChannelTextChangedListener()
                    .buffer(Channel.CONFLATED)
                    .onEach {
                        try {
                            if (hasFocus()) {
                                if (homeViewModel.selectedCurrencyCode.value != null) {
                                    val mValue = if (it.isNullOrEmpty()) "0" else it.toString()
                                    homeViewModel.convertToBitcoin(
                                        mValue,
                                        homeViewModel.selectedCurrencyCode.value!!
                                    )
                                    rotateSwapIcon()
                                }
                            }
                        } catch (ex: Exception) {
                            handleConversionException(ex)
                        }
                    }
                    .launchIn(lifecycleScope)
            }
            infoButton.setOnClickListener { showInfoDialog() }
            exchangeButton.setOnClickListener {
                Snackbar.make(
                    requireView(),
                    "feature disabled",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setAdapters() {
        binding.bpiList.adapter = bpiListAdapter
    }

    private fun setObservers() {
        homeViewModel.apply {
            bpiMediatorLiveData.observe(viewLifecycleOwner) {
                lifecycleScope.launch(Dispatchers.Default) {
                    launch(Dispatchers.Main) {
                        binding.bitcoinListProgressBar.visibility =
                            if (it?.bpiData.isNullOrEmpty()) {
                                View.VISIBLE
                            } else {
                                homeViewModel.selectedCurrencyCode.also { stringLiveData ->
                                    if (stringLiveData.value == null) homeViewModel.savedStateHandle.set(
                                        HomeViewModel.SELECTED_CURRENCY_CODE,
                                        it?.bpiData?.first()?.code
                                    )
                                }
                                View.GONE
                            }
                        val hasListAnimPlayed = homeViewModel.savedStateHandle.get<Boolean>(
                            HAS_BPI_LIST_ANIMATION_PLAYED
                        )
                        if (hasListAnimPlayed != null && !hasListAnimPlayed) {
                            val animation: LayoutAnimationController =
                                AnimationUtils.loadLayoutAnimation(
                                    requireContext(),
                                    R.anim.bpi_list_layout_anim
                                )
                            binding.bpiList.layoutAnimation = animation
                            homeViewModel.savedStateHandle.set(
                                HAS_BPI_LIST_ANIMATION_PLAYED, true
                            )
                        }
                        bpiListAdapter.submitList(it?.bpiData)
                    }
                    if (it?.bpi?.time?.updated != null) {
                        val rawTimeUpdated = async {
                            TimeResolver.getFormattedTimeInCurrentTimeZone(
                                TimeResolver.RSS_FORMAT,
                                it.bpi.time.updated,
                                Locale.ENGLISH
                            )
                        }
                        val timeUpdatedAgo = async {
                            if (::updateTimeService.isInitialized) updateTimeService.time =
                                it.bpi.time.updated
                            return@async getString(
                                R.string.last_updated,
                                TimeResolver.getLastUpdatedTime(
                                    it.bpi.time.updated,
                                    requireContext(),
                                    Locale.ENGLISH
                                )
                            )
                        }
                        withContext(Dispatchers.Main) {
                            binding.homeAppBarLayout.apply {
                                lastUpdatedRaw.text = rawTimeUpdated.await()
                                timeUpdated.text = HtmlParser.getStringFromHtml(
                                    timeUpdatedAgo.await(),
                                    requireContext()
                                )
                            }
                        }
                    }
                }
            }
            fromBitcoinConversionLiveData.observe(viewLifecycleOwner) {
                binding.homeAppBarLayout.currencyTextField.editText.setText(it.convertedResult)
            }
            toBitcoinConversionLiveData.observe(viewLifecycleOwner) {
                binding.homeAppBarLayout.bitcoinTextField.editText?.setText(it.convertedResult)
            }
            selectedCurrencyCode.observe(viewLifecycleOwner) {
                binding.homeAppBarLayout.currencyTextField.hint = it
                lifecycleScope.launch {
                    try {
                        isChangingHint = true
                        homeViewModel.convertFromBitcoin(
                            binding.homeAppBarLayout.bitcoinTextField.editText?.text.toString(),
                            it
                        )
                    } catch (ex: Exception) {
                        handleConversionException(ex)
                    } finally {
                        isChangingHint = false
                    }
                }
            }
        }
    }

    private fun setListeners() {
        homeMotionLayout.setTransitionListener(this)
    }

    private fun handleConversionException(ex: Exception) {
        when (ex) {
            is IllegalStateException -> Log.e(
                HOME_FRAGMENT_TAG,
                "currency was not found during conversion: ${ex.message}"
            )
            else -> Log.d(HOME_FRAGMENT_TAG, "ex: ${ex.message}")
        }
    }

    private fun rotateSwapIcon() {
        exchangeObjectAnimator.start()
    }

    private fun createMenu() {
        val popUpMenu = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PopupMenu(requireContext(), binding.homeAppBarLayout.currencyTextField, Gravity.END)
        } else {
            PopupMenu(requireContext(), binding.homeAppBarLayout.currencyTextField)
        }
        popUpMenu.apply {
            menuInflater.inflate(R.menu.btc_menu, menu)
            menu.children.find { homeViewModel.selectedCurrencyCode.value == it.title }
                ?.isChecked =
                true
            setOnMenuItemClickListener(this@HomeFragment)
            show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        homeViewModel.savedStateHandle.apply {
            set(
                HomeViewModel.TO_BITCOIN_EDIT_TEXT,
                binding.homeAppBarLayout.currencyTextField.editText?.text.toString()
            )
            set(
                HomeViewModel.FROM_BITCOIN_EDIT_TEXT,
                binding.homeAppBarLayout.bitcoinTextField.editText?.text.toString()
            )
            set(
                HomeViewModel.IS_BITCOIN_EDIT_FOCUSED,
                binding.homeAppBarLayout.bitcoinTextField.editText?.isFocused
            )
            set(
                HomeViewModel.IS_CURRENCY_EDIT_FOCUSED,
                binding.homeAppBarLayout.currencyTextField.editText?.isFocused
            )
        }
    }

    private fun showInfoDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.what_is_bitcoin))
            .setMessage(getString(R.string.about_bitcoin))
            .show()
    }

    override fun onTimeUpdate(lastUpdated: CharSequence?) {
        Log.d(HOME_FRAGMENT_TAG, "TIME: $lastUpdated")
        binding.homeAppBarLayout.timeUpdated.text = lastUpdated
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val selected = when (item?.itemId) {
            R.id.eur -> "EUR"
            R.id.usd -> "USD"
            R.id.gbp -> "GBP"
            else -> null
        }
        homeViewModel.savedStateHandle.set(HomeViewModel.SELECTED_CURRENCY_CODE, selected)
        return false
    }

    private fun enableOrDisableHiddenViews(isEnabled: Boolean) {
        binding.homeAppBarLayout.apply {
            currencyTextField.isEnabled = isEnabled
            bitcoinTextField.isEnabled = isEnabled
            backText.isEnabled = isEnabled
            exchangeButton.isEnabled = isEnabled
        }
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        binding.apply {
            underListView.translationY = -underListView.height * p3
            bpiList.translationY = ScreenUnit.convertDpToPixel(20f) * p3
        }
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        val isEnabled = p1 == R.id.end
        enableOrDisableHiddenViews(isEnabled)
        homeViewModel.savedStateHandle.set(HomeViewModel.MOTION_TRANSITION_ID, p1)
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        Log.i(HOME_FRAGMENT_TAG, "transition_started")
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        Log.i(HOME_FRAGMENT_TAG, "transition_triggered")
    }

}