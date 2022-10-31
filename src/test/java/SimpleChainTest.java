import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.simplecoin.Block;

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
}
