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
    let onUnregisteredUsersListPage = `<ul class="list-group list-group-horizontal-lg">`;
    let list = document.querySelector("ul");
    console.log(data);
    onUnregisteredUsersListPage += data
        .map((user) => `<form class="form-register register-grid">
                            <p>${user.username}</p>
                            <p>${user.firstName}</p>
                            <p>${user.lastName}</p>
                            <p>${user.address}</p>
                            <button class="btn btn-dark condensed small-caps" id="btn-map" type="submit">Voir sur la carte</button>
                            <button class="btn btn-dark condensed small-caps" id="btn-accept" type="submit">Accepter</button>
                            <button class="btn btn-dark condensed small-caps" id="btn-refuse" type="submit">Refuser</button>
                            <input type="radio" id="nephew" value="nephew">
                            <label for="nephew">Neveu</label><br>
                            <input type="radio" id="antique_dealer" value="antique_dealer">
                            <label for="antique_dealer">Antiquaire</label><br>
                        </form>`)
        .join("");
    onUnregisteredUsersListPage += "</ul>";
    return (page.innerHTML += onUnregisteredUsersListPage);
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
