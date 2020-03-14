package org.example.project;

import org.junit.Assert;
import org.junit.Test;

import com.password4j.BCryptFunction;
import com.password4j.CompressedPBKDF2Function;
import com.password4j.HashBuilder;
import com.password4j.HashChecker;
import com.password4j.HashingFunction;
import com.password4j.PBKDF2Function;
import com.password4j.Password;
import com.password4j.SCryptFunction;
import com.password4j.WithHmac;


public class PublicPasswordTest
{

    private static final TestSuite[] PBKDF2_TEST = new TestSuite[] {
            new TestSuite("r+bFUweFtsxrHGRTOEcxvV7kMu5Un9QvtmlXea2KHFv1neacSPd078QAfVKY+QM8AkHVq2kwXntk7O642DTP7A==", "password",
                    "salt", null, PBKDF2Function.getInstance(WithHmac.SHA512, 1000, 512)),

            new TestSuite("x/daChKTPQGTKZrBsmPIqJ3KtqcaYni8FqdziEgPRw9gowIpZxfzW7UI8gqZj0pI5xChr5RDxjYjc8yMbucHHw==", "password",
                    "salt", "pepper", PBKDF2Function.getInstance(WithHmac.SHA512, 1000, 512)),

            new TestSuite("EgvuM3qhGradmNwl2b1Z5uPnasY=", "123", "456", "",
                    PBKDF2Function.getInstance(WithHmac.SHA1, 49999, 160)) };

    @Test
    public void test()
    {

        for (TestSuite test : PBKDF2_TEST)
        {
            Assert.assertEquals(test.hashingFunction.toString(), test.hash,
                    Password.hash(test.password).addSalt(test.salt).addPepper(test.pepper).with(test.hashingFunction)
                            .getResult());
        }

    }

    /**
     * Must compile.
     */
    @Test
    public void testAccessibility()
    {
        try
        {
            String password = "";
            String salt = "";
            int saltLength = 1;
            String pepper = "";

            HashBuilder hb = Password.hash(password);
            hb.addPepper(pepper);
            hb.addPepper();
            hb.addRandomSalt();
            hb.addRandomSalt(saltLength);
            hb.addSalt(salt);

            hb.withCompressedPBKDF2();
            hb.withSCrypt();
            hb.withBCrypt();
            hb.withPBKDF2();

            HashChecker hc = Password.check(password, password);
            hc.addPepper(pepper);
            hc.addPepper();
            hc.addSalt(salt);
            hc.withCompressedPBKDF2();
            hc.withSCrypt();
            hc.withBCrypt();
            hc.withPBKDF2();

            CHB chb = Password.hash(password, CHB::new);
            chb.foo();

            CHC chc = Password.check(password, password, CHC::new);
            chc.bar();

            WithHmac.SHA256.code();
            WithHmac.values();
            WithHmac.SHA1.bits();

            PBKDF2Function.getInstance(WithHmac.SHA512, 1, 1);
            PBKDF2Function.getInstance(password, 1, 1);
            CompressedPBKDF2Function.getInstance(WithHmac.SHA512, 1, 1);
            CompressedPBKDF2Function.getInstance(password, 1, 1);
            CompressedPBKDF2Function.getInstanceFromHash(password);

            BCryptFunction.getInstance(1);
            SCryptFunction.getInstance(2, 1, 1);
            SCryptFunction.getInstanceFromHash(password);
        }
        catch (Exception e)
        {
            //
        }
    }

    private class CHB extends HashBuilder<CHB>
    {

        public CHB(String plain)
        {
            super(plain);
        }

        public boolean foo()
        {
            return true;
        }
    }

    private class CHC extends HashChecker<CHC>
    {

        public CHC(String plain, String hash)
        {
            super(plain, hash);
        }

        public boolean bar()
        {
            return true;
        }
    }

    private static class TestSuite
    {
        private String hash;

        private String password;

        private String salt;

        private String pepper;

        private HashingFunction hashingFunction;

        TestSuite(String hash, String password, String salt, String pepper, HashingFunction hashingFunction)
        {
            this.hash = hash;
            this.password = password;
            this.salt = salt;
            this.pepper = pepper;
            this.hashingFunction = hashingFunction;
        }
    }

}
