package com.timebusker.scala

import java.io.{File, PrintWriter}

import scala.io.Source

/*
  * @DESC:Scala_06_DefFunction：函数定义操作
  * @author:timebusker
  * @date:2019 /7/8
  */
object Scala_06_DefFunction {

  // Scala可以不用申明函数的返回值=》l类型自动推导
  // 递归里面必须指定返回值类型

  // Scala 有函数和方法，二者在语义上的区别很小。
  // Scala 方法是类的一部分，而函数是一个对象可以赋值给一个变量。换句话来说在类中定义的函数即是方法。
  def main(args: Array[String]): Unit = {
    // Scala 函数传名调用(call-by-name)
    // Scala的解释器在解析函数参数(function arguments)时有两种方式：
    // 传值调用（call-by-value）：先计算参数表达式的值，再应用到函数内部
    // 传名调用（call-by-name）：将未计算的参数表达式直接应用到函数内部
    // 在进入函数内部前，传值调用方式就已经将参数表达式的值计算完毕，而传名调用是在函数内部进行参数表达式的值计算的。
    // 这就造成了一种现象，每次使用传名调用时，解释器都会计算一次表达式的值。
    doPrintln(getTime(), "HaHaHa..")
    // 一般情况下函数调用参数，就按照函数定义时的参数顺序一个个传递。但是我们也可以通过指定函数参数名，并且不需要按照顺序向函数传递参数
    doPrintln(str = "Hahaha..", time = 123444L)

    // 递归函数:数可以调用它本身
    for (i <- 1 to 5) {
      // 设置默认参数值可以不传参数
      println("当前阶乘值：" + factorial())
    }

    // 高阶函数（Higher-Order Function）就是操作其他函数的函数。
    // 高阶函数可以使用其他函数作为参数，或者使用函数作为输出结果。
    println(apply(layout, 10))

    // 函数 f 和 值 v 作为参数，而函数 f 又调用了参数 v
    def apply(f: Int => String, v: Int) = f(v)

    def layout[A](x: A) = "[" + x.toString() + "]"

    // 函数嵌套
    // 在 Scala 函数内定义函数，定义在函数内的函数称之为局部函数。

    // 匿名函数：箭头左边是参数列表，右边是函数体。
    var nn = (str: String) => {
      println(str)
    }
    // 调用匿名函数
    nn("********************************************************")

    // 偏应用函数:需要提供函数需要的所有参数，只需要提供部分，或不提供所需参数。
    // 进行变量绑定
    val logWithDateBound = log(System.nanoTime(), _: String)
    log(System.nanoTime(), "aaaaaaaa")
    logWithDateBound("bbbbbbbb")
    logWithDateBound("CCCCCCCC")

    // 函数柯里化(Currying)
    // 将原来接受两个参数的函数变成新的接受一个参数的函数的过程。新的函数返回一个以原有第二个参数为参数的函数。

    // 可变参数函数：参数个数是不固定的(不适用于匿名函数)
    def sumi(ii: Int*) = {
      var sum = 0
      for (i <- ii) {
        sum += i
      }
      sum
    }

    println("\t\t" + sumi(1, 2, 3))
  }

  // 日志输出函数
  def log(bigInt: BigInt, message: String) = {
    println(bigInt + "----" + message)
  }

  // 递归函数实现阶乘
  // Scala 可以为函数参数指定默认参数值
  def factorial(bigInt: BigInt = 5): BigInt = {
    if (bigInt <= 1) {
      1
    } else {
      bigInt * factorial(bigInt - 1)
    }
  }

  // 输出系统时间
  // 该方法在变量名和变量类型使用 => 符号来设置传名调用。
  def doPrintln(time: => Long, str: String) = {
    println("已经进入doPrintln()函数")
    println("当前系统时间：" + time)
    println("字符串值：" + str)
  }

  // 获取系统时间
  def getTime = {
    println("获取系统当前时间\t\t时间单位：纳秒")
    System.nanoTime()
  }

}
