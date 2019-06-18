package sync.bddg

import org.rosi_project.model_management.core.PlayerSync

abstract class Port(protected var name: String, protected var owner: BDD) extends PlayerSync {

  def getName(): String = {
    name
  }

  def setName(n: String): Unit = {
    name = n
    +this changeName ()
  }

  def getOwner(): BDD = {
    owner
  }

  def setOwner(o: BDD): Unit = {
    owner = o
    +this changeOwner ()
  }

  override def toString(): String = {
    "Port:" + " name=" + name
  }

}



    