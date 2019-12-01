package com.timebusker.test.spark_ml_practice.ch03

import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.LinearSVC
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

/**
 * @Description :LinearSupportVectorMachineModel
 * @Author :Administrator
 * @Date: 2019/12/1 14:58
 **/
object LinearSupportVectorMachineModel {

  /**
   * spark只能支持线性的二分类
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[4]")
    conf.setAppName(this.getClass.getSimpleName)
    val spark = SparkSession.builder().config(conf).getOrCreate()

    // val data = spark.read.format("libsvm").load("D:\\test-data\\AppDataETL$_1575123346166\\part-*")
    // data.show
    // val df = data.toDF()
    // df.groupBy("label").count().show()

    // 设置训练集与测试集的比列
    // 随机拆分种子 seed
    // val Array(trainingData, testData) = data.randomSplit(Array(0.8, 0.2), seed = 1234L)

    // val lsvc = new LinearSVC()
    //   // 最大迭代轮次数
    //   .setMaxIter(10)
    //   // 回归参数，防止过拟合
    //   .setRegParam(0.2)
    //  requirement failed: LinearSVC only supports binary classification. 5 classes detected
    // 传入分类目标共有五类，不能使用二分类处理或者需要减少分类目标数---改造方案如下：
    // val model = lsvc.fit(trainingData)
    // model.transform(testData).show()

    // 1、一类对其他


    // 2、一对一分类---虽有优化，但大数据量下不建议使用
    val data = MLUtils.loadLibSVMFile(spark.sparkContext, "D:\\test-data\\AppDataETL$_1575123346166\\part-*").cache()
    //combinations(n: Int): Iterator[List[A]] 取列表中的n个元素进行组合，返回不重复的组合列表，结果一个迭代器
    // 得到十种结果组合
    val labels = data.map(_.label).distinct().collect().sorted.combinations(2).map(x => (x.mkString("_"), x))
    labels.foreach(println)

    // 多分类可以参考使用决策树分类算法
  }


}
