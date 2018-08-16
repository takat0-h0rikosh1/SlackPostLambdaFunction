package sample

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.typesafe.config.ConfigFactory
import sample.SampleHandler.{SampleRequest, SampleResponse}

import scala.beans.BeanProperty
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object SampleHandler {
  class SampleRequest(@BeanProperty val value: String = "slack message post start.")
  case class SampleResponse(@BeanProperty value: String = "slack message post done.")
}

class SampleHandler  extends RequestHandler[SampleRequest, SampleResponse] {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val config = ConfigFactory.load()
  private val sqsClient = new SQSClient(config)
  private val slackClient = new SlackClient(config)

  def handleRequest(input: SampleRequest, context: Context): SampleResponse = {
    val eventualUnit = for {
      messages <- sqsClient.receive
      eventualMessages = messages.map(m => slackClient.post(m).map(_ => m))
      eventualUnits = eventualMessages.map(_.flatMap(sqsClient.delete).recover({ case _ => () }))
      _ <- Future.sequence(eventualUnits)
    } yield SampleResponse()

    Await.result(eventualUnit, 3.second)
  }
}
