package sample

import com.amazonaws.services.sqs.model.Message
import com.typesafe.config.Config
import gigahorse.support.okhttp.Gigahorse
import gigahorse.{HeaderNames, MimeTypes, Request}

import scala.concurrent.{ExecutionContext, Future}

class SlackClient(config: Config) {

  private val Url = config.getString("slack.api.post.message.url")
  private val ApiToken = config.getString("slack.api.token")
  private val requestWithBody: String => Request = { body: String =>
    Gigahorse
      .url(Url)
      .post(body)
      .addHeaders(
        HeaderNames.AUTHORIZATION -> s"Bearer $ApiToken",
        HeaderNames.CONTENT_TYPE -> MimeTypes.JSON
      )
  }

  def post(message: Message)(implicit ec: ExecutionContext): Future[Unit] =
    Gigahorse.withHttp(Gigahorse.config) { http =>
      http
        .run(requestWithBody(message.getBody), Gigahorse.asString)
        .flatMap { s =>
          SlackAPIResponse
            .fromJsString(s)
            .fold(e => Future.failed(e), _ => Future.successful(()))
        }
    }
}
