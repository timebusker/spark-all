package com.timebusker.scala.starter

import java.io.File

/*
  * @DESC:Scala_01_Hello:第一个学习用例
  * @author:timebusker
  * @date:2019 /7/8
  */
object Scala_01_Hello {

  /**
    * 基本语法:
    * 1、区分大小写,Scala是大小写敏感的，这意味着标识Hello 和 hello在Scala中会有不同的含义。
    *
    * 2、类名 - 对于所有的类名的第一个字母要大写。
    * 示例：class MyFirstScalaClass
    *
    * 3、方法名称 - 所有的方法名称的第一个字母用小写。
    * 示例：def myMethodName()
    *
    * 4、程序文件名 - 程序文件的名称应该与对象名称完全匹配（文件名和对象名称不匹配，程序将无法编译）。
    *
    * 5、def main(args: Array[String]) - Scala程序从main()方法开始处理，这是每一个Scala程序的强制程序入口部分。
    *
    * 6、Unit 表示无值,和其他语言中void等同
    */
  def main(args: Array[String]): Unit = {
    println("Hello, This is a first scala program!")

    doWhile(10)

    val res = doFor(1, 10)
    println("-----------------\t" + res)

    doException
  }

  /**
    * object对象中，独立于方法的代码仍然可以自动运行
    *
    * 类似于java中的静态代码块，类加载后便开始运行
    */
  println("我是独立于方法的静态代码.....我正在运行")

  /**
    * 传入方法的变量在方法内部属于常量
    * 无参数的方法调用时可以省略括号传参
    *
    * @param i
    */
  def doWhile(i: Int): Unit = {
    var j = 0
    do {
      j += 1
      println(j)
    } while (j < i)
  }

  /**
    * 定义有返回值的方法：方法返回值是以方法内最后一个语句执行的结果为准
    * 无参数的方法调用时可以省略括号传参
    *
    * @param i
    * @param ii
    * @return
    */
  def doFor(i: Int, ii: Int): Int = {
    for (j <- i to ii) {
      println("第一个FOR\t" + j)
    }
    // java 读取当前项目目录
    var files = new File("/").listFiles()
    for (file <- files) {
      println(file)
    }
    ii + i
  }

  /**
    * 代码异常信息捕获
    */
  def doException = {
    try {
      // 抛出异常
      throw new IllegalArgumentException("异常信息是.....")
    } catch {
      case npe: NullPointerException => println(npe.getMessage)
      case iae: IllegalArgumentException => {
        println(iae.getMessage)
        println("产生异常了，在这里的代码块开始处理....")
      }
    } finally {
      println("finally是最后执行的代码块...")
    }
  }
}
