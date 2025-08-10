package domain.services

// Dependencies
import data.repository.EmployeeRepository
import domain.models.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID

// Employee Service Implementation
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository // Inject the EmployeeRepository
) : EmployeeService {

    // Implement the EmployeeService methods

    // Implement the hireEmployee method
    override suspend fun hireEmployee(
        name: String,
        position: String,
        hireDate: LocalDate,
        salary: Double,
        contactNumber: String,
        email: String,
        address: String,
        emergencyContact: String?,
        dateOfBirth: String?,
        identificationNumber: String?,
        userId: String
    ): Boolean = withContext(Dispatchers.IO) {
        val id = UUID.randomUUID().toString()
        val newEmployee = Employee(
            id = id,
            userId = userId,
            name = name,
            position = position,
            hireDate = hireDate,
            salary = salary,
            contactNumber = contactNumber,
            email = email,
            address = address,
            emergencyContact = emergencyContact,
            dateOfBirth = dateOfBirth,
            identificationNumber = identificationNumber,
            isActive = true
        )
        delay(1000)
        return@withContext employeeRepository.save(newEmployee)
    }

    // Implement the updateEmployee method
    override suspend fun updateEmployee(
        id: String,
        name: String?,
        position: String?,
        salary: Double?,
        contactNumber: String?,
        email: String?,
        address: String?,
        emergencyContact: String?,
        isActive: Boolean?,
        dateOfBirth: LocalDate?,
        identificationNumber: String?
    ): Boolean =withContext(Dispatchers.IO) {
        val existingEmployee = employeeRepository.findById(id) ?: return@withContext false

        val updatedEmployee = existingEmployee.copy(
            name = name ?: existingEmployee.name,
            position = position ?: existingEmployee.position,
            salary = salary ?: existingEmployee.salary,
            contactNumber = contactNumber ?: existingEmployee.contactNumber,
            email = email ?: existingEmployee.email,
            address = address ?: existingEmployee.address,
            emergencyContact = emergencyContact ?: existingEmployee.emergencyContact,
            isActive = isActive ?: existingEmployee.isActive,
            dateOfBirth = (dateOfBirth ?: existingEmployee.dateOfBirth) as String?,
            identificationNumber = identificationNumber ?: existingEmployee.identificationNumber
        )
        delay(1000)
        return@withContext employeeRepository.update(updatedEmployee).isSuccess
    }

    // Implement the fireEmployee method
    override suspend fun fireEmployee(id: String): Boolean = withContext(Dispatchers.IO) {
        val existingEmployee = employeeRepository.findById(id) ?: return@withContext false
        val disabledEmployee = existingEmployee.copy(isActive = false)
        delay(1000)
        return@withContext employeeRepository.update(disabledEmployee).isSuccess
    }

    // Implement the getEmployeeById method
    override suspend fun getEmployeeById(id: String): Employee? {
        return employeeRepository.findById(id)
    }

    // Implement the listAllEmployees method
    override suspend fun listAllEmployees(): List<Employee> {
        delay(1000)
        return employeeRepository.findAll()
    }
}
