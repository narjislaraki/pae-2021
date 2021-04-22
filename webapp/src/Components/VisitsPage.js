import callAPI from "../utils/api";
import { RedirectUrl } from "./Router";
import { getUserSessionData } from "../utils/session.js";
import PrintError from "./PrintError";

const API_BASE_URL = "/api/visits/";
let page = document.querySelector("#page");
let userData = getUserSessionData();
let VisitsPage = () => {
    let menu = `
    <div class="menuAdmin">
        <div id="visits" class="condensed small-caps menuAdminOn">Visites</div>
        <div id="advancedSearches" class="condensed small-caps">Recherche avancées</div>
        <div id="confirmRegister" class="condensed small-caps">Confirmation des inscriptions</div>
    </div>
    `;
    let visitPage = `
    <div class="visits-title small-caps">
        <button class="buttonsVisits" id="btnWaiting">En attente</button>
        <button class="buttonsVisits" id="btnToTreat">À traiter</button>
    </div>
    `;
    page.innerHTML = menu + visitPage;
    let visits = document.getElementById("visits");
    visits.addEventListener("click", onVisits);

    let advancedSearches = document.getElementById("advancedSearches");
    advancedSearches.addEventListener("click", onAdvancedSearches);

    let confirmRegister = document.getElementById("confirmRegister");
    confirmRegister.addEventListener("click", onConfirmRegister);

    let btnWaiting = document.getElementById("btnWaiting");
    btnWaiting.addEventListener("click", onVisitsWainting);

    let btnToTreat = document.getElementById("btnToTreat");
    btnToTreat.addEventListener("click", onVisitsToTreat);
    onVisitsWainting();

};

const onVisits = (e) => {
    e.preventDefault();
    console.log("to visits");
    RedirectUrl("/visits");
};

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

const onVisitsWainting = async () => {
    //todo
    //btnToTreat.disabled = true;
    //btnWaiting.disabled = false;
    let listVisitsWaiting;
    try{
        listVisitsWaiting = await callAPI(
            API_BASE_URL + "notConfirmedVisits",
            "GET",
            userData.token,
            undefined,
        );
        
    } catch(err){
        if (err == "Error: Admin only") {
            err.message = "Seuls les administrateurs peuvent accéder à cette page !";
          }
          console.error("VisitsPage::onVisitsWainting", err);
          PrintError(err);
    }
    let visitsWaiting =  `
    <div class="visitsWaiting">
        <table class="table table-light">
            <thead>
                <tr>
                    <th scope="col">Client</th>
                    <th scope="col">Nombres de meubles</th>
                    <th scope="col">Adresse</th>
                    <th scope="col"></th>
                </tr>
            </thead>
        <tbody class="eachVisit">
    </div
    `;
    visitsWaiting += listVisitsWaiting
        .map((visit) =>
            `<tr>
                <td>${visit.client.firstName} ${visit.client.lastName}</td>
                <td>x</td>
                <td><p class="block-display">${visit.warehouseAddress.street} ${visit.warehouseAddress.buildingNumber} ${(visit.warehouseAddress.unitNumber == null ? "" : "/" + user.address.unitNumber)}<br>
                    ${visit.warehouseAddress.postCode} - ${visit.warehouseAddress.city} <br>
                    ${visit.warehouseAddress.country}</p></td>
                <td><button name="onVisit" class="btn btn-dark condensed small-caps block-display" data-id="${visit.idRequest}" type="submit">Consulter la demande de visite</button></td>
            </tr>`
        ).join("");
    page.innerHTML += visitsWaiting;
    page.innerHTML += `</tbody></table>`;
    let listVisit = document.getElementsByName("onVisit");
    Array.from(listVisit).forEach((e) => {
        e.addEventListener("click", onClickVisit);
    });
}

async function onClickVisit (e)  {
    let idVisit = e.srcElement.dataset.id;
    let visit;
    
    try {
        visit = await callAPI(
            API_BASE_URL + idVisit,
            "GET",
            userData.token,
            undefined);
    } catch (err) {
        console.error("VisitsPage::onClickVisit", err);
        PrintError(err);
    }console.log(visit);
    console.log(visit.warehouseAddress);
    console.log(visit.timeSlot);
    console.log(visit.warehouseAddress.street);
    page.innerHTML += `
            <div class="popupVisit">
                <h2>Confirmer la visite ?</h2>
                <div>
                    <h4>Plage horaire : </h4> ${visit.timeSlot}<br>
                    <h4>Adresse : </h4> <div>${visit.warehouseAddress.street} ${visit.warehouseAddress.buildingNumber} ${(visit.warehouseAddress.unitNumber == null ? "" : "/" + user.address.unitNumber)}<br>
                    ${visit.warehouseAddress.postCode} - ${visit.warehouseAddress.city} <br>
                    ${visit.warehouseAddress.country} </div><br>
                <h4>Meuble(s) : </h4><br>
                <h4>Date de la visite</h4><input type="datetime-local" id="scheduledDateTime" min="${Date.now}" max="9999-31-12T23:59" pattern="[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}">
                <h4>Motif du refus : </h4><textarea id="explanatoryNote"></textarea><br>
                <button id="confirmVisitBtn" name="confirmBtn" data-id="${visit.idRequest}" type="submit">Confirmer</button>
                <button id="cancelVisitBtn" name="cancelBtn" data-id="${visit.idRequest}" type="submit">Refuser</button></div>
                <span class="btnClose"></span>
            </div>
    `;
    let popupVisit = document.getElementsByClassName("popupVisit");
    let btnConfirm = document.getElementById("confirmVisitBtn");
    btnConfirm.addEventListener("click", onConfirm);
    let btnCancel = document.getElementById("cancelVisitBtn");
    btnCancel.addEventListener("click", onCancel);
}

const onVisitsToTreat = (e) => {

}


const onConfirm = async (e) => {
    let id = e.srcElement.dataset.id;
    let scheduledDateTime = document.getElementById('scheduledDateTime').value;
    if (scheduledDateTime == ""){
        let error = {
            message: "Veuillez d'abord entrer une date et heure de visite",
        }
        PrintError(error);
        return;
    }
    try{
        await callAPI(
            API_BASE_URL + id + "/accept",
            "POST",
            userData.token,
            {
                scheduledDateTime: scheduledDateTime,
            },
        );
    }catch(err){
        console.log("VisitsPage::onConfirm", err);
        PrintError(err);
    }
    VisitsPage();
}

const onCancel = async (e) => {
    let id = e.srcElement.dataset.id;
    let explanatoryNote = document.getElementById("explanatoryNote").value;
    if (explanatoryNote == ""){
        let error = {
            message: "Veuillez d'abord entrer un motif expliquant la raison du refus",
        }
        PrintError(error);
        return;
    }
    try{
        await callAPI(
            API_BASE_URL + id + "/cancel",
            "POST",
            userData.token,
            {
                explanatoryNote : explanatoryNote,
            },
        );
    }catch (err){
        console.log("VisitsPage::onCancel", err);
        PrintError(err);
    }
    VisitsPage();
}
export default VisitsPage;