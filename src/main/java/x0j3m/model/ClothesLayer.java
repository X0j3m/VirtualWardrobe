package x0j3m.model;

public enum ClothesLayer {
    BASE_LAYER,
    MID_LAYER,
    OUTER_LAYER,
    ACCESSORY,
    FOOTWEAR,
    HEADWEAR,
    BOTTOMWEAR;

    @Override
    public String toString() {
        return this.toString().toLowerCase().replace("_", "");
    }
}
