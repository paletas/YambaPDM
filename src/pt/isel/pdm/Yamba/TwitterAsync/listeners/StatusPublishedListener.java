package pt.isel.pdm.Yamba.TwitterAsync.listeners;

import winterwell.jtwitter.Twitter.Status;

public interface StatusPublishedListener {

	public void onStatusPublished(Status status);
	
}
