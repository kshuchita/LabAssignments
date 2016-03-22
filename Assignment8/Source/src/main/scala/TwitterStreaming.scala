//import edu.umkc.fv.NLPUtils._
//import edu.umkc.fv.Utils._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
 * Created by pradyumnad on 07/07/15.
 */
object TwitterStreaming {

  def main(args: Array[String]) {


    

    val filters =args



    // Set the system properties so that Twitter4j library used by twitter stream
    // can use them to generate OAuth credentials

    System.setProperty("twitter4j.oauth.consumerKey", "FXNorKa8CAU1XQLE294uIcULo")
    System.setProperty("twitter4j.oauth.consumerSecret", "qLzNIBo62wI4D9EvxRng6x2IMkvkdevAk1XVcil01bkRh9Y4n4")
    System.setProperty("twitter4j.oauth.accessToken", "476949496-ioXB7frNiYDuY38qtOkRPvPYhARlmJ19QgNgCteO")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "PGQ0maBjQdGfSB8FmQch7Xi7elyWUv2hAhtkieRkkgnpz")

    //Create a spark configuration with a custom name and master
    // For more master configuration see  https://spark.apache.org/docs/1.2.0/submitting-applications.html#master-urls

    System.setProperty("hadoop.home.dir","F:\\winutils");

    val sparkConf = new SparkConf().setAppName("STweetsApp").setMaster("local[*]")
    //Create a Streaming COntext with 2 second window
    val ssc = new StreamingContext(sparkConf,Seconds(5))

    //Using the streaming context, open a twitter stream (By the way you can also use filters)
    //Stream generates a series of random tweets

    val stream = TwitterUtils.createStream(ssc, None, filters)
    stream.saveAsTextFiles("Output")

   // stream.saveAsTextFiles("F:\\Twitter")

       //Map : Retrieving Hash Tags

   //val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))


    /*if(stream.toString().contains("lang='es'")){
/*      val wc = stream.map(status => status.getText())
      wc.foreachRDD(rdd => {
        val count = rdd.count().toInt
        val lines = rdd.take(count)

        lines.foreach(line=>{  val sentimentAnalyzer: SentimentAnalyzer = new SentimentAnalyzer
          val tweetWithSentiment: TweetWithSentiment = sentimentAnalyzer.findSentiment(line)
          System.out.println(tweetWithSentiment)
        }

        )
      }
      )*/

    }*/

    /*  val wcs = stream.map(conc => conc.toString().contains("lang='en'"))

     val result = wc.flatMap(line=>{line.split(" ")}).map(word=>(word,1)).cache()
     val output=result.reduceByKey(_+_)*/

    val wc = stream.map(status => status.getText())


    wc.foreachRDD(rdd => {
      val count = rdd.count().toInt
      val lines = rdd.take(count)

      lines.foreach(line=>{  val sentimentAnalyzer: SentimentAnalyzer = new SentimentAnalyzer
        val tweetWithSentiment: TweetWithSentiment = sentimentAnalyzer.findSentiment(line)
        System.out.println(tweetWithSentiment)
      }

     )
     }
    )


    ssc.start()


    ssc.awaitTermination()



    // SocketClient.sendCommandToRobot("here it is")
  }
}
