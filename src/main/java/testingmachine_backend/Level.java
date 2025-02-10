package testingmachine_backend;

public enum Level {

    /**
     * PLM_10009 {0} системийн утга олдсонгүй
     */
    PLM_10009,
    /**
     * PLM_10010 {0} системийн утга = {1} бичлэг олдсонгүй
     */
    PLM_10010,
    /**
     * PLM_10011 процесс олдсонгүй: {0}
     */
    PLM_10011,;

    @Override
    public String toString() {
        return this.name();
    }
}
