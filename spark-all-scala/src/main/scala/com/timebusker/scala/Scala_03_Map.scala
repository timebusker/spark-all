package com.timebusker.scala

/*
  * @DESC:Scala_03_Map：Map操作
  * @author:timebusker
  * @date:2019 /7/8
  */
object Scala_03_Map {

  // Map(映射)是一种可迭代的键值对（key/value）结构
  // Map 也叫哈希表（Hash tables）
  // Map 有两种类型，可变与不可变，区别在于可变对象可以修改它，而不可变对象不可以。
  // 默认情况下 Scala 使用不可变 Map。
  // 如果你需要使用可变集合，你需要显式的引入 import scala.collection.mutable.Map 类
  def main(args: Array[String]): Unit = {
    // 申明哈希表
    // 空哈希表，键为字符串，值为整型
    var map: Map[String, Int] = Map()
    // Map 键值对演示
    val colors = Map("red" -> "#FF0000", "azure" -> "#F0FFFF")

    // MAP添加键值对
    map += ("111" -> 1)
    map += ("222" -> 2)
    map += ("333" -> 3)
    // k,v取值
    for ((k, v) <- map) {
      println(k + "\t" + v)
    }
    // 使用“_”占位符
    for ((k, _) <- map) {
      println(k)
    }

    println(map.values)
  }

}
