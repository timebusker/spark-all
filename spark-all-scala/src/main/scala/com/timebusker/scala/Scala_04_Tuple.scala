package com.timebusker.scala

/*
  * @DESC:Scala_04_Tuple：元组操作
  * @author:timebusker
  * @date:2019 /7/8
  */
object Scala_04_Tuple {

  //元组Tuple
  //元组是Scala提供的一种特殊的数据结构，它允许多个不同类型的元素组合在一起被使用
  // Unit 表示无值,和其他语言中void等同
  // 索引从1开始
  def main(args: Array[String]): Unit = {
    // 元组的申明和使用
    // 第一种方式：
    val tuple = (1, 3.14, "元组", true, 3l, 4.4f)
    println(tuple._1)
    println(tuple._2)
    println(tuple._3)
    println(tuple)

    // 第二种方式：
    val (length, width, height) = (1.3, 0.7, 1.8)
    println(length)
    println(width)

    // 第三种方式：当是多元组时，可以简单定义成：1 -> "one"，等效于(1, "one")
    val mutltuple = ("A", "B", (11 -> 22 -> 33 -> 44))
    println(mutltuple)
  }

}
