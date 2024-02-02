package com.solvd.laba.xml.jaxb;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbIdentifiableAdapter extends XmlAdapter<JaxbIdentifiableAdapter.Helper, String> {


    @Override
    public String unmarshal(Helper helper) throws Exception {
        return null;
    }

    @Override
    public Helper marshal(String s) throws Exception {
        return null;
    }

    public static class Helper {

        @XmlAttribute
        public long id;
    }
}
