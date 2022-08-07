package me.zero.threedshareware.registry.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ThreeDItem extends Item {
    private static final Random random = new Random();

    public ThreeDItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        ChatFormatting randomFormat = ChatFormatting.values()[random.nextInt(ChatFormatting.values().length)];
        tooltip.add(Component.literal("Tasty!").withStyle(randomFormat));
    }

    @Override
    public ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity user) {
        ItemStack resultStack = super.finishUsingItem(stack, level, user);
        if (user instanceof LocalPlayer localPlayer) {
            //ClientPlayerEntity player = (ClientPlayerEntity) user;

        }

        return resultStack;
    }
}