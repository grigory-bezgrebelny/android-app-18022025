package com.example.myapplication.ui

import android.os.Bundle
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.toDp
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ViewDropdownBinding
import com.example.myapplication.entity.Gender
import com.example.myapplication.repo.GenderRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val AGE_PLACEHOLDER_TEXT ="_ _"
    }

    @Inject
    lateinit var genderRepo: GenderRepo

    @Suppress("UNCHECKED_CAST")
    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(genderRepo) as T
            }
        }
    }

    private var activityMainBinding: ActivityMainBinding? = null
    private var adapter: AgeAdapter? = AgeAdapter()
    private var popup: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupView(binding)
        bindView(binding)
        bindEvent()

        activityMainBinding = binding
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
        activityMainBinding = null
        popup = null
    }

    private fun setupView(binding: ActivityMainBinding) {
        binding.btnMale.setOnClickListener {
            viewModel.maleClicked()
        }

        binding.btnFemale.setOnClickListener {
            viewModel.femaleClicked()
        }

        binding.clAge.setOnClickListener {
            showPopupWindow()
        }

        binding.btnNext.setOnClickListener {
             viewModel.nextClicked()
        }
    }

    private fun showPopupWindow() {
        val binding = activityMainBinding ?: return

        val popupBinding = ViewDropdownBinding.inflate(layoutInflater, binding.main, false)
        popup = PopupWindow(popupBinding.root, toDp(110), toDp(185), true)
        popupBinding.root.adapter = adapter
        val ll = popupBinding.root.layoutManager
        if (ll is LinearLayoutManager) {
            ll.scrollToPosition(viewModel.selectedAgePositionState.value)
        }
        popup?.showAsDropDown(binding.clAge)
        popup?.setOnDismissListener {
            popupBinding.root.adapter = null
        }
    }

    private fun bindView(binding: ActivityMainBinding) {
        lifecycleScope.launch {
            viewModel.selectedGenderState.collect {
                binding.btnMale.isSelected = it == Gender.MALE
            }
        }
        lifecycleScope.launch {
            viewModel.selectedGenderState.collect {
                binding.btnFemale.isSelected = it == Gender.FEMALE
            }
        }
        lifecycleScope.launch {
            viewModel.selectedAgeState.collect {
                binding.tvAge.text = it?.toString() ?: AGE_PLACEHOLDER_TEXT
            }
        }
        lifecycleScope.launch {
            viewModel.nextButtonEnabledState.collect {
                binding.btnNext.isEnabled = it
            }
        }

        lifecycleScope.launch {
            viewModel.ageListState.collect {
                adapter?.dataList = viewModel.ageListState.value
            }
        }
    }

    private fun bindEvent() {
        lifecycleScope.launch {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is MainViewModel.UiEvent.ShowMessage -> {
                        Toast
                            .makeText(this@MainActivity, event.value.asString(this@MainActivity), Toast.LENGTH_LONG)
                            .show()
                    }

                    MainViewModel.UiEvent.DismissPopup -> {
                        popup?.dismiss()
                    }
                }
            }
        }
    }
}
