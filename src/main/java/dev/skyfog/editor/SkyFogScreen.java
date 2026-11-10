package dev.skyfog.editor;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public final class SkyFogScreen extends Screen {
    private final Screen parent;

    private static final int[] SKY_PRESETS = {
            0x7657D5, 0x4A90E2, 0xFF8A65, 0x1C2541, 0x65C18C, 0xF2C14E
    };
    private static final int[] FOG_PRESETS = {
            0x8BC6E8, 0xB39DDB, 0xF6A6B2, 0xA7D8A8, 0xD6D6D6, 0xF4D35E
    };
    private int skyIndex;
    private int fogIndex;

    public SkyFogScreen(Screen parent) {
        super(Text.literal("Sky & Fog Editor"));
        this.parent = parent;
        skyIndex = findIndex(SKY_PRESETS, SkyFogConfig.skyColor);
        fogIndex = findIndex(FOG_PRESETS, SkyFogConfig.fogColor);
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int top = this.height / 2 - 74;
        int buttonWidth = 190;

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Небо: " + hex(SkyFogConfig.skyColor)),
                button -> {
                    skyIndex = (skyIndex + 1) % SKY_PRESETS.length;
                    SkyFogConfig.skyColor = SKY_PRESETS[skyIndex];
                    button.setMessage(Text.literal("Небо: " + hex(SkyFogConfig.skyColor)));
                    SkyFogConfig.save();
                }).dimensions(center - buttonWidth / 2, top, buttonWidth, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Туман: " + hex(SkyFogConfig.fogColor)),
                button -> {
                    fogIndex = (fogIndex + 1) % FOG_PRESETS.length;
                    SkyFogConfig.fogColor = FOG_PRESETS[fogIndex];
                    button.setMessage(Text.literal("Туман: " + hex(SkyFogConfig.fogColor)));
                    SkyFogConfig.save();
                }).dimensions(center - buttonWidth / 2, top + 27, buttonWidth, 20).build());

        addDrawableChild(new StrengthSlider(
                center - buttonWidth / 2, top + 54, buttonWidth, 20, SkyFogConfig.fogStrength));

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Ефекти: " + (SkyFogConfig.enabled ? "УВІМКНЕНО" : "ВИМКНЕНО")),
                button -> {
                    SkyFogConfig.enabled = !SkyFogConfig.enabled;
                    button.setMessage(Text.literal("Ефекти: " + (SkyFogConfig.enabled ? "УВІМКНЕНО" : "ВИМКНЕНО")));
                    SkyFogConfig.save();
                }).dimensions(center - buttonWidth / 2, top + 82, buttonWidth, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Готово"), button -> {
            SkyFogConfig.save();
            this.client.setScreen(parent);
        }).dimensions(center - buttonWidth / 2, top + 112, buttonWidth, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int center = this.width / 2;
        int top = this.height / 2 - 96;
        context.fill(center - 124, top, center + 124, top + 236, 0xE91B1D2B);
        context.fill(center - 124, top, center + 124, top + 3, 0xFF8B6CFF);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, center, top + 12, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Натисни O, щоб відкрити меню"), center, top + 214, 0xFFB8B8C8);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        SkyFogConfig.save();
        this.client.setScreen(parent);
    }

    private static int findIndex(int[] values, int color) {
        for (int i = 0; i < values.length; i++) if (values[i] == color) return i;
        return 0;
    }

    private static String hex(int color) {
        return String.format("#%06X", color & 0xFFFFFF);
    }

    private static final class StrengthSlider extends SliderWidget {
        StrengthSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Text.empty(), value);
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Text.literal("Сила туману: " + Math.round(value * 100) + "%"));
        }

        @Override
        protected void applyValue() {
            SkyFogConfig.fogStrength = (float) value;
            SkyFogConfig.save();
        }
    }
}
