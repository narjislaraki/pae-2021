import { getUserSessionData, currentUser } from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
import { FurniturePage } from "./FurniturePage.js";
import waitingSpinner from "./WaitingSpinner";

const API_BASE_URL = "/api/furnitures/";

let furnitureListTab;



let furnitureListPage =
  `
        <div class="all-furn-title small-caps">Tous les meubles</div>

        <div class="parent-furnitures-container">

`;


async function FurnitureListPage() {
  waitingSpinner();
  let page = document.querySelector("#page");
  let furnitures;
  if (currentUser) {
    let userData = getUserSessionData();
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
  }
  else {
    try {
      furnitures = await callAPI(
        API_BASE_URL + "public",
        "GET",
        undefined,
        undefined);
    } catch (err) {
      console.error("FurnitureListPage::get listfurnitures", err);
      PrintError(err);
    }
  }

  page.innerHTML = furnitureListPage;
  let data =
    furnitures.map((element) => {
      page.innerHTML +=
        `
        <div data-id="${element.id}" class="item-card furniture">
            <div data-id="${element.id}" class="item-img-container">
                <img data-id="${element.id}" src="${element.favouritePhoto}" alt="" class="item-img">
                <h3 data-id="${element.id}" class="item-img-hover condensed">Voir<br>article</h3>
            </div>
            <div data-id="${element.id}" class="item-name">${element.description}</div>
            <div data-id="${element.id}" class="item-price condensed">${element.offeredSellingPrice == 0 ? "N/A" : element.offeredSellingPrice}</div><div class="currency" style="font-size: 18px;">euro</div>
        </div>
    `;

    });


  //close the div
  page.innerHTML += `</div>`;
  let list = document.getElementsByClassName("furniture");
  Array.from(list).forEach((e) => {
    e.addEventListener("click", onFurniture);
  });

  console.log(furnitures)
};

const onFurniture = (e) => {
  if (!currentUser){
    let err = {
      message: "Vous devez être connecté pour accéder à ce contenu",
    }
    PrintError(err);
    return;
  }
  let id = e.srcElement.dataset.id;
  waitingSpinner();
  FurniturePage(id);
};




export default FurnitureListPage;