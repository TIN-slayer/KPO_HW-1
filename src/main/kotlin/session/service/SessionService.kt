package session.service

import movie.service.MovieService
import session.dao.SessionDao
import session.entity.SessionEntity

interface SessionService {

    fun addSession(
        movieId: Int,
        startDateTime: String,
        price: Int,
        id: Int = -1,
        seatTable: MutableList<MutableList<Boolean>> = MutableList(5) { MutableList(5) { true } },
        hasStarted: Boolean = false
    )

    fun findSessionById(id: Int): SessionEntity

    fun deleteSession(id: Int)

    fun editSession(
        id: Int,
        movieId: Int = -1,
        startDateTime: String = "",
        price: Int = -1,
        seatTable: MutableList<MutableList<Boolean>> = MutableList(0) { MutableList(0) { true } },
        hasStarted: Boolean = false
    )

    fun buyTicket(id: Int, row: Int, col: Int)

    fun refundTicket(id: Int, row: Int, col: Int)

    fun startSession(id: Int)

    fun getSessionInfo(session: SessionEntity): String

    fun printSessions()
}