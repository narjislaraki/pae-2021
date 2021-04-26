import callAPI from "../utils/api";
import {RedirectUrl} from "./Router";
import {getUserSessionData} from "../utils/session.js";
import PrintError from "./PrintError";
import Navbar from "./Navbar";

const API_BASE_URL = "/api/visits/";
let page = document.querySelector("#page");
let userData;
let listVisitsToBeProcessed;

let VisitsToBeProcessedPage = () => {
    userData = getUserSessionData();
    let menu = `
    <div class="menuAdmin">
        <button id="visits">Visites en attente</button>
        <button class="menuAdminOn" id="visitsToBeProcessed">Visites à traiter</button>
        <button id="advancedSearches">Recherche avancées</button>
        <button id="confirmRegister">Confirmation des inscriptions</button>
    </div>
    `;
    let visitToBeProcessedPage = `<div class="visits-title small-caps">
    <div class="all-furn-title small-caps">Visites à traiter</div>
    </div>`;
    page.innerHTML = menu + visitToBeProcessedPage;

    onVisitsToBeProcessed();

};

const onVisits = (e) => {
    e.preventDefault();
    console.log("to visits to be processed");
    RedirectUrl("/visits");
};

const onVisitsToBeProcessedPage = (e) => {
    e.preventDefault();
    console.log("to visits to be processed");
    RedirectUrl("/visitsToBeProcessed");
}

const onAdvancedSearches = (e) => {
    e.preventDefault();
    console.log("to advancedSearches")
    RedirectUrl("/advancedSearches");
};

const onConfirmRegister = (e) => {
    e.preventDefault();
    console.log("toConfirmRegistration");
    RedirectUrl("/confirmRegistration");
};


const onVisitsToBeProcessed = async() => {
    try {
        listVisitsToBeProcessed = await callAPI(
            API_BASE_URL + "toBeProcessedVisits",
            "GET",
            userData.token,
            undefined,
        );
        console.log(listVisitsToBeProcessed);

    } catch (err) {
        if (err == "Error: Admin only") {
            err.message = "Seuls les administrateurs peuvent accéder à cette page !";
        }
        console.error("VisitsPage::onVisitsToBeProcessed", err);
        PrintError(err);
    }
    let visitsToBeProcessed = `
    <div class="visitsWaiting">
        <table class="table table-light">
            <thead>
                <tr>
                    <th scope="col">Client</th>
                    <th scope="col">Nombres de meubles</th>
                    <th scope="col">Date</th>
                    <th scope="col">Adresse
                </tr>
            </thead>
        <tbody class="eachVisit">
    `;
    visitsToBeProcessed += listVisitsToBeProcessed
        .map((visit) =>
            `<tr>
                <td>${visit.client.firstName} ${visit.client.lastName}</td>
                <td>x</td>
                <td>${visit.scheduledDateTime}
                <td><p class="block-display">${visit.warehouseAddress.street} ${visit.warehouseAddress.buildingNumber} ${(visit.warehouseAddress.unitNumber == null ? "" : "/" + visit.warehouseAddress.unitNumber)}<br>
                    ${visit.warehouseAddress.postCode} - ${visit.warehouseAddress.city} <br>
                    ${visit.warehouseAddress.country}</p></td>
            </tr>

            `
        ).join("");
    
    page.innerHTML += visitsToBeProcessed;
    page.innerHTML += `</tbody></table></div>`;

    let visits = document.getElementById("visits");
    visits.addEventListener("click", onVisits);

    let visitsATraiter = document.getElementById("visitsToBeProcessed");
    visitsATraiter.addEventListener("click", onVisitsToBeProcessed);

    let advancedSearches = document.getElementById("advancedSearches");
    advancedSearches.addEventListener("click", onAdvancedSearches);
    
    let confirmRegister = document.getElementById("confirmRegister");
    confirmRegister.addEventListener("click", onConfirmRegister);
}

export default VisitsToBeProcessedPage;

