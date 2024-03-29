package rocks.blackblock.screenbuilder.interfaces;

import net.minecraft.item.ItemStack;
import rocks.blackblock.screenbuilder.TexturedScreenHandler;

@FunctionalInterface
public interface SelectEventListener {
    void onSelect(TexturedScreenHandler screen, ItemStack stack);
}
