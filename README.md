# Simple Blockchain in Kotlin

### Tools 

- Kotlin
- Ktor
- Koin
- RocksDB
- Gradle

### How to Run 

````shell
git clone https://github.com/phvicente/simple-blockchain.git
cd simple-blockchain-kotlin
./gradlew run
gradle run
````

***the application will be available at:*** http://localhost:8080.

### Testing the Project with cURL

- Adding a New Block
````shell
curl -X POST http://localhost:8080/blocks/add \
     -H "Content-Type: application/json" \
     -d '[
           {"id": "tx1", "content": "Transaction 1"},
           {"id": "tx2", "content": "Transaction 2"}
         ]'

````

- Recover All Blocks
````shell
curl -X GET http://localhost:8080/blocks
````