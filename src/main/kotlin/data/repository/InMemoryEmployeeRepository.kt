package data.repository

// Dependencies
import domain.models.Employee
import java.time.LocalDate // For LocalDate
import java.util.UUID // For Employee ID generation
import kotlin.collections.set

class InMemoryEmployeeRepository: EmployeeRepository{

    // In-memory storage for employees
    private val employees = mutableMapOf<String , Employee>()

    init {
        // Initialize the in-memory storage
        val employee1 = Employee(
            UUID.randomUUID().toString(),
            "1234567890",
            "Alan",
            "Manager",
            LocalDate.of(2000, 1, 1),
            2323.0,
            "00003333",
            "whoma@example.com",
            "qamishlo",
            )
        employees[employee1.id] = employee1

        val employee2 = Employee(
            UUID.randomUUID().toString(),
            "1234567890",
            "John",
            "Manager",
            LocalDate.of(2000, 1, 1),
            2323.0,
            "00003333",
            "whoma@example.com",
            "qamishlo",
        )
        employees[employee2.id] = employee2

        val employee3 = Employee(
            UUID.randomUUID().toString(),
            "1234567890",
            "Jane",
            "Manager",
            LocalDate.of(2000, 1, 1),
            2323.0,
            "00003333",
            "whoma@example.com",
            "qamishlo",
        )

        employees[employee3.id] = employee3
    }

    /**
     * Save an employee to the in-memory storage
     * @param employee The employee to be saved
     * @return true if the employee was saved successfully
     */
    override fun save(employee: Employee): Boolean {
        val id = employee.id.ifBlank { UUID.randomUUID().toString() }

        val employeeWithId = employee.copy(id =id)

        employees[id] = employeeWithId
        return true
    }

    /**
     * Find an employee by their ID
     * @param id The ID of the employee to find
     * @return The employee with the specified ID, or null if not found
     */
    override fun findById(id: String): Employee? {
        return employees[id]
    }

    /**
     * Find an employee by their user ID
     * @param userId The user ID of the employee to find
     * @return The employee with the specified user ID, or null if not found
     */
    override fun findByUserId(userId: String): Employee? {
        return employees.values.firstOrNull{it.userId == userId}
    }

    /**
     * Find all employees
     * @return A list of all employees
     */
    override fun findAll(): List<Employee> {
        return employees.values.toList()
    }

    /**
     * Update Employee
     * @return A Result<Employee>
     */
    override fun update(employee: Employee): Result<Employee> {
        val id=employee.id

        if (!employees.containsKey(id)){
            return Result.failure(IllegalArgumentException("No employee found with id: $id"))
        }
        employees[id] = employee
        return Result.success(employee)
    }

    /**
     * Delete an employee by their ID
     * @param id The ID of the employee to delete
     * @return true if the employee was deleted successfully
     */
    override fun delete(id: String): Boolean {
        return employees.remove(id) != null
    }

    /**
     * Find employees by their position
     * @param position The position of the employees to find
     * @return A list of employees with the specified position
     */
    override fun findByPosition(position: String): List<Employee> {
        return employees.values.filter { it.position == position }

    }

    /**
     * Find an employee by their contact number
     * @param contactNumber The contact number of the employee to find
     * @return The employee with the specified contact number, or null if not found
     */
    override fun findByContactNumber(contactNumber: String): Employee? {
        return employees.values.firstOrNull { it.contactNumber == contactNumber }
    }

    /**
     * Find an employee by their email
     * @param email The email of the employee to find
     * @return The employee with the specified email, or null if not found
     */
    override fun findByEmail(email: String): Employee? {
        return employees.values.firstOrNull { it.email == email }
    }

    /**
     * Find an employee by their identification number
     * @param idNumber The identification number of the employee to find
     * @return The employee with the specified identification number, or null if not found
     */
    override fun findByIdentificationNumber(idNumber: String): Employee? {
        return employees.values.firstOrNull { it.identificationNumber == idNumber }
    }

    /**
     * Find employees by their active status
     * @param isActive The active status of the employees to find
     * @return A list of employees with the specified active status
     */
    override fun findByIsActive(isActive: Boolean): List<Employee> {
        return employees.values.filter { it.isActive == isActive }
    }

    /**
     * Find employees by their name
     * @param name The name of the employees to find
     * @return A list of employees with the specified name
     */
    override fun findByName(name: String): List<Employee> {
        return employees.values.filter { it.name.equals(name, ignoreCase = true) }
    }

    /**
     * Find employees by their date of birth
     * @param dob The date of birth of the employees to find
     * @return A list of employees with the specified date of birth
     */
    override fun findByDateOfBirth(dob: LocalDate): List<Employee> {
        return employees.values.filter { false }
    }

    /**
     * Find employees by their hire date
     * @param hireDate The hire date of the employees to find
     * @return A list of employees with the specified hire date
     */
    override fun findByHireDate(hireDate: LocalDate): List<Employee> {
        return employees.values.filter { it.hireDate == hireDate }
    }


}