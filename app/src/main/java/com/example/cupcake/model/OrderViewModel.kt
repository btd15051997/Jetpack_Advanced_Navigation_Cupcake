package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class OrderViewModel : ViewModel() {

    private val _quantity = MutableLiveData<Int>()
    val quantiy: LiveData<Int>
        get() = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String>
        get() = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    /**
     *  You will use Transformations.map() method to format the price to use the local currency.
     *  You'll transform the original price as a decimal value (LiveData<Double>) into a string value (LiveData<String>).
     * */
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }


    val dateOptions get() = getPickupOptions()

    init {
        resetOrder()
    }

    fun resetOrder() {
        _quantity.value = INT_DEFAULT
        _flavor.value = STRING_EMPTY
        _price.value = FLOAT_DEFAULT
        _date.value = dateOptions[INT_DEFAULT]
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    private fun updatePrice() {
        var calculatedPrice = (quantiy.value ?: 0) * PRICE_PER_CUPCAKE
        if(_date.value == dateOptions[0]) calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        _price.value = calculatedPrice
    }

    // return list data buy items
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatDate = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calender = Calendar.getInstance()
        repeat(4) {
            options.add(formatDate.format(calender.time))
            calender.add(Calendar.DATE, 1)
        }
        return options
    }

    companion object {
        private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00
        private const val PRICE_PER_CUPCAKE = 2.00
        private const val STRING_EMPTY = ""
        private const val INT_DEFAULT = 0
        private const val FLOAT_DEFAULT = 0.0
    }
}