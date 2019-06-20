package ttc2019.worksum

import sum.bddg._

import ttc2019.metamodels.create.BddCreationHelper
import org.rosi_project.model_management.core.ModelElementLists
import ttc2019.IWriteOutputModel

object WriteSumBddOutput extends IWriteOutputModel {
  
  def generateEverything(outputFile: String): Unit = {
    val creation = new BddCreationHelper()
    
    val pkgName = "sum.bddg."
    
    val bd = ModelElementLists.getDirectElementsFromType(pkgName + "BDD").asInstanceOf[Set[BDD]]
    val in = ModelElementLists.getDirectElementsFromType(pkgName + "InputPort").asInstanceOf[Set[InputPort]]
    val ou = ModelElementLists.getDirectElementsFromType(pkgName + "OutputPort").asInstanceOf[Set[OutputPort]]
    val as = ModelElementLists.getDirectElementsFromType(pkgName + "Assignment").asInstanceOf[Set[Assignment]]
    val le = ModelElementLists.getDirectElementsFromType(pkgName + "Leaf").asInstanceOf[Set[Leaf]]
    val su = ModelElementLists.getDirectElementsFromType(pkgName + "Subtree").asInstanceOf[Set[Subtree]]
    
    val apo = ModelElementLists.getDirectElementsFromType(pkgName + "AssignmentPortOutputPort").asInstanceOf[Set[AssignmentPortOutputPort]]
    val bpp = ModelElementLists.getDirectElementsFromType(pkgName + "BDDPortsPort").asInstanceOf[Set[BDDPortsPort]]
    val btt = ModelElementLists.getDirectElementsFromType(pkgName + "BDDTreesTree").asInstanceOf[Set[BDDTreesTree]]
    val brt = ModelElementLists.getDirectElementsFromType(pkgName + "BDDRootTree").asInstanceOf[Set[BDDRootTree]]
    val laa = ModelElementLists.getDirectElementsFromType(pkgName + "LeafAssignmentsAssignment").asInstanceOf[Set[LeafAssignmentsAssignment]]
    val spi = ModelElementLists.getDirectElementsFromType(pkgName + "SubtreePortInputPort").asInstanceOf[Set[SubtreePortInputPort]]
    val szt = ModelElementLists.getDirectElementsFromType(pkgName + "SubtreeTreeForZeroTree").asInstanceOf[Set[SubtreeTreeForZeroTree]]
    val sot = ModelElementLists.getDirectElementsFromType(pkgName + "SubtreeTreeForOneTree").asInstanceOf[Set[SubtreeTreeForOneTree]]
    
    /*println(bd)
    println(in)
    println(ou)
    println(as)
    println(le)
    println(su)
    
    println(apo)
    println(bpp)
    println(btt)
    println(brt)
    println(laa)
    println(spi)
    println(szt)
    println(sot)*/
    
    //add normal instances
    bd.foreach(o => {
      creation.createBDD(o, o.getName())
    })
    in.foreach(o => {
      creation.createInputPort(o, o.getName())
    })
    ou.foreach(o => {
      creation.createOutputPort(o, o.getName())
    })
    as.foreach(o => {
      creation.createAssignment(o, o.getValue())
    })
    le.foreach(o => {
      creation.createLeaf(o)
    })
    su.foreach(o => {
      creation.createSubtree(o)
    })    
    
    var setLeafs: Set[Leaf] = Set.empty
    var setAssignment: Set[Assignment] = Set.empty
    
    //add connections    
    bpp.foreach(o => {
      creation.addBDDPortConnection(o.getTargetIns())
    })
    brt.foreach(o => {
      creation.addBDDRootConnection(o.getTargetIns())
    })
    btt.foreach(o => {
      creation.addBDDTreesConnection(o.getTargetIns())
      if (o.getTargetIns().isInstanceOf[Leaf]) {
        setLeafs += o.getTargetIns().asInstanceOf[Leaf]
      }
    })
    spi.foreach(o => {
      creation.addInputSubtreeConnection(o.getTargetIns(), o.getSourceIns())
    })
    szt.foreach(o => {
      creation.addSubtreeZeroTreeConnection(o.getTargetIns(), o.getSourceIns())
      if (o.getTargetIns().isInstanceOf[Leaf]) {
        setLeafs += o.getTargetIns().asInstanceOf[Leaf]
      }
    })
    sot.foreach(o => {
      creation.addSubtreeOneTreeConnection(o.getTargetIns(), o.getSourceIns())
      if (o.getTargetIns().isInstanceOf[Leaf]) {
        setLeafs += o.getTargetIns().asInstanceOf[Leaf]
      }
    })
    laa.filter(o => setLeafs.contains(o.getSourceIns())).foreach(o => {
      creation.addLeafAssignmentConnection(o.getSourceIns(), o.getTargetIns())
      setAssignment += o.getTargetIns()
    })
    //critical only push assignments that are connected
    apo.filter(o => setAssignment.contains(o.getSourceIns())).foreach(o => {
      creation.addOutputAssignmentConnection(o.getTargetIns(), o.getSourceIns())
    })
    
    //generate output file
    creation.generate(outputFile)
  }
}