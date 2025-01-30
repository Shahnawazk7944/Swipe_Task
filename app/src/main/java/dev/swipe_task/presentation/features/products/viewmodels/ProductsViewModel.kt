package dev.swipe_task.presentation.features.products.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductsStates())
    val state = _state.asStateFlow()

    fun productsEvents(event: ProductsEvents) {
        when (event) {
            ProductsEvents.GetProducts -> {
                getProducts()
            }

            ProductsEvents.ClearFailure -> {
                _state.update { it.copy(failure = null) }
            }
        }
    }

    private fun getProducts() {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProducts()
                .onRight { products ->
                    _state.update {
                        it.copy(
                            products = products,
                            loading = false
                        )
                    }
                }.onLeft { failure ->

                    _state.update { it.copy(failure = failure, loading = false) }
                }
        }
    }

}
