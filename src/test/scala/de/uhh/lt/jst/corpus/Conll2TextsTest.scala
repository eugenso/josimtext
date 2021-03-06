package de.uhh.lt.jst.corpus

import com.holdenkarau.spark.testing.DatasetSuiteBase
import de.uhh.lt.testing.tags.NeedsMissingFiles
import org.scalatest._
import de.uhh.lt.jst.utils.Util

class Conll2TextsTest extends FlatSpec with Matchers  with DatasetSuiteBase {

  def run(inputPath:String): Unit = {
    val outputPath = inputPath + "-output"
    Util.delete(outputPath)
    val config = Conll2Texts.Config(inputPath, outputPath)
    Conll2Texts.run(spark, config)
  }

  it should "extract text from a conll file" in {
    val conllPath = getClass.getResource("/conll-1000-tokens.csv.gz").getPath
    run(conllPath)
  }

  it should "extract text from line" in {
    Conll2Texts.getText("") shouldEqual("")
    Conll2Texts.getText("# text = ") shouldEqual("")
    Conll2Texts.getText("# text = a") shouldEqual("a")
    Conll2Texts.getText("# text = This is some text. ") shouldEqual("This is some text.")
  }
}
