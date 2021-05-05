import callAPI from "../utils/api";
import {RedirectUrl} from "./Router";
import {currentUser, getUserSessionData} from "../utils/session.js";
import PrintError from "./PrintError";
import Navbar from "./Navbar";
import WaitingSpinner from "./WaitingSpinner.js"
import {convertDateTimeToStringDate} from "../utils/tools.js";

const API_BASE_URL = "/api/visits/";
let page = document.querySelector("#page");
let userData;
let listVisitsOfAClient;

const EN_ATTENTE = `<h4><span class="badge bg-warning small-caps">en attente</span></h4></div></div>`;
const ACHETE = `<h4><span class="badge bg-success small-caps">acheté</span></h4></div></div>`;
const REFUSE = `<h4><span class="badge bg-danger small-caps">refusé</span></h4></div></div>`;
const EN_RESTAURATION = `<h4><span class="badge bg-warning small-caps">en restauration</span></h4></div></div>`;
const DEPOSE_EN_MAGASIN = `<h4><span class="badge bg-success small-caps">déposé en magasin</span></h4></div></div>`;
const EN_VENTE = `<h4><span class="badge bg-success small-caps">en vente</span></h4></div></div>`;
const SOUS_OPTION = `<h4><span class="badge bg-succes small-caps">sous option</span></h4></div></div>`;
const VENDU = `<h4><span class="badge bg-success small-caps">vendu</span></h4></div></div>`;
const RETIRE = `<h4><span class="badge bg-danger small-caps">retiré de la vente</span></h4></div></div>`;

const VISIT_EN_ATTENTE =  `<h4><span class="badge bg-warning small-caps">en attente</span></h4></div></div>`;
const ACCEPTEE = `<h4><span class="badge bg-success small-caps">acceptée</span></h4></div></div>`;
const ANNULEE =  `<h4><span class="badge bg-danger small-caps">annulée</span></h4></div></div>`;


let VisitsForClientPage = () => {
    userData = getUserSessionData();
    let menu = `
    <div class="menuAdmin">
        <div class="condensed small-caps" id="myTransactions">Mes transactions</div>
        <div class="condensed small-caps menuAdminOn" id="myVisits">Mes visites</div>
    </div>
    `;

    let visitClientPage = `<div class="visits-title small-caps">
        <div class="all-furn-title small-caps">Mes visites</div>
    </div>`;
    page.innerHTML = menu + visitClientPage;

    let myTransactions = document.getElementById("myTransactions");
    myTransactions.addEventListener("click", onMyTransactions);

    let myVisits = document.getElementById("myVisits");
    myVisits.addEventListener("click", onMyVisits);

    onVisitsForClient();
}


const onMyTransactions = (e) => {
    e.preventDefault();
    console.log("to my transactions");
    RedirectUrl("/transactions");
}

const onMyVisits = (e) => {
    e.preventDefault();
    console.log("to my visits");
    RedirectUrl("/visitsForClient");
}

const onVisitsForClient = async () => {
    try{
        listVisitsOfAClient = await callAPI(
            API_BASE_URL + "/" + currentUser.id +"/myVisits",
            "GET",
            userData.token,
            undefined,
        );
    }catch (err) {
        console.error("VisitsPage::onVisitsWaiting", err);
        PrintError(err);
    }
    let visits = `
    <div class="visitsWaiting">
        <table class="table table-light">
            <thead>
                <tr>
                    <th scope="col">Date de la demande</th>
                    <th scope="col">Nombres de meubles</th>
                    <th scope="col">Adresse</th>
                    <th scope="col">Etat de la visite</th>
                    <th scope="col">Date et heure prévue de la visite</th>
                    <th scope="col">Raison de l'annulation</th>
                    <th scope="col"></th>
                </tr>
            </thead>
        <tbody class="eachVisit">
    `;
    visits += listVisitsOfAClient
        .map((visit) =>
            `<tr>
                <td>${convertDateTimeToStringDate(visit.requestDateTime)}</td>
                <td>${visit.amountOfFurnitures}</td>
                <td><p class="block-display">${visit.warehouseAddress.street} ${visit.warehouseAddress.buildingNumber} ${(visit.warehouseAddress.unitNumber == null ? "" : "/" + visit.warehouseAddress.unitNumber)}<br>
                    ${visit.warehouseAddress.postCode} - ${visit.warehouseAddress.city} <br>
                    ${visit.warehouseAddress.country}</p></td>
                <td><div id="condition${visit.idRequest}" value="${visit.visitCondition}"></div></td>
                <td>${(visit.scheduledDateTime == null ? "/" : convertDateTimeToStringDate(visit.scheduledDateTime))}</td>
                <td>${(visit.explanatoryNote == null ? "/" : visit.explanatoryNote)}</td>
                <td><button name="trigger_popup_fricc" class="btn btn-dark condensed small-caps block-display" data-id="${visit.idRequest}" type="submit">Consulter les meubles</button></td>
            </tr>

            `
        ).join("");
    
        page.innerHTML += visits;
        page.innerHTML += `</tbody></table></div>`;
        for (let i = 0; i < listVisitsOfAClient.length; i++){
            let div = document.getElementById("condition"+listVisitsOfAClient[i].idRequest);
            console.log(div);
            let condition = "";
            switch(div.getAttribute("value")){
                case "EN_ATTENTE":
                    condition = VISIT_EN_ATTENTE;
                    break;
                case "ACCEPTEE":
                    condition = ACCEPTEE;
                    break;
                case "ANNULEE":
                    condition = ANNULEE;
                    break;
            }
            div.innerHTML = condition;
        }
        let listVisit = document.getElementsByName("trigger_popup_fricc");
        Array.from(listVisit).forEach((e) => {
            e.addEventListener("click", onClickVisit);
        });
}

async function onClickVisit(e) {
    let idVisit = e.srcElement.dataset.id;
    let visit = listVisitsOfAClient.filter(e => e.idRequest == idVisit)[0];
    let popupVisit = `
    <div class="hover_bkgr_fricc" data-id="${visit.idRequest}">
        <span class="helper"></span>
        <div>
            <div class="popupCloseButton" data-id="${visit.idRequest}">
                &times;
            </div>
            <h2>Voici les meubles de la visite : </h2>
            <div>
                <h4>Meuble(s) : </h4><br>
                <div id="allFurnitures"></div>
            </div>
        </div>
    </div>
    `;

    document.getElementById("popups").innerHTML = popupVisit;
    WaitingSpinner(document.getElementById("allFurnitures"))
    Array.from(document.getElementsByClassName("hover_bkgr_fricc")).forEach((element) => {
        if (element.dataset.id == idVisit) {
            element.style.display = "block";
            return;
        }
    });

    Array.from(document.getElementsByClassName("popupCloseButton")).forEach((e) => {
        e.addEventListener("click", onClose);
    });

    let allFurnitures = document.getElementById("allFurnitures");
    let toAdd = `
        <div class="allFurnituresForOnVisit">
            <ol>
    `;
    let listFurnituresForOnVisit;
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
        console.error("VisitsForClientPage::onVisitsForClient", err);
        PrintError(err);
    }

    listFurnituresForOnVisit
        .map((furniture) => {
            toAdd += `
                <li>
                    <div class="furniture" id="${furniture.id}" data-id="${furniture.id}">
                        ${furniture.description}<br>
                        Etat : <div id="furnCondition${furniture.id}" value="${furniture.condition}"></div>
                        <div class="photoDiv">`;
            furniture.listPhotos.map(e => {
                toAdd += `<img class="imageVisits" src="${e.photo}">`
            });
                        toAdd +=  `</div>
                    </div>
                </li>
            `
        });
    toAdd += `</ol></div>`;

    

    allFurnitures.innerHTML = toAdd;
    for (let i = 0; i < listFurnituresForOnVisit.length; i++){
        let div = document.getElementById("furnCondition"+listFurnituresForOnVisit[i].id);
        let condition = "";
        switch(div.getAttribute("value")){
            case "EN_ATTENTE":
                condition = EN_ATTENTE;
                break;
            case "ACHETE":
                condition = ACHETE;
                break;
            case "REFUSE":
                condition = REFUSE;
                break;
            case "EN_RESTAURATION":
                condition = EN_RESTAURATION;
                break;
            case "DEPOSE_EN_MAGASIN":
                condition = DEPOSE_EN_MAGASIN;
                break;
            case "EN_VENTE":
                condition = EN_VENTE;
                break;
            case "SOUS_OPTION":
                condition = SOUS_OPTION;
                break;
            case "VENDU":
                condition = VENDU;
                break;
            case "RETIRE":
                condition = RETIRE;
                break;
        }
        div.innerHTML = condition;
    }
    
    
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

export default VisitsForClientPage;

