package com.timebusker.scala.starter

import java.text.SimpleDateFormat
import java.util.Date

/**
 * @DESC:Scala_10_CollectionCompar
 * @author:timebusker
 * @date:2019/7/23
 */
object Scala_10_CollectionCompar {

  def main(args: Array[String]): Unit = {
    val format = new SimpleDateFormat("YYYYMMddHH")
    val date = format.format(new Date())
    println(date)
  }

}
