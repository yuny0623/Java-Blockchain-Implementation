import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.simplecoin.Block;

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
}
