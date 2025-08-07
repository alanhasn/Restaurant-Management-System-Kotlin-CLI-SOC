package data.repository

import domain.models.Order
import domain.models.utils.OrderStatus
import java.time.LocalDateTime
import java.util.UUID

class InMemoryOrderRepository: OrderRepository {
    private val orders = mutableMapOf<String , Order>()

    override fun save(order: Order): Order {
        val id = order.id.ifBlank { UUID.randomUUID().toString() }
        val orderWithId = order.copy(id=id)
        orders[id] = orderWithId
        return orderWithId
    }

    override fun findById(id: String): Order? {
        return orders[id]
    }

    override fun findByStatus(status: OrderStatus): List<Order> {
        return orders.values.filter { it.status == status }
    }

    override fun findByCustomer(customerId: String): List<Order> {
        return orders.values.filter { it.customerId == customerId }
    }


    override fun findByTable(tableId: String): List<Order> {
        return orders.values.filter { it.tableId == tableId }
    }

    override fun update(order: Order): Result<Order> {
        val id = order.id
        if (!orders.containsKey(id)){
            return Result.failure(IllegalArgumentException("No order found with id: $id"))
        }
        orders[id] = order
        return Result.success(order)
    }

    override fun delete(id: String): Boolean {
        return orders.remove(id) != null
    }
}