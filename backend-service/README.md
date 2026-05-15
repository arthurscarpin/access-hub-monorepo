backend-service

```bash
# Private Key
openssl genpkey -algorithm RSA -out authz.pem -pkeyopt rsa_keygen_bits:2048
# Public Key
openssl rsa -pubout -in authz.pem -out authz.pub
```