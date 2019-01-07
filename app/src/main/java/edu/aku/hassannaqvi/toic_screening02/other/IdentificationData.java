package edu.aku.hassannaqvi.toic_screening02.other;

import java.io.Serializable;

/**
 * Created by ali.azaz on 02/09/18.
 */

public class IdentificationData implements Serializable {
    String vSlip,/*teamNo,uc,tehsil,*/hhno;

    public String getvSlip() {
        return vSlip;
    }
/*
    public String getTeamNo() {
        return teamNo;
    }

    public String getUc() {
        return uc;
    }

    public String getTehsil() {
        return tehsil;
    }*/

    public String getHhno() {
        return hhno;
    }

    public IdentificationData(String vSlip, /*String teamNo, String uc, String tehsil, */String hhno) {
        this.vSlip = vSlip;
     /*   this.teamNo = teamNo;
        this.uc = uc;
        this.tehsil = tehsil;*/
        this.hhno = hhno;
    }
}
