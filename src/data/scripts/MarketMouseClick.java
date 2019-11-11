package data.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MarketMouseClick {
    private static boolean flagBeginClickAll = false;
    private int row;
    private int col;
    private int x;
    private int y;
    private MarketTabManager marketTabManager;
    private boolean isMarket = false;
    private boolean lastClick = false;
    private boolean enableControl = false;

    public void updateFrame() {
        if (flagBeginClickAll) {
            if (lastClick) {
                pressControl();
                mouseClick(x, y);
                lastClick = false;
                flagBeginClickAll = false;
            } else if (!(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ||
                  Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))) {
                  pressControl();
            } else if (col >= MarketSizeInfo.getColumns()) {
                row++;
                col = 0;
                clickSort(isMarket);
            } else if (marketTabManager.isEmpty()) {
                clickSort(isMarket);
                lastClick = true;
                row = MarketSizeInfo.getRows() + 1;
            } else if (row <= MarketSizeInfo.getRows()) {
                pressControl();
                mouseClick(x + MarketSizeInfo.SQUARE_SIZE * col, y);
                col++;
            } else {
                flagBeginClickAll = false;
            }
        } else if (enableControl) {
            releaseControl();
            clickSort(isMarket);
        }
    }

    public void clickAllMarket() {
        clickAll(MarketSizeInfo.getFirstColumnX(), MarketSizeInfo.getFirstRowMarketY(), getMarket(), null);
    }

    public void clickAllFleet() {
        clickAll(MarketSizeInfo.getFirstColumnX(), MarketSizeInfo.getFirstRowFleetY(), null, getCargoFleet());
    }

    private void clickAll(final int x, final int y, final MarketAPI marketAPI, final CargoAPI cargoAPI) {
        if (flagBeginClickAll) return;

        this.row = 0;
        this.col = 0;
        this.x = x;
        this.y = y;
        this.isMarket = marketAPI != null;
        this.marketTabManager = isMarket ? new MarketTabManager(marketAPI) : new MarketTabManager(cargoAPI);
        flagBeginClickAll = true;
        clickSort(isMarket);
    }

    private void clickSort(boolean isMarket) {
        if (isMarket) {
            clickSortMarket();
        } else {
            clickSortFleet();
        }
    }

    private void clickSortMarket() {
        mouseClick(MarketSizeInfo.getSortX(), MarketSizeInfo.getSortMarketY());
    }

    private void clickSortFleet() {
        mouseClick(MarketSizeInfo.getSortX(), MarketSizeInfo.getSortFleetY());
    }

    private void pressControl() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            enableControl = true;
        } catch (Exception e) {
        }
    }

    private void releaseControl() {
        try {
            Robot robot = new Robot();
            robot.keyRelease(KeyEvent.VK_CONTROL);
            enableControl = false;
        } catch (Exception e) {
        }
    }

    private void mouseClick(int x, int y) {
        Point currentPlayerMousePoint = getCurrentMousePoint();

        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseMove(currentPlayerMousePoint.x, currentPlayerMousePoint.y);
        } catch (Exception e) {
        }
    }

    private Point getCurrentMousePoint() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    private CargoAPI getCargoFleet() {
        return Global.getSector().getPlayerFleet().getCargo();
    }

    private MarketAPI getMarket() {
        return Global.getSector().getCampaignUI().getCurrentInteractionDialog().getInteractionTarget().getMarket();
    }
}