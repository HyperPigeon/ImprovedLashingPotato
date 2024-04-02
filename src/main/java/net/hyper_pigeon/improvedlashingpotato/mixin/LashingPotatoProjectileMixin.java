package net.hyper_pigeon.improvedlashingpotato.mixin;

import net.hyper_pigeon.improvedlashingpotato.Whiplash;
import net.minecraft.class_9508;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_9508.class)
public abstract class LashingPotatoProjectileMixin extends ProjectileEntity implements Whiplash {

    private Entity hookedEntity = null;

    @Shadow
    private native void method_58940(boolean bl);

    @Shadow
    public native PlayerEntity method_58943();

    @Shadow
    private native void method_58937(float f);

    public LashingPotatoProjectileMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canHit", at = @At("TAIL"), cancellable = true)
    public void canHitEntity(Entity entity, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if(!entityHitResult.getEntity().equals(getOwner())) {
            this.setVelocity(Vec3d.ZERO);
            this.method_58940(true);
            PlayerEntity playerEntity = this.method_58943();
            if (playerEntity != null) {
                this.hookedEntity = entityHitResult.getEntity();
                double d = playerEntity.getPos().subtract(entityHitResult.getPos()).length();
                this.method_58937(Math.max((float) d * 0.5F - 3.0F, 1.5F));
            }
        }
    }

    public Entity getHookedEntity(){
        return hookedEntity;
    }

    public void pullHookedEntity(){
        Entity owner = this.getOwner();
        if (owner != null && hookedEntity != null) {
            Vec3d vec3d = new Vec3d(owner.getX() - this.getX(), owner.getY() - this.getY(), owner.getZ() - this.getZ()).normalize().multiply(1.2);
            owner.setVelocity(Vec3d.ZERO);
            hookedEntity.setVelocity(hookedEntity.getVelocity().add(vec3d));
        }
    }

    public void pullTowardsHookedEntity(){
        Entity owner = this.getOwner();
        if (owner != null && hookedEntity != null) {
            Vec3d vec3d = new Vec3d(this.getX() - owner.getX() , this.getY() - owner.getY(), this.getZ() - owner.getZ()).normalize().multiply(1.2);
            owner.setVelocity(vec3d);
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/class_9508.checkBlockCollision ()V", shift = At.Shift.AFTER))
    public void setPositionToHookedEntity(CallbackInfo ci){
        if(hookedEntity != null) {
            this.setPosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8), this.hookedEntity.getZ());
        }
    }

}
