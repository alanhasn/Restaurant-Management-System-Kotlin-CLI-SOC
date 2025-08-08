package domain.services

import data.repository.EmployeeRepository
import domain.models.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID

class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository
) : EmployeeService {

    override suspend fun hireEmployee(
        name: String,
        position: String,
        hireDate: LocalDate,
        salary: Double,
        contactNumber: String,
        email: String,
        address: String,
        emergencyContact: String?,
        dateOfBirth: LocalDate?,
        identificationNumber: String?,
        userId: String // نفترض أن هناك علاقة بين الموظف والمستخدم (User)
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
            dateOfBirth = dateOfBirth ?: existingEmployee.dateOfBirth,
            identificationNumber = identificationNumber ?: existingEmployee.identificationNumber
        )
        delay(1000)
        return@withContext employeeRepository.update(updatedEmployee).isSuccess
    }

    override suspend fun fireEmployee(id: String): Boolean = withContext(Dispatchers.IO) {
        val existingEmployee = employeeRepository.findById(id) ?: return@withContext false
        val disabledEmployee = existingEmployee.copy(isActive = false)
        delay(1000)
        return@withContext employeeRepository.update(disabledEmployee).isSuccess
    }

    override suspend fun getEmployeeById(id: String): Employee? {
        return employeeRepository.findById(id)
    }

    override suspend fun listAllEmployees(): List<Employee> {
        delay(1000)
        return employeeRepository.findAll()
    }
}
