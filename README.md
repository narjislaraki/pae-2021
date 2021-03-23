# PAE Project 2021 - Group 03

## RESTful API : available operations

### Operations associated with authentication

<br>
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
    those information need to be unique in the DB: email, username.
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



---
## Properties
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