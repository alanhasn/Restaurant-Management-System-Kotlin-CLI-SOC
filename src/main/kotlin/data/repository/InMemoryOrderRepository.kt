package data.repository

// Dependencies
import domain.models.Order
import domain.models.utils.OrderStatus
import java.util.UUID // For Order ID generation

class InMemoryOrderRepository: OrderRepository {

    // List of orders in memory
    private val orders = mutableMapOf<String , Order>()

    /**
     * Save a new order and return the saved order
     * @param order: The order to be saved
     * @return The saved order
     */
    override fun save(order: Order): Order {
        val id = order.id.ifBlank { UUID.randomUUID().toString() }
        val orderWithId = order.copy(id=id)
        orders[id] = orderWithId
        return orderWithId
    }

    /**
     * Find an order by its ID
     * @param id: The ID of the order to find
     * @return The found order or null if not found
     */
    override fun findById(id: String): Order? {
        return orders[id]
    }

    /**
     * Find orders by their status
     * @param status: The status of the orders to find
     * @return A list of orders with the specified status
     */
    override fun findByStatus(status: OrderStatus): List<Order> {
        return orders.values.filter { it.status == status }
    }

    /**
     * Find orders by their customer ID
     * @param customerId: The ID of the customer to find orders for
     * @return A list of orders for the specified customer
     */
    override fun findByCustomer(customerId: String): List<Order> {
        return orders.values.filter { it.customerId == customerId }
    }

    /**
     * Find orders by their table ID
     * @param tableId: The ID of the table to find orders for
     * @return A list of orders for the specified table
     */
    override fun findByTable(tableId: String): List<Order> {
        return orders.values.filter { it.tableId == tableId }
    }

    /**
     * Find all orders
     * @return A list of all orders
     */
    override fun findAll(): List<Order> {
        return orders.values.toList()
    }

    /**
     * Update an existing order
     * @param order: The order to update
     * @return True if the order was updated, false otherwise
     */
    override fun update(order: Order): Boolean {
        val id = order.id
        if (!orders.containsKey(id)){
            return false
        }
        orders[id] = order
        return true
    }

    /**
     * Delete an order by its ID
     * @param id: The ID of the order to delete
     * @return True if the order was deleted, false otherwise
     */
    override fun delete(id: String): Boolean {
        return orders.remove(id) != null
    }
}