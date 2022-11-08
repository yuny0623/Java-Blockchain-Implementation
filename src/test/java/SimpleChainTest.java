import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.simplecoin.*;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleChainTest {

    @Test
    public void 블록_생성_테스트(){
        // given
        Block genesisBlock = new Block("first block", "0");
        Block secondBlock = new Block("second block", genesisBlock.hash);
        Block thirdBlock = new Block("thrid block", secondBlock.hash);

        // when
        System.out.println("Hash for block 1: " + genesisBlock.hash);
        System.out.println("Hash for block 2: " + secondBlock.hash);
        System.out.println("Hash for block 3: " + thirdBlock.hash);

        // then
        Assertions.assertNotNull(genesisBlock.hash);
        Assertions.assertNotNull(secondBlock.hash);
        Assertions.assertNotNull(thirdBlock.hash);
    }

    @Test
    public void GSON라이브러리로_블록_생성_테스트(){
        // given
        ArrayList<Block> blockchain = new ArrayList<>();
        blockchain.add(new Block("first block", "0"));
        blockchain.add(new Block("second block", blockchain.get(blockchain.size() - 1).hash));
        blockchain.add(new Block("third block", blockchain.get(blockchain.size() - 1).hash));

        // when
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);

        // then
        Assertions.assertNotNull(blockchainJson);
    }

    @Test
    public void 채굴테스트(){
        // given
        ArrayList<Block> blockchain = new ArrayList<>();
        int difficulty = 6;

        // when
        blockchain.add(new Block("first block", "0"));
        System.out.println("Trying to Mine Block 1... ");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("second block", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("Trying to Mine Block 2... ");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("third block", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("Trying to Mine Block 3... ");
        blockchain.get(2).mineBlock(difficulty);

        boolean isValid = SimpleChain.isChainValid();
        System.out.println("\nBlockChain is Valid: " + isValid);

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);

        // then
        Assertions.assertNotNull(blockchainJson);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void 트랜잭션_생성_유효성검증_테스트(){
        // given
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();

        // when
        System.out.println("Private and public keys: ");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);

        // then
        System.out.println("Is signature verified.");
        System.out.println(transaction.verifySignature());
        Assertions.assertTrue(transaction.verifySignature());
    }

    @Test
    public void 최종_테스트(){
        // given
        ArrayList<Block> blockchain = new ArrayList<>();
        HashMap<String, TransactionOutput> UTXOs = new HashMap<>(); // list of all unspent transactions.
        int difficulty = 3;
        float minimumTransaction = 0.1f;
        Wallet walletA;
        Wallet walletB;
        Transaction genesisTransaction;

        // when
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction
        genesisTransaction.transactionId = "0"; //manually set the transaction id
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        SimpleChain.addBlock(genesis);

        // then
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        SimpleChain.addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        SimpleChain.addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Assertions.assertTrue(SimpleChain.isChainValid());
    }
}

