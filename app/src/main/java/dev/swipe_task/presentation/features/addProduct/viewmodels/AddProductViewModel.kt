package dev.swipe_task.presentation.features.addProduct.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.data.repository.ConnectivityRepository
import dev.data.repository.ProductRepository
import dev.models.AddProductRequest
import dev.models.ErrorResponse
import dev.swipe_task.presentation.features.utils.Validator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddProductStates())
    val state = _state.asStateFlow()

    val isConnected = connectivityRepository
        .isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    init {
        viewModelScope.launch {
            isConnected
                .collectLatest { isOnline ->
                    _state.update { it.copy(isOnline = isOnline) }
                    Log.d("---- isOnline", "$isOnline")
                }
        }
    }

    fun addProductEvents(event: AddProductEvents) {
        when (event) {
            is AddProductEvents.AddProduct -> {
                if (state.value.isOnline) {
                    addProduct(event.addProductsRequest)
                } else {
                    addProductToLocalDb(event.addProductsRequest)
                }
            }

            AddProductEvents.ClearFailure -> {
                _state.update { it.copy(failure = null, isProductRequestAddedToLocalDb = null) }
            }

            is AddProductEvents.ProductNameChanged -> {
                val error = Validator.validateProductName(event.productName)
                _state.update {
                    it.copy(
                        productName = event.productName,
                        productNameError = error?.message
                    )
                }
            }

            is AddProductEvents.ProductTypeChanged -> {
                val error = Validator.validateProductType(event.productType)
                _state.update {
                    it.copy(
                        productType = event.productType,
                        productTypeError = error?.message
                    )
                }
            }

            is AddProductEvents.PriceChanged -> {
                val error = Validator.validatePrice(event.price)
                _state.update {
                    it.copy(
                        price = event.price,
                        priceError = error?.message
                    )
                }
            }

            is AddProductEvents.TaxChanged -> {
                val error = Validator.validateTax(event.tax)
                _state.update {
                    it.copy(
                        tax = event.tax,
                        taxError = error?.message
                    )
                }
            }

            is AddProductEvents.ProductImagesChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        productImages = currentState.productImages + event.productImage
                    )
                }
            }

            is AddProductEvents.ProductImageBitmapChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        imageBitmaps = currentState.imageBitmaps + event.productImageBitmaps
                    )
                }
            }

            AddProductEvents.ClearAddedProduct -> {
                _state.update { it.copy(addedProduct = null) }
            }
        }
    }

    private fun addProduct(addProductRequest: AddProductRequest) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProductRequest(addProductRequest = addProductRequest)
                .onRight { addedProduct ->
                    _state.update {
                        it.copy(
                            addedProduct = addedProduct,
                            loading = false
                        )
                    }.also {
                        _state.update {
                            it.copy(
                                productName = "", productNameError = null,
                                productType = "", productTypeError = null,
                                price = "", priceError = null,
                                tax = "", taxError = null,
                                productImages = emptyList(), imageBitmaps = emptyList()
                            )
                        }
                    }
                    Log.d("---- vmAdded", "$addedProduct")
                }.onLeft { failure ->
                    Log.d("---- vmAdd", "$failure")
                    _state.update { it.copy(failure = failure, loading = false) }
                }
        }
    }

    private fun addProductToLocalDb(addProductRequest: AddProductRequest) {
        _state.update { it.copy(loading = true, isProductRequestAddedToLocalDb = null) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProduct(addProductRequest = addProductRequest)
                .onRight {
                    delay(1000)
                    _state.update {
                        it.copy(
                            isProductRequestAddedToLocalDb = "You're Offline! Product added to local db",
                            loading = false
                        )
                    }.also {
                        _state.update {
                            it.copy(
                                productName = "", productNameError = null,
                                productType = "", productTypeError = null,
                                price = "", priceError = null,
                                tax = "", taxError = null,
                                productImages = emptyList(), imageBitmaps = emptyList()
                            )
                        }
                    }
                    Log.d("---- vmAdded", "woo")
                }.onLeft {
                    _state.update {
                        it.copy(
                            loading = false,
                            failure = ErrorResponse(error = "Something went wrong, try again later")
                        )
                    }
                    Log.d("---- vmFailure", "${state.value.isProductRequestAddedToLocalDb}")
                }
        }
    }

}
