package k2b6s9j.BoatCraft.boat.wood.oak

import k2b6s9j.BoatCraft.boat.Boat.{ItemCustomBoat, RenderBoat, EntityCustomBoat}
import k2b6s9j.BoatCraft.boat.Materials
import k2b6s9j.BoatCraft.boat.Modifiers

object TNT {

  class Entity extends EntityCustomBoat with Materials.Entity.Wood.Oak with Modifiers.Entity.TNT {

  }

  class Item extends ItemCustomBoat {

  }

  class Render extends RenderBoat with Materials.Render.Wood.Oak with Modifiers.Render.TNT {

    override def getEntity(): EntityCustomBoat = {
      val entity: Entity =  null
      return entity
    }

  }

}