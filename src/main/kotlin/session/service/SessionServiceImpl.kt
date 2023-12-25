package session.service

import movie.service.MovieService
import session.dao.SessionDao
import session.entity.SessionEntity
import session.service.exception.SessionIncorrectDataException
import session.service.exception.SessionIncorrectRefundException
import session.service.exception.SessionIncorrectSeatException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class SessionServiceImpl(private val sessionDao: SessionDao, private val movieService: MovieService) : SessionService {

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")
        var sessionCount = 0
    }

    init {
        sessionCount = sessionDao.getMaxSessionId()
    }

    override fun addSession(
        movieId: Int,
        startDateTime: String,
        price: Int,
        id: Int,
        seatTable: MutableList<MutableList<Boolean>>,
        hasStarted: Boolean
    ) {
        // могут прокинуть исключения (обрабатываем их в классе интерфейс)
        movieService.findMovieById(movieId)
        try {
            LocalDateTime.parse(startDateTime, formatter)
        } catch (ex: DateTimeParseException) {
            throw SessionIncorrectDataException("Некорректная дата и время сеанса")
        }
        if (price < 0) {
            throw SessionIncorrectDataException("Цена на сеанс меньше нуля")
        }
        var newId = id;
        if (newId == -1) {
            newId = sessionCount++
            println("Добавили сеанс с Id: $newId")
        }
        val newSession = SessionEntity(movieId, startDateTime, price, newId, seatTable, hasStarted)
        sessionDao.saveSession(newSession)
    }

    override fun findSessionById(id: Int) = sessionDao.findSessionById(id)

    override fun deleteSession(id: Int) = sessionDao.deleteSession(id)

    override fun editSession(
        id: Int,
        movieId: Int,
        startDateTime: String,
        price: Int,
        seatTable: MutableList<MutableList<Boolean>>,
        hasStarted: Boolean
    ) {
        // могут прокинуть исключения (обрабатываем их в классе интерфейс)
        val oldSession = findSessionById(id)
        deleteSession(id)
        var newMovieId = movieId
        var newStartDateTime = startDateTime
        var newPrice = price
        var newSeatTable = seatTable
        var newHasStarted = hasStarted
        if (newMovieId == -1) {
            newMovieId = oldSession.movieId
        }
        if (newStartDateTime == "") {
            newStartDateTime = oldSession.startDateTime
        }
        if (newPrice == -1) {
            newPrice = oldSession.price
        }
        if (newSeatTable.size == 0) {
            newSeatTable = oldSession.seatTable
        }
        if (!newHasStarted) {
            newHasStarted = oldSession.hasStarted
        }
        addSession(newMovieId, newStartDateTime, newPrice, id, newSeatTable, newHasStarted)
    }

    override fun buyTicket(id: Int, row: Int, col: Int) {
        // могут прокинуть исключения (обрабатываем их в классе интерфейс)
        val session = findSessionById(id)
        val normRow = row + 1
        val normCol = col + 1
        if (row < 0 || row > 4 || col < 0 || col > 4) {
            throw SessionIncorrectSeatException("Ряд и номер места должны быть от 1 до 5")
        }
        if (session.seatTable[row][col]) {
            session.seatTable[row][col] = false
        } else {
            throw SessionIncorrectSeatException("Место $normRow-$normCol уже занято")
        }
        editSession(id = id, seatTable = session.seatTable)
    }

    override fun refundTicket(id: Int, row: Int, col: Int) {
        // могут прокинуть исключения (обрабатываем их в классе интерфейс)
        val session = findSessionById(id)
        if (!session.hasStarted) {
            session.seatTable[row][col] = true
        } else {
            throw SessionIncorrectRefundException("Сеанс уже начался, возврат билета невозможен")
        }
        editSession(id = id, seatTable = session.seatTable)
    }

    override fun startSession(id: Int) {
        // могут прокинуть исключения (обрабатываем их в классе интерфейс)
        editSession(id = id, hasStarted = true)
    }

    override fun getSessionInfo(session: SessionEntity): String {
        val movie = movieService.findMovieById(session.movieId)
        val startDateTime = LocalDateTime.parse(session.startDateTime, formatter)
        val endDateTime = movie.durationInMinutes.let {
            startDateTime.plusMinutes(it.toLong())
        }
        return "movieTitle: ${movie.title} | price: ${session.price} | startDateTime: $startDateTime | endDateTime: $endDateTime | hasStarted: ${session.hasStarted}"
    }


    override fun printSessions() {
        val sessionEntities = sessionDao.getSessions()
        sessionEntities.forEach {
            println("id: ${it.id} | ${getSessionInfo(it)} | seatTable:")
            for (row in it.seatTable) {
                for (col in row) {
                    print("${if (col) 1 else 0} ")
                }
                println()
            }
        }
    }
}