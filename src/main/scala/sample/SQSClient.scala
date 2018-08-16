package sample

import com.amazonaws.services.sqs.model.{Message, ReceiveMessageRequest}
import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
import com.typesafe.config.Config

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

class SQSClient(config: Config) {

  private val queueUrl = config.getString("aws.sqs.queue.url")
  private val sqsClient: AmazonSQS = AmazonSQSClientBuilder.defaultClient()
  private val receiveRequest: ReceiveMessageRequest = {
    val request = new ReceiveMessageRequest
    request.setMaxNumberOfMessages(10)
    request.setQueueUrl(queueUrl)
    request
  }

  def receive(implicit ec: ExecutionContext): Future[Seq[Message]] =
    Future {
      sqsClient.receiveMessage(receiveRequest).getMessages.asScala
    }

  def delete(msg: Message)(implicit ec: ExecutionContext): Future[Unit] =
    Future{
      sqsClient.deleteMessage(queueUrl, msg.getReceiptHandle)
    }

}
