package com.timebusker.scala.starter

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

/**
  * @DESC:SQLContextApplication:使用SQLContext测试SQL
  * @author:timebusker
  * @date:2019/8/27
  */
object SQLContextApplication {

  /**
    * spark1.X，可以使用SQLContext创建获取上下文信息并执行spark sql
    * @param args
    */

  def main(args: Array[String]): Unit = {

    val path = "D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-spark-sql\\src\\main\\resources"

    val conf = new SparkConf()

    //在测试或者生产中，AppName和Master我们是通过脚本进行指定，需要注释掉
    conf.setMaster("local[2]").setAppName("SQLContextApplication")
    val sc = new SparkContext(conf)
    val context = new SQLContext(sc)

    val people = context.read.json(path);
    people.printSchema()
    people.show()

    // 关闭资源
    sc.stop()
  }

}
