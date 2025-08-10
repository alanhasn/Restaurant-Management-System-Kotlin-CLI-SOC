package domain.services

import domain.models.Table

// Interface for table service operations
interface TableService {
    suspend fun addTable(table: Table): Boolean
    suspend fun updateTable(id:String , table: Table): Boolean
    suspend fun deleteTable(id: String): Boolean
    suspend fun getTableById(id: String): Table?
    suspend fun reserveTable(id: String): Boolean
    suspend fun listAllTables(): List<Table>
}