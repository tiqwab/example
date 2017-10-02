package com.tiqwab.example

import com.softwaremill.sttp._
import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend

import scala.concurrent.Future

object SttpMain {

  def main(args: Array[String]): Unit = {
    implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
    implicit val sttpBackend = AsyncHttpClientFutureBackend()

    val sort: Option[String] = None
    val query = "http language:scala"

    // Simple request
    val request = sttp.get(
      uri"https://api.github.com/search/repositories?q=$query&sort=$sort")
    val response = request
      .send()
      .map(resp => {
        println(resp.code) // 200
        println(resp.contentLength) // None
        println(resp.body) // // Right({"total_count":3041,"incomplete_results":false,"items":[{...}, ...]
      })

    // Illegal uri
    val illegalRequest = sttp.get(
      uri"https://api.github.com/search/repositoriess?q=$query&sort=$sort")
    val illegalResponse = illegalRequest
      .send()
      .map(resp => {
        println(resp.code) // 404
        println(resp.body) // Left({"message":"Not Found","documentation_url":"https://developer.github.com/v3"})
      })

    // Runtime error
    val errorResponse = sttp
      .contentType("application/json")
      .body("""{"name": "Taro"}""")
      .post(uri"http://localhost:8080")
      .send()
      .recoverWith { case e => println(e); Future.successful() } // jav  a.ne  t.ConnectException: Connection refused: localhost/0:0:0:0:0:0:0:1:8080

    val fut = for {
      _ <- response
      _ <- illegalResponse
      _ <- errorResponse
    } yield {
      ()
    }
    fut.onComplete(_ => sttpBackend.close())
  }

}
