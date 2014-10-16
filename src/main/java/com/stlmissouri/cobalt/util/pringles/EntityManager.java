package com.stlmissouri.cobalt.util.pringles;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.events.system.PluginLoadedEvent;
import com.stlmissouri.cobalt.plugins.friends.Friends;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * Manage entities
 *
 * @author Ramisme
 * @since Apr 26, 2013
 */
public final class EntityManager {

    private final Minecraft minecraft = Minecraft.getMinecraft();

    private final AuraManager auraManager;
    private Friends friends;

    public EntityManager(final AuraManager auraManager, Friends friends) {
        this.auraManager = auraManager;
        this.friends = friends;
        EventManager.register(this);
    }

    @EventTarget
    public void onFriendsLoaded(PluginLoadedEvent event) {
        if (!event.getPluginClass().equals(Friends.class))
            return;
        this.friends = (Friends) event.getPlugin();
    }

    /**
     * Return the closest entity based on distance.
     *
     * @param range
     * @return
     */
    public EntityLivingBase getClosestEntity(final double range) {
        double distance = range; // localized distance variable
        EntityLivingBase tempEntity = null;

        for (Entity entity : (List<Entity>) minecraft.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase)) {
                continue;
            }

            final EntityLivingBase living = (EntityLivingBase) entity;
            if (!canAttackEntity(living)) {
                continue;
            }

            final double curDistance = minecraft.thePlayer
                    .getDistanceToEntity(living);
            if (curDistance <= distance) {
                distance = curDistance;
                tempEntity = living;
            }
        }

        return tempEntity;
    }

    /**
     * Return the closest entity based on angles.
     *
     * @param angle
     * @return
     */
    public EntityLivingBase getClosestEntityToCursor(final float angle) {
        float distance = angle; // localized distance variable
        EntityLivingBase tempEntity = null;

        for (Entity entity : (List<Entity>) minecraft.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase)) {
                continue;
            }

            final EntityLivingBase living = (EntityLivingBase) entity;
            if (!canAttackEntity(living)) {
                continue;
            }

            final float[] angles = getAngles(living);
            final float yaw = getDistanceBetweenAngles(minecraft.thePlayer.rotationYawHead, angles[0]);
            final float pitch = getDistanceBetweenAngles(minecraft.thePlayer.rotationPitch, angles[1]);
            if (yaw > angle || pitch > angle)
                continue;
            final float curDistance = (yaw + pitch) / 2F;

            if (curDistance <= distance) {
                distance = curDistance;
                tempEntity = living;
            }
        }

        return tempEntity;
    }

    /**
     * Return the entity that is closest to the cursor. If such an entity does
     * not exist, return the closest entity instead.
     *
     * @param angle
     * @param distance
     * @return
     */
    public EntityLivingBase getEntity(final float angle, final double distance) {
        final EntityLivingBase distanceCheck = getClosestEntity(distance);
        final EntityLivingBase angleCheck = getClosestEntityToCursor(angle);
        return angleCheck != null ? angleCheck : distanceCheck;
    }

    /**
     * Returns the fixed distance between angles.
     *
     * @param ang1
     * @param ang2
     * @return
     */
    public float getDistanceBetweenAngles(final float ang1, final float ang2) {
        return Math.abs(((ang1 - ang2 + 180) % 360 + 360) % 360 - 180);
    }

    /**
     * Determines whether an entity is actually able to be attacked.
     *
     * @param entity
     * @return
     */
    public boolean canAttackEntity(final EntityLivingBase entity) {
        return entity != minecraft.thePlayer && shouldAttack(entity);
    }

    /**
     * Determines whether an entity should be attacked.
     *
     * @param entityLiving
     * @return
     */
    private boolean shouldAttack(EntityLivingBase entityLiving) {

        if (entityLiving.deathTime > 0 || !entityLiving.isEntityAlive()) {
            return false;
        }

        if (!(minecraft.thePlayer.canEntityBeSeen(entityLiving)))
            return false;

        if (minecraft.thePlayer.getDistanceToEntity(entityLiving) > auraManager
                .getRange()) {
            return false;
        }

        if (entityLiving instanceof EntityPlayer && friends.getPlayerManager().getProperties((EntityPlayer)entityLiving).isFriend()) {
            return false;
        }

        if (entityLiving instanceof EntityAnimal && !auraManager.isAnimal()) {
            return false;
        }

        if (entityLiving instanceof EntityMob && !auraManager.isMob()) {
            return false;
        }
        return true;
    }

    /**
     * Return an array of necessary angles to attack the desired entity.
     *
     * @param entityLiving The desired entity
     * @return Float array of angles
     * @author Stul Missouri
     * @see com.stlmissouri.cobalt.module.combat.KillAura
     */
    public float[] getAngles(final EntityLivingBase entityLiving) {
        double difX = entityLiving.posX - minecraft.thePlayer.posX, difY = (entityLiving.posY - minecraft.thePlayer.posY)
                + (entityLiving.getEyeHeight() / 1.4F), difZ = entityLiving.posZ
                - minecraft.thePlayer.posZ;
        double helper = Math.sqrt(difX * difX + difZ * difZ);

        float yaw = (float) (Math.atan2(difZ, difX) * 180 / Math.PI) - 90;
        float pitch = (float) -(Math.atan2(difY, helper) * 180 / Math.PI);
        return (new float[]{yaw, pitch});
    }
}
