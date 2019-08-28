package com.timebusker.scala.starter

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive.HiveContext

/**
  * @DESC:HiveContextApplication：使用HiveContext测试
  * @author:timebusker
  * @date:2019/8/27
  */
object HiveContextApplication {

  /**
    * spark1.X，可以使用HiveContext创建获取上下文信息并获取hive中元数据来执行sql
    * @param args
    */
  def main(args: Array[String]): Unit = {

    val path = "D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-spark-sql\\src\\main\\resources"

    //在测试或者生产中，AppName和Master我们是通过脚本进行指定，需要注释掉
    val conf = new SparkConf()
    conf.setAppName("HiveContextApplication").setMaster("local[2]")

    val sc = new SparkContext()
    val hive = new HiveContext(sc);

    // 直接插叙操作hive中的表
    hive.table("tb_sub_sougou").show()
    hive.sql("select * from tb_sub_sougou").show()

    // 关闭资源
    sc.stop()
  }
}
