package sample

import io.circe.Decoder
import io.circe.parser.decode

case class SlackAPIResponse(ok: Boolean, error: Option[String])

object SlackAPIResponse {

  class SlackPostMessageError(
      message: String,
      cause: Option[Throwable] = None
  ) extends Exception(message, cause.orNull) {
    def this(message: String, cause: Throwable) = this(message, Some(cause))
  }

  object SlackPostMessageError {
    def apply(stringOpt: Option[String]): SlackPostMessageError =
      stringOpt
        .map(msg => new SlackPostMessageError(msg))
        .getOrElse(new SlackPostMessageError("There is no errors"))
  }

  implicit val decoder: Decoder[SlackAPIResponse] =
    Decoder.forProduct2("ok", "error")(SlackAPIResponse.apply)

  def fromJsString(
      jsonString: String): Either[SlackPostMessageError, SlackAPIResponse] =
    decode[SlackAPIResponse](jsonString) match {
      case Right(response @ SlackAPIResponse(true, _)) =>
        Right(response)
      case Right(_ @SlackAPIResponse(false, msgOpt)) =>
        Left(SlackPostMessageError(msgOpt))
      case Left(e) =>
        Left(new SlackPostMessageError(e.getMessage, e))
    }
}
