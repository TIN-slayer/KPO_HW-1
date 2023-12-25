package movie.dao

import movie.entity.MovieEntity
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jsonDao.JsonDao
import movie.dao.exception.MovieDaoNotFoundException

class JsonMovieDao : MovieDao {

    companion object {
        private const val JSON_PATH = "movie.json"
    }

    private val movieEntities: MutableList<MovieEntity>

    init {
        // Не получилось выделить этот init в метод класса JsonDao из-за проблем с кастом
        // movieEntities = JsonDao.decodeJson(JSON_PATH)
        val gson = Gson()
        val file = File(JSON_PATH)
        if (file.length() == 0L) {
            file.writeText("")
            movieEntities = mutableListOf()
        } else {
            val json = file.readText()
            val listType = object : TypeToken<MutableList<MovieEntity>>() {}.type
            movieEntities = gson.fromJson(json, listType)
        }
    }

    override fun getMaxMovieId(): Int = (if (movieEntities.size > 0) movieEntities.maxOf { it.id } + 1 else 0)

    override fun saveMovie(movieEntity: MovieEntity) {
        movieEntities.add(movieEntity)
        JsonDao.encodeJson(JSON_PATH, movieEntities)
    }

    override fun findMovieById(id: Int): MovieEntity =
        movieEntities.find { it.id == id } ?: throw MovieDaoNotFoundException("Фильм c id $id не найден")


    override fun deleteMovie(id: Int) {
        val len = movieEntities.size
        movieEntities.removeIf { it.id == id }
        if (len == movieEntities.size) throw MovieDaoNotFoundException("Фильм c id $id не найден")
        JsonDao.encodeJson(JSON_PATH, movieEntities)
    }

    override fun getMovies(): MutableList<MovieEntity> = movieEntities
}