package com.timebusker.test.spark_ml_practice.ch03

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * @Description: DecisionTreeClassifier 决策树分类
 * @Author: Administrator
 * @DateL: 2019/12/1 17:20
 **/
object DecisionTreeClassifierModel {

  // 设置应用日志级别
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[4]")
    conf.setAppName(this.getClass.getSimpleName)
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val data = spark.read.format("libsvm").load("D:\\test-data\\test\\ch02\\AppDataETL$_1576078692965\\part-00003")

    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

    val model = new DecisionTreeClassifier().setMaxMemoryInMB(2048).fit(trainingData)
    val predictions = model.transform(testData)

    import spark.implicits._
    predictions.groupBy("label").count().show()
    predictions.filter($"label" === $"prediction").groupBy("label").count().show()

    // 查看效果
    predictions.show(1000)
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
