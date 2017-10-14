package org.toyworld.publicmodule;

import org.toyworld.session.clientsession;
import org.toyworld.toycontext.onecontext;
import org.toyworld.api.*;

public class servicemgr {
    public static void bindservice(onecontext rundata,clientsession session){
        String[] modulename = rundata.skey.split(";");
    	protocol pro = rundata.market.getprotocolmodule(modulename[0]);
        pro.Bindhandle(rundata.market.gethandlemodule(modulename[1]));
        session.getservice().BindProtocol(pro);
    }
}
