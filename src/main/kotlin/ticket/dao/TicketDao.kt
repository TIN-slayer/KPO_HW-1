package ticket.dao

import ticket.entity.TicketEntity

interface TicketDao {
    fun getMaxTicketId(): Int
    fun saveTicket(ticketEntity:  TicketEntity)
    fun findTicketById(id: Int):  TicketEntity
    fun deleteTicket(id: Int)
    fun getTickets(): MutableList<TicketEntity>
}