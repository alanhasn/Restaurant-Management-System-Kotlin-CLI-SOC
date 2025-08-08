package domain.services

import domain.models.Table


interface TableService {
    suspend fun addTable(name: String, capacity: Int): Boolean
    suspend fun updateTable(id: String, name: String?, capacity: Int?, isAvailable: Boolean?): Boolean
    suspend fun deleteTable(id: String): Boolean
    suspend fun getTableById(id: String): Table?
    suspend fun listAllTables(): List<Table>
    suspend fun markTableAsOccupied(id: String): Boolean
    suspend fun markTableAsAvailable(id: String): Boolean
}