import { getUserSessionData} from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
import { FurniturePage } from "./FurniturePage.js";
const API_BASE_URL = "api/furnitures/";

let furnitureListTab;



let furnitureListPage = 
`
        <div class="all-furn-title small-caps">Tous les meubles</div>

        <div class="parent-furnitures-container">

`;


async function FurnitureListPage(){  
    let page = document.querySelector("#page");
    let furnitures;
    try{
      furnitures = await callAPI(
        API_BASE_URL,
        "GET",
        undefined,
        undefined);
    }catch(err){
      console.error("FurnitureListPage::get listfurnitures", err);
      PrintError(err);
    }
    page.innerHTML = furnitureListPage;
    furnitures.forEach(element => {
        page.innerHTML += 
        `
        <div class="item-card">
            <div class="item-img-container">
                <img src="${element.favouritePhoto}" alt="" class="item-img">
                <h3 class="item-img-hover condensed">Voir<br>article</h3>
            </div>
            <div class="item-name">${element.description}</div>
            <div class="item-price condensed">${element.offeredSellingPrice}</div><div class="currency" style="font-size: 18px;">euro</div>
        </div>
    `;
    });


    //close the div
    page.innerHTML += `</div>`;
    const user = getUserSessionData();
  };

  //return a list of all the buyable furnitures
  function getBuyableFurnituresList(){

  }


  export default FurnitureListPage;