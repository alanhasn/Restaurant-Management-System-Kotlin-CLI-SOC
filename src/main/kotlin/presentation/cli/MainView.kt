package presentation.cli

// Dependencies
import data.repository.*
import domain.models.User
import domain.models.utils.UserRole
import domain.services.*

/**
 * MainView serves as the entry point and coordinator for the presentation layer.
 * It initializes all necessary services and manages the navigation between different views.
 */
object MainView {

    // Data Layer - In-memory repositories
    private val userRepository = InMemoryUserRepository()
    private val customerRepository = InMemoryCustomerRepository()
    private val orderRepository = InMemoryOrderRepository()
    private val menuItemRepository = InMemoryMenuItemRepository()
    private val tableRepository = InMemoryTableRepository()
    private val paymentRepository = InMemoryPaymentRepository()
    private val employeeRepository = InMemoryEmployeeRepository()

    // Service Layer - Service implementations
    private val authService: AuthService = AuthServiceImpl(userRepository)
    private val customerService: CustomerService = CustomerServiceImpl(customerRepository)
    private val menuService: MenuService = MenuServiceImpl(menuItemRepository)
    private val tableService: TableService = TableServiceImpl(tableRepository)
    private val orderService: OrderService = OrderServiceImpl(orderRepository, menuService)
    private val paymentService: PaymentService = PaymentServiceImpl(paymentRepository)
    private val employeeService: EmployeeService = EmployeeServiceImpl(employeeRepository)

    // Presentation Layer - Views
    private val authView = AuthView(authService, customerService)
    private val customerView = CustomerView(customerService)
    private val employeeView = EmployeeView(employeeService)
    private val orderView = OrderView(orderService, menuService, customerView, employeeView)
    private val menuView = MenuView(menuService)
    private val tableView = TableView(tableService)
    private val paymentView = PaymentView(paymentService, orderView)

    /**
     * Starts the application by showing the authentication screen.
     */
    fun start() {
        println("\n======= Welcome to Flavor Hive ==========")
        runAuthenticationFlow()
    }

    /**
     * Manages the main application loop, starting with authentication and directing to role-based menus.
     */
    private fun runAuthenticationFlow() {
        while (true) {
            val user = authView.showAuthMenu()
            if (user == null) { // User chose to exit
                return
            }

            // Navigate to the appropriate menu based on the user's role
            when (user.role) {
                UserRole.ADMIN -> showAdminMenu(user)
                UserRole.EMPLOYEE -> showStaffMenu(user)
                UserRole.CUSTOMER -> showCustomerMenu(user)
            }
        }
    }

    /**
     * Displays the admin menu with all available administrative options.
     */
    private fun showAdminMenu(user: User) {
        println("\nWelcome, Admin ${user.username}!")
        while (true) {
            println("\n=== Admin Dashboard ===")
            println("1. Manage Employees")
            println("2. Manage Customers")
            println("3. Manage Orders")
            println("4. Manage Menu")
            println("5. Manage Tables")
            println("6. Manage Payments")
            println("7. Logout")
            // Prompt user to choose an option
            when (readIntInRange(1..7)) {
                1 -> employeeView.showEmployeeMenu()
                2 -> customerView.showCustomerMenu()
                3 -> orderView.showOrderMenu()
                4 -> menuView.showMenu()
                5 -> tableView.showTableMenu()
                6 -> paymentView.showPaymentMenu()
                7 -> {
                    println("\nLogging out...")
                    return
                }
            }
        }
    }

    /**
     * Displays the staff menu with order and table management options.
     */
    private fun showStaffMenu(user: User) {
        println("\nWelcome, ${user.username}!")
        while (true) {
            println("\n=== Staff Dashboard ===")
            println("1. Create New Order")
            println("2. View All Orders")
            println("3. Update Order Status")
            println("4. Manage Tables")
            println("5. Logout")
            // Prompt user to choose an option
            when (readIntInRange(1..5)) {
                1 -> orderView.createOrder()
                2 -> orderView.showOrderMenu()
                3 -> orderView.listAllOrders()
                4 -> tableView.showTableMenu()
                5 -> {
                    println("\nLogging out...")
                    return
                }
            }
        }
    }

    /**
     * Displays the customer menu for self-service options.
     */
    private fun showCustomerMenu(user: User) {
        println("\nWelcome, ${user.username}!")
        val customerSelfServiceView = CustomerSelfServiceView(customerService, orderService, user, menuView, orderView, tableView , paymentView)
        customerSelfServiceView.showSelfServiceMenu()
    }

    /**
     * Helper function to read an integer within a specified range from user input.
     */
    private fun readIntInRange(range: IntRange): Int {
        while (true) {
            print("Enter your choice: ")
            try {
                val input = readLine()?.trim()?.toInt() ?: throw NumberFormatException()
                if (input in range) return input
                println("Please enter a number between ${range.first} and ${range.last}.")
            } catch (e: NumberFormatException) {
                println("Invalid input. Please enter a valid number.")
            }
        }
    }
}