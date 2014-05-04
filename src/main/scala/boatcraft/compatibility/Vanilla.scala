package boatcraft.compatibility

import java.util.ArrayList

import scala.collection.JavaConversions.{asScalaBuffer, mapAsScalaMap}

import boatcraft.api.Registry
import boatcraft.api.boat.ItemCustomBoat
import boatcraft.api.traits.{Block, Material}
import boatcraft.compatibility.vanilla.VanillaGuiHandler
import boatcraft.compatibility.vanilla.materials.crystal.{Diamond, Emerald, Obsidian}
import boatcraft.compatibility.vanilla.materials.metal.{Gold, Iron}
import boatcraft.compatibility.vanilla.materials.wood.{Acacia, Birch, DarkOak, Jungle, Oak, OreDict_Wood, Spruce}
import boatcraft.compatibility.vanilla.blocks.{Furnace, Chest, Workbench}
import boatcraft.core.{BoatCraft, GUIHandler}
import boatcraft.core.utilities.Recipes
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{CraftingManager, IRecipe}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.oredict.ShapedOreRecipe
import boatcraft.core.blocks.Empty

object Vanilla extends CompatModule {
  private var useOreDictWood = false

  override def doPreInit(event: FMLPreInitializationEvent) {
    readConfig

    replaceBoatRecipe

    GUIHandler.handlerMap.put(code, VanillaGuiHandler)
  }

  override def doPostInit(event: FMLPostInitializationEvent) {
    var toRemove = new ArrayList[IRecipe]

    for (recipe <- CraftingManager.getInstance getRecipeList) {
      if (recipe.isInstanceOf[IRecipe]
        && !recipe.isInstanceOf[ShapedOreRecipe]
        && recipe.asInstanceOf[IRecipe].getRecipeOutput != null
        && recipe.asInstanceOf[IRecipe].getRecipeOutput.stackTagCompound != null
        && recipe.asInstanceOf[IRecipe].getRecipeOutput.stackTagCompound.getString("material")
        != null
        && recipe.asInstanceOf[IRecipe].getRecipeOutput.stackTagCompound.getString("material")
        .equals(OreDict_Wood toString)) {
        toRemove add recipe.asInstanceOf[IRecipe]
        log info "Removed non-oredict ore-dict wood boat recipe: " + recipe
      }
    }

    CraftingManager.getInstance.getRecipeList removeAll toRemove
  }

  override protected def getMaterials: List[Material] = {
    val materials: List[Material] = List[Material]()
    if (!useOreDictWood) {
      materials ++ woodMaterials
    }
    else {
      materials.::(OreDict_Wood)

      var stack = new ItemStack(ItemCustomBoat)
      stack.stackTagCompound = new NBTTagCompound
      stack.stackTagCompound setString("material", OreDict_Wood toString)

      for ((name, modifier) <- Registry.blocks) {
        stack.stackTagCompound setString("modifier", name)
        if (modifier.getContent != null)
          GameRegistry.addRecipe(new ShapedOreRecipe(stack,
            "wmw", "www",
            Character valueOf 'w', "planksWood",
            Character valueOf 'm', modifier getContent))
        else
          GameRegistry.addRecipe(new ShapedOreRecipe(stack,
            "w w", "www",
            Character valueOf 'w', "planksWood"))
      }
    }
    materials ++ List[Material] (
      Iron,
      Gold,
      Obsidian,
      Diamond,
      Emerald
    )

    materials //TODO: HOLY SHIT... Do I need to simplify that or what?
  }

  private val woodMaterials: List[Material] = List[Material] (
    Oak,
    Spruce,
    Birch,
    Jungle,
    Jungle,
    Acacia,
    DarkOak
  )

  override protected def getModifiers: List[Block] =
    List[Block](
      Chest,
      Furnace,
      Workbench)

  private def replaceBoatRecipe {
    Recipes removeRecipe new ItemStack(Items.boat)

    var stack = new ItemStack(ItemCustomBoat)

    stack.stackTagCompound = new NBTTagCompound
    stack.stackTagCompound setString("material", Oak toString)
    stack.stackTagCompound setString("block", Empty toString)

    GameRegistry addShapelessRecipe(stack, Items.boat)
  }

  private def readConfig {
    useOreDictWood = BoatCraft.config get("Vanilla.General", "useOreDictWoods", false,
      "If set to true, the different wood types will not be generated.\n" +
        "Instead, there will be only one \"wood\" Boat") getBoolean false

    if (BoatCraft.config hasChanged)
      BoatCraft.config save
  }
}
