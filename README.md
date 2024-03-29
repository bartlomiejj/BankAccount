# Bank

## RUN APP

mvn clean install -DskipTests

docker-compose build

docker-compose up

## TODO
1. Exception handler
2. Custom exceptions
3. Introduce bid/ask rate, instead of mid
4. Test coverage
5. Refactor exchange service to base on DTO, instead of entities
6. Enable validators
7. Introduce exchange type where the user can decide to, e.g., buy 1 USD or sell 10 PLN for USD.
8. Format readme

## Services:

### 1. Currency Rate Provider - /api/currency

- **PUT /refresh** - Downloads currencies from NBP and saves in the DB. 
- Refresh meant to be scheduled by an external application.

  Example curl:
   ```bash
   curl --request PUT \
      --url http://localhost:8080/api/currency/refresh

### 2. Accounts - /api/account

- **POST** - creates new account <br/>
   Example curl:
   ```bash
  curl --request POST \
      --url http://localhost:8080/api/account \
      --header 'Content-Type: application/json' \
      --data '{
      "name": "Tom",
      "surname": "Jones",
      "pesel": "123456789",
      "currencyCode": "PLN",
      "initialBalance": 12.00
      }'
--  Response:<br/>
      `{
      "name": "Tom",
      "surname": "Jones",
      "pesel": "123456789",
      "accounts": [
      {
      "accountNumber": "3998074867",
      "currencyCode": "USD",
      "balance": 12.00
      }
      ]
      }`

- **GET /{accountNumber}** - returns data about provided account<br/>
    Example curl: <br/>
  ```bash
      curl --request GET \
      --url http://localhost:8080/api/account/3998074867`
-- Response: <br/>
    `{
    "accountNumber": "3998074867",
    "currencyCode": "USD",
    "balance": 12.00
    }`

- **GET /customer/{pesel}** - returns data about provided customer <br/>
    Example curl: <br/>
     ```bash
  curl --request GET \
      --url http://localhost:8080/api/account/customer/123456789` <br/>

-- Response: <br/>
 `{
      "name": "Tom",
      "surname": "Jones",
      "pesel": "123456789",
      "accounts": [
      {
      "accountNumber": "3998074867",
      "currencyCode": "USD",
      "balance": 12.00
      },
      {
      "accountNumber": "2773966336",
      "currencyCode": "PLN",
      "balance": 12.00
      }
      ]
      }`
### 3. Currency exchange - /api/exchange <br/>
- ** PUT ** - exchange currency based on mid rate <br/>
   Example curl: <br/>
   ```bash
  curl --request PUT \
   --url http://localhost:8080/api/exchange \
   --header 'Content-Type: application/json' \
   --data '{
   "pesel": "123456789",
   "sourceAccount": "2773966336",
   "sourceCurrency": "PLN",
   "targetAccount": "3998074867",
   "targetCurrency": "USD",
   "targetValue": 1.00
   }'`
-- Response: <br/>
   ```json
   {
   "name":"Tom",
   "surname":"Jones",
   "pesel":"123456789",
   "accounts":[
      {
         "accountNumber":"3998074867",
         "currencyCode":"USD",
         "balance":13.00
      },
      {
         "accountNumber":"2773966336",
         "currencyCode":"PLN",
         "balance":8.003400
      }
   ]
}