import callAPI from "../utils/api";
import PrintError from "./PrintError.js";
import {getUserSessionData, removeSessionData} from "../utils/session.js";
let userData = getUserSessionData();
let adresse = ``; 
const API_BASE_URL = "api/admin/";
let confirmRegistrationPage = `<h4 id="pageTitle">Confirmer l'inscription</h4>`;

const ConfirmRegistrationPage = async () => {
    let page = document.querySelector("#page");
    page.innerHTML = confirmRegistrationPage;
    try{
        console.log("onUnregisteredUsersList b");
        //ça foire ici
        const unregisteredUsers = await callAPI(
          API_BASE_URL + "unvalidatedList",
          "GET",
          undefined,
          undefined);
        console.log(unregisteredUsers + "zkzk");
        onUnregisteredUsersList(unregisteredUsers);
        console.log("onUnregisteredUsersList d");
    }catch(err){
        console.error("ConfirmRegistrationPage::onUnregisteredUsersList", err);
        PrintError(err);
    }
};

const onUnregisteredUsersList = (data) =>{
    console.log("onUnregisteredUsersList a");
    let onUnregisteredUsersListPage = 
    `<table class="table table-light">
      <thead>
        <tr>
          <th scope="col">Pseudo</th>
          <th scope="col">Prénom</th>
          <th scope="col">Nom</th>
          <th scope="col">Adresse</th>
          <th scope="col"></th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
       
        
    `;
    console.log(data);
    onUnregisteredUsersListPage += data
        .map((user) => `<tr>
                            <td>${user.username}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td><p class="block-display">${user.address}</p>
                            <button class="btn btn-dark condensed small-caps block-display" id="btn-map${user.id}">Voir sur la carte</button></td>
                            <td><input type="radio" id="nephew${user.id}" name="role${user.id}" value="nephew">
                            <label for="nephew${user.id}">Neveu</label><br>
                            <input type="radio" id="antique_dealer${user.id}" name="role${user.id}" value="antique_dealer">
                            <label for="antique_dealer${user.id}">Antiquaire</label><br></td>
                            <td><button class="btn btn-dark condensed small-caps block-display" id="btn-accept${user.id}" type="submit">Accepter</button><br>
                            <button class="btn btn-dark condensed small-caps block-display" id="btn-refuse${user.id}" type="submit">Refuser</button></td>
                            
                            </tr>`)
        .join("");
    page.innerHTML += onUnregisteredUsersListPage;
    page.innerHTML += `</tr></tbody></table>` ;
    return page;
}

const onError = (err) => {
    console.error("ConfirmRegistrationPage::onError:", err);
    let errorMessage;
    if (err.message) {
      errorMessage = err.message;
    } else errorMessage = err;
    if (errorMessage.includes("jwt expired"))
      errorMessage += "<br> Please logout first, then login.";
    RedirectUrl("/error", errorMessage);
    //TODO
  };

export default ConfirmRegistrationPage;
