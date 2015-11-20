package com.dataart.springtraining.auxiliary;

import javax.servlet.http.HttpServletResponse;

public class HeaderSetter
{
	public static void setHeaders(HttpServletResponse response)
	{
		response.setHeader("pragma", "no-cache");
		response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
		response.setHeader("Expires", "0");
	}
}
