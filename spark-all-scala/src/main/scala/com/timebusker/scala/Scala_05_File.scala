package com.timebusker.scala

import java.io.{File, PrintWriter}

import scala.io.Source

/*
  * @DESC:Scala_05_File：文件操作
  * @author:timebusker
  * @date:2019 /7/8
  */
object Scala_05_File {

  def main(args: Array[String]): Unit = {
    // 读取本地文件
    // 可以对getLines应用toArray或toBuffer方法，将这些行放到数组或缓冲当中。
    var localFile = Source.fromFile("D:\\command.bat")
    for (line <- localFile.getLines()) {
      println(line)
    }
    localFile.close()
    // 读取网络资源文件
    var onlineFile = Source.fromURL("https://gitee.com/", "utf-8")
    for (line <- onlineFile.getLines()) {
      println(line)
    }
    // 去取文件后可进行查、缓存、内容识别等一系列操作

    // 写文件
    val wr = new PrintWriter(new File("d:\\gitee.txt"))
    for (line <- onlineFile.getLines()) {
      wr.write(line.getBytes.toString)
    }
    wr.close()
    onlineFile.close()

    print("Please enter your input : ")
    val line = Console.readLine
    println("Thanks, you just typed: " + line)
  }

}
