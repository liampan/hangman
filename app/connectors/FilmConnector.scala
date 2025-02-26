package connectors

import com.google.inject.Inject
import conf.ApplicationConfig
import models.{Film, Films}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FilmConnector @Inject()(appConfig: ApplicationConfig,
                              wsClient: WSClient) {

  def getFilms(): Future[List[Film]] = {

    Future.sequence((1 to 20).toList.map(page => getFilmPage(page)))
      .map {
        _.flatten
          .filter(_.title.matches("([\\w '!?,-:]+)"))
          .filterNot(_.title.matches(".*[0-9].*"))
      }

  }

  private def getFilmPage(page: Int): Future[List[Film]] = {

    wsClient.url(s"https://api.themoviedb.org/3/movie/top_rated?api_key=${appConfig.apiKey}&language=en-GB&page=${page}").get.map { response =>
      response.json.as[Films].results
    }
  }
}
