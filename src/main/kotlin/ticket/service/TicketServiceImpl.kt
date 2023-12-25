package ticket.service

import session.service.SessionService
import ticket.dao.TicketDao
import ticket.entity.TicketEntity
import ticket.service.exception.TicketIncorrectDataException

class TicketServiceImpl(private val ticketDao: TicketDao, private val sessionService: SessionService) : TicketService {

    companion object {
        var ticketCount = 0
    }

    init {
        ticketCount = ticketDao.getMaxTicketId()
    }

    override fun addTicket(
        sessionId: Int,
        row: Int,
        col: Int,
        id: Int
    ) {
        // могут прокинуть исключения (обрабатываем их в классе интерфейс)
        sessionService.findSessionById(sessionId)
        // На всякий случай проверка
        if (row < 0 || row > 4 || col < 0 || col > 4){
            throw TicketIncorrectDataException("Ряд и номер места должны быть от 1 до 5")
        }
        var newId = id
        if (newId == -1) {
            newId = ticketCount++
            println("Продали билет ${row + 1}-${col + 1} c Id: $newId на сеанс c id: $sessionId")
        }
        val newTicket = TicketEntity(sessionId, row, col, newId)
        ticketDao.saveTicket(newTicket)
    }

    override fun findTicketById(id: Int) = ticketDao.findTicketById(id)

    override fun deleteTicket(id: Int) = ticketDao.deleteTicket(id)

    override fun sellTicket(sessionId: Int, row: Int, col: Int) {
        // могут прокинуть исключения (обрабатываем их в классе интерфейс)
        sessionService.sellTicket(sessionId, row, col)
        addTicket(sessionId, row, col)
    }

    override fun refundTicket(id: Int) {
        // могут прокинуть исключения (обрабатываем их в классе интерфейс)
        val ticket = findTicketById(id)
        sessionService.refundTicket(ticket.sessionId, ticket.row, ticket.col)
        println("Оформили возврат на билет с id: ${ticket.id}")
        deleteTicket(id)
    }


    override fun printTickets() {
        val ticketEntities = ticketDao.getTickets()
        ticketEntities.forEach {
            val session = sessionService.findSessionById(it.sessionId)
            println("id: ${it.id} | row: ${it.row} | col: ${it.col} | ${sessionService.getSessionInfo(session)}")
        }
    }
}