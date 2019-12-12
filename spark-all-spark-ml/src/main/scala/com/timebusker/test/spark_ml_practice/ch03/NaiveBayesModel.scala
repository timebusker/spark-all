package com.timebusker.test.spark_ml_practice.ch03

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * @Description: NaiveBayesModel朴素贝叶斯模型分类
 * @Author: Administrator
 * @Date: 2019/11/30 22:23
 **/
object NaiveBayesModel {

  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[*]")
    conf.setAppName(this.getClass.getSimpleName)
    val spark = SparkSession.builder().config(conf).getOrCreate()

    // val data = spark.read.textFile("D:\\test-data\\AppDataETL$_1575123346166\\part-*")
    val data = spark.read.format("libsvm").load("D:\\test-data\\test\\ch02\\AppDataETL$_1576078692965\\part-00003")
    data.show
    val df = data.toDF()
    df.groupBy("label").count().show()

    // 设置训练集与测试集的比列
    // 随机拆分种子 seed
    val Array(trainingData, testData) = data.randomSplit(Array(0.8, 0.2), seed = 1234L)
    // 训练模型
    val model = new NaiveBayes().fit(trainingData)
    // 测试模型--预测
    val predictions = model.transform(testData)
    // 查看效果
    import spark.implicits._
    predictions.groupBy("label").count().show()
    predictions.filter($"label" === $"prediction").groupBy("label").count().show()

    // 模型评估器
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")

    val accuracy = evaluator.evaluate(predictions)
    println("Accuracy: " + accuracy)

    model.write.overwrite.save("D:\\test-data\\" + this.getClass.getSimpleName)
    spark.stop()
  }
}
