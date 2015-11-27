package com.soledede.recomend.util.scala

import com.alibaba.fastjson.JSON

import scala.List
import scala.collection.JavaConversions._

import org.apache.hadoop.hbase.HBaseConfiguration

import HbaseTool

/**
  * Provides DAL for Customer entities for MySQL database.
  */
object ModuleDAO {
  private var hb = HbaseTool
}

class ModuleDAO {

  {
    //lond the config of Hbase，create Table recomend
    var confHbase = HBaseConfiguration.create()
    confHbase.set("hbase.zookeeper.property.clientPort", "2181")
    confHbase.set("hbase.zookeeper.quorum", "spark1.soledede.com,spark2.soledede.com,spark3.soledede.com")
    confHbase.set("hbase.master", "spark1.soledede.com:60000")
    confHbase.addResource("/opt/cloudera/parcels/CDH/lib/hbase/conf/hbase-site.xml")
    ModuleDAO.hb.setConf(confHbase)
  }


  /**
    *
    * @param id
    * @return
    */
  def get(id: String): java.util.List[String] = {

    //val jsonaAyr:JSONArray = ModuleDAO.hb.getSingleValue("recomend",id,"fruitTopCF","fruitId")

    val jsonStr: String = ModuleDAO.hb.getSingleValue("recomend", id, "fruitTopCF", "fruitId").toString
    val jsonaAyr = JSON.parseArray(jsonStr)
    if (jsonaAyr == null || jsonaAyr.length <= 0) return null
    val results = new java.util.ArrayList[String]()
    var returnList = for (item <- jsonaAyr) yield {
      val stringItem: String = String.valueOf(item)
      val resArray = stringItem.split("::")
      resArray(0)
    }

    returnList = for (itemid <- returnList; if (itemid != null)) yield itemid
    returnList.foreach { r => results.add(r) }
    results.toList
  }


  def test() {
    println("yesk")
  }
}




