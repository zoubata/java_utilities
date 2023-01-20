package com.zoubworld.Crypto.server.Utils;

public interface IStoreObject {

	public int hashCode();

	public boolean equals(Object obj);

	public IStoreObject Parser(String line);

	public String toLine();

}