import { getUserSessionData} from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
const API_BASE_URL = "api/furnitures/";

let furnitureListTab;



let furnitureListPage = 
`
        <div class="all-furn-title small-caps">Tous les meubles</div>

        <div class="parent-furnitures-container">

`;


const FurnitureListPage = () => {  
    let page = document.querySelector("#page");
    page.innerHTML = furnitureListPage;
    let buyableFurnitureList = getBuyableFurnituresList();
    buyableFurnitureList.forEach(element => {
        page.innerHTML += 
        `
        <div class="item-card">
            <div class="item-img-container">
                <img src="${}" alt="" class="item-img">
                <h3 class="item-img-hover condensed">Voir<br>article</h3>
            </div>
            <div class="item-name">${}</div>
            <div class="item-price condensed">${}</div><div class="currency" style="font-size: 18px;">euro</div>
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