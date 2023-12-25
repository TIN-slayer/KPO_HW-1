package movie.dao

import movie.entity.MovieEntity

interface MovieDao {
    fun getMaxMovieId(): Int
    fun saveMovie(movieEntity: MovieEntity)
    fun findMovieById(id: Int): MovieEntity
    fun deleteMovie(id: Int)
    fun getMovies(): MutableList<MovieEntity>
}