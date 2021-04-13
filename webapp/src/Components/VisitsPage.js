import callAPI from "../utils/api";
import {RedirectUrl} from "./Router";
import {getUserSessionData} from "../utils/session.js";
import PrintError from "./PrintError";

const API_BASE_URL = "/api/visits/";
let page = document.querySelector("#page");
let userData = getUserSessionData();
let VisitsPage = () => {
    let menu = `
    <div class="menuAdmin">
        <button class="menuAdminOn" id="visits">Visites</button>
        <button id="advancedSearches">Recherche avancées</button>
        <button id="confirmRegister">Confirmation des inscriptions</button>
    </div>
    `;
    let visitPage = `<div class="visits-title small-caps">
    <div class="all-furn-title small-caps">Visites en attente</div>
    </div>`;
    page.innerHTML = menu + visitPage;

    onVisitsWaiting();

};

const onVisits = (e) => {
    e.preventDefault();
    console.log("to visits");
    RedirectUrl("/visits");
};

const onAdanvancedSearches = (e) => {
    e.preventDefault();
    console.log("to advancedSearches")
    RedirectUrl("/advancedSearches");
};

const onConfirmRegister = (e) => {
    e.preventDefault();
    console.log("toConfirmRegistration");
    RedirectUrl("/confirmRegistration");
};

const onVisitsWaiting = async () => {
    //todo
    //btnToTreat.disabled = true;
    //btnWaiting.disabled = false;

    let listVisitsWaiting;
    try {
        listVisitsWaiting = await callAPI(
            API_BASE_URL + "notConfirmedVisits",
            "GET",
            userData.token,
            undefined,
        );

    } catch (err) {
        if (err == "Error: Admin only") {
            err.message = "Seuls les administrateurs peuvent accéder à cette page !";
        }
        console.error("VisitsPage::onVisitsWaiting", err);
        PrintError(err);
    }
    let visitsWaiting = `
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
    `;
    visitsWaiting += listVisitsWaiting
        .map((visit) =>
            `<tr>
                <td>${visit.client.firstName} ${visit.client.lastName}</td>
                <td>x</td>
                <td><p class="block-display">${visit.warehouseAddress.street} ${visit.warehouseAddress.buildingNumber} ${(visit.warehouseAddress.unitNumber == null ? "" : "/" + user.address.unitNumber)}<br>
                    ${visit.warehouseAddress.postCode} - ${visit.warehouseAddress.city} <br>
                    ${visit.warehouseAddress.country}</p></td>
                <td><button name="trigger_popup_fricc" class="btn btn-dark condensed small-caps block-display" data-id="${visit.idRequest}" type="submit">Consulter la demande de visite</button></td>
            </tr>

    <div class="hover_bkgr_fricc" data-id="${visit.idRequest}">
        <span class="helper"></span>
        <div>
            <div class="popupCloseButton" data-id="${visit.idRequest}">
                &times;
            </div>
            <h2>Confirmer la visite ?</h2>
            <div>
                <span class="titre">Plage horaire : </span> ${visit.timeSlot}<br>
                <h4>Adresse : </h4>
                <div>${visit.warehouseAddress.street} ${visit.warehouseAddress.buildingNumber} ${(visit.warehouseAddress.unitNumber == null ? "" : "/" + user.address.unitNumber)}<br>
                    ${visit.warehouseAddress.postCode} - ${visit.warehouseAddress.city} <br>
                    ${visit.warehouseAddress.country} 
                </div><br>
                <h4>Meuble(s) : </h4><br>
                <h4>Date de la visite</h4><input type="datetime-local" class="scheduledDateTime" id="scheduledDateTime${visit.idRequest}" min="${Date.now}" max="9999-31-12T23:59">
                <h4>Motif du refus : </h4><textarea class="explanatoryNote" id="explanatoryNote${visit.idRequest}"></textarea><br>
                <div class="container">
                    <div class="row">
                        <button class="btn btn-outline-success col-6 confirmVisitBtn" name="confirmBtn" data-id="${visit.idRequest}" type="submit">Confirmer</button>
                        <button class="btn btn-outline-danger col-6 cancelVisitBtn" name="cancelBtn" data-id="${visit.idRequest}" type="submit">Refuser</button>
                    </div>
                </div>
            </div>
        </div>
    </div>`
        ).join("");
    page.innerHTML += visitsWaiting;
    page.innerHTML += `</tbody></table></div>`;
    let listVisit = document.getElementsByName("trigger_popup_fricc");
    Array.from(listVisit).forEach((e) => {
        e.addEventListener("click", onClickVisit);
    });
    Array.from(document.getElementsByClassName("popupCloseButton")).forEach((e) => {
        e.addEventListener("click", onClose);
    })

    Array.from(document.getElementsByClassName("confirmVisitBtn")).forEach((e) => {
        e.addEventListener("click", onConfirm);
    })
    Array.from(document.getElementsByClassName("cancelVisitBtn")).forEach((e) => {
        e.addEventListener("click", onCancel);
    })

    let visits = document.getElementById("visits");
    visits.addEventListener("click", onVisits);

    let advancedSearches = document.getElementById("advancedSearches");
    advancedSearches.addEventListener("click", onAdanvancedSearches);
    
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

function onClickVisit(e) {
    let idVisit = e.srcElement.dataset.id;

    Array.from(document.getElementsByClassName("hover_bkgr_fricc")).forEach((element) => {
        if (element.dataset.id == idVisit) {
            element.style.display = "block";
            return;
        }
    });
}


const onConfirm = async (e) => {
    let id = e.srcElement.dataset.id;
    let scheduledDateTime = document.getElementById('scheduledDateTime'+id).value;
    if (scheduledDateTime == "") {
        let error = {
            message: "Veuillez d'abord entrer une date et heure de visite",
        }
        PrintError(error);
        return;
    }
    try {
        await callAPI(
            API_BASE_URL + id + "/accept",
            "POST",
            userData.token,
            {
                scheduledDateTime: scheduledDateTime,
            },
        );
    } catch (err) {
        console.log("VisitsPage::onConfirm", err);
        PrintError(err);
    }
    VisitsPage();
}

const onCancel = async (e) => {
    let id = e.srcElement.dataset.id;
    let explanatoryNote = document.getElementById("explanatoryNote"+id).value;
    if (explanatoryNote == "") {
        let error = {
            message: "Veuillez d'abord entrer un motif expliquant la raison du refus",
        }
        PrintError(error);
        return;
    }
    try {
        await callAPI(
            API_BASE_URL + id + "/cancel",
            "POST",
            userData.token,
            {
                explanatoryNote: explanatoryNote,
            },
        );
    } catch (err) {
        console.log("VisitsPage::onCancel", err);
        PrintError(err);
    }
    VisitsPage();
}
export default VisitsPage;