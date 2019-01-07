package edu.aku.hassannaqvi.toic_screening02.other;

import java.io.Serializable;

/**
 * Created by ali.azaz on 02/09/18.
 */

public class childData implements Serializable {
    String enrollID,/*child_name,f_name,*/serialNo;

    public String getEnrollID() {
        return enrollID;
    }

   /* public String getChild_name() {
        return child_name;
    }

    public String getF_name() {
        return f_name;
    }*/

    public String getSerialNo() {
        return serialNo;
    }

    public childData(String enrollID, /*String child_name, String f_name, */String serialNo) {
        this.enrollID = enrollID;
  /*      this.child_name = child_name;
        this.f_name = f_name;*/
        this.serialNo = serialNo;
    }
}
