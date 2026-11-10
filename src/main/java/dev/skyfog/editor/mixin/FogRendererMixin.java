package dev.skyfog.editor.mixin;

import dev.skyfog.editor.SkyFogConfig;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Inject(method = "getFogColor", at = @At("RETURN"), cancellable = true)
    private void skyfog$changeFog(Camera camera, float tickDelta, ClientWorld world,
                                  int viewDistance, float skyDarkness,
                                  CallbackInfoReturnable<Vector4f> cir) {
        if (!SkyFogConfig.enabled) return;
        Vector4f original = cir.getReturnValue();
        int color = SkyFogConfig.fogColor;
        float strength = SkyFogConfig.fogStrength;
        float red = ((color >> 16) & 255) / 255.0f;
        float green = ((color >> 8) & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;
        cir.setReturnValue(new Vector4f(
                original.x + (red - original.x) * strength,
                original.y + (green - original.y) * strength,
                original.z + (blue - original.z) * strength,
                original.w
        ));
    }
}
