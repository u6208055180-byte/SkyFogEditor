package dev.skyfog.editor.mixin;

import dev.skyfog.editor.SkyFogConfig;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.WorldEnvironmentAttributeAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Inject(method = "addClientSideAttributes", at = @At("RETURN"), cancellable = true)
    private void skyfog$overrideVisualAttributes(
            WorldEnvironmentAttributeAccess.Builder builder,
            CallbackInfoReturnable<WorldEnvironmentAttributeAccess.Builder> cir) {

        WorldEnvironmentAttributeAccess.Builder updated = cir.getReturnValue()
                .constant(EnvironmentAttributes.SKY_COLOR_VISUAL,
                        original -> SkyFogConfig.enabled ? SkyFogConfig.skyColor : original)
                .constant(EnvironmentAttributes.FOG_COLOR_VISUAL,
                        original -> SkyFogConfig.enabled ? SkyFogConfig.fogColor : original);

        cir.setReturnValue(updated);
    }
}
