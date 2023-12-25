package movie.service

import movie.entity.MovieEntity
import movie.dao.MovieDao
import movie.service.exception.MovieIncorrectDataException

class MovieServiceImpl(private val movieDao: MovieDao) : MovieService {

    companion object {
        var movieCount = 0
    }

    init {
        movieCount = movieDao.getMaxMovieId()
    }


    override fun addMovie(title: String, duration: Int, genre: String, id: Int) {
        if (duration < 0){
            throw MovieIncorrectDataException("Продолжительность фильма меньше нуля")
        }
        var newId = id
        if (newId == -1) {
            newId = movieCount++
            println("Добавили фильм с Id: $newId")
        }
        val newMovie = MovieEntity(title, duration, genre, newId)
        movieDao.saveMovie(newMovie)
    }

    override fun findMovieById(id: Int) = movieDao.findMovieById(id)

    override fun deleteMovie(id: Int) = movieDao.deleteMovie(id)

    override fun editMovie(id: Int, title: String, duration: Int, genre: String) {
        val oldMovie = findMovieById(id)
        deleteMovie(id)
        var newTitle = title
        var newDuration = duration
        var newGenre = genre
        if (newTitle == "") {
            newTitle = oldMovie.title
        }
        if (newDuration == -1) {
            newDuration = oldMovie.durationInMinutes
        }
        if (newGenre == "") {
            newGenre = oldMovie.genre
        }
        addMovie(newTitle, newDuration, newGenre, id)

    }

    override fun printMovies() {
        val movieEntities = movieDao.getMovies()
        movieEntities.forEach {
            println("id: ${it.id} | title: ${it.title} | duration: ${it.durationInMinutes} | genre: ${it.genre}")
        }
    }
}