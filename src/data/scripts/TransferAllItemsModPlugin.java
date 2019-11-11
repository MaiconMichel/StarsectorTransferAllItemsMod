package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class TransferAllItemsModPlugin extends BaseModPlugin {
    private static TransferAllItems transferAllItems;

    @Override
    public void onApplicationLoad() throws Exception {
        ConfigJSON.reloadSettings();
        transferAllItems = new TransferAllItems();
    }

    @Override
    public void onGameLoad(boolean newGame) {
        Global.getSector().addTransientScript(transferAllItems);
    }
}