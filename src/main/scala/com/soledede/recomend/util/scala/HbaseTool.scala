
package com.soledede.recomend.util.scala

/**
 * Created by wengbenjue on 2014/9/15.
 */

import java.io.{IOException, ObjectInputStream, ByteArrayInputStream}

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp
import org.apache.hadoop.hbase.filter.{RowFilter, RegexStringComparator, Filter}
import org.apache.hadoop.hbase.util.Bytes
import scala.collection.mutable
import  scala.collection.JavaConversions._


object HbaseTool {

  val table = new mutable.HashMap[String,HTable]()
  var conf = HBaseConfiguration.create()

  def setConf(c:Configuration)={
    conf = c
  }

  def getTable(tableName:String):HTable={

    table.getOrElse(tableName,{
      println("----new connection ----")
      val tbl = new HTable(conf, tableName)
      table(tableName)= tbl
      tbl
    })
  }

  def getSingleValue(tableName:String,rowKey:String,family:String,qualifiers:String):AnyRef={
    val table_t =getTable(tableName)
    val row1 =  new Get(Bytes.toBytes(rowKey))
    val HBaseRow = table_t.get(row1)
    var obh:AnyRef  = null
      //new JSONArray[List[String]]
    //var bv = new Array[Byte](0)
   // var bv = new String
    if(HBaseRow != null && !HBaseRow.isEmpty){
     val b = new ByteArrayInputStream(HBaseRow.getValue(Bytes.toBytes(family), Bytes.toBytes(qualifiers)))
      val o = new ObjectInputStream(b)
      obh = o.readObject()
       o.close()
      //bv = new String(HBaseRow.getValue(Bytes.toBytes(family), Bytes.toBytes(qualifiers)),"UTF-8")
      }
      obh
  }

  def getValue(tableName:String,rowKey:String,family:String,qualifiers:Array[String]):Array[(String,String)]={
    var result:AnyRef = null
    val table_t =getTable(tableName)
    val row1 =  new Get(Bytes.toBytes(rowKey))
    val HBaseRow = table_t.get(row1)
    if(HBaseRow != null && !HBaseRow.isEmpty){
      result = qualifiers.map(c=>{
        (tableName+"."+c, Bytes.toString(HBaseRow.getValue(Bytes.toBytes(family), Bytes.toBytes(c))))
      })
    }
    else{
      result=qualifiers.map(c=>{
        (tableName+"."+c,"null")  })
    }
    result.asInstanceOf[Array[(String,String)]]
  }

  def getMapSingleValue(tableName:String,rowKey:String,family:String,qualifiers:String):String={
    var result:AnyRef = null
    val table_t =getTable(tableName)
    val row1 =  new Get(Bytes.toBytes(rowKey))
    val HBaseRow = table_t.get(row1)
    if(HBaseRow != null && !HBaseRow.isEmpty) {
      result = Bytes.toString(HBaseRow.getValue(Bytes.toBytes(family), Bytes.toBytes(qualifiers)))
    }
    result.asInstanceOf[String]
  }

  def putValue(tableName:String,rowKey:String, family:String,qualifierValue:Array[(String,String)]) {
    val table =getTable(tableName)
    val new_row  = new Put(Bytes.toBytes(rowKey))
    qualifierValue.map(x=>{
      var column = x._1
      val value = x._2
      val tt = column.split("\\.")
      if (tt.length == 2) column=tt(1)
      if(!(value.isEmpty))
        new_row.add(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value))
    })
    table.put(new_row)
  }

  def scanLike(tableName:String,words: String):AnyRef={
    val table =getTable(tableName)
    val scan: Scan= new Scan();
    val filter = new RowFilter(CompareOp.EQUAL,new RegexStringComparator(".*"+words));
    scan.setFilter(filter);
     var scanner:ResultScanner= null
    var obh:AnyRef = null
    try {
      scanner = table.getScanner(scan);

    for(res <- scanner){
      val cellList = res.listCells()
      val b = new ByteArrayInputStream(cellList.get(0).getValueArray)
      val o = new ObjectInputStream(b)
      obh = o.readObject()
      o.close()
    }

    } catch {
      case e: Exception => e.printStackTrace()
    }
    obh
  }

  def close(tableName: String) =getTable(tableName).close()

  val family = "F"
}



