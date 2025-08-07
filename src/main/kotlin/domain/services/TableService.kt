package domain.services

import domain.models.Table


interface TableService {
    fun addTable(name: String, capacity: Int): Boolean
    fun updateTable(id: String, name: String?, capacity: Int?, isAvailable: Boolean?): Boolean
    fun deleteTable(id: String): Boolean
    fun getTableById(id: String): Table?
    fun listAllTables(): List<Table>
    fun markTableAsOccupied(id: String): Boolean
    fun markTableAsAvailable(id: String): Boolean
}