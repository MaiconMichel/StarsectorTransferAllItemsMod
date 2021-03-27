package data.scripts;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;

import java.util.*;

@SuppressWarnings("SpellCheckingInspection")
public class MarketTabManager {
    private List<SubmarketAPI> submarkets;
    private Map<SubmarketAPI, Float> quantityList = new HashMap<>();
    private CargoAPI cargoCurrent;
    private float quantityOld = -1;
    private float quantityCurrent = -1;
    private float countForceEnd = 0;

    public MarketTabManager(MarketAPI market) {
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

        return (quantityCurrent == 0 && quantityOld == quantityCurrent) || countForceEnd > 5;
    }

    private void updateCargo() {
        if (cargoCurrent == null) {
            if (submarkets.size() == 1) {
                cargoCurrent = submarkets.get(0).getCargo();
            } else {
                for (SubmarketAPI submarket : submarkets) {
                    float quantityOld = quantityList.get(submarket);
                    float quantityNew = getQuantity(submarket.getCargo());

                    if (quantityOld != quantityNew) {
                        cargoCurrent = submarket.getCargo();
                        return;
                    }
                }
            }
        }
    }

    private void updateQuantityList() {
        quantityList.clear();

        for (SubmarketAPI submarket : submarkets) {
            quantityList.put(submarket, getQuantity(submarket.getCargo()));
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