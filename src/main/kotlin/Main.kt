import session.mainException.SessionException
import ticket.mainException.TicketException
import userInterface.UserInterface

fun main(args: Array<String>) {
    UserInterface.start()



//    UserInterface.movieService.addMovie("Rembo", 80, "Action")
//    UserInterface.movieService.addMovie("Evangelion", 90, "Anime")
//    UserInterface.movieService.editMovie(1, "Evangelion 2.0 + 1.0", 120, "none")
//    UserInterface.movieService.printMovies()
//    UserInterface.sessionService.addSession(1, "19.12.2023 09:00", 300)
//    UserInterface.sessionService.addSession(0, "05.06.2023 13:30", 500)
//    try {
//        UserInterface.ticketService.buyTicket(0, 3, 4) // Eva
//        UserInterface.ticketService.buyTicket(0, 2, 2) // Eva
//        UserInterface.ticketService.buyTicket(1, 0, 3) // Rembo
//        UserInterface.ticketService.buyTicket(1, -7, 3) // Rembo
//    } catch (ex: SessionException) {
//        println(ex.message)
//    }
//    try {
//        UserInterface.sessionService.startSession(1)
//        UserInterface.ticketService.refundTicket(0)
//        UserInterface.ticketService.refundTicket(2)
//    } catch (ex: SessionException) {
//        println(ex.message)
//    } catch (ex: TicketException) {
//        println(ex.message)
//    }
//    UserInterface.sessionService.editSession(0, 1, "", 400)
//    UserInterface.sessionService.editSession(1, 1, "10.06.2023 14:45", 0)
//    UserInterface.sessionService.printSessions()
//    UserInterface.ticketService.printTickets()
}