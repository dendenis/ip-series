package series

import java.io.PrintWriter

/**
  * Created by dkirsanov on 17.07.17.
  */
object ResultWriter {

  private val pw = new PrintWriter(Config.dstPath)

  def write(str: String) = pw.write(s"$str\n")

  def close() = pw.close()

}
