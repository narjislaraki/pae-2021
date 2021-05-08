import callAPI from "../utils/api";
import {RedirectUrl} from "./Router.js";
import {getUserSessionData} from "../utils/session.js";
import PrintError from "../utils/PrintError.js";
import PrintMessage from "../utils/PrintMessage.js";
import WaitingSpinner from "../utils/WaitingSpinner.js";
import {convertDateTimeToStringDate} from "../utils/tools.js";

const API_BASE_URL = "/api/visits/";
let page = document.querySelector("#page");
let userData;
let listVisitsToBeProcessed;
let listFurnituresForOnVisit;

let VisitsToBeProcessedPage = () => {
    userData = getUserSessionData();
    let menu = `
    <div class="menuAdmin">
        <div class="condensed small-caps" id="visits">Visites en attente</div>
        <div class="condensed small-caps menuAdminOn" id="visitsToBeProcessed">Visites à traiter</div>
        <div class="condensed small-caps" id="advancedSearches">Recherches avancées</div>
        <div class="condensed small-caps" id="confirmRegister">Confirmation des inscriptions</div>
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
    RedirectUrl("/visits");
};

const onVisitsToBeProcessedPage = (e) => {
    e.preventDefault();
    RedirectUrl("/visitsToBeProcessed");
}

const onAdvancedSearches = (e) => {
    e.preventDefault();
    RedirectUrl("/advancedSearches");
};

const onConfirmRegister = (e) => {
    e.preventDefault();
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
    } catch (err) {
        if (err == "Error: Admin only") {
            err.message = "Seuls les administrateurs peuvent accéder à cette page !";
        }
        console.error("VisitsPage::onVisitsToBeProcessed", err);
        PrintError(err);
    }
    let visitsToBeProcessed = `
    <div class="visitsToBeProcessed">
        <table class="table table-light">
            <thead>
                <tr>
                    <th scope="col">Date de la demande</th>
                    <th scope="col">Client</th>
                    <th scope="col">Nombres de meubles</th>
                    <th scope="col">Date</th>
                    <th scope="col">Adresse</th>
                    <th scope="col"></th>
                </tr>
            </thead>
        <tbody class="eachVisit" >
    `;
    visitsToBeProcessed += listVisitsToBeProcessed
        .map((visit) =>
            `
            <div data-id="${visit.idRequest}">
            <tr>
                <td>${convertDateTimeToStringDate(visit.requestDateTime)}</td>
                <td>${visit.client.firstName} ${visit.client.lastName}</td>
                <td>${visit.amountOfFurnitures}</td>
                <td>${convertDateTimeToStringDate(visit.scheduledDateTime)}
                <td><p class="block-display">${visit.warehouseAddress.street} ${visit.warehouseAddress.buildingNumber} ${(visit.warehouseAddress.unitNumber == null ? "" : "/" + visit.warehouseAddress.unitNumber)}<br>
                    ${visit.warehouseAddress.postCode} - ${visit.warehouseAddress.city} <br>
                    ${visit.warehouseAddress.country}</p></td>
                <td><button name="trigger_popup_fricc" class="btn btn-dark condensed small-caps block-display" data-id="${visit.idRequest}" type="submit">Traiter la demande de visite</button></td>
            </tr></div>    
            `
        ).join("");
    
    

    page.innerHTML += visitsToBeProcessed;
    page.innerHTML += `</tbody></table></div>`;
    let listVisit = document.getElementsByName("trigger_popup_fricc");
    Array.from(listVisit).forEach((e) => {
        e.addEventListener("click", onClickVisit);
    });

    let visits = document.getElementById("visits");
    visits.addEventListener("click", onVisits);

    let visitsATraiter = document.getElementById("visitsToBeProcessed");
    visitsATraiter.addEventListener("click", onVisitsToBeProcessed);

    let advancedSearches = document.getElementById("advancedSearches");
    advancedSearches.addEventListener("click", onAdvancedSearches);
    
    let confirmRegister = document.getElementById("confirmRegister");
    confirmRegister.addEventListener("click", onConfirmRegister);
}

const onClose = (e) => {
    e.preventDefault();
    let idVisit = e.srcElement.dataset.id;

    Array.from(document.getElementsByClassName("hover_bkgr_fricc")).forEach((element) => {
        if (element.dataset.id == idVisit) {
            element.style.display = "none";
            return;
        }
    });
}

async function onClickVisit(e){
    let idVisit = e.srcElement.dataset.id;
    let visit = listVisitsToBeProcessed.filter(e => e.idRequest == idVisit)[0];
    let popupVisit = `
    <div class="hover_bkgr_fricc" data-id="${visit.idRequest}">
    <span class="helper"></span>
    <div>
        <div class="popupCloseButton" data-id="${visit.idRequest}">
            &times;
        </div>
        <h2>Veuillez confirmer ou refuser les propositions de meubles : </h2>
        <form id="formFurnituresToBeProcessed">
        <div>
                <span class="titre">Client : </span> ${visit.client.lastName} ${visit.client.firstName}<br>
                <h4>Adresse : </h4>
                <div>${visit.warehouseAddress.street} ${visit.warehouseAddress.buildingNumber} ${(visit.warehouseAddress.unitNumber == null ? "" : "/" + visit.warehouseAddress.unitNumber)}<br>
                    ${visit.warehouseAddress.postCode} - ${visit.warehouseAddress.city} <br>
                    ${visit.warehouseAddress.country} 
                </div>
                <span class="titre">Date : </span>${visit.scheduledDateTime}<br>
                <h4>Meuble(s) : </h4><br>
                
                <div id="allFurnitures"></div>
                <div class="center">
                    <button class="btn btn-outline-success col-6 confirmVisitBtn" name="confirmBtn" data-id="${visit.idRequest}" type="submit">Confirmer</button>
                </div>
        </div>
        </form>
    </div>
    
    
    `;

    document.getElementById("popups").innerHTML = popupVisit;
    WaitingSpinner(document.getElementById("allFurnitures"));

    Array.from(document.getElementsByClassName("hover_bkgr_fricc")).forEach((element) => {
        if (element.dataset.id == idVisit) {
            element.style.display = "block";
        }
    });

    Array.from(document.getElementsByClassName("popupCloseButton")).forEach((e) => {
        e.addEventListener("click", onClose);
    });

    
    Array.from(document.getElementsByClassName("confirmVisitBtn")).forEach((e) => {
        e.addEventListener("click", onConfirm);
    });

    let allFurnitures = document.getElementById("allFurnitures");
    let toAdd = `
        <div class="allFurnituresForOnVisit">
            <ol>
    `;

    
    try{
        listFurnituresForOnVisit = await callAPI(
            API_BASE_URL + idVisit + "/furnitures",
            "GET",
            userData.token,
            undefined,
        )
    }catch (err) {
        if (err == "Error: Admin only") {
            err.message = "Seuls les administrateurs peuvent accéder à cette page !";
        }
        console.error("VisitsToBeProcessed::onClickVisit", err);
        PrintError(err);
    }

    listFurnituresForOnVisit
        .map((furniture)=>{
            let idFurniture = 0
            toAdd += `
                <li>
                    <div class="furniture" id="${furniture.id}" data-id="${furniture.id}">
                        ${furniture.description}
                        <div class="photoDiv">`;
                        furniture.listPhotos.map(e => {
                            toAdd += `<img class="imageVisits" src="${e.photo}">`
                        });
                        toAdd += `</div>   <label class="switch">
                                <input class="switch-input" type="checkbox" id="check${furniture.id}"/>
                                <span class="switch-label" data-on="Confirmer" data-off="Refuser"></span> 
                                <span class="switch-handle"></span> 
                            </label>
                            <h4>Prix d'achat : <input type="number" class="form-control input-card" id="purchaseprice${furniture.id}" name="purchaseprice${furniture.id}">€</h4>
                            <h4>Date d'emport' : </h4><input type="datetime-local" class="pickUpDate" name="pickupdate${furniture.id}" id="pickupdate${furniture.id}" min="${Date.now}" max="9999-31-12T23:59">
                        `;
                        toAdd += "</div></li>";
                        idFurniture++;
        });
        toAdd += `
            </ol></div>
        `;
        allFurnitures.innerHTML = toAdd;
}

const onConfirm = async (e) => {
    e.preventDefault();
    let idVisit = e.srcElement.dataset.id;
    let visit = {
        idRequest : idVisit,
        furnitureList:[],
    }
    for (let i = 0; i < listFurnituresForOnVisit.length; i++){
        let idA = document.getElementsByClassName("furniture")[i].dataset.id;
        let furniture = {
          id: idA,
          purchasePrice: document.getElementById("purchaseprice" + idA).value == "" ? null : document.getElementById("purchaseprice" + idA).value,
          pickUpDate: document.getElementById("pickupdate" + idA).value == "" ? null : document.getElementById("pickupdate" + idA).value,
          condition: document.getElementById("check"+idA).checked ? "acheté" : "refusé"
        }
        visit.furnitureList[i] = furniture;
    }

    try{
        const sendList = await callAPI(
            "/api/furnitures/furnituresListToBeProcessed", 
            "POST",
            userData.token,
            visit,
        );
        if (sendList){
            if (window.location.pathname == "/visitsToBeProcessed"){
                VisitsToBeProcessedPage();
            }
            PrintMessage("La demande de visite a bien été traitée.");
        }
    }
    catch (err) {
        if (err == "Error: Missing fields or uncorrect fields") {
          err.message = "Veuillez entrer un prix d'achat (positif) et une date d'emport si vous décidez d'acheter le meuble !";
        }
        console.error("VisitToBeProcessedPage::onConfirm", err);
        PrintError(err);
      }
    onClose(e);

}

export default VisitsToBeProcessedPage;

