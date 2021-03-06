package data.scripts;

import com.fs.starfarer.api.Global;

@SuppressWarnings("WeakerAccess")
public class MarketSizeInfo {
    public static final int SQUARE_SIZE = 100;
    private static final int MAX_QUANTITY_ITEMS = 120;

    private static final int MAX_COLUMNS = 10;
    private static final float MAX_VISIBLE_ROWS = 3.32F;
    private static final int HEIGHT_INFO_TOP = 81;
    private static final int HEIGHT_TAB = 17;
    private static final int HEIGHT_BUTTONS_MID = 63;
    private static final int HEIGHT_BAR_BOTTOM = 35;
    private static final int HEIGHT_MIN_BORDER = 20;
    private static final int WIDTH_LEFT_PANEL = 273;
    private static final int WIDTH_BORDER = 20;

    private static int columns;
    private static int rows;
    private static int firstColumnX;
    private static int firstRowMarketY;
    private static int firstRowFleetY;
    private static int sortX;
    private static int sortMarketY;
    private static int sortFleetY;

    static {
        columns = calcColumns();
        rows = calcRows(columns);

        float rowsVisible = calcRowsVisible();
        int heightGrid  = calcHeightGrid(rowsVisible);
        int startDrawAreaY = calcStartDrawAreaY(heightGrid);
        int startDrawAreaX = calcStartDrawAreaX(columns);

        firstColumnX = calcFirstColumnX(startDrawAreaX);
        firstRowMarketY = calcFirstRowMarketY(startDrawAreaY);
        firstRowFleetY = calcFirstRowFleetY(firstRowMarketY, heightGrid);

        sortX = calcSortX(startDrawAreaX, columns);
        sortMarketY = calcSortMarketY(startDrawAreaY);
        sortFleetY = calcSortFleetY(firstRowFleetY);

        //showInfo();
    }

    public static int getColumns() {
        return columns;
    }

    public static int getRows() {
        return rows;
    }

    public static int getFirstColumnX() {
        return StarsectorAppInfo.getX() + firstColumnX;
    }

    public static int getFirstRowMarketY() {
        return StarsectorAppInfo.getY() + firstRowMarketY;
    }

    public static int getFirstRowFleetY() {
        return StarsectorAppInfo.getY() + firstRowFleetY;
    }

    public static int getSortX() {
        return StarsectorAppInfo.getX() + sortX;
    }

    public static int getSortMarketY() {
        return StarsectorAppInfo.getY() + sortMarketY;
    }

    public static int getSortFleetY() {
        return StarsectorAppInfo.getY() + sortFleetY;
    }

    private static int calcColumns() {
        int columns = (StarsectorAppInfo.getScreenWidth() - WIDTH_BORDER * 2 - WIDTH_LEFT_PANEL) / SQUARE_SIZE;
        return Math.min(columns, MAX_COLUMNS);
    }

    private static int calcRows(int columns) {
        return MAX_QUANTITY_ITEMS / columns;
    }

    private static float calcRowsVisible() {
        float rowsVisible = (float)Math.floor((StarsectorAppInfo.getScreenHeight() - HEIGHT_MIN_BORDER * 2 - HEIGHT_TAB * 2 -
                              HEIGHT_INFO_TOP - HEIGHT_BUTTONS_MID - HEIGHT_BAR_BOTTOM) / 2F / SQUARE_SIZE * 100) / 100;
        return Math.min(rowsVisible, MAX_VISIBLE_ROWS);
    }

    private static int calcHeightGrid(float rowsVisible) {
        return (int)(HEIGHT_TAB + rowsVisible * SQUARE_SIZE);
    }

    private static int calcHeightDrawArea(int heightGrid) {
        return 2 * heightGrid + HEIGHT_INFO_TOP + HEIGHT_BUTTONS_MID;
    }

    private static int calcStartDrawAreaY(int heightGrid) {
        return (StarsectorAppInfo.getScreenHeight() - calcHeightDrawArea(heightGrid) - HEIGHT_BAR_BOTTOM) / 2;
    }

    private static int calcStartDrawAreaX(int columns) {
        int value = (StarsectorAppInfo.getScreenWidth() - columns * SQUARE_SIZE) / 2;
        return Math.max(value, WIDTH_LEFT_PANEL + WIDTH_BORDER);
    }

    private static int calcFirstRowMarketY(int startDrawAreaY) {
        return startDrawAreaY + HEIGHT_INFO_TOP + HEIGHT_TAB + SQUARE_SIZE / 2;
    }

    private static int calcFirstRowFleetY(int firstRowMarketY, int heightGrid) {
        return firstRowMarketY + heightGrid + HEIGHT_BUTTONS_MID;
    }

    private static int calcSortX(int startDrawAreaX, int columns) {
        return startDrawAreaX + (columns - 1) * SQUARE_SIZE + SQUARE_SIZE / 2;
    }

    private static int calcSortMarketY(int startDrawAreaY) {
        return startDrawAreaY + HEIGHT_INFO_TOP + HEIGHT_TAB / 2;
    }

    private static int calcSortFleetY(int firstRowFleetY) {
        return firstRowFleetY - (SQUARE_SIZE + HEIGHT_TAB) / 2;
    }

    private static int calcFirstColumnX(int startDrawAreaX) {
        return startDrawAreaX + SQUARE_SIZE / 2;
    }

    private static void showInfo() {
        Global.getSector().getCampaignUI().addMessage("Market: (" + getFirstColumnX() + "," + getFirstRowMarketY() + ") Sort: (" + getSortX() + "," + getSortMarketY() + ") - " +
                                                            "Fleet: (" + getFirstColumnX() + "," + getFirstRowFleetY()  + ") Sort: (" + getSortX() + "," + getSortFleetY()  + ") ");
    }
}