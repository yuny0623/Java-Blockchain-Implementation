package org.simplecoin;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class SimpleChain {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static int difficulty = 6;
    public static void main(String[] args) {
        blockchain.add(new Block("first block", "0"));
        System.out.println("Trying to Mine Block 1... ");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("second block", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("Trying to Mine Block 2... ");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("third block", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("Trying to Mine Block 3... ");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockChain is Valid: " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }

    public static boolean isChainValid(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i = 1; i <blockchain.size(); ++i){
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            if(!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("Current Hashes not equal");
                return false;
            }
            if(!previousBlock.hash.equals(currentBlock.previousHash)){
                System.out.println("Previous Hashes not equal");
                return false;
            }
            if(!currentBlock.hash.substring(0, difficulty).equals(hashTarget)){
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
