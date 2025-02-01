package dev.swipe_task.presentation.features.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.data.repository.ConnectivityRepository
import dev.data.repository.ProductRepository
import dev.models.AddProductRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val connectivityRepository: ConnectivityRepository,
    private val repository: ProductRepository
) : ViewModel() {

    private val _localUploadSuccessMessage = MutableStateFlow<String?>(null)
    val localUploadSuccessMessage: StateFlow<String?> = _localUploadSuccessMessage.asStateFlow()

    private val _isConnected = MutableSharedFlow<Boolean>(replay = 1)
    val isConnected: SharedFlow<Boolean> = _isConnected.asSharedFlow()

    init {
        viewModelScope.launch {
            connectivityRepository.isConnected.collect { isConnected ->
                _isConnected.emit(isConnected)
            }
        }

        viewModelScope.launch {
            isConnected.collectLatest { isConnected ->
                if (isConnected) {
                    addProductToLocalDb()
                }
            }
        }
    }

    private fun addProductToLocalDb() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllProducts().collect { products ->
                if (products.isNotEmpty()) {
                    products.forEach { product ->
                        addProduct(product)
                    }
                    repository.deleteAllProducts()
                    _localUploadSuccessMessage.value = "All local products uploaded successfully"
                    delay(500)
                    _localUploadSuccessMessage.value = null
                }
            }
        }
    }

    private fun addProduct(addProductRequest: AddProductRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProductRequest(addProductRequest = addProductRequest)
                .onRight { addedProduct ->

                }.onLeft { failure ->

                }
        }
    }
}