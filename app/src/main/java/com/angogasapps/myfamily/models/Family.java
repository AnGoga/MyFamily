package com.angogasapps.myfamily.models;

public class Family {
    private static Family family;

    public static Family getInstance(){
        synchronized (Family.class){
            if (family == null)
                family = new Family();
            return family;
        }
    }
}
