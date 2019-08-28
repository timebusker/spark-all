package com.timebusker.scala.starter

/*
  * @DESC:Scala_07_ForLoop：循环的操作扩展
  * @author:timebusker
  * @date:2019 /7/8
  */
object Scala_07_ForLoop {

  def main(args: Array[String]): Unit = {
    // 左箭头 <- 用于为变量 j 赋值
    // 变量 j 通过 表达式‘1 to 10’进行类型推导输出
    for (j <- 1 to 10) {
      print(j + "\t")
    }
    println("\n***********************************************")
    // until : （end）不包含
    for (j <- 1 until 10) {
      print(j + "\t")
    }
    println("\n***********************************************")
    // 在 for 循环 中你可以使用分号 (;) 来设置多个区间，它将迭代给定区间所有的可能值(多个区间的组合)。
    for (i <- 1 to 4; j <- 5 to 8) {
      print(i + ":" + j + "\t")
    }
    println("\n***********************************************")
    // 循环集合:for 循环会迭代所有集合的元素
    // 循环过滤：
    var list = List(1, 2, 3, 4, 5, 6, 7, 8)
    for (l <- list; if l != 2; if l <= 6) {
      print(l + "\t")
    }
    println("\n***********************************************")
    // 使用yield：将for 循环的返回值作为一个变量存储(用作集合过滤)
    var retVal = for {jj <- list if jj != 2; if jj <= 6} yield jj
    println(retVal)
  }

}
