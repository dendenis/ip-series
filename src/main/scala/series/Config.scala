package series

import java.io.FileInputStream
import java.util.Properties

/**
  * Created by dkirsanov on 16.07.17.
  */
object Config {
  private val p = new Properties
  p.load(new FileInputStream("config.properties"))

  val window = getParam("window").toLong
  val dstPath = getParam("dst_path")

  val collectingInterval = window * 2 + 1000L

  private def getParam(paramName: String): String = {
    val paramValue = p.getProperty(paramName)
    if (paramValue == null) throw new RuntimeException(s"no param with name $paramName in config.properties") else paramValue
  }

}
