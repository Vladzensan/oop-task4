import commoncipher.Cipher;

module AESCipher {
    requires Ciphers;
    provides Cipher with aescipher.AESCipher;
}