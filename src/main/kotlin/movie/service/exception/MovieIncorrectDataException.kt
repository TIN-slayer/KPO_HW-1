package movie.service.exception

import movie.mainException.MovieException

class MovieIncorrectDataException(message: String): MovieException(message)