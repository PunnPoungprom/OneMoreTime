package net.nopunintendeds.onemoretime.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.nopunintendeds.onemoretime.OneMoreTime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "applyDamage", at = @At(value = "HEAD"), cancellable = true)
	private void adjustFinalHealth(DamageSource source, float amount, CallbackInfo ci) {
		boolean inList  = source.isOf(DamageTypes.EXPLOSION) || source.isOf(DamageTypes.FALL) ||
				source.isOf(DamageTypes.PLAYER_ATTACK) ||
				source.isOf(DamageTypes.MOB_ATTACK) ||
				source.isOf(DamageTypes.MAGIC) ||
				source.isOf(DamageTypes.MOB_PROJECTILE) ||
				source.isOf(DamageTypes.PLAYER_EXPLOSION);

		if(this.getHealth() - amount <= 0.0f && this.getHealth() >= 12f && inList) {
			ci.cancel();
			this.setHealth(1f);
		}
	}
}