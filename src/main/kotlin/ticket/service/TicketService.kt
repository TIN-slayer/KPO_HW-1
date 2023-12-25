package ticket.service

import ticket.entity.TicketEntity

interface TicketService {
    fun addTicket(
        sessionId: Int,
        row: Int,
        col: Int,
        id: Int = -1
    )

    fun findTicketById(id: Int): TicketEntity

    fun deleteTicket(id: Int)

    fun buyTicket(sessionId: Int, row: Int, col: Int)

    fun refundTicket(id: Int)

    fun printTickets()
}