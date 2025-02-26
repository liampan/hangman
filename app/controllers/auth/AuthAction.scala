package controllers.auth

import com.google.inject.Inject
import conf.ApplicationConfig
import play.api.mvc.Results.Redirect
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class AuthAction @Inject()(val parser: BodyParsers.Default,
                           appConfig: ApplicationConfig)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[Request, AnyContent] with ActionRefiner[Request, Request] {

  override protected def refine[A](request: Request[A]): Future[Either[Result, Request[A]]] = {

    val sessionUUID = request.session.get("UUID")

    sessionUUID.fold[Future[Either[Result, Request[A]]]] {
      Future.successful(Left(Redirect(controllers.routes.HomeController.createSession())))
    } {
      _ =>
        Future.successful(Right(request))
    }
  }
}
