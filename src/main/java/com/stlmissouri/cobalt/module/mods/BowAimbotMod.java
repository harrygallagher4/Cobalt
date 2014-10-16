package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.mod.GameLoopEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.plugins.friends.Friends;
import com.stlmissouri.cobalt.util.pringles.AuraManager;
import com.stlmissouri.cobalt.util.pringles.EntityManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

/**
 * User: Stl
 * Date: 4/7/2014
 * Time: 10:22 PM
 * Use:  Calculates and aims at the angle required to hit a target player/entity
 * <p/>
 * Also, this is just about the most well documented module in Cobalt. If you decide
 * to steal it, at least read some of the documentation and maybe you'll become a
 * little bit better at physics
 */
public class BowAimbotMod extends CobaltModule {

    private EntityLivingBase target;

    private final EntityManager entityManager;
    private final AuraManager auraManager;

    public BowAimbotMod(Cobalt cobalt) {
        super(cobalt, "BowAimbot");
        this.auraManager = new AuraManager();
        this.auraManager.setAnimal(true);
        this.auraManager.setMob(true);
        this.auraManager.setRange(128);
        this.entityManager = new EntityManager(this.auraManager, this.cobalt.pluginManager.getPlugin(Friends.class));
        this.updateKeybind(Keyboard.KEY_K);
    }

    @EventTarget
    public void onUpdate(GameLoopEvent event) {
        if (this.mc.thePlayer == null) {
            this.target = null;
            return;
        }
        this.target = entityManager.getClosestEntityToCursor(60F);
        if (target == null)
            return;
        if (!isUsingBow())
            return;
        int use = mc.thePlayer.getItemInUse().getMaxItemUseDuration() - mc.thePlayer.getItemInUseCount();
        float progress = use / 20.0F;
        progress = (progress * progress + progress * 2.0F) / 3.0F;
        if (progress >= 1.0F)
            progress = 1.0F;
        double v = progress * 3.0F;
        //Static MC gravity
        double g = 0.05F;
        setAngles(v, g);
    }

    private boolean isUsingBow() {
        if (mc.thePlayer.getItemInUse() == null)
            return false;
        return mc.thePlayer.getItemInUse().getItem().getUnlocalizedName().equalsIgnoreCase("item.bow");
    }

    private void setAngles(double v, double g) {
        double pitch = -Math.toDegrees(getLaunchAngle(this.target, v, g));
        if(Double.isNaN(pitch))
            return;
        double difX = this.target.posX - mc.thePlayer.posX, difZ = this.target.posZ - mc.thePlayer.posZ;
        float yaw = (float) (Math.atan2(difZ, difX) * 180 / Math.PI) - 90;
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationYawHead = yaw;
        mc.thePlayer.rotationPitch = (float) pitch;
    }

    /**
     * Gets launch angle required to hit a target with the specified velocity and gravity
     *
     * @param targetEntity Target entity
     * @param v            Projectile velocity
     * @param g            World gravity
     * @return
     */
    private float getLaunchAngle(EntityLivingBase targetEntity, double v, double g) {
        double yDif = ((targetEntity.posY + (targetEntity.getEyeHeight() / 2)) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()));
        double xDif = (targetEntity.posX - mc.thePlayer.posX);
        double zDif = (targetEntity.posZ - mc.thePlayer.posZ);

        /**
         * Pythagorean theorem to merge x/z
         *           /|
         *          / |
         * xCoord  /  | zDif
         *        /   |
         *       /    |
         *      /_____|
         * (player) xDif
         */
        double xCoord = Math.sqrt((xDif * xDif) + (zDif * zDif));

        return theta(v, g, xCoord, yDif);
    }

    /**
     * Calculates launch angle to hit a specified point based on supplied parameters
     *
     * @param v Projectile velocity
     * @param g World gravity
     * @param x x-coordinate
     * @param y y-coordinate
     * @return angle of launch required to hit point x,y
     * <p/>
     * Whoa there! You just supplied us with a method to hit a 2D point, but Minecraft is a 3D game!
     * <p/>
     * Yeah. Unfortunately this is 100x easier to do than write a method to find the 3D point,
     * so we can just merge the x/z axis of Minecraft into one (using the pythagorean theorem).
     * Have a look at getLaunchAngle to see how that's done
     */
    private float theta(double v, double g, double x, double y) {
        double yv = 2 * y * (v * v);
        double gx = g * (x * x);
        double g2 = g * (gx + yv);
        double insqrt = (v * v * v * v) - g2;
        double sqrt = Math.sqrt(insqrt);

        double numerator = (v * v) + sqrt;
        double numerator2 = (v * v) - sqrt;

        double atan1 = Math.atan2(numerator, g * x);
        double atan2 = Math.atan2(numerator2, g * x);

        /**
         * Ever heard of a quadratic equation? We're gonna have to have two different results
         * here, duh! It's probably best to launch at the smaller angle because that will
         * decrease the total flight time, thus leaving less room for error. If you're just
         * trying to impress your friends you could probably fire it at the maximum angle, but
         * for the sake of simplicity, we'll use the smaller one here.
         */
        return (float) Math.min(atan1, atan2);
    }

}
