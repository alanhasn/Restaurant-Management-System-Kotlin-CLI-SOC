package data.repository

// Dependencies
import domain.models.Table
import domain.models.utils.TableStatus
import java.util.UUID // For table ID generation


// In-Memory Table Repository (Implementation of TableRepository interface)
class InMemoryTableRepository: TableRepository{

    // In-memory table repository to store tables
    private val tables = mutableMapOf<String , Table>()

    init {
        // Initialize with some sample tables
        val table1 = Table(UUID.randomUUID().toString() ,1, 4, TableStatus.AVAILABLE , "Location 1", "Description 1")
        val table2 = Table(UUID.randomUUID().toString() ,2, 6, TableStatus.AVAILABLE , "Location 2", "Description 2")
        val table3 = Table(UUID.randomUUID().toString() ,3, 8, TableStatus.AVAILABLE , "Location 3", "Description 3")
        val table4 = Table(UUID.randomUUID().toString() ,4, 10, TableStatus.RESERVED , "Location 4", "Description 4")
        val table5 = Table(UUID.randomUUID().toString() ,5, 12, TableStatus.RESERVED , "Location 5", "Description 5")
        tables[table1.id] = table1
        tables[table2.id] = table2
        tables[table3.id] = table3
        tables[table4.id] = table4
        tables[table5.id] = table5
    }
    /*
     * Save a table to the in-memory storage
     * @param table The table to be saved
     * @return true if the table was saved successfully
     */
    override fun save(table: Table): Boolean {
        val id = table.id.ifBlank { UUID.randomUUID().toString() }
        val tableWithId = table.copy(id=id)
        tables[id] = tableWithId
        return true
    }

    /*
     * Find a table by its ID
     * @param id The ID of the table to find
     * @return The found table or null if not found
     */
    override fun findById(id: String): Table? {
        return tables[id]
    }

    /*
     * Find a table by its table number
     * @param tableNumber The table number of the table to find
     * @return The found table or null if not found
     */
    override fun findByTableNumber(tableNumber: Int): Table? {
        return tables.values.firstOrNull { it.tableNumber == tableNumber }
    }

    /*
     * Find all tables
     * @return A list of all tables
     */
    override fun findAll(): List<Table> {
        return tables.values.toList()
    }

    /*
     * Find tables by their status
     * @param status The status of the tables to find
     * @return A list of tables with the specified status
     */
    override fun findByStatus(status: TableStatus): List<Table> {
        return tables.values.filter { it.status == status}
    }

    override fun tableReserve(id: String): Boolean {
        if (!tables.containsKey(id)){
            return false
        }
        if (tables[id]?.status == TableStatus.RESERVED){
            return false
        }
        tables[id]?.status = TableStatus.RESERVED
        return true
    }

    /*
     * Update a table in the in-memory storage
     * @param table The table to be updated
     * @return true if the table was updated successfully
     */
    override fun update(table: Table): Boolean {
        val id = table.id
        if (!tables.containsKey(id)){
            return false
        }
        tables[id] = table
        return true
    }

    /*
     * Delete a table by its ID
     * @param id The ID of the table to delete
     * @return true if the table was deleted successfully
     */
    override fun delete(id: String): Boolean {
        return tables.remove(id) != null
    }
}