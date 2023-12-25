package ticket.service.exception

import ticket.mainException.TicketException

class TicketIncorrectDataException(message: String) : TicketException(message)