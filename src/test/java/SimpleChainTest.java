import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.simplecoin.*;

import java.security.Security;
import java.util.ArrayList;

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
}

