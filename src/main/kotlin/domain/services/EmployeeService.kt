package domain.services

import domain.models.Employee


interface EmployeeService {
    fun hireEmployee(name: String, role: String): Boolean
    fun updateEmployee(id: String, name: String?, role: String?): Boolean
    fun fireEmployee(id: String): Boolean
    fun getEmployeeById(id: String): Employee?
    fun listAllEmployees(): List<Employee>
}
