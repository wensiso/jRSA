# jRSA Examples
Applications examples from jRSA library.

## FileEncrypter
jRSA File Encrypter - An educational tool for teaching the RSA algorithm

```
usage:
java FileEncrypter -e <plain_text_file> -o <encrypted_file> [-k] [-v]
java FileEncrypter -d <encrypted_file> -o <plain_text_file> [-k] [-v]

 -d,--decrypt <encrypt_filename>   encrypted file
 -e,--encrypt <msg_filename>       plain text file
 -k                                creates a new RSA key pair
 -o,--output <dest_filename>       output filename
 -t,--test <message>               test the program, without write files
 -v                                show plain text file on terminal
```

## Encrypted Chat
jRSA Encrypted Chat - An educational tool for teaching the RSA algorithm

Main file: 
```
EncryptedChat.java
```
