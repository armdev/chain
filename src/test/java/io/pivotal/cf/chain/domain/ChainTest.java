package io.pivotal.cf.chain.domain;

import io.pivotal.cf.chain.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ChainTest {

    @Autowired
    private Chain chain;

    @Autowired
    private MerkleTree merkleTree;

    @Test
    public void testChain() throws VerificationException {
        chain.clear();
        assertTrue(chain.leaves() == 0);

        merkleTree.loadRandomEntries(10);
        chain.addBlock(merkleTree);

        try {
            chain.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        assertTrue(chain.leaves() == 10);

        merkleTree.loadRandomEntries(15);
        chain.addBlock(merkleTree);

        try {
            chain.verify();
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        assertTrue(chain.leaves() == 25);
    }

    @Test
    public void testLoadToManyEntries() {
        merkleTree.clear();
        chain.clear();
        try {
            for (int i = 0; i < 5; i++) {
                merkleTree.loadRandomEntries(MerkleTree.MAX_MERKLE_ENTRIES);
                chain.addBlock(merkleTree);
                merkleTree.clear();
            }
        } catch (VerificationException e) {
            fail("should not have thrown an exception.");
        }

        try {
            merkleTree.loadRandomEntries(1);
            chain.addBlock(merkleTree);
        } catch (VerificationException e) {
            //expected
        }

        merkleTree.clear();
        chain.clear();
    }
}