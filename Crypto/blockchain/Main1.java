package com.zoubworld.Crypto.blockchain;

public class Main1 {

  public static void main(String[] args) {
    Blockchain blockchain = new Blockchain(6);
    blockchain.addBlock(blockchain.newBlock("Tout sur le Bitcoin"));
    blockchain.addBlock(blockchain.newBlock("Sylvain Saurel"));
    blockchain.addBlock(blockchain.newBlock("https://www.toutsurlebitcoin.fr"));
  
    System.out.println("Blockchain valid ? " + blockchain.isBlockChainValid());
    System.out.println(blockchain);
  }

}