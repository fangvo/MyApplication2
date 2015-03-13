package com.fangvo.myapplication2.app;

public class ClientsTable {
    public String cname;
    public String adres;
    public Long inn;
    public Long kpp;
    public String bank;
    public Long rs;
    public Long ks;
    public Long bik;
    public String ctype = "Покупатель";
    public String dir;

    ClientsTable(String _cname,String _adres,Long _inn,Long _kpp,String _bank,Long _rs,Long _ks, Long _bik,String _dir){
        cname = _cname;
        adres = _adres;
        inn = _inn;
        kpp = _kpp;
        bank = _bank;
        rs = _rs;
        ks = _ks;
        bik  = _bik;
        dir = _dir;

    }

}
