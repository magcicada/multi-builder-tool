package igentuman.mbtool.integration.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IngredientsButton {
    private static final ResourceLocation RECIPE_BUTTON =
            new ResourceLocation("minecraft", "textures/gui/recipe_button.png");

    private static final int TEX_W = 20;
    private static final int TEX_H = 18;
    private static final int HOVER_V = 19;

    private Rect2i area = new Rect2i(0, 0, 10, 9);
    public MultiblockStructureRecipe recipe;

    public static IngredientsButton create(MultiblockStructureRecipe recipe) {
        return new IngredientsButton(recipe);
    }

    private IngredientsButton(MultiblockStructureRecipe recipe) {
        this.recipe = recipe;
    }

    public void updateBounds(Rect2i area) {
        this.area = area;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= area.getX()
                && mouseX < area.getX() + area.getWidth()
                && mouseY >= area.getY()
                && mouseY < area.getY() + area.getHeight();
    }

    public void draw(GuiGraphics graphics, double mouseX, double mouseY) {
        int v = isMouseOver(mouseX, mouseY) ? HOVER_V : 0;

        graphics.pose().pushPose();
        try {
            graphics.pose().translate(area.getX(), area.getY(), 0);
            graphics.pose().scale(
                    area.getWidth() / (float) TEX_W,
                    area.getHeight() / (float) TEX_H,
                    1.0F
            );
            graphics.blit(
                    RECIPE_BUTTON,
                    0, 0,
                    0, v,
                    TEX_W, TEX_H,
                    256, 256
            );
        } finally {
            graphics.pose().popPose();
        }
    }

    public void drawTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        List<Component> lines = new ArrayList<>();

        for (ItemStack item : recipe.getIngredients().getItems()) {
            lines.add(
                    Component.literal(item.getCount() + "x ")
                            .append(item.getHoverName())
            );
        }

        if (!lines.isEmpty()) {
            graphics.renderComponentTooltip(
                    Minecraft.getInstance().font,
                    lines,
                    mouseX,
                    mouseY
            );
        }
    }
}