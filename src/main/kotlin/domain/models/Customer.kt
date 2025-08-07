package domain.models

data class Customer(
    val id: String,
    val name: String,
    val email: String? = null,
    val phone: String,
    val address: String? = null,
    val notes: String? = null,
    val isActive: Boolean = true
)
