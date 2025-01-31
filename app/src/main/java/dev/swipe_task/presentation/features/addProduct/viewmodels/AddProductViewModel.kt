package dev.swipe_task.presentation.features.addProduct.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.data.repository.ProductRepository
import dev.models.AddProductRequest
import dev.swipe_task.presentation.features.utils.Validator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddProductStates())
    val state = _state.asStateFlow()

    fun addProductEvents(event: AddProductEvents) {
        when (event) {
            is AddProductEvents.AddProduct -> {
                addProduct(event.addProductsRequest)
            }

            AddProductEvents.ClearFailure -> {
                _state.update { it.copy(failure = null) }
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
                    Log.d("---- vmAdded","$addedProduct")
                }.onLeft { failure ->
                    Log.d("---- vmAdd","$failure")
                _state.update { it.copy(failure = failure, loading = false) }
            }
        }
    }

}
