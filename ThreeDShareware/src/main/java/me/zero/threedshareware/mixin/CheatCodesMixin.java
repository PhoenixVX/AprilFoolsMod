package me.zero.threedshareware.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.InputConstants;
import me.zero.aprilfools.AprilFoolsMod;
import me.zero.threedshareware.misc.CheatCodes;
import me.zero.threedshareware.registry.ModItems;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class CheatCodesMixin {

    private final RandomSource random = RandomSource.create();
    @Shadow
    @Final
    private Minecraft minecraft;
    private String CURRENT_STRING = "";

    @Inject(at = @At("HEAD"), method = "charTyped")
    public void onChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
        if (AprilFoolsMod.CONFIG.threeDSharewareConfig.cheatCodesEnabled && window == this.minecraft.getWindow().getWindow() && this.minecraft.player != null) {
            if (!InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), 67) || !InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), 292)) {
                this.CURRENT_STRING += Character.toUpperCase((char) codePoint);
            }
            if (this.CURRENT_STRING.length() > CheatCodes.MAX_CHEAT_LEN) {
                this.CURRENT_STRING = this.CURRENT_STRING.substring(this.CURRENT_STRING.length() - CheatCodes.MAX_CHEAT_LEN);
            }

            // Is single player server correct?
            MinecraftServer server = this.minecraft.getSingleplayerServer();
            GameProfile profile = this.minecraft.player != null ? this.minecraft.player.getGameProfile() : null;
            if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT6)) {
                this.minecraft.gui.getChat().addMessage(Component.literal("There is no cow level"));
            } else if (server != null && profile != null) {
                ServerPlayer player = server.getPlayerList().getPlayer(profile.getId());
                if (player != null) {
                    if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT5)) {
                        player.getInventory().add(new ItemStack(ModItems.RED_KEY));
                        player.getInventory().add(new ItemStack(ModItems.YELLOW_KEY));
                        player.getInventory().add(new ItemStack(ModItems.BLUE_KEY));
                        this.minecraft.gui.getChat().addMessage(Component.literal("Got all keys!"));
                    } else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT4)) {
                        ItemStack enchantedCrossBow = new ItemStack(Items.CROSSBOW);
                        EnchantmentHelper.setEnchantments(ImmutableMap.of(Enchantments.MULTISHOT, 12), enchantedCrossBow);
                        player.getInventory().add(enchantedCrossBow);
                        player.getInventory().add(EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.DIAMOND_SWORD), 30, true));
                        player.getInventory().add(EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.DIAMOND_AXE), 30, true));
                        player.getInventory().add(EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.DIAMOND_PICKAXE), 30, true));
                        player.getInventory().add(EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.DIAMOND_HOE), 30, true));
                        player.getInventory().add(EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.SHEARS), 30, true));
                        player.getInventory().add(EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.BOW), 30, true));
                        player.setItemSlot(EquipmentSlot.HEAD, EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.DIAMOND_HELMET), 30, true));
                        player.setItemSlot(EquipmentSlot.CHEST, EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.DIAMOND_CHESTPLATE), 30, true));
                        player.setItemSlot(EquipmentSlot.LEGS, EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.DIAMOND_LEGGINGS), 30, true));
                        player.setItemSlot(EquipmentSlot.FEET, EnchantmentHelper.enchantItem(this.random, new ItemStack(Items.DIAMOND_BOOTS), 30, true));
                        player.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
                        this.minecraft.gui.getChat().addMessage(Component.literal("Got all equipment!"));
                    } else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT2)) {
                        player.getAbilities().mayfly = !player.getAbilities().mayfly;
                        player.onUpdateAbilities();
                        this.minecraft.gui.getChat().addMessage(Component.literal("FLYING=VERY YES"));
                    } else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT1)) {
                        player.getAbilities().invulnerable = !player.getAbilities().invulnerable;
                        player.onUpdateAbilities();
                        this.minecraft.gui.getChat().addMessage(Component.literal("Nothing can stop you!"));
                    } else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT3)) {
                        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 1200, 20));
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 20));
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 20));
                        this.minecraft.gui.getChat().addMessage(Component.literal("Gordon's ALIVE!"));
                    } else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT7)) {
                        Vec3 playerPos = player.position();
                        Vec3 vector = new Vec3(playerPos.x + (double) (this.random.nextFloat() * 3.0F), playerPos.y, playerPos.z + (double) (this.random.nextFloat() * 3.0F));
                        Horse horse = EntityType.HORSE.create(player.level);
                        if (horse != null) {
                            horse.absMoveTo(vector.x, vector.y, vector.z);
                            horse.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200.0D);
                            horse.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(3.0D);
                            horse.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(3.0D);
                            horse.setTamed(true);
                            horse.setTemper(0);
                            // TODO: Make equipping horse armor work
                            horse.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_HORSE_ARMOR));
                            horse.equipSaddle(SoundSource.NEUTRAL);
                            //horse.equip(400, new ItemStack(Items.SADDLE));
                            player.level.addFreshEntity(horse);
                        }
                        this.minecraft.gui.getChat().addMessage(Component.literal("VROOM!"));
                    } else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT8)) {
                        for (int var3 = 0; var3 < 5; ++var3) {
                            Creeper creeper = EntityType.CREEPER.create(player.level);
                            creeper.absMoveTo(player.getX() + 0.5D, player.getY() + 0.5D, player.getZ() + 0.5D);
                            //creeper.wouldPoseNotCollide();
                            player.level.addFreshEntity(creeper);
                            this.minecraft.gui.getChat().addMessage(Component.literal("Special creeper has been spawned nearby!"));
                            break;
                        }
                    }
                }
            }
        }
    }
}