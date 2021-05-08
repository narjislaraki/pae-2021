import {getUserSessionData, currentUser} from "../utils/session.js";
import {RedirectUrl} from "./Router.js";
import callAPI from "../utils/api.js";
import PrintError from "../utils/PrintError.js";
import {FurniturePage} from "./FurniturePage.js";
import waitingSpinner from "../utils/WaitingSpinner";

const API_BASE_URL = "/api/furnitures/";

let furnitureListPage =
    `
        <div class="all-furn-title small-caps" id="titlePage">Tous les meubles</div>

        <div class="parent-furnitures-container">

`;


async function FurnitureListPage(pageData) {
    if (!currentUser) {
        RedirectUrl("/");
        return;
    }
    waitingSpinner();
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    let typeOfFurnitureId;
    let titleHtml;
    if (pageData && pageData.title) {
        typeOfFurnitureId = pageData.idTypeOfFurniture;
        titleHtml = pageData.title;
    }
    let page = document.querySelector("#page");
    let furnitures;
    let userData = getUserSessionData();
    if (typeOfFurnitureId == null) {
        try {
            furnitures = await callAPI(
                API_BASE_URL,
                "GET",
                userData.token,
                undefined);
        } catch (err) {
            console.error("FurnitureListPage::get listfurnitures", err);
            PrintError(err);
        }
    } else {
        try {
            furnitures = await callAPI(
                API_BASE_URL + "type/" + typeOfFurnitureId,
                "GET",
                userData.token,
                undefined);
        } catch (err) {
            console.error("FurnitureListPage::get listfurnitures", err);
            PrintError(err);
        }
    }


    page.innerHTML = furnitureListPage;

    let data;
    if (furnitures.length === 0) {
        data = "Aucun meuble actuellement";
    } else {
        let section = `<section class="furnitureListMenu">`;
        furnitures.map((element) => {
            if (element.favouritePhoto){

                section +=
                    `
                        <div data-id="${element.id}" class="item-card furniture">
                            <div data-id="${element.id}" class="item-img-container">
                                <img data-id="${element.id}" src="${element.favouritePhoto}" alt="" class="item-img">
                                <h3 data-id="${element.id}" class="item-img-hover condensed">Voir<br>article</h3>
                            </div>
                            <div data-id="${element.id}" class="item-name">${element.description}</div>
                            <div data-id="${element.id}" class="item-price condensed">${element.offeredSellingPrice == 0 ? "N/A" : element.offeredSellingPrice}</div><div class="euro">euro</div>
                        </div>
                    `;
        }else{

                section +=
                    `
                        <div data-id="${element.id}" class="item-card furniture">
                            <div data-id="${element.id}" class="item-img-container">
                                <img data-id="${element.id}" src="../assets/furniture_sketch.jpg" alt="" class="item-img">
                                <h3 data-id="${element.id}" class="item-img-hover condensed">Voir<br>article</h3>
                            </div>
                            <div data-id="${element.id}" class="item-name">${element.description}</div>
                            <div data-id="${element.id}" class="item-price condensed">${element.offeredSellingPrice == 0 ? "N/A" : element.offeredSellingPrice}</div><div class="euro">euro</div>
                        </div>
                    `;
            }
        });
        section +=`</section>`;
        page.innerHTML += section;
    }


    //close the div
    page.innerHTML += `</div>`;
    let list = document.getElementsByClassName("furniture");
    Array.from(list).forEach((e) => {
        e.addEventListener("click", onFurniture);
    });
    if (titleHtml) {
        let title = document.getElementById("titlePage");
        title.innerHTML = "Voici tous les meubles de type \"" + titleHtml + "\"";
    }
}

const onFurniture = (e) => {
    let id = e.srcElement.dataset.id;
    FurniturePage(id);
};


export default FurnitureListPage;