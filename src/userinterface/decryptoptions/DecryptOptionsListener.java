package decryptoptions;

public interface DecryptOptionsListener{
	void optionsSelect(int ident, int length, String masterKey, String fileKeyPatch);
}