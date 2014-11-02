package com.lipssmackingparadise.reviewapp.server;

public interface ServerConnectListener {
	public void retrieveCompleted (String result);
	public void sendCompleted (String result);
	public void errorOnServerConnect ();
}
