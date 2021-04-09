import callAPI from "../utils/api";
import PrintError from "./PrintError.js";
import { getUserSessionData, currentUser } from "../utils/session.js";
import { RedirectUrl } from "./Router";
let userData;
let adresse = ``;
const API_BASE_URL = "/api/users/";
let confirmRegistrationPage = `<div class="all-furn-title small-caps">Confirmer l'inscription</div>`;

const ConfirmRegistrationPage = async () => {
  if (currentUser == null || currentUser.role != "ADMIN") {
    RedirectUrl("/");
  }
  userData = getUserSessionData();
  let page = document.querySelector("#page");
  page.innerHTML = confirmRegistrationPage;
  try {
    const unregisteredUsers = await callAPI(
      API_BASE_URL + "unvalidatedList",
      "GET",
      userData.token,
      undefined);
    onUnregisteredUsersList(unregisteredUsers);
  } catch (err) {
    if (err == "Error: Admin only") {
      err.message = "Seuls les administrateurs peuvent accéder à cette page !";
    }
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

const onUnregisteredUsersList = (data) => {
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

  //{"id":6,"unitNumber":0,"street":"La rue","buildingNumber":"42","city":"bac","postCode":"4000","country":"street"}
  onUnregisteredUsersListPage += data
    .map((user) =>
      `<tr data-id="${user.id}">
                            <td>${user.username}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td><p class="block-display">${user.address.street}, ${user.address.buildingNumber}${(user.address.unitNumber == null ? "" : "/" + user.address.unitNumber)}<br>
                            ${user.address.postCode} - ${user.address.city} <br>
                            ${user.address.country}</p>
                            <button class="btn btn-dark condensed small-caps block-display" id="btn-map" data-id="${user.id}">Voir sur la carte</button></td>
                            <td><input type="radio" id="nephew${user.id}" name="role${user.id}" data-id="${user.id}" value="nephew">
                            <label for="nephew${user.id}" data-id="${user.id}">Neveu</label><br>
                            <input type="radio" id="antique_dealer${user.id}" data-id="${user.id}" name="role${user.id}" value="antique_dealer">
                            <label for="antique_dealer${user.id}">Antiquaire</label><br>
                            <input type="radio" id="client${user.id}" name="role${user.id}" data-id="${user.id}" value="client" checked="checked">
                            <label for="client${user.id}" data-id="${user.id}">Client</label><br></td>
                            <td><button name="accept" class="btn btn-dark condensed small-caps block-display btn-accept" data-id="${user.id}" type="submit">Accepter</button><br>
                            <button name="refuse" class="btn btn-dark condensed small-caps block-display btn-refuse" data-id="${user.id}" type="submit">Refuser</button></td>
                            </tr>
                            `)
    .join("");
  page.innerHTML += onUnregisteredUsersListPage;
  page.innerHTML += `</tbody></table>`;
  //<div class="white-space"></div>

  return page;
}

const onAccept = async (e) => {
  let id = e.srcElement.dataset.id;
  let userRole = "client";

  if (document.getElementById('nephew' + id).checked) {
    userRole = "admin";
  }
  if (document.getElementById('antique_dealer' + id).checked) {
    userRole = "antiquaire";
  }

  try {
    await callAPI(
      API_BASE_URL + id + "/accept/",
      "POST",
      userData.token,
      {
        role: userRole,
      },
    );
  } catch (err) {
    console.error("ConfirmRegistrationPage::onAccept", err);
    PrintError(err);
  }
  ConfirmRegistrationPage();
};

const onRefuse = async (e) => {
  let id = e.srcElement.dataset.id;
  try {
    await callAPI(
      API_BASE_URL + id,
      "DELETE",
      userData.token,
      undefined,
    );
  } catch (err) {
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
