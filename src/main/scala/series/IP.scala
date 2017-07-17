package series

import java.text.SimpleDateFormat
import java.util.Date

/**
  * Created by dkirsanov on 16.07.17.
  */

class IP(entry: Entry) {

  val ip = entry.ip

  private var lastEntry = entry
  private var lastSeriesLoginT = Long.MinValue
  private var lastSeries: Option[Series] = None

  def add(entry: Entry) = {
    if (entry.t <= lastSeriesLoginT + Config.window) {
      lastSeries match {
        case Some(series) => series.add(entry)
        case None         => lastSeries = Some(new Series(lastEntry, entry))
      }
      lastSeriesLoginT = entry.t
    }
    else if (entry.t <= lastEntry.t + Config.window) {
      writeLastSeries()
      lastSeries = Some(new Series(lastEntry, entry))
      lastSeriesLoginT = entry.t
    }
    lastEntry = entry
  }

  def needToCollect(currentSequenceT: Long) = lastEntry.t + Config.collectingInterval < currentSequenceT

  def writeLastSeries() = lastSeries.foreach(x => ResultWriter.write(x.toCSV(ip)))

}

class Series(firstEntry: Entry, secondEntry: Entry) {

  private val firstLoginT = firstEntry.t
  private var lastLoginT = secondEntry.t
  private var users = Map[String, Long]()

  add(firstEntry)
  add(secondEntry)

  def add(entry: Entry) = {
    lastLoginT = entry.t
    if (users.get(entry.user).isEmpty)
      users ++= Map(entry.user -> entry.t)
  }

  def toCSV(ip: String) = {
    val userInfo = users.map(x => s""""${x._1}":"${getDate(x._2)}"""").mkString(",")
    s""""$ip";"${getDate(firstLoginT)}";"${getDate(lastLoginT)}";$userInfo"""
  }

  def getDate(t: Long) = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(t))

}

case class Entry(user: String, ip: String, t: Long)