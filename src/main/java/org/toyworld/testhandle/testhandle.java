package org.toyworld.testhandle;

import org.toyworld.api.servicehandle;
import org.toyworld.api.serviceparams;
import org.toyworld.protocol.http.minihttphandleparams;

public class testhandle implements servicehandle {

	@Override
	public void Excute(serviceparams params) {
		// TODO Auto-generated method stub
		System.out.println("handle hello world");
		minihttphandleparams handleparams = (minihttphandleparams)params.bindObj;
		handleparams.GetResp().SetVer("HTTP/1.1");
		handleparams.GetResp().SetStatus("200");
		handleparams.GetResp().GetHeader().put("Content-Type", "text/html");
		String body = "hrz hello lalalla........";
		System.arraycopy(body.getBytes(), 0, handleparams.GetResp().GetBody() , 0, body.getBytes().length);
		handleparams.GetResp().GetHeader().put("Content-Length", String.valueOf(body.getBytes().length));
		handleparams.GetResp().SetBodySize(body.getBytes().length);
	}

}
