package com.angogasapps.myfamily.objects;

import androidx.annotation.NonNull;

public class User {
    public String id = "";
    public String phone = "";
    public String family = "";
    public String name = "";
    public Long birthday = 0L;

    public User(){
        this.id = "";
        this.phone = "";
        this.family = "";
        this.name = "";
        this.birthday = 0L;
    }

    @Override
    public String toString() {
        return "id = " + this.id + "\nphone = " + this.phone + "\nfamily = " + this.family +
                "\nname = " + this.name + "\nbirthday = " + this.birthday;
    }
}
