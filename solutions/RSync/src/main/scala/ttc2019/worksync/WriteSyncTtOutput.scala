package ttc2019.worksync

import sync.tt._
import ttc2019.metamodels.create.TtCreationHelper
import org.rosi_project.model_management.core.ModelElementLists
import ttc2019.IWriteOutputModel

object WriteSyncTtOutput extends IWriteOutputModel {
  def generateEverything(outputFile: String): Unit = {
    val creation = new TtCreationHelper()
    
    val pkgName = "sync.tt."
    
    val tt = ModelElementLists.getDirectElementsFromType(pkgName + "TruthTable").asInstanceOf[Set[TruthTable]]
    val in = ModelElementLists.getDirectElementsFromType(pkgName + "InputPort").asInstanceOf[Set[InputPort]]
    val ou = ModelElementLists.getDirectElementsFromType(pkgName + "OutputPort").asInstanceOf[Set[OutputPort]]
    val ro = ModelElementLists.getDirectElementsFromType(pkgName + "Row").asInstanceOf[Set[Row]]
    val ce = ModelElementLists.getDirectElementsFromType(pkgName + "Cell").asInstanceOf[Set[Cell]]
    
    /*println(tt)
    println(in)
    println(ou)
    println(ro)
    println(ce)*/
    
    //add normal instances
    tt.foreach(o => {
      creation.createTT(o, o.getName())
    })
    in.foreach(o => {
      creation.createInputPort(o, o.getName())
    })
    ou.foreach(o => {
      creation.createOutputPort(o, o.getName())
    })
    ro.foreach(o => {
      creation.createRow(o)
    })
    ce.foreach(o => {
      creation.createCell(o, o.getValue())
    })     
    
    //add connections 
    tt.foreach(o => {
      o.getPorts().foreach(p => {
        creation.addTTPortConnection(p)
      })
      o.getRows().foreach(r => {
        creation.addTTRowConnection(r)
      })
    })
    in.foreach(o => {
      o.getCells().foreach(c => {
        creation.addPortCellsConnection(o, c)
      })
    })
    ou.foreach(o => {
      o.getCells().foreach(c => {
        creation.addPortCellsConnection(o, c)
      })
    })
    ro.foreach(o => {
      o.getCells().foreach(c => {
        creation.addRowCellsConnection(o, c)
      })
    })    
    
    //generate output file
    creation.generate(outputFile)
  }
}