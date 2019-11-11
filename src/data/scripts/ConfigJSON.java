package data.scripts;

import org.json.JSONException;
import org.lazywizard.lazylib.JSONUtils;
import org.lazywizard.lazylib.JSONUtils.*;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public final class ConfigJSON {
    private static final String FILE_NAME = "transfer_all_items_settings.json";
    private static final String DEFAULT_FILE_NAME = "transfer_all_items_settings.json.default";
    private static int keyTransferFleetToMarket = getKeyTransferFleetToMarketDefault();
    private static int keyTransferMarketToFleet = getKeyTransferMarketToFleetDefault();

    public static int getKeyTransferFleetToMarket() {
        return keyTransferFleetToMarket;
    }

    public static int getKeyTransferMarketToFleet() {
        return keyTransferMarketToFleet;
    }

    public static void reloadSettings() throws IOException, JSONException {
        final CommonDataJSONObject json = loadFile();

        keyTransferFleetToMarket = json.optInt("keyTransferFleetToMarket", getKeyTransferFleetToMarketDefault());
        keyTransferMarketToFleet = json.optInt("keyTransferMarketToFleet", getKeyTransferMarketToFleetDefault());
    }

    private static CommonDataJSONObject loadFile() throws IOException, JSONException {
        return JSONUtils.loadCommonJSON(FILE_NAME, DEFAULT_FILE_NAME);
    }

    private static int getKeyTransferFleetToMarketDefault() {
        return Keyboard.KEY_UP;
    }

    private static int getKeyTransferMarketToFleetDefault() {
        return Keyboard.KEY_DOWN;
    }
}