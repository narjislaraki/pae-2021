# PAE Project 2021 - Group 03 - Last modification : 04-05-2021

## RESTful API : available operations

### Operations associated with authentication


<table style="border:1px solid black; border-collapse: collapse">

<tr>
    <th style="border:1px solid black;">URI</th>
    <th style="border:1px solid black;">Function</th>
    <th style="border:1px solid black;">Auths?</th>
    <th style="border:1px solid black;">Operation</th>
</tr>

<tr>
    <td style="border:1px solid black;">auths/login</td>
    <td style="border:1px solid black;">POST</td>
    <td style="border:1px solid black;">No</td>
    <td style="border:1px solid black;">
    Check the credentials (username and password) and send back the User and a JWT token if validated.
    </td>
</tr>

<tr>
    <td style="border:1px solid black;">auths/register</td>
    <td style="border:1px solid black;">POST</td>
    <td style="border:1px solid black;">No</td>
    <td style="border:1px solid black;">
    Send the information (email, username, password, password confirmation, first name, last name, address [street, building number, city, post code, country and optionally unit number]) to the server, which one will send a response back.
    The following information need to be unique in the DB: email, username.
    </td>
</tr>

<tr>
    <td style="border:1px solid black;">auths/user/{id}</td>
    <td style="border:1px solid black;">GET</td>
    <td style="border:1px solid black;">Yes</td>
    <td style="border:1px solid black;">
    Retrieving of the information such as id and username.
    </td>
</tr>

</table>


### Operations associated with users


<table style="border:1px solid black; border-collapse: collapse">

<tr>
    <th style="border:1px solid black;">URI</th>
    <th style="border:1px solid black;">Function</th>
    <th style="border:1px solid black;">Auths?</th>
	<th style="border:1px solid black;">Admin?</th>
    <th style="border:1px solid black;">Operation</th>
</tr>

<tr>
    <td style="border:1px solid black;">users/{id}</td>
    <td style="border:1px solid black;">GET</td>
    <td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
    <td style="border:1px solid black;">
    Returns a user with their id, username and role
    </td>
</tr>

<tr>
    <td style="border:1px solid black;">users/unvalidatedList</td>
    <td style="border:1px solid black;">GET</td>
    <td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
    <td style="border:1px solid black;">
    Returns a list populated with every account not yet validated
    </td>
</tr>

<tr>
	<td style="border:1px solid black;">users/validatedList</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Returns a list populated with all validated accounts
	</td>

<tr>
    <td style="border:1px solid black;">users/{id}/accept</td>
    <td style="border:1px solid black;">POST</td>
    <td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
    <td style="border:1px solid black;">
   	Validate a user. {id} is the user's ID. The role of the user is passed in Json form.
   	The role has to be <i>client</i>, <i>admin</i> or <i>antiquaire</i>.
    </td>
</tr>

<tr>
    <td style="border:1px solid black;">users/{id}</td>
    <td style="border:1px solid black;">DELETE</td>
    <td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
    <td style="border:1px solid black;">
    Delete a user from the DB. {id} is the user's id.
    </td>
</tr>

<tr>
	<td style="border:1px solid black;">users/{id}/transactionsBuyer</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a list populated with sales whose buyer id is the one passed in the URI.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">users/{id}/transactionsSeller</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a list populated with sales made by the user whose id is the one passed in the URI.
	</td>
</tr>

</table>

### Operations associated with furnitures

<table style="border:1px solid black; border-collapse: collapse">

<tr>
    <th style="border:1px solid black;">URI</th>
    <th style="border:1px solid black;">Function</th>
    <th style="border:1px solid black;">Auths?</th>
	<th style="border:1px solid black;">Admin?</th>
    <th style="border:1px solid black;">Operation</th>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/public</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a list of furnitures for the unlogged users.
	The list will be different depending on the role of the user.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a list of furnitures for the logged users.
	The list will be different depending on the role of the user.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/slider</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a list of furnitures to be placed int the carousel.
	These furnitures are the furnitures <i>en vente</i>, <i>sous option</i> or <i>vendus</i>.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/type/{idType}</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a list of furnitures of a certain type.
	{idType} is the type of furniture id selected by the user.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/public/{id}</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Get a specific furniture for unlogged users by giving its id.
	{id} is the furniture id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{id}</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a specific furniture for logger users by giving its id.
	{id} is the furniture id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{id}/workShop</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Changes the state of the furniture given by its id to "<i>en restauration</i>".
	{id} is the furniture id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{id}/dropOfStore</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Changes the state of the furniture given by its id to "<i>déposé en magasin</i>".
	{id} is the furniture id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{id}/offeredForSale</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Changes the state of the furniture given by its id to "<i>en vente</i>". The method also set the offered selling price.
	{id} is the furniture id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{id}/withdrawSale</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Withdraw a furniture and set its state to a  "<i>withdrawn</i>" state.
	{id} is the furniture id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/sale</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Add a sale and change the state of the furniture from the sale to "<i>vendu</i>".
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/furnituresListToBeProcessed</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Change the state of the furniture on the list to "<i>accepté</i>" or "<i>refusé</i>" and if the furniture is purchased, update its purchase price and date of shipment.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{idFurniture}/edit</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Allows to edit a furniture such as: description, type id, offered selling price and favourite photo ID. Also allows to deal with photos with id's lists for: display, hide, delete. Finally, allows to add photos to a furniture.
	{idFuniture} is the furniture id.
	</td>
</tr>

</table>

### Operations associated with types of furnitures

<table style="border:1px solid black; border-collapse: collapse">

<tr>
    <th style="border:1px solid black;">URI</th>
    <th style="border:1px solid black;">Function</th>
    <th style="border:1px solid black;">Auths?</th>
	<th style="border:1px solid black;">Admin?</th>
    <th style="border:1px solid black;">Operation</th>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/typeOfFurnitureList</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a list of types of furniture.
	</td>
</tr>

</table>

### Operations associated with photos

<table style="border:1px solid black; border-collapse: collapse">

<tr>
    <th style="border:1px solid black;">URI</th>
    <th style="border:1px solid black;">Function</th>
    <th style="border:1px solid black;">Auths?</th>
	<th style="border:1px solid black;">Admin?</th>
    <th style="border:1px solid black;">Operation</th>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{idFurniture}/photos</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns the photos related to a particuliar furniture.
	{idFuniture} is the furniture id.
	</td>
</tr>

</table>

### Operations associated with options

<table style="border:1px solid black; border-collapse: collapse">

<tr>
    <th style="border:1px solid black;">URI</th>
    <th style="border:1px solid black;">Function</th>
    <th style="border:1px solid black;">Auths?</th>
	<th style="border:1px solid black;">Admin?</th>
    <th style="border:1px solid black;">Operation</th>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{id}/getOption</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Getting active option from a specific furniture by giving its id.
	{id} is the id of furniture.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{idFurniture}/getSumOfOptionDays</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Getting the sum of days of options that the logged user has already taken for the furniture given by idFurniture.
	{idFurniture} is the furniture id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{id_option}/cancelOption</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Cancel an option.
	{id_option} is the option id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">furnitures/{idFurniture}/{idUser}/introduceOption</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Introduce an option related to a furniture and a user.
	{idFuniture} is the furniture id. {idUser} is the user id.
	</td>
</tr>

</table>

#### Operations associated with requests for visits

<table style="border:1px solid black; border-collapse: collapse">

<tr>
    <th style="border:1px solid black;">URI</th>
    <th style="border:1px solid black;">Function</th>
    <th style="border:1px solid black;">Auths?</th>
	<th style="border:1px solid black;">Admin?</th>
    <th style="border:1px solid black;">Operation</th>
</tr>

<tr>
	<td style="border:1px solid black;">visits/notConfirmedVisits</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Returns a list of requests for visits with the status "<i>en attente</i>".
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">visits/toBeProcessedVisits</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Returns a list of requests for visits with the status "<i>accepté</i>" but with furnitures having the status "<i>proposé</i>".
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">visits/{idVisit}/furnitures</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Returns a list of furnitures for one request for visit.
	{idVisit} is the visit id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">visits/{id}/accept</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Accepts the request for visit given by its id. Change its state to "<i>acceptée</i>".
	{id} the request for visit id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">visits/{id}/cancel</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">
	Cancel the request for visit given by its id. Change its state to "<i>annulée</i>".
	{id} the request for visit id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">visits/{id}</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a specific visit for logged users by giving its id.
	</td>
</tr>

<tr>
	<td style="border:1px solid black;">visits/introduce</td>
	<td style="border:1px solid black;">POST</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Introduce a request for visit. It also adds the address into the database if this is different from the customer's address. This method adds each furniture and for each furniture, each photo.
	</td>
</tr>

</table>

### Operations associated with sales

<table style="border:1px solid black; border-collapse: collapse">

<tr>
    <th style="border:1px solid black;">URI</th>
    <th style="border:1px solid black;">Function</th>
    <th style="border:1px solid black;">Auths?</th>
	<th style="border:1px solid black;">Admin?</th>
    <th style="border:1px solid black;">Operation</th>
</tr>

<tr>
	<td style="border:1px solid black;">sales</td>
	<td style="border:1px solid black;">GET</td>
	<td style="border:1px solid black;">Yes</td>
	<td style="border:1px solid black;">No</td>
	<td style="border:1px solid black;">
	Returns a list of all the sales.
	</td>
</tr>

</table>

---
## Properties
### JWTSecret

<p>A custom string not to share to encrypt the token</p>

### BaseUri

<p>The base url of the application</p>

### url

<p>The database's url</p>

### user

<p>The database's user</p>

### password

<p>The database's password</p>

### Logger options

#### Two fields exist in the properties file
#### logPath
<p>It can be set to default by writing <i>default</i>.<br>
Doing this, the default location will be the project folder appended by <i>/Logs</i>.<br>
<b>logPath=default</b></p>

<p>It can also be set to any path following this format <i>A:/a/custom/folder/</i>.<br>
Note that the last slash <i>/</i> in the path is not mandatory but using backslashes is not managed.<br>
<b>logPath=A:/a/custom/folder/</b></p>

#### logFileName
<p>The name, with its extension.<br>
No default name is managed here. Thus this field must be filled in.<br>
<b>logFileName=logs.log</b></p>

#### Conclusion
<p><b>logPath=A:/a/custom/folder/</b><br>
<b>logFileName=myPersonnalLogs.log</b><br><br>
Are going to log in <b>A:/a/custom/folder/myPersonnalLogs.log</b></p>
-----=====-----
<p><b>logPath=default/</b><br>
<b>logFileName=logs.log</b><br><br>
Are going to log in <b>X:/the/project/location/Logs/logs.log</b></p>

### SendStackTraceToClient

<p>Boolean to set to TRUE if the stacktrace from the backend's errors has to be sent with the errors</p>

### connectionQuantity

<p>The maximum amount of connection to be able to stay open in the connection pool.</p>
