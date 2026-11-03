package igentuman.mbtool.integration.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Passive material-list indicator. Uses Minecraft rendering instead of JEI's
 * internal button classes, which are not stable between JEI releases.
 */
public class IngredientsButton {
    private static final ItemStack ICON = new ItemStack(Items.BOOK);
    private Rect2i bounds = new Rect2i(0, 0, 10, 10);
    public MultiblockStructureRecipe recipe;

    public static IngredientsButton create(MultiblockStructureRecipe recipe) {
        return new IngredientsButton(recipe);
    }

    private IngredientsButton(MultiblockStructureRecipe recipe) {
        this.recipe = recipe;
    }

    public void updateBounds(Rect2i bounds) {
        this.bounds = bounds;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= bounds.getX() && mouseX < bounds.getX() + bounds.getWidth()
                && mouseY >= bounds.getY() && mouseY < bounds.getY() + bounds.getHeight();
    }

    public void draw(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().pushPose();
        try {
            graphics.pose().translate(bounds.getX(), bounds.getY(), 0);
            graphics.pose().scale(bounds.getWidth() / 16.0f, bounds.getHeight() / 16.0f, 1);
            graphics.renderItem(ICON, 0, 0);
        } finally {
            graphics.pose().popPose();
        }
    }

    public void drawTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        List<Component> lines = new ArrayList<>();
        for (ItemStack item : recipe.getIngredients().getItems()) {
            lines.add(Component.literal(item.getCount() + "x ").append(item.getHoverName()));
        }
        if (!lines.isEmpty()) {
            graphics.renderTooltip(Minecraft.getInstance().font, lines, Optional.empty(), mouseX, mouseY);
        }
    }
}
