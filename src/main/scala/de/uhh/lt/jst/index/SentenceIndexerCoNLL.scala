package de.uhh.lt.jst.index

import de.uhh.lt.conll.CoNLLParser
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.sql.SparkSession
import org.elasticsearch.spark._

object SentenceIndexerCoNLL extends ElasticSearchIndexer with Serializable {

  override val description: String = "Index text of the sentences only from CoNLL file with ElasticSearch"

  override def run(spark: SparkSession, config: ConfigType): Unit = {
    val hadoopConfig = new Configuration
    hadoopConfig.set("textinputformat.record.delimiter", "\n\n")

    spark.sparkContext
      .newAPIHadoopFile(config.inputDir, classOf[TextInputFormat], classOf[LongWritable], classOf[Text], hadoopConfig)
      .map { record => record._2.toString }
      .map { CoNLLParser.parseSingleSentence }
      .map{ sentence => Map(
        "insert_id" -> config.insertID,
        "sentence_hash" -> sentence.hashCode,
        "document_id" -> sentence.documentID,
        "sentence_id" -> sentence.sentenceID,
        "text" -> sentence.text)
      }
      .saveToEs(config.outputIndex)
  }
}

/* Mapping:

PUT depcc
{
    "mappings": {
      "sentences": {
        "properties": {
          "document_id": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "insert_id": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "sentence_hash": {
            "type": "long"
          },
          "sentence_id": {
            "type": "long"
          },
          "text": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      }
    }
}
*/