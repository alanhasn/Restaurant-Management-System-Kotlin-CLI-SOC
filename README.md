# Restaurant Management System

A Kotlin-based restaurant management system that handles table reservations, order processing, menu management, and payment processing.

## Project Structure

```
│
├── src/
│   ├── data/
│   │   └── repository/
│   │       ├── UserRepository.kt                → interface
│   │       ├── InMemoryUserRepository.kt        → class
│   │       ├── CustomerRepository.kt            → interface
│   │       ├── InMemoryCustomerRepository.kt    → class
│   │       ├── EmployeeRepository.kt            → interface
│   │       ├── InMemoryEmployeeRepository.kt    → class
│   │       ├── TableRepository.kt               → interface
│   │       ├── InMemoryTableRepository.kt       → class
│   │       ├── MenuItemRepository.kt            → interface
│   │       ├── InMemoryMenuItemRepository.kt    → class
│   │       ├── OrderRepository.kt               → interface
│   │       ├── InMemoryOrderRepository.kt       → class
│   │       ├── OrderItemRepository.kt           → interface
│   │       ├── InMemoryOrderItemRepository.kt   → class
│   │       ├── PaymentRepository.kt             → interface
│   │       └── InMemoryPaymentRepository.kt     → class
│
│   ├── domain/
│   │   ├── models/
│   │   │   ├── User.kt                          → data class
│   │   │   ├── Customer.kt                      → data class
│   │   │   ├── Employee.kt                      → data class
│   │   │   ├── Table.kt                         → data class
│   │   │   ├── MenuItem.kt                      → data class
│   │   │   ├── Order.kt                         → data class
│   │   │   ├── OrderItem.kt                     → data class
│   │   │   └── Payment.kt                       → data class
│   │
│   │   └── services/
│   │       ├── AuthService.kt                   → interface
│   │       ├── AuthServiceImpl.kt               → class
│   │       ├── CustomerService.kt               → interface
│   │       ├── CustomerServiceImpl.kt           → class
│   │       ├── EmployeeService.kt               → interface
│   │       ├── EmployeeServiceImpl.kt           → class
│   │       ├── MenuService.kt                   → interface
│   │       ├── MenuServiceImpl.kt               → class
│   │       ├── OrderService.kt                  → interface
│   │       ├── OrderServiceImpl.kt              → class
│   │       ├── PaymentService.kt                → interface
│   │       ├── PaymentServiceImpl.kt            → class
│   │       ├── TableService.kt                  → interface
│   │       └── TableServiceImpl.kt              → class
│
│   ├── presentation/
│   │   ├── MainMenu.kt                          → class
│   │   ├── AuthView.kt                          → class
│   │   ├── CustomerView.kt                      → class
│   │   ├── EmployeeView.kt                      → class
│   │   ├── MenuView.kt                          → class
│   │   ├── OrderView.kt                         → class
│   │   ├── PaymentView.kt                       → class
│   │   └── TableView.kt                         → class
│
│   ├── utils/
│   │   ├── InputHelper.kt                       → object
│   │   ├── Logger.kt                            → object
│   │   └── Session.kt                           → object
│
│   └── Main.kt                                  → main function
│
├── build.gradle.kts                             → Gradle config
├── settings.gradle.kts                          → Project settings
└── README.md                                    → Markdown documentation

```

## Features

- **User Management**: Authentication and authorization for different user roles (Admin, Manager, Waiter, Chef, etc.)
- **Table Management**: Track table status and capacity
- **Menu Management**: CRUD operations for menu items with categories
- **Order Processing**: Create, update, and track orders
- **Payment Processing**: Handle payments and refunds
- **Reporting**: Generate reports for sales and inventory

## Getting Started

### Prerequisites

- JDK 17 or higher
- Gradle 7.0 or higher
- Kotlin 1.6.0 or higher

### Building the Project

```bash
# Clone the repository
git clone <repository-url>
cd restaurant-system

# Build the project
./gradlew build

# Run tests
./gradlew test
```

### Running the Application

```bash
# Run the application
./gradlew run
```

## Architecture

The application follows a clean architecture pattern with the following layers:

1. **Data Layer**: Handles data access and persistence
   - Repositories for each domain entity
   - In-memory implementations (can be replaced with database implementations)

2. **Domain Layer**: Contains business logic
   - Domain models (data classes)
   - Service interfaces and implementations
   - Business rules and validations

3. **Presentation Layer**: Handles user interaction
   - CLI interface (initial implementation)
   - Web interface (future)

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Built with Kotlin
- Uses Gradle for build automation
- Follows Clean Architecture principles
