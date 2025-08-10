package domain.models.utils

// enum class for order status
enum class OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    READY_TO_SERVE,
    SERVED,
    CANCELLED,
    COMPLETED
}
