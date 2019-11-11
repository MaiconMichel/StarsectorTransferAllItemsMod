package data.scripts;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;

import java.util.ArrayList;
import java.util.List;

public class MarketTabManager {
    private MarketAPI market;
    private List<SubmarketAPI> submarkets;
    private List<Float> quantityList = new ArrayList<>();
    private CargoAPI cargoCurrent;
    private float quantityOld = -1;
    private float quantityCurrent = -1;
    private float countForceEnd = 0;

    public MarketTabManager(MarketAPI market) {
        this.market = market;
        this.submarkets = getSubmarket(market);
        updateQuantityList();
    }

    public MarketTabManager(CargoAPI cargoAPI) {
        this.cargoCurrent = cargoAPI;
    }

    public boolean isEmpty() {
        updateCargo();

        if (cargoCurrent != null) {
            quantityOld     = quantityCurrent;
            quantityCurrent = getQuantity(cargoCurrent);
        } else {
            countForceEnd++;
        }

        return (quantityCurrent >= 0 && quantityOld == quantityCurrent) || countForceEnd > 1;
    }

    private void updateCargo() {
        if (cargoCurrent == null) {
            if (submarkets.size() == 1) {
                cargoCurrent = submarkets.get(0).getCargo();
            } else {
                for (int i = 0; i < submarkets.size(); i++) {
                    SubmarketAPI submarket = submarkets.get(i);
                    float quantity = quantityList.get(i);

                    if (getQuantity(submarket.getCargo()) != quantity) {
                        cargoCurrent = submarket.getCargo();
                        return;
                    }
                }
            }
        }
    }

    private void updateQuantityList() {
        quantityList.clear();

        if (submarkets != null) {
            for (SubmarketAPI submarketAPI : submarkets) {
                quantityList.add(getQuantity(submarketAPI.getCargo()));
            }
        }
    }

    private float getQuantity(CargoAPI cargo) {
        return cargo.getQuantity(CargoAPI.CargoItemType.RESOURCES, null) +
               cargo.getQuantity(CargoAPI.CargoItemType.WEAPONS, null) +
               cargo.getQuantity(CargoAPI.CargoItemType.SPECIAL, null) +
               cargo.getQuantity(CargoAPI.CargoItemType.FIGHTER_CHIP, null);
    }

    private List<SubmarketAPI> getSubmarket(MarketAPI marketAPI) {
        List<SubmarketAPI> submarketList = new ArrayList<>();

        addIfNotNull(submarketList, marketAPI.getSubmarket(Submarkets.GENERIC_MILITARY));
        addIfNotNull(submarketList, marketAPI.getSubmarket(Submarkets.SUBMARKET_STORAGE));
        addIfNotNull(submarketList, marketAPI.getSubmarket(Submarkets.SUBMARKET_OPEN));
        addIfNotNull(submarketList, marketAPI.getSubmarket(Submarkets.SUBMARKET_BLACK));
        addIfNotNull(submarketList, marketAPI.getSubmarket(Submarkets.LOCAL_RESOURCES));

        return submarketList;
    }

    private void addIfNotNull(List<SubmarketAPI> submarketList, SubmarketAPI submarketAPI) {
        if (submarketAPI != null) {
            submarketList.add(submarketAPI);
        }
    }
}