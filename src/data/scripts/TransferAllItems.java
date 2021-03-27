package data.scripts;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUITabId;
import org.lwjgl.input.Keyboard;

public class TransferAllItems implements EveryFrameScript {
    private boolean keyDown = false;
    private MarketMouseClick marketMouseClick;

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {
        if (Global.getSector().getCampaignUI().getCurrentCoreTab() == CoreUITabId.CARGO) {
            getMarketMouseClick().updateFrame();
            checkInput();
        } else if (marketMouseClick != null) {
            marketMouseClick = null;
        }
    }

    private MarketMouseClick getMarketMouseClick() {
        if (marketMouseClick == null) {
            marketMouseClick = new MarketMouseClick();
        }

        return  marketMouseClick;
    }

    private void checkInput() {
        final boolean keyTransferFleetToMarket = Keyboard.isKeyDown(ConfigJSON.getKeyTransferFleetToMarket());
        final boolean keyTransferMarketToFleet = Keyboard.isKeyDown(ConfigJSON.getKeyTransferMarketToFleet());

        if (!keyDown) {
            if (keyTransferFleetToMarket) {
                transferCargoFleetToMarket();
                keyDown = true;
            } else if (keyTransferMarketToFleet) {
                transferCargoMarketToFleet();
                keyDown = true;
            }
        } else if (!keyTransferFleetToMarket && !keyTransferMarketToFleet) {
            keyDown = false;
        }
    }

    private void transferCargoFleetToMarket() {
        marketMouseClick.clickAllFleet();
    }

    private void transferCargoMarketToFleet() {
        marketMouseClick.clickAllMarket();
    }
}