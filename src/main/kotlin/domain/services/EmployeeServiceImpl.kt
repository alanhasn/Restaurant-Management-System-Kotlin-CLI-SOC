package domain.services

import data.repository.EmployeeRepository
import domain.models.Employee
import java.time.LocalDate
import java.util.UUID

class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository
) : EmployeeService {

    override fun hireEmployee(
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
    ): Boolean {
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
        return employeeRepository.save(newEmployee)
    }

    override fun updateEmployee(
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
    ): Boolean {
        val existingEmployee = employeeRepository.findById(id) ?: return false

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

        return employeeRepository.update(updatedEmployee).isSuccess
    }

    override fun fireEmployee(id: String): Boolean {
        val existingEmployee = employeeRepository.findById(id) ?: return false
        val disabledEmployee = existingEmployee.copy(isActive = false)
        return employeeRepository.update(disabledEmployee).isSuccess
    }

    override fun getEmployeeById(id: String): Employee? {
        return employeeRepository.findById(id)
    }

    override fun listAllEmployees(): List<Employee> {
        return employeeRepository.findAll()
    }
}
