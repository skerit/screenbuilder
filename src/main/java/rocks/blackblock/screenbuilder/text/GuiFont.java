package rocks.blackblock.screenbuilder.text;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.theepicblock.polymc.api.resource.ModdedResources;
import io.github.theepicblock.polymc.api.resource.PolyMcResourcePack;
import io.github.theepicblock.polymc.impl.misc.logging.SimpleLogger;
import net.minecraft.util.Formatting;
import rocks.blackblock.screenbuilder.BBSB;
import rocks.blackblock.screenbuilder.textures.TexturePiece;
import rocks.blackblock.screenbuilder.utils.GuiUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The GUI font class, used to register pieces of GUI textures in a font
 *
 * @author   Jelle De Loecker   <jelle@elevenways.be>
 * @since    0.1.1
 * @version  0.1.1
 */
public class GuiFont extends Font {

    private ArrayList<TexturePiece> texture_pieces = new ArrayList<>();
    private int index = 0;
    private char current_char = (char) 33;

    public GuiFont(String name) {
        super(name, 0);

        // GUI Screen Titles are always colored grey, and any coloring "tints" the bitmap used for the character
        // So we always have to undo the tinting, by setting the color to white
        this.font_style = this.font_style.withColor(Formatting.WHITE);
    }

    /**
     * Get the next free character to use
     * (All the initial control codes are skipped)
     * @since   0.1.1
     */
    public char getNextChar() {
        this.index++;
        this.current_char = Font.getNextChar(this.current_char);
        return this.current_char;
    }

    /**
     * Add a texture piece to the list
     * @since   0.1.1
     */
    public void registerTexturePiece(TexturePiece piece) {
        this.texture_pieces.add(piece);

        if (BBSB.HAS_INITIALIZED) {
            BBSB.log("Warning! Registering texture piece after initialization:", piece);
            //Thread.dumpStack();
        }
    }

    /**
     * Get the JSON string for this font
     */
    public JsonObject getJson() {

        JsonObject root = new JsonObject();
        JsonArray providers = new JsonArray();
        root.add("providers", providers);

        HashMap<String, JsonArray> provider_chars = new HashMap<>();

        // Always add the space provider
        JsonObject space_provider = new JsonObject();
        space_provider.addProperty("type", "space");
        JsonObject advances_obj = new JsonObject();
        advances_obj.addProperty(" ", 4);
        space_provider.add("advances", advances_obj);
        providers.add(space_provider);

        for (TexturePiece piece : this.texture_pieces) {
            String path = piece.getPath();
            String ascent_id = path + "_" + piece.getAscent();

            JsonArray chars = provider_chars.get(ascent_id);

            // If this provider doesn't have an entry for the current ascent_id yet,
            // create it
            if (chars == null) {

                JsonObject provider = new JsonObject();

                int ascent = piece.getAscent();
                int height = piece.getGuiHeight();

                // If the ascent is larger than the height,
                // minecraft will refuse to load the entire font
                if (ascent > height) {
                    ascent = height;

                    BBSB.log("The ascent of " + piece.getJsonFilename() + " is larger than its height, this will cause issues!");
                }

                provider.addProperty("type", "bitmap");
                provider.addProperty("file", piece.getJsonFilename());
                provider.addProperty("ascent", ascent);
                provider.addProperty("height", height);

                chars = new JsonArray();
                provider.add("chars", chars);
                providers.add(provider);

                provider_chars.put(ascent_id, chars);
                chars.add("");
            }

            // Multiple array elements count as different Y levels,
            // so we need to add the characters to the first entry
            String char_string = chars.get(0).getAsString();
            char_string += piece.getCharacter();
            chars.remove(0);
            chars.add(char_string);
        }

        return root;
    }

    /**
     * Add this font resources to the given data pack
     */
    public void addToResourcePack(ModdedResources moddedResources, PolyMcResourcePack pack, SimpleLogger logger) {

        JsonObject root = this.getJson();
        String json = root.toString();

        String target_path_str = "font/gui.json";

        pack.setAsset(BBSB.NAMESPACE, target_path_str, (location, gson) -> {
            GuiUtils.writeToPath(location, json);
        });

        // @TODO: add the images of the texture pieces to the pack

        HashMap<String, Boolean> registered_piece = new HashMap<>();

        for (TexturePiece piece : this.texture_pieces) {

            String image_path = piece.getPath();
            Boolean already_registered = registered_piece.get(image_path);

            if (already_registered == null) {
                registered_piece.put(image_path, true);
                String path = "textures/" + image_path;

                pack.setAsset(BBSB.NAMESPACE, path, (location, gson) -> {
                    GuiUtils.writeToPath(location, piece.getImage());
                });
            }
        }

        /*
        for (TexturePiece piece : this.texture_pieces) {
            String path = "assets/bbsb/textures/" + piece.getPath();
            GuiUtils.writeToPath(buildLocation.resolve(path), piece.getImage());
        }*/
    }
}
