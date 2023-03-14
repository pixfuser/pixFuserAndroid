# <img src="https://user-images.githubusercontent.com/85285176/224109146-9a6548a9-c9c8-4f9f-a2ac-f4c8807d8af7.png" width="54px" style="margin-bottom:-16px">  pixFuserAndroid

<br>

Android app prototype of crypro card "PixFuser" project

This prototype app contains logic of nfc usage for transactions

This screen let up connect your wallet from Phantom Wallet app
<div align="center">
  <img src="https://user-images.githubusercontent.com/5672290/224949189-22710a81-96fb-4c3f-a42e-41f7e4e8ccd5.jpeg" width="350px">
</div>

<br>

On this screen you can make simple transaction using Phantom wallet and input field or make an nfc transaction for other app user
<div align="center">
  <img src="https://user-images.githubusercontent.com/5672290/224949186-b3b171a6-2fb9-45c6-93bd-f0075393447f.jpeg" width="350px">
</div>

<br>

This screen needed to handle other user nfc trasaction
<div align="center">
  <img src="https://user-images.githubusercontent.com/5672290/224949173-df5cf252-0aff-4c9a-87eb-7b8a1db3c373.jpeg" width="350px">
</div>

<br>

App logic: 

We use phantomBridge(https://github.com/DESTROYED/PhantomBridgeAndroid/tree/develop) library to connect our app to Phantom Wallet

When wallet is connected we redirect user to a page 
<div align="center">
  <img src="https://user-images.githubusercontent.com/5672290/224949186-b3b171a6-2fb9-45c6-93bd-f0075393447f.jpeg" width="350px">
</div>
here user can make a simple transaction, but if receipient got our app, we can make an nfc transer. 

First of all our app genereate a key-pair and both users send to each their Pub key, to make our data transfer more secure, using Diffie-ellman algorythm
(https://en.wikipedia.org/wiki/Diffie%E2%80%93Hellman_key_exchange)

First user send to second one transation without signature with sender wallet info, when receipient got encrypted transaction, we can easily decrypt it and add our wallet info to this one, then we encrypt if and send back.
Whem First user got a full transaction, we sign it using Phantom Wallet and send it to Solana Blockchain.
