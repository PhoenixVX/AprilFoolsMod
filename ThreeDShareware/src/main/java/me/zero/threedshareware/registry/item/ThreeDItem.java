package me.zero.threedshareware.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ThreeDItem extends Item {
    private static final Random random = new Random();

    public ThreeDItem (Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip (ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Formatting randomFormat = Formatting.values()[random.nextInt(Formatting.values().length)];
        tooltip.add(Text.literal("Tasty!").formatted(randomFormat));
    }

    @Override
    public ItemStack finishUsing (ItemStack stack, World world, LivingEntity user) {
        ItemStack resultStack = super.finishUsing(stack, world, user);
        if (user instanceof ClientPlayerEntity) {
            ClientPlayerEntity player = (ClientPlayerEntity) user;

        }

        return resultStack;
    }
}