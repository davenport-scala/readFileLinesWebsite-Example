package tech.christopherdavenport.readfilelines

import cats.effect.IO
import fs2._
import java.nio.file.Paths

object Main extends StreamApp[IO] {

  val absoluteFilePath = "/home/davenpcm/Documents/ScalaProjects/Examples/readfilelines/bar.txt"

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, StreamApp.ExitCode] =
    for {
      _ <- Stream.eval(getFileContent.compile.drain)
    } yield StreamApp.ExitCode(0)
  

  val getFileContent = io.file.readAll[IO](Paths.get(absoluteFilePath), 512)
    .through(fs2.text.utf8Decode)
    .through(fs2.text.lines)
    .evalMap(withEachLine)


  def withEachLine(line: String): IO[Unit] = {
    if (line.contains("Sarasota Jazz Project")) IO(println(line)) else IO.unit
  }

}