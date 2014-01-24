package k2b6s9j.BoatCraft.core.utilities

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{ShapelessRecipes, ShapedRecipes, CraftingManager, IRecipe}
import java.util
import k2b6s9j.BoatCraft.core.BoatCraft

object Recipes {

  def RemoveRecipe(resultItem: ItemStack) {
    var recipeResult: ItemStack = null
    val recipes = CraftingManager.getInstance.getRecipeList.asInstanceOf[util.ArrayList[Any]]
    for (scan <- 0 until recipes.size) {
      val tmpRecipe = recipes.get(scan).asInstanceOf[IRecipe]
      if (tmpRecipe.isInstanceOf[ShapedRecipes])
      {
        val recipe: ShapedRecipes = tmpRecipe.asInstanceOf[ShapedRecipes]
        recipeResult = recipe.getRecipeOutput
      }

      if (tmpRecipe.isInstanceOf[ShapelessRecipes])
      {
        val recipe: ShapelessRecipes = tmpRecipe.asInstanceOf[ShapelessRecipes]
        recipeResult = recipe.getRecipeOutput
      }

      if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
      {
        BoatCraft.log.info("Removed Recipe: " + recipes.get(scan) + " -> " + recipeResult)
        recipes.remove(scan)
      }
    }
  }
}