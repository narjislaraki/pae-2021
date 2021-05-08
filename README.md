# PAE Project 2021 - Group 03 - Last modification : 04-05-2021

## RESTful API : available operations

### Operations associated with authentication


<table style="border:1px solid black; padding:5px border-collapse: collapse">

<tr>
    <th style="border:1px solid black; padding:5px">URI</th>
    <th style="border:1px solid black; padding:5px">Function</th>
    <th style="border:1px solid black; padding:5px">Auths?</th>
    <th style="border:1px solid black; padding:5px">Operation</th>
    <th style="border:1px solid black; padding:5px">Error thrown</th>
</tr>

<tr>
    <td style="border:1px solid black; padding:5px">auths/user</td>
    <td style="border:1px solid black; padding:5px">GET</td>
    <td style="border:1px solid black; padding:5px">Yes</td>
    <td style="border:1px solid black; padding:5px">
    Retrieving of the information such as user's id and username. Those information are retrieved by the given token only.
    </td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
    <td style="border:1px solid black; padding:5px">auths/login</td>
    <td style="border:1px solid black; padding:5px">POST</td>
    <td style="border:1px solid black; padding:5px">No</td>
    <td style="border:1px solid black; padding:5px">
    Check the credentials (username and password) and send back the User and a JWT token if validated.
    </td>
    <td style="border:1px solid black; padding:5px">412 - if null or empty email and/or password<br>401 - if the credentials are wrong or the user is not validated yet</td>
</tr>

<tr>
    <td style="border:1px solid black; padding:5px">auths/register</td>
    <td style="border:1px solid black; padding:5px">POST</td>
    <td style="border:1px solid black; padding:5px">No</td>
    <td style="border:1px solid black; padding:5px">
    Send the information (email, username, password, password confirmation, first name, last name, address [street, building number, city, post code, country and optionally unit number]) to the server, which one will send a response back.
    The following information need to be unique in the DB: email, username.
    </td>
    <td style="border:1px solid black; padding:5px">412 - if null or empty email, password, password verification, address street, address building number, address postcode, address country and/or address city<br>
    412 - if the email or the username are already in use</td>
</tr>

</table>


### Operations associated with users


<table style="border:1px solid black; padding:5px border-collapse: collapse">

<tr>
    <th style="border:1px solid black; padding:5px">URI</th>
    <th style="border:1px solid black; padding:5px">Function</th>
    <th style="border:1px solid black; padding:5px">Auths?</th>
	<th style="border:1px solid black; padding:5px">Admin?</th>
    <th style="border:1px solid black; padding:5px">Operation</th>
    <th style="border:1px solid black; padding:5px">Error thrown</th>
</tr>

<tr>
    <td style="border:1px solid black; padding:5px">users/{id}</td>
    <td style="border:1px solid black; padding:5px">GET</td>
    <td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
    <td style="border:1px solid black; padding:5px">
    Returns a user with their id, username and role
    </td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
    <td style="border:1px solid black; padding:5px">users/unvalidatedList</td>
    <td style="border:1px solid black; padding:5px">GET</td>
    <td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
    <td style="border:1px solid black; padding:5px">
    Returns a list populated with every account not yet validated
    </td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">users/validatedList</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list populated with all validated accounts
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">users/{id_user}/transactionsBuyer</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list populated with sales whose buyer id is the one passed in the URI.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">users/{id_user}/transactionsSeller</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list populated with sales made by the user whose id is the one passed in the URI.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
    <td style="border:1px solid black; padding:5px">users/{id_user}/accept</td>
    <td style="border:1px solid black; padding:5px">POST</td>
    <td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
    <td style="border:1px solid black; padding:5px">
   	Validate a user. {id_user} is the user's ID. The role of the user is passed in Json form.
   	The role has to be <i>client</i>, <i>admin</i> or <i>antiquaire</i>.
    </td>
    <td style="border:1px solid black; padding:5px">412 - if the users's id is invalid or does not match any user, the user is already validated, no role is given or the role does not fit to "client", "antiquaire" or "admin"</td>
</tr>

<tr>
    <td style="border:1px solid black; padding:5px">users/{id_user}</td>
    <td style="border:1px solid black; padding:5px">DELETE</td>
    <td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
    <td style="border:1px solid black; padding:5px">
    Delete a user from the DB. {id_user} is the user's id.
    </td>
    <td style="border:1px solid black; padding:5px">412 - if the users's id is invalid or does not match any user</td>
</tr>

</table>

### Operations associated with furnitures

<table style="border:1px solid black; padding:5px border-collapse: collapse">

<tr>
    <th style="border:1px solid black; padding:5px">URI</th>
    <th style="border:1px solid black; padding:5px">Function</th>
    <th style="border:1px solid black; padding:5px">Auths?</th>
	<th style="border:1px solid black; padding:5px">Admin?</th>
    <th style="border:1px solid black; padding:5px">Operation</th>
    <th style="border:1px solid black; padding:5px">Error thrown</th>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/public</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of furnitures for the unlogged users.
	The list will be different depending on the role of the user.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of furnitures for the logged users.
	The list will be different depending on the role of the user.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/slider</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of furnitures to be placed int the carousel.
	These furnitures are the furnitures <i>en vente</i>, <i>sous option</i> or <i>vendus</i>.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/slider/{id_type}</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of furnitures having the type provided in parameter by its is, to be placed int the carousel.
	These furnitures are the furnitures <i>en vente</i>, <i>sous option</i> or <i>vendus</i>.
	{id_type} is the id of type of furnitures.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/type/{id_type}</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of furnitures of a certain type.
	{id_type} is the type of furniture id selected by the user.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/public/{id_furniture}</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Get a specific furniture for unlogged users by giving its id.
	{id_furniture} is the furniture id.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a specific furniture for logger users by giving its id.
	{id_furniture} is the furniture id.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/research</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Get a list of furnitures for research purposes.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}/workShop</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Changes the state of the furniture given by its id to "<i>en restauration</i>".
	{id_furniture} is the furniture id.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if the furniture's id is invalid or the furniture condition is not to "acheté"</td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}/dropOfStore</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Changes the state of the furniture given by its id to "<i>déposé en magasin</i>".
	{id_furniture} is the furniture id.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if the furniture's id is invalid or the furniture condition is not to "en restauration" or "acheté"</td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}/offeredForSale</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Changes the state of the furniture given by its id to "<i>en vente</i>". The method also set the offered selling price.
	{id_furniture} is the furniture id.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if the furniture's id is invalid or the furniture condition is not to "déposé en magasin"</td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}/withdrawSale</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Withdraw a furniture and set its state to a  "<i>withdrawn</i>" state.
	{id_furniture} is the furniture id.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if the furniture's id is invalid or the furniture condition is not to "en vente" or "déposé en magasin"</td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/furnituresListToBeProcessed</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Change the state of the furniture on the list to "<i>accepté</i>" or "<i>refusé</i>" and if the furniture is purchased, update its purchase price and date of shipment.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if one of the furniture has is state "acheté" and : has a purchase price <= 0 or has a null pickup date</td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}/edit</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Allows to edit a furniture such as: description, type id, offered selling price and favourite photo ID. Also allows to deal with photos with id's lists for: display, hide, delete. Finally, allows to add photos to a furniture.
	{id_furniture} is the furniture id.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if the furniture's id is invalid or if some images are not related to the furniture</td>
</tr>

</table>

### Operations associated with types of furnitures

<table style="border:1px solid black; padding:5px border-collapse: collapse">

<tr>
    <th style="border:1px solid black; padding:5px">URI</th>
    <th style="border:1px solid black; padding:5px">Function</th>
    <th style="border:1px solid black; padding:5px">Auths?</th>
	<th style="border:1px solid black; padding:5px">Admin?</th>
    <th style="border:1px solid black; padding:5px">Operation</th>
    <th style="border:1px solid black; padding:5px">Error thrown</th>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/typeOfFurnitureList</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of types of furniture.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

</table>

### Operations associated with photos

<table style="border:1px solid black; padding:5px border-collapse: collapse">

<tr>
    <th style="border:1px solid black; padding:5px">URI</th>
    <th style="border:1px solid black; padding:5px">Function</th>
    <th style="border:1px solid black; padding:5px">Auths?</th>
	<th style="border:1px solid black; padding:5px">Admin?</th>
    <th style="border:1px solid black; padding:5px">Operation</th>
    <th style="border:1px solid black; padding:5px">Error thrown</th>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}/photos</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns the photos related to a particuliar furniture.
	{id_furniture} is the furniture id.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

</table>

### Operations associated with options

<table style="border:1px solid black; padding:5px border-collapse: collapse">

<tr>
    <th style="border:1px solid black; padding:5px">URI</th>
    <th style="border:1px solid black; padding:5px">Function</th>
    <th style="border:1px solid black; padding:5px">Auths?</th>
	<th style="border:1px solid black; padding:5px">Admin?</th>
    <th style="border:1px solid black; padding:5px">Operation</th>
    <th style="border:1px solid black; padding:5px">Error thrown</th>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}/getOption</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Getting active option from a specific furniture by giving its id.
	{id_furniture} is the id of furniture.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}/getSumOfOptionDays</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Getting the sum of days of options that the logged user has already taken for the furniture given by idFurniture.
	{id_furniture} is the furniture id.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">furnitures/{id_furniture}/{id_user}/introduceOption</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Introduce an option related to a furniture and a user.
	{id_furniture} is the furniture id. {id_user} is the user id.
	</td>
    <td style="border:1px solid black; padding:5px">401 - if the amount of days are not between 1 and 5 ranges comprised, if the user has already used his 5 days of options on the furniture or if the amount of days + the already taken option'sdays are over 5</td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">options/{id_option}/cancelOption</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Cancel an option.
	{id_option} is the option id.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if the option's id is invalid,  the cancellation reason is null or empty</td>
</tr>

</table>

#### Operations associated with requests for visits

<table style="border:1px solid black; padding:5px border-collapse: collapse">

<tr>
    <th style="border:1px solid black; padding:5px">URI</th>
    <th style="border:1px solid black; padding:5px">Function</th>
    <th style="border:1px solid black; padding:5px">Auths?</th>
	<th style="border:1px solid black; padding:5px">Admin?</th>
    <th style="border:1px solid black; padding:5px">Operation</th>
    <th style="border:1px solid black; padding:5px">Error thrown</th>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">visits/notConfirmedVisits</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of requests for visits with the status "<i>en attente</i>".
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">visits/toBeProcessedVisits</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of requests for visits with the status "<i>accepté</i>" but with furnitures having the status "<i>proposé</i>".
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">visits/myVisits/{id_user}</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of requests for visits introduced by the client given by his id.
	{id_user} is the client id;
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">visits/{id_visit}/furnitures</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of furnitures for one request for visit.
	{id_visit} is the visit id.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">visits/{id_visit}</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a specific visit for logged users by giving its id.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">visits/{id_visit}/accept</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Accepts the request for visit given by its id. Change its state to "<i>acceptée</i>".
	{id_visit} the request for visit id.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if the given visit's id is invalid or the scheduled time is not given or its syntax is incorrect</td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">visits/{id_visit}/cancel</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">
	Cancel the request for visit given by its id. Change its state to "<i>annulée</i>".
	{id_visit} the request for visit id.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if the given visit's id is invalid or no explanation note is given (null or empty)</td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">visits/introduce</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Introduce a request for visit. It also adds the address into the database if this is different from the customer's address. This method adds each furniture and for each furniture, each photo.
	</td>
    <td style="border:1px solid black; padding:5px">412 - if the warehouse address is incomplete and has null/empty fields (except unit number)</td>
</tr>

</table>

### Operations associated with sales

<table style="border:1px solid black; padding:5px border-collapse: collapse">

<tr>
    <th style="border:1px solid black; padding:5px">URI</th>
    <th style="border:1px solid black; padding:5px">Function</th>
    <th style="border:1px solid black; padding:5px">Auths?</th>
	<th style="border:1px solid black; padding:5px">Admin?</th>
    <th style="border:1px solid black; padding:5px">Operation</th>
    <th style="border:1px solid black; padding:5px">Error thrown</th>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">sales</td>
	<td style="border:1px solid black; padding:5px">GET</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Returns a list of all the sales.
	</td>
    <td style="border:1px solid black; padding:5px"></td>
</tr>

<tr>
	<td style="border:1px solid black; padding:5px">sales</td>
	<td style="border:1px solid black; padding:5px">POST</td>
	<td style="border:1px solid black; padding:5px">Yes</td>
	<td style="border:1px solid black; padding:5px">No</td>
	<td style="border:1px solid black; padding:5px">
	Add a sale and change the state of the targeted furniture to "<i>vendu</i>".
	</td>
    <td style="border:1px solid black; padding:5px">412 - the sale is invalid or the furniture'is in the sale is invalid</td>
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

---
## Important Information

<p>Please note that UCC classes do not manage any transaction closure in case of error.<br>This particular control is done by the exceptionHandler: when an exception happens, the handler checks if a transaction is running and closes it if necessary.</p>
