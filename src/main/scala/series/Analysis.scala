package series

import java.text.SimpleDateFormat

/**
  * Created by dkirsanov on 16.07.17.
  */
object Analysis {

  def main(args: Array[String]) {
    parseAndAnalyze(args(0))
  }

  def parseAndAnalyze(srcPath: String) = {
    val src = scala.io.Source.fromFile(srcPath)
    src.getLines().foreach { x =>
      try {
        val line = x.replaceAll("\"", "").split(',')
        Analyzer.addEntry(Entry(line(0), line(1), dateToMillis(line(2))))
      }
      catch {
        case t: Throwable => println(s"Line $x cannot be parsed cause of exception ${t.getMessage}")
      }
    }
    src.close()
    Analyzer.writeLastSeries()
    ResultWriter.close()
  }

  def dateToMillis(date: String) = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime

}


