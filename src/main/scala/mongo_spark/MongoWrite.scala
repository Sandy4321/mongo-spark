package mongo_spark

import org.apache.spark.SparkContext._
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.rdd.RDD
import org.apache.hadoop.conf.Configuration
import org.bson.BSONObject
import com.mongodb.hadoop.MongoInputFormat
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import com.mongodb.hadoop.BSONFileInputFormat

object MongoWrite {
  def writeBsonRDD[T](rdd: RDD[T], path: String, format: String = "serialized_file") = {
    val config = new Configuration()
    format match {
      case "serialized_file" => {
        rdd.saveAsObjectFile(path)
      }
      case "bson" => {
        rdd
          .asInstanceOf[RDD[(Object, BSONObject)]]
          .saveAsNewAPIHadoopFile(path, classOf[Any], classOf[Any], classOf[com.mongodb.hadoop.BSONFileOutputFormat[Any, Any]])
      }
      case "mongodb" => {
        config.set("mongo.output.uri", path)
        rdd
          .asInstanceOf[RDD[(Object, BSONObject)]]
          .saveAsNewAPIHadoopFile("file:///dummy", classOf[Any], classOf[Any], classOf[com.mongodb.hadoop.MongoOutputFormat[Any, Any]], config)
      }
    }
  }
}