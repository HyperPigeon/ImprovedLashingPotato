package net.hyper_pigeon.improvedlashingpotato;

import net.minecraft.entity.Entity;

public interface Whiplash {
    Entity getHookedEntity();
    void pullHookedEntity();
    void pullTowardsHookedEntity();
}
