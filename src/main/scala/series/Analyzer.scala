package series

/**
  * Created by dkirsanov on 16.07.17.
  */

object Analyzer {

  private var ips = Map[String, IP]()
  private var lastCollectingT: Option[Long] = None

  def addEntry(entry: Entry) = {
    lastCollectingT match {
      case Some(t) if t + Config.collectingInterval < entry.t =>
        collectOldIPs(entry.t)
        lastCollectingT = Some(entry.t)
      case None                                               =>
        lastCollectingT = Some(entry.t)
      case _                                                  =>
    }

    entry.ip match {
      case "0.0.0.0" =>
      case ip        =>
        ips.get(ip) match {
          case Some(x) => x.add(entry)
          case None    => ips += (ip -> new IP(entry))
        }
    }
  }

  def collectOldIPs(currentSequenceT: Long) = {
    ips = ips.filterNot { x =>
      val condition = x._2.needToCollect(currentSequenceT)
      if (condition)
        x._2.writeLastSeries()
      condition
    }
  }

  def writeLastSeries() = ips.foreach(x => x._2.writeLastSeries())

}

