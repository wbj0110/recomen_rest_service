package com.copal.recomend.util.scala

import scala.List
import scala.collection.JavaConversions._
import scala.util.parsing.json.JSONArray

import org.apache.hadoop.hbase.HBaseConfiguration

import com.copal.recomend.util.scala.HbaseTool

/**
 * Provides DAL for Customer entities for MySQL database.
 */
object ModuleDAO{
  private  var hb = HbaseTool
}

class ModuleDAO  {

  {
    //lond the config of Hbase，create Table recomend
    var confHbase = HBaseConfiguration.create()
    confHbase.set("hbase.zookeeper.property.clientPort", "2181")
    confHbase.set("hbase.zookeeper.quorum", "spark1.xiaomishu.com,spark2.xiaomishu.com,spark4.xiaomishu.com,spark9.xiaomishu.com,spark10.xiaomishu.com")
    confHbase.set("hbase.master", "h1.xiaomishu.com:60000")
    confHbase.addResource("/opt/cloudera/parcels/CDH/lib/hbase/conf/hbase-site.xml")
    ModuleDAO.hb.setConf(confHbase)
  }




  /**
   * Retrieves specific customer from database.
   *
   * @param id id of the customer to retrieve
   * @return customer entity with specified id
   */
  def get(cityid:Long,id: String):java.util.List[String] = {
    
    val jsonaAyr:JSONArray = ModuleDAO.hb.getSingleValue("recomend",id,"top","resid")
    if(jsonaAyr==null || jsonaAyr.list.length<=0) return null
     val list = jsonaAyr.list
      val results = new java.util.ArrayList[String]()
     // var resIdList =  for(resNum <- list;if(resNum != null)) yield ModuleDAO.hb.getMapSingleValue("mapping",resNum.toString,"res","resid")
     // resIdList =  resIdList.filter(resid => resid != null)
      //val  resIdList =  list.filter(resid => resid != null)
      //ModuleDAO.hb.getMapSingleValue("mapping",id,"res","userid")
      //list.foreach(println)
     //Left(notFoundError(id))

     var returnList = for(res <- list) yield {
       val stringRes:String = String.valueOf(res)
        val resArray = stringRes.split("::")
       val city_id = resArray(1)
        if(city_id.toString.trim.equals(cityid.toString.trim)) resArray(0).toString
         else null
      }
     
     returnList = for(resid <- returnList;if(resid!=null)) yield resid
    returnList.foreach { r => results.add(r.toString()) }
    results.toList
  }
  
 
def test(){
  println("yesk")
}
}




