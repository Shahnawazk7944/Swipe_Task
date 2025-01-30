package dev.swipe_task.presentation.features.utils

import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductStates


data class ValidationError(val message: String)

object Validator {

    fun validateProductName(productName: String): ValidationError? {
        if (productName.isBlank()) {
            return ValidationError(
                "Product name cannot be empty"
            )
        }
        if (productName.length < 3) {
            return ValidationError(
                "Product name must be at least 3 characters long"
            )
        }
        if (productName.any { it.isDigit() }) {
            return ValidationError(
                "Product name should not contain any digits"
            )
        }
        return null
    }

    fun validateProductType(productType: String): ValidationError? {
        if (productType.isBlank()) {
            return ValidationError(
                "Product Type cannot be empty"
            )

        }
        if (productType.length < 3) {
            return ValidationError(
                "Product Type must be at least 3 characters long"
            )
        }
        if (productType.any { it.isDigit() }) {
            return ValidationError(
                "Product Type should not contain any digits"
            )
        }
        return null
    }

    fun validatePrice(price: String): ValidationError? {
        if (price.isBlank()) {
            return ValidationError("Price cannot be empty")
        }
        if (!price.matches(Regex("^[0-9]+(\\.[0-9]+)?$"))) {
            return ValidationError("Price must be a valid decimal number")
        }
        return null
    }

    fun validateTax(tax: String): ValidationError? {
        if (tax.isBlank()) {
            return ValidationError("Tax cannot be empty")
        }
        if (!tax.matches(Regex("^[0-9]+(\\.[0-9]+)?$"))) {
            return ValidationError("Tax must be a valid decimal number")
        }
        return null
    }
}

fun AddProductStates.isAddProductFormValid(): Boolean {
    return productNameError == null &&
            productTypeError == null &&
            priceError == null &&
            taxError == null && productName.isNotBlank() && productType.isNotBlank() && price.isNotBlank() && tax.isNotBlank()
}