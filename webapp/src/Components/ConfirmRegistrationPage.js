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
        //ça foire ici
        const unregisteredUsers = await callAPI(
          API_BASE_URL + "unvalidatedList",
          "GET",
          undefined,
          undefined);
        onUnregisteredUsersList(unregisteredUsers);
    }catch(err){
        console.error("ConfirmRegistrationPage::onUnregisteredUsersList", err);
        PrintError(err);
    }
    let validateBtn = document.getElementsByClassName("btn-accept");
    let refuseBtn = document.getElementsByClassName("btn-refuse");
    Array.from(validateBtn).forEach((e) => {
      e.addEventListener("click", onAccept);
    });
    Array.from(refuseBtn).forEach((e) => {
      e.addEventListener("click", onRefuse);
    });
};

const onUnregisteredUsersList = (data) =>{
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
    onUnregisteredUsersListPage += data
        .map((user) => `<tr data-id="${user.id}">
                            <td>${user.username}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td><p class="block-display">${user.address}</p>
                            <button class="btn btn-dark condensed small-caps block-display" id="btn-map" data-id="${user.id}">Voir sur la carte</button></td>
                            <td><input type="radio" id="nephew" name="role${user.id}" data-id="${user.id}" value="nephew">
                            <label for="nephew${user.id}" data-id="${user.id}">Neveu</label><br>
                            <input type="radio" id="antique_dealer" data-id="${user.id}" name="role${user.id}" value="antique_dealer">
                            <label for="antique_dealer${user.id}">Antiquaire</label><br></td>
                            <td><button name="accept" class="btn btn-dark condensed small-caps block-display btn-accept" data-id="${user.id}" type="submit">Accepter</button><br>
                            <button name="refuse" class="btn btn-dark condensed small-caps block-display btn-refuse" data-id="${user.id}" type="submit">Refuser</button></td>
                            </tr>`)
        .join("");
    page.innerHTML += onUnregisteredUsersListPage;
    page.innerHTML += `</tr></tbody></table>` ;
    return page;
}

const onAccept = async (e) => {
  let id = e.srcElement.dataset.id;
  let user = {
    "id": id,
  };
  try{
    const userRegistered = await callAPI(
      API_BASE_URL + "accept",
      "PATCH",
      undefined,
      user
    );
  }catch(err){
    console.error("ConfirmRegistrationPage::onAccept", err);
    PrintError(err);
  }
  ConfirmRegistrationPage();
}; 

const onRefuse = async (e) => {
  let id = e.srcElement.dataset.id;
  let user = {
    "id": id,
  };
  try{
    const userRegistered = await callAPI(
      API_BASE_URL + "refuse/" + user.id,
      "DELETE",
      undefined,
      undefined,
    );
  }catch(err){
    console.error("ConfirmRegistrationPage::onRefuse", err);
    PrintError(err);
  }
  ConfirmRegistrationPage();
}; 


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
