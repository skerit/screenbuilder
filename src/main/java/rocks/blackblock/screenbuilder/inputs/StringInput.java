package rocks.blackblock.screenbuilder.inputs;

import net.minecraft.screen.NamedScreenHandlerFactory;
import rocks.blackblock.screenbuilder.BBSB;
import rocks.blackblock.screenbuilder.ScreenBuilder;
import rocks.blackblock.screenbuilder.TexturedScreenHandler;
import rocks.blackblock.screenbuilder.interfaces.RenamedEventListener;

public class StringInput extends BaseInput {

    // The registered GUI
    public static ScreenBuilder GUI;

    // A renamed listener
    protected RenamedEventListener on_rename_listener = null;

    // The default name
    protected String default_name = "Enter some text...";

    /**
     * Set the renamed listener
     *
     * @author   Jelle De Loecker   <jelle@elevenways.be>
     * @since    0.1.0
     */
    public void setRenamedListener(RenamedEventListener listener) {
        this.on_rename_listener = listener;
    }

    /**
     * Get the screenbuilder
     *
     * @author  Jelle De Loecker   <jelle@elevenways.be>
     * @since   0.1.0
     */
    @Override
    public ScreenBuilder getScreenBuilder() {
        return GUI;
    }

    /**
     * Register the GUI
     *
     * @author   Jelle De Loecker   <jelle@elevenways.be>
     * @since    0.1.0
     */
    public static ScreenBuilder registerScreen() {

        if (GUI != null) {
            return GUI;
        }

        GUI = new ScreenBuilder("string_input");
        GUI.setNamespace(BBSB.NAMESPACE);
        GUI.useCustomTexture(true);
        GUI.useAnvil();

        GUI.addButton(2).addLeftClickListener((screen, slot) -> {

            NamedScreenHandlerFactory factory = screen.getOriginFactory();

            if (factory instanceof StringInput input) {

                String new_value = screen.getRenamedValue();

                // Handle screen behaviour first (might close this screen)
                TexturedScreenHandler new_screen = input.handleScreenBehaviour(screen);

                if (new_screen == null) {
                    new_screen = screen;
                }

                // Call rename listener afterwards
                if (input.on_rename_listener != null) {
                    input.on_rename_listener.onRenamed(new_screen, new_value);
                }

            } else {
                screen.showPreviousScreen();
            }
        });

        GUI.register();

        return GUI;
    }
}
