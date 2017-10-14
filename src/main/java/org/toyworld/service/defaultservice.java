package org.toyworld.service;

import org.toyworld.api.protocol;
import org.toyworld.api.service;
import org.toyworld.api.serviceparams;

public class defaultservice implements service {
	protocol proto;
	@Override
	public void Excute(serviceparams params) {
		// TODO Auto-generated method stub
		proto.Excute(params);
	}

	@Override
	public void BindProtocol(protocol pro) {
		// TODO Auto-generated method stub
		proto = pro;
	}

}
