package ticket.dao

import ticket.entity.TicketEntity
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jsonDao.JsonDao
import ticket.dao.exception.TicketDaoNotFoundException

class JsonTicketDao : TicketDao {
    companion object {
        private const val JSON_PATH = "data/ticket.json"
    }

    private val ticketEntities: MutableList<TicketEntity>

    init {
        val gson = Gson()
        val file = File(JSON_PATH)
        if (file.length() == 0L) {
            file.writeText("")
            ticketEntities = mutableListOf()
        } else {
            val json = file.readText()
            val listType = object : TypeToken<MutableList<TicketEntity>>() {}.type
            ticketEntities = gson.fromJson(json, listType)
        }
    }

    override fun getMaxTicketId(): Int = (if (ticketEntities.size > 0) ticketEntities.maxOf { it.id } + 1 else 0)

    override fun saveTicket(ticketEntity: TicketEntity) {
        ticketEntities.add(ticketEntity)
        JsonDao.encodeJson(JSON_PATH, ticketEntities)
    }

    override fun findTicketById(id: Int): TicketEntity {
        return ticketEntities.find { it.id == id } ?: throw TicketDaoNotFoundException("Сеанс с Id $id не найден")
    }

    override fun deleteTicket(id: Int) {
        val len = ticketEntities.size
        ticketEntities.removeIf { it.id == id }
        if (len == ticketEntities.size) throw TicketDaoNotFoundException("Сеанс с Id $id не найден")
        JsonDao.encodeJson(JSON_PATH, ticketEntities)
    }

    override fun getTickets(): MutableList<TicketEntity> = ticketEntities
}
