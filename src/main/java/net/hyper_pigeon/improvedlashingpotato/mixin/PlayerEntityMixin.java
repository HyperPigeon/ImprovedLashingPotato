package net.hyper_pigeon.improvedlashingpotato.mixin;

import net.hyper_pigeon.improvedlashingpotato.Whiplash;
import net.minecraft.class_9508;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    private class_9508 field_50477;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/util/math/Vec3d.subtract (Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", shift = At.Shift.BEFORE), cancellable = true)
    public void grappleMob(CallbackInfo ci){
        Whiplash whiplash = (Whiplash) field_50477;
        if(whiplash.getHookedEntity() != null) {
            if(this.squaredDistanceTo(whiplash.getHookedEntity()) <= 9) {
                this.addVelocity(this.getVelocity().multiply(-1));
                field_50477.remove(RemovalReason.DISCARDED);
                field_50477 = null;
            }
            else {
                if(whiplash.getHookedEntity() instanceof LivingEntity livingEntity) {
                    if(livingEntity.getMaxHealth() > this.getMaxHealth()){
                        whiplash.pullTowardsHookedEntity();
                    }
                    else {
                        whiplash.pullHookedEntity();
                    }
                }
            }
            ci.cancel();
        }
    }

//    @Inject(method = "method_58864", at = @At("HEAD"), cancellable = true)
//    public void cancelVelocity(Vec3d vec3d, double d, float f, CallbackInfo ci){
//        if (this.field_50477 != null && this.field_50477.method_58941() && !this.isOnGround()) {
//            Whiplash whiplash = (Whiplash) field_50477;
//            if (whiplash.getHookedEntity() != null){
//                super.method_58864(vec3d,d,f);
//                ci.cancel();
//            }
//        }
//    }
}
