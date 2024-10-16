import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asm_kot_ph45484.CartData
import com.example.asm_kot_ph45484.ProductData


class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartData>>(emptyList())
    val cartItems: LiveData<List<CartData>> = _cartItems

    // Thêm sản phẩm vào giỏ hàng
    fun addToCart(product: ProductData, quantity: Int) {
        if (product.id == null) {
            Log.e("CartViewModel", "Product ID is null. Cannot add to cart.")
            return
        }

        val currentList = _cartItems.value.orEmpty().toMutableList()
        val existingItem = currentList.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentList.add(CartData(product, quantity))
        }
        _cartItems.value = currentList // Cập nhật lại giá trị LiveData
        Log.e("CartViewModel", "Cart items updated: $currentList") // Log để kiểm tra danh sách giỏ hàng
    }

    // Xóa sản phẩm khỏi giỏ hàng
    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value?.filter { it.product.id.toString() != productId }
    }
    fun clearCart() {
        _cartItems.value = emptyList() // Cập nhật lại giỏ hàng thành danh sách rỗng
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    fun updateQuantity(productId: String, newQuantity: Int) {
        _cartItems.value = _cartItems.value?.mapNotNull { cartItem ->
            if (cartItem.product.id.toString() == productId) {
                if (newQuantity > 0) {
                    // Nếu số lượng > 0 thì cập nhật số lượng
                    cartItem.copy(quantity = newQuantity)
                } else {
                    // Nếu số lượng = 0 thì trả về null để loại bỏ sản phẩm khỏi giỏ hàng
                    null
                }
            } else {
                cartItem
            }
        }
    }
}
