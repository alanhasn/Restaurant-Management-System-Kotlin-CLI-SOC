package presentation.cli

// Dependencies
import domain.models.Order
import domain.models.utils.OrderStatus
import domain.models.utils.OrderType
import domain.services.MenuService
import domain.services.OrderService
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

/**
 * OrderView handles all order-related user interactions.
 * It communicates with the OrderService to perform operations.
 */
class OrderView(
    private val orderService: OrderService,
    private val menuItemService: MenuService,
    private val customerView: CustomerView,
    private val employeeView: EmployeeView
) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    /**
     * Displays the order management menu and handles user input.
     */
    fun showOrderMenu() {
        while (true) {
            println("\n=== Order Management ===")
            println("1. Create New Order")
            println("2. View All Orders")
            println("3. View Order Details")
            println("4. Update Order Status")
            println("5. Add Item to Order")
            println("6. Remove Item from Order")
            println("7. Back to Main Menu")
            print("\nChoose an option (1-7): ")

            when (readIntInRange(1..7)) {
                1 -> createOrder()
                2 -> listAllOrders()
                3 -> viewOrderDetails()
                4 -> updateOrderStatus()
                5 -> addItemToOrder()
                6 -> removeItemFromOrder()
                7 -> {
                    println("\nReturning to main menu...")
                    return
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    /**
     * Creates a new order by collecting user input.
     */
     fun createOrder() = runBlocking {
        println("\n=== Create New Order ===")

        // Get order type
        println("\nSelect order type:")
        OrderType.entries.forEachIndexed { index, type ->
            println("${index + 1}. ${type.name.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase() }}")
        }
        print("Choose order type (1-${OrderType.entries.size}): ")

        val orderType = try {
            val typeIndex = readIntInRange(1..OrderType.entries.size)
            OrderType.entries[typeIndex]
        } catch (e: Exception) {
            println("Invalid order type. Using default: DINE_IN")
            OrderType.DINE_IN
        }

        // Get table ID for dine-in orders
        val tableId = if (orderType == OrderType.DINE_IN) {
            readNonEmptyInput("Enter table ID: ", "Table ID cannot be empty for DINE-IN orders.")
        } else {
            null
        }

        customerView.listCustomers()
        // Get customer ID (optional)
        val customerId = readOptionalInput("Enter customer ID (optional): ")

        // Get employee ID (required)
        employeeView.listAllEmployees()
        val employeeId = readNonEmptyInput("Enter employee ID (who is taking the order): ", "Employee ID is required.")

        try {
            val orderId = orderService.createOrder(
                orderType = orderType,
                employeeId = employeeId,
                tableId = tableId,
                customerId = customerId
            )
            println("\nOrder created successfully! Order ID: $orderId")

            // Ask to add items to the order
            while (true) {
                print("\nAdd items to this order? (yes/no): ")
                when (readLine()?.trim()?.lowercase()) {
                    "yes", "y" -> addItemToOrder(orderId)
                    "no", "n" -> break
                    else -> println("Please answer 'yes' or 'no'.")
                }
            }
        } catch (e: Exception) {
            println("\nError creating order: ${e.message}")
        }
    }

    /**
     * Lists all orders with cleaner vertical formatting.
     */
    fun listAllOrders() = runBlocking {
        try {
            val orders = orderService.listAllOrders()

            if (orders.isEmpty()) {
                println("\nNo orders found.")
                return@runBlocking
            }

            println("\n=== Order List ===")
            println("-".repeat(40))

            orders.forEachIndexed { index, order ->
                val status = formatOrderStatus(order.status)
                val type = order.orderType.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
                val total = "$${order.totalAmount.setScale(2)}"
                val time = order.orderTime.format(dateTimeFormatter)

                println("Order #${index + 1}")
                println("ID     : ${order.id}")
                println("Status : $status")
                println("Type   : $type")
                println("Total  : $total")
                println("Time   : $time")
                println("-".repeat(40))
            }

            println("Total orders: ${orders.size}")
        } catch (e: Exception) {
            println("\nError fetching orders: ${e.message}")
        }
    }

    /**
     * Displays detailed information about a specific order.
     */
    fun viewOrderDetails() = runBlocking {
        val order = findOrder("Enter order ID: ") ?: return@runBlocking

        println("\n=== Order Details ===")
        println("Order ID   : ${order.id}")
        println("Order Time : ${order.orderTime.format(dateTimeFormatter)}")
        println("Status     : ${formatOrderStatus(order.status)}")
        println("Type       : ${order.orderType.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }}")

        order.tableId?.let { println("Table ID : $it") }
        order.customerId?.let { println("Customer ID : $it") }

        println("\n--- Order Items ---")
        if (order.orderItems.isEmpty()) {
            println("No items in this order.")
        } else {
            order.orderItems.forEachIndexed { index, item ->
                val total = item.unitPrice.multiply(BigDecimal(item.quantity))
                println("${index + 1}. Item ID   : ${item.menuItemId}")
                println("   Quantity : ${item.quantity}")
                println("   Price    : $${item.unitPrice}")
                println("   Total    : $${total.setScale(2)}")
                println("-".repeat(30))
            }
            println("Grand Total : $${order.totalAmount.setScale(2)}")
        }

        order.estimatedReadyTime?.let {
            println("\nEstimated Ready : ${it.format(dateTimeFormatter)}")
        }
        order.actualDeliveryTime?.let {
            println("Delivered At   : ${it.format(dateTimeFormatter)}")
        }
    }

    /**
     * Updates the status of an order.
     */
    private fun updateOrderStatus() = runBlocking {
        showOrders()
        val order = findOrder("Enter order ID to update: ") ?: return@runBlocking
        
        println("\nCurrent status: ${formatOrderStatus(order.status)}")
        println("\nAvailable statuses:")
        
        OrderStatus.entries.forEachIndexed { index, status ->
            println("${index + 1}. ${status.name.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase()}}")
        }
        
        print("\nSelect new status (1-${OrderStatus.entries.size}): ")
        
        try {
            val statusIndex = readIntInRange(1..OrderStatus.entries.size) - 1
            val newStatus = OrderStatus.entries[statusIndex]
            
            if (orderService.updateOrderStatus(order.id, newStatus)) {
                println("\n Order status updated successfully!")
                
                // If status is READY_TO_SERVE, update the estimated ready time
                if (newStatus == OrderStatus.READY_TO_SERVE) {
                    println("\nThe order is ready to be served!")
                }
            } else {
                println("\nFailed to update order status.")
            }
        } catch (e: Exception) {
            println("\nError updating order status: ${e.message}")
        }
    }

    /**
     * Adds an item to an existing order.
     */
    private fun addItemToOrder(orderId: String? = null) = runBlocking {
        val targetOrderId = orderId ?: run {
            findOrder("Enter order ID to add items to: ")?.id ?: return@runBlocking
        }
        // show menu items
        showMenuItems()
        print("\nEnter menu item ID: ")
        val menuItemId = readLine()?.trim() ?: return@runBlocking
        
        print("Enter quantity: ")
        val quantity = try {
            readLine()?.trim()?.toIntOrNull() ?: 1
        } catch (e: NumberFormatException) {
            println("Invalid quantity. Using default: 1")
            1
        }
        
        try {
            if (orderService.addItemToOrder(targetOrderId, menuItemId, quantity)) {
                println("\nItem added to order successfully!")
            } else {
                println("\nFailed to add item to order.")
            }
        } catch (e: Exception) {
            println("\nError adding item to order: ${e.message}")
        }
    }

    /**
     * Removes an item from an order.
     */
    private fun removeItemFromOrder() = runBlocking {
        showOrders()
        val order = findOrder("Enter order ID to remove items from: ") ?: return@runBlocking
        
        if (order.orderItems.isEmpty()) {
            println("\nThis order has no items to remove.")
            return@runBlocking
        }
        
        println("\nSelect item to remove:")
        order.orderItems.forEachIndexed { index, item ->
            println("${index + 1}. ${item.menuItemId} (Qty: ${item.quantity})")
        }
        
        print("\nEnter item number to remove (1-${order.orderItems.size}): ")
        val itemIndex = try {
            readIntInRange(1..order.orderItems.size) - 1
        } catch (e: Exception) {
            println("Invalid selection.")
            return@runBlocking
        }
        
        val itemToRemove = order.orderItems[itemIndex]
        
        try {
            if (orderService.removeItemFromOrder(order.id, itemToRemove.menuItemId)) {
                println("\nItem removed from order successfully!")
            } else {
                println("\nFailed to remove item from order.")
            }
        } catch (e: Exception) {
            println("\n Error removing item from order: ${e.message}")
        }
    }

    private fun showMenuItems() = runBlocking {
        val menuItems = menuItemService.listAllMenuItems()

        println("\n=== Menu Items ===")
        if (menuItems.isEmpty()) {
            println("No menu items found.")
            return@runBlocking
        }

        println("-".repeat(40))
        menuItems.forEachIndexed { index, menuItem ->
            println("Item #${index + 1}")
            println("ID    : ${menuItem.id}")
            println("Name  : ${menuItem.name}")
            println("Price : $${menuItem.price.setScale(2)}")
            println("-".repeat(40))
        }

        println("Total menu items: ${menuItems.size}")
    }

    fun showOrders() = runBlocking {
        val orders = orderService.listAllOrders()
        if (orders.isEmpty()) {
            println("\nNo orders found.")
            return@runBlocking
        }

        println("\n=== Orders ===")
        println("-".repeat(40))

        orders.forEachIndexed { index, order ->
            val status = formatOrderStatus(order.status)
            val type = order.orderType.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
            val total = "$${order.totalAmount.setScale(2)}"
            val time = order.orderTime.format(dateTimeFormatter)

            println("Order #${index + 1}")
            println("ID     : ${order.id}")
            println("Status : $status")
            println("Type   : $type")
            println("Total  : $total")
            println("Time   : $time")
            println("-".repeat(40))
        }

        println("Total orders: ${orders.size}")
    }
    /**
     * Helper function to find an order by ID.
     */
    private suspend fun findOrder(prompt: String): Order? {
        while (true) {
            print("\n$prompt")
            val orderId = readLine()?.trim() ?: ""

            if (orderId.isBlank()) {
                println("Operation cancelled.")
                return null
            }

            try {
                val order = orderService.getOrderById(orderId)
                if (order != null) return order

                println(" Order not found. Try again or press Enter to cancel.")
            } catch (e: Exception) {
                println("Error finding order: ${e.message}")
            }
        }
    }

    /**
     * Formats the order status with appropriate emoji.
     */
    private fun formatOrderStatus(status: OrderStatus): String {
        return when (status) {
            OrderStatus.PENDING -> "Pending"
            OrderStatus.CONFIRMED -> "Confirmed"
            OrderStatus.PREPARING -> "Preparing"
            OrderStatus.READY_TO_SERVE -> "Ready to Serve"
            OrderStatus.SERVED -> "Served"
            OrderStatus.CANCELLED -> "Cancelled"
            OrderStatus.COMPLETED -> "Completed"
        }
    }

    /**
     * Helper function to read an integer within a specified range from user input.
     */
    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            try {
                val input = readLine()?.trim()?.toInt()
                    ?: throw NumberFormatException("Input cannot be empty")
                
                if (input in range) return input
                println("Please enter a number between ${range.first} and ${range.last}.")
            } catch (e: NumberFormatException) {
                print("Invalid input. Please enter a valid number: ")
            }
        }
    }

    /**
     * Helper function to read non-empty input from user.
     */
    private fun readNonEmptyInput(prompt: String, errorMessage: String): String {
        while (true) {
            print("\n$prompt")
            val input = readLine()?.trim() ?: ""
            
            if (input.isNotBlank()) return input
            println(errorMessage)
        }
    }

    /**
     * Helper function to read optional input from user.
     */
    private fun readOptionalInput(prompt: String): String? {
        print("\n$prompt")
        return readLine()?.trim()?.takeIf { it.isNotBlank() }
    }
}