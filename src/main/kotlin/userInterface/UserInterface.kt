package userInterface

import movie.dao.JsonMovieDao
import movie.mainException.MovieException
import movie.service.MovieService
import movie.service.MovieServiceImpl
import session.dao.JsonSessionDao
import session.mainException.SessionException
import session.service.SessionService
import session.service.SessionServiceImpl
import ticket.dao.JsonTicketDao
import ticket.mainException.TicketException
import ticket.service.TicketService
import ticket.service.TicketServiceImpl


class UserInterface {
    companion object {
        private val movieService: MovieService = MovieServiceImpl(JsonMovieDao())
        private val sessionService: SessionService = SessionServiceImpl(JsonSessionDao(), movieService)
        private val ticketService: TicketService = TicketServiceImpl(JsonTicketDao(), sessionService)
        private val coms = arrayOf(
            "помощь", "инфо по фильмам", "добавить фильм", "отредактировать фильм",
            "удалить фильм", "инфо по сеансам", "добавить сеанс", "отредактировать сеанс", "удалить сеанс",
            "начать сеанс", "инфо по билетам", "купить билет", "вернуть билет", "выход"
        )

        fun start() {
            println("Здравствуйте, вы можете использовать следующие команды для управления кинотеатром:")
            coms.forEach { println(it) }
            while (true) {
                try {
                    println("Введите команду:")
                    val com = readln()
                    when (com) {
                        "помощь" -> {
                            println("Доступные команды:")
                            coms.forEach { println(it) }
                        }

                        "инфо по фильмам" -> movieService.printMovies()
                        "добавить фильм" -> addMovie()
                        "отредактировать фильм" -> editMovie()
                        "удалить фильм" -> deleteMovie()
                        "инфо по сеансам" -> sessionService.printSessions()
                        "добавить сеанс" -> addSession()
                        "отредактировать сеанс" -> editSession()
                        "удалить сеанс" -> deleteSession()
                        "начать сеанс" -> startSession()
                        "инфо по билетам" -> ticketService.printTickets()
                        "купить билет" -> buyTicket()
                        "вернуть билет" -> refundTicket()
                        "выход" -> break
                    }
                } catch (ex: MovieException) {
                    println(ex.message)
                } catch (ex: SessionException) {
                    println(ex.message)
                } catch (ex: TicketException) {
                    println(ex.message)
                }
            }

        }

        private fun addMovie() {
            println("Введите название фильма, длительность в минутах и жанр через пробел:")
            try {
                val args = readln().split(' ')
                if (args.size != 3) {
                    println("Аргументов должно быть 3")
                    return
                }
                movieService.addMovie(args[0], args[1].toInt(), args[2])
            } catch (ex: NumberFormatException) {
                println("Длительность должна быть натуральным числом")
            }
        }

        private fun editMovie() {
            println(
                "Введите id фильма, который хотите изменить, " +
                        "новое название, новую длительность и новый жанр через пробел. " +
                        "В случае нежелания менять менять конкретную позицию пишите -"
            )
            try {
                val args = readln().split(' ')
                if (args.size != 4) {
                    println("Аргументов должно быть 4")
                    return
                }
                val id = args[0].toInt()
                val title = if (args[1] != "-") args[1] else ""
                val duration = if (args[2] != "-") args[2].toInt() else -1
                val genre = if (args[3] != "-") args[3] else ""
                movieService.editMovie(id, title, duration, genre)
                println("Отредактирован фильм с id: $id")
            } catch (ex: NumberFormatException) {
                println("id и длительность должны быть натуральными числами")
            }
        }

        private fun deleteMovie() {
            println("Введите id фильма, который хотите удалить:")
            try {
                val id = readln().toInt()
                movieService.deleteMovie(id)
                println("Удалили фильм с id: $id")
            } catch (ex: NumberFormatException) {
                println("id должно быть натуральным числом")
            }
        }

        private fun addSession() {
            println("Введите id фильма, дату и время начала сеанса в подобном формате (27.12.2023-09:00) и стоимость билета через пробел:")
            try {
                val args = readln().split(' ')
                if (args.size != 3) {
                    println("Аргументов должно быть 3")
                    return
                }
                sessionService.addSession(args[0].toInt(), args[1], args[2].toInt())
            } catch (ex: NumberFormatException) {
                println("id фильма и цена должны быть натуральными числами")
            }
        }

        private fun editSession() {
            println(
                "Введите id сеанса, который хотите изменить, " +
                        "новый id фильма, новую дата и время начала и новую стоимость билета через пробел. " +
                        "В случае нежелания менять менять конкретную позицию пишите -"
            )
            try {
                val args = readln().split(' ')
                if (args.size != 4) {
                    println("Аргументов должно быть 4")
                    return
                }
                val id = args[0].toInt()
                val movieId = if (args[1] != "-") args[1].toInt() else -1
                val startDateTime = if (args[2] != "-") args[2] else ""
                val price = if (args[3] != "-") args[3].toInt() else -1
                sessionService.editSession(id, movieId, startDateTime, price)
                println("Отредактирован сеанс с id: $id")
            } catch (ex: NumberFormatException) {
                println("id сеанса, id фильма и длительность должны быть натуральными числами")
            }
        }

        private fun deleteSession() {
            println("Введите id сеанса, который хотите удалить:")
            try {
                val id = readln().toInt()
                sessionService.deleteSession(id)
                println("Удалили сеанс с id: $id")
            } catch (ex: NumberFormatException) {
                println("id должно быть натуральным числом")
            }
        }

        private fun startSession() {
            println("Введите id сеанса, который хотите начать:")
            try {
                val id = readln().toInt()
                sessionService.startSession(id)
                println("Начали сеанс с id: $id")
            } catch (ex: NumberFormatException) {
                println("id должно быть натуральным числом")
            }
        }

        private fun buyTicket() {
            println("Введите id сеанса, номер ряда (от 1 до 5) и место (от 1 до 5) через пробел:")
            try {
                val args = readln().split(' ')
                if (args.size != 3) {
                    println("Аргументов должно быть 3")
                    return
                }
                ticketService.buyTicket(args[0].toInt(), (args[1].toInt() - 1), (args[2].toInt() - 1))
            } catch (ex: NumberFormatException) {
                println("id, ряд и место должны быть натуральными числами")
            }
        }

        private fun refundTicket() {
            println("Введите id билета, который хотите вернуть:")
            try {
                val id = readln().toInt()
                ticketService.refundTicket(id)
            } catch (ex: NumberFormatException) {
                println("id должно быть натуральным числом")
            }
        }
    }
}