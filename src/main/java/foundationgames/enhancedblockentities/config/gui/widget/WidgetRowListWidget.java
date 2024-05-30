package foundationgames.enhancedblockentities.config.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.render.RenderLayer;

import java.util.ArrayList;
import java.util.List;

public class WidgetRowListWidget extends ElementListWidget<WidgetRowListWidget.Entry> {
    public static final int SPACING = 3;

    public final int rowWidth;
    public final int rowHeight;

    public WidgetRowListWidget(MinecraftClient mc, int w, int h, int y, int rowWidth, int rowHeight) {
        super(mc, w, h, y, rowHeight + SPACING);
        this.rowWidth = rowWidth;
        this.rowHeight = rowHeight;
    }

    public void add(ClickableWidget ... widgets) {
        if (widgets.length == 0) return;

        var grid = new GridWidget();
        grid.setColumnSpacing(SPACING);
        var adder = grid.createAdder(widgets.length);

        int width = (this.rowWidth - ((widgets.length - 1) * SPACING)) / widgets.length;

        for (var widget : widgets) {
            widget.setDimensions(width, this.rowHeight);
            adder.add(widget);
        }

        grid.refreshPositions();

        this.addEntry(new Entry(grid));
    }

    @Override
    public int getRowWidth() {
        return rowWidth;
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.width - 6;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        context.fillGradient(RenderLayer.getGuiOverlay(), this.getX(), this.getY(), this.getRight(), this.getY() + 4, -16777216, 0, 0);
        context.fillGradient(RenderLayer.getGuiOverlay(), this.getX(), this.getBottom() - 4, this.getRight(), this.getBottom(), 0, -16777216, 0);
    }

    public static class Entry extends ElementListWidget.Entry<Entry> {
        private final GridWidget widget;
        private final List<ClickableWidget> children = new ArrayList<>();

        public Entry(GridWidget widget) {
            this.widget = widget;
            widget.forEachChild(children::add);
        }

        @Override
        public List<? extends Element> children() {
            return this.children;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.widget.setPosition(x - 3, y);
            this.widget.refreshPositions();

            this.widget.forEachChild(c -> c.render(context, mouseX, mouseY, tickDelta));
        }
    }
}
