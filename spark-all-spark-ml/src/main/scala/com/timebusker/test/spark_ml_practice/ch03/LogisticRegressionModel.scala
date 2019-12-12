package com.timebusker.test.spark_ml_practice.ch03

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession

/**
 * @Description:LogisticRegressionModel
 * @Author:Administrator
 * @Date2019/12/11 17:19
 **/
object LogisticRegressionModel {

  // 设置应用日志级别
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[4]")
    conf.setAppName(this.getClass.getSimpleName)
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val data = spark.read.format("libsvm").load("D:\\test-data\\test\\ch02\\AppDataETL$_1576078692965\\part-00003")
    data.show
    val df = data.toDF()
    df.groupBy("label").count().show()

    // 二分类逻辑回归
    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    import spark.implicits._

    val Array(transData, testData) = data.randomSplit(Array(0.7, 0.3))
    val lrModel = lr.fit(transData)
    val predictions = lrModel.transform(testData)
    predictions.groupBy("label").count().show()
    predictions.filter($"label" === $"prediction").groupBy("label").count().show()

    // 模型评估器
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")

    val accuracy = evaluator.evaluate(predictions)
    println("Accuracy: " + accuracy)

    println("****************************************************************************************************")
    println("****************************************************************************************************")
    println("****************************************************************************************************")

    // 多分类逻辑回归
    val mlr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)
      .setFamily("multinomial")

    val mlrModel = mlr.fit(transData)
    val mPredictions = lrModel.transform(testData)
    mPredictions.filter($"label" === $"prediction").count()

    // 模型评估器
    val mEvaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")

    val mAccuracy = mEvaluator.evaluate(mPredictions)
    println("Accuracy: " + mAccuracy)

    spark.stop
  }

}
