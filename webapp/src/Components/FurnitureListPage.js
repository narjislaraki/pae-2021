import { getUserSessionData, currentUser } from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
import { FurniturePage } from "./FurniturePage.js";
const API_BASE_URL = "/api/furnitures/";

let furnitureListTab;



let furnitureListPage =
  `
        <div class="all-furn-title small-caps">Tous les meubles</div>

        <div class="parent-furnitures-container">

`;


async function FurnitureListPage() {
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
        API_BASE_URL + "/public",
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
      console.log(element);
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
  page.innerHTML += `<div class="white-space"></div></div>`;
  let list = document.getElementsByClassName("furniture");
  console.log(list, "ici");
  Array.from(list).forEach((e) => {
    e.addEventListener("click", onFurniture);
  });

  const user = getUserSessionData();
};

const onFurniture = (e) => {
  console.log(e);
  let id = e.srcElement.dataset.id;
  console.log(id);
  FurniturePage(id);
};




export default FurnitureListPage;