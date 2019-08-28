package com.timebusker.scala.starter

/*
  * @DESC:Scala_02_Array：数组操作
  * @author:timebusker
  * @date:2019 /7/8
  */
object Scala_02_Array {

  // Scala 语言中提供的数组是用来存储固定大小的同类型元素
  def main(args: Array[String]): Unit = {
    //  数组声明
    // 声明一个字符串类型的数组，数组长度为 3 ，可存储 3 个元素
    var a: Array[String] = new Array[String](3)
    var b = new Array[String](3)
    // 赋值
    a(0) = "Runoob"
    a(1) = "Baidu"
    a(4 / 2) = "Google"

    // var test = Array[Int](1, 2, 3, 4, 5, 6, 7, 8)
    var test = Array(1, 2, 3, 4, 5, 6, 7, 8)
    // 输出所有数组元素
    for (i <- test) {
      print(i + "\t")
    }
    println("\n******************************************************")
    // 多维数组:数组的数组
    var arr1 = Array.ofDim[Int](3, 3)
    var arr2 = Array(Array(1, 2, 3, 4, 5), Array(6, 7, 8, 9, 10))
    arr1(1)(1) = 9
    for (i <- 0 until 2; j <- 0 until 5) {
      println(arr2(i)(j))
    }
    println("\n******************************************************")
    for (i <- 0 until 2) {
      for (j <- 0 until 5) {
        println(arr2(i)(j))
      }
    }

    // 合并数组
    var aa = Array(1, 2, 3)
    var bb = Array(4, 5, 6)
    var masta = aa ++ bb
    var mastb = aa union bb
    var mastc = aa.intersect(bb)

    for (i <- 0 to 5) {
      print(masta(i) + "\t" + mastb(i) + "\t" + "\n")
    }

    //    var ab = ArrayBuffer[String]();
    //    ab.insert(111)
    //    ab.insert(222)
    //    ab.insert(333)

    val ya = Array(3, 4, 1, 2)
    val result = for (elem <- ya if elem % 1 == 0) yield 2 * elem
    println(result.toString)
    // 使用sum方法，元素类型必须是数值类型:整型，浮点数或者BigInteger/BigDecimal
    println(result.sum)
    // min和max输出数组或数组缓冲中最小和最大的元素
    println(result.min + "\t" + result.max)
    // sorted方法将数组或数组缓冲排序并返回经过排序的数组或数组缓冲，不会修改原始数组．
    // 可以使用sortWith方法提供一个比较函数．
    println(result.sorted.toString)
    // 如果想要显示数组或者数组缓冲的内容，可以使用mkString，允许指定元素之间的分隔符
    println(result.mkString("###").toString)
    println(result.mkString("@", "%%", "@").toString)
  }


  def rangeArray = {
    var arr = Range(1, 30, 4)
    println(arr)

    var brr = for {
      ar <- 1 until 30
      if (ar - 1) % 4 == 0
    } yield ar
    println(brr)

    // 设置步长
    println("*********************************************")
    var a = Array(1, 2, 3, 4, 5)
    for (i <- 0 until(a.length, 2) if i + 1 < a.length) {
      var t = a(i);
      a(i) = a(i + 1);
      a(i + 1) = t;
    }
    a.foreach(println)
  }

}
