import callAPI from "../utils/api";
import { RedirectUrl } from "./Router";
import { currentUser, getUserSessionData } from "../utils/session.js";
import PrintError from "../utils/PrintError";
import waitingSpinner from "../utils/WaitingSpinner.js";
import {FurniturePage} from "./FurniturePage.js";

const API_BASE_URL = "/api/users/";
let page = document.querySelector("#page");
let salesAsBuyer = []; 
let salesAsSeller = [];
let furnitures = [];

let title = `
<div class="menuAdmin">
        <div class="condensed small-caps menuAdminOn" id="myTransactions">Mes transactions</div>
        <div class="condensed small-caps" id="myVisits">Mes visites</div>
    </div>
<div class="furns-title-container">
            <div class="all-furn-title small-caps">Transactions</div>

            
        </div>
        

        <div class="accordion" id="accordion-transactions">
            <div class="accordion-item">
              <h2 class="accordion-header" id="headingOne">
                <button class="accordion-button header4 small-caps" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                    Meubles vendus
                </button>
              </h2>
              <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordion-transactions">
                <div class="accordion-body">
                    <div class="search-bar">
                        <input type="text" name="searchBar" id="searchBar-sold" class="search-bar-input" placeholder="Rechercher..." aria-label="search" />
                        <button class="search-bar-submit"><i class="bi bi-search"></i></button>
                    </div>
                    <div class="parent-furnitures-container" id="soldFurnAcc">

                    </div>
                </div>
              </div>
            </div>
            <div class="accordion-item">
              <h2 class="accordion-header" id="headingTwo">
                <button class="accordion-button collapsed header4 small-caps" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                    Meubles achetés
                </button>
              </h2>
              <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#accordion-transactions">
                <div class="accordion-body">
                    <div class="search-bar">
                        <input type="text" name="searchBar" id="searchBar-bought" class="search-bar-input" placeholder="Rechercher..." aria-label="search" />
                        <button class="search-bar-submit"><i class="bi bi-search"></i></button>
                    </div>
                    <div class="parent-furnitures-container" id="boughtFurnAcc">

                    </div>
                </div>
              </div>
            </div>
          </div>
`;



async function TransactionsPage() {
    page.innerHTML = title;

    let myTransactions = document.getElementById("myTransactions");
    myTransactions.addEventListener("click", onMyTransactions);

    let myVisits = document.getElementById("myVisits");
    myVisits.addEventListener("click", onMyVisits);
    
    let soldFurnAcc = document.querySelector("#soldFurnAcc");
    let boughtFurnAcc = document.querySelector("#boughtFurnAcc");
    let searchSold = document.querySelector("#searchBar-sold");
    let searchBought = document.querySelector("#searchBar-bought");
    waitingSpinner(soldFurnAcc);
    waitingSpinner(boughtFurnAcc);

    searchSold.addEventListener('keyup', (e) => {
      const searchValue = e.target.value.toLowerCase();
      const filteredFurnitures = salesAsBuyer.filter(furn => {
        return furn.furniture.description.toLowerCase().includes(searchValue);
    });
      waitingSpinner(soldFurnAcc);
      displayFurnSeller(filteredFurnitures, soldFurnAcc);
  });

    searchBought.addEventListener('keyup', (e) => {

      const searchValue = e.target.value.toLowerCase();

      const filteredFurnitures = salesAsSeller.filter(furn => {
          return furn.furniture.description.toLowerCase().includes(searchValue);
      });
      
      waitingSpinner(boughtFurnAcc);
      displayFurnBuyer(filteredFurnitures, boughtFurnAcc);
  });
    
    let userData = getUserSessionData();
    try {
        salesAsBuyer = await callAPI(
          API_BASE_URL + currentUser.id + "/transactionsBuyer",
          "GET",
          userData.token,
          undefined);

        salesAsSeller = await callAPI(
            API_BASE_URL + currentUser.id + "/transactionsSeller",
            "GET",
            userData.token,
            undefined);

        furnitures = await callAPI(
            "/api/furnitures",
            "GET",
            userData.token,
            undefined);

    } catch (err) {
        console.error("TransactionsPage::get sales", err);
        PrintError(err);
    }
    displayFurnBuyer(salesAsBuyer, boughtFurnAcc);

    displayFurnSeller(salesAsSeller, soldFurnAcc);

  }

  const displayFurnSeller = (list, destination) => {
    const htmlString = list
        .map((element) => {
          if (element.favouritePhoto){
            return `
            <div data-id="${element.id}" class="item-card furniture">
              <div data-id="${element.id}" class="item-img-container">
                  <img data-id="${element.id}" src="${element.favouritePhoto}" alt="" class="item-img">
                  <h3 data-id="${element.id}" class="item-img-hover condensed onFurniture">Voir<br>article</h3>
              </div>
              <div data-id="${element.id}" class="item-name">${element.description}</div>
              <div data-id="${element.id}" class="item-price condensed">${element.offeredSellingPrice == 0 ? "N/A" : element.offeredSellingPrice}</div><div class="euro" >euro</div>
          </div>
        `;
          }else{
            return `
            <div data-id="${element.id}" class="item-card furniture">
              <div data-id="${element.id}" class="item-img-container">
                  <img data-id="${element.id}" src="../assets/furniture_sketch.jpg" alt="" class="item-img">
                  <h3 data-id="${element.id}" class="item-img-hover condensed onFurniture">Voir<br>article</h3>
              </div>
              <div data-id="${element.id}" class="item-name">${element.description}</div>
              <div data-id="${element.id}" class="item-price condensed">${element.offeredSellingPrice == 0 ? "N/A" : element.offeredSellingPrice}</div><div class="euro" >euro</div>
          </div>
        `;
          }
            
        })
        .join('');
    destination.innerHTML = htmlString;
    let listDesFurnitures = document.getElementsByClassName("onFurniture");
    Array.from(listDesFurnitures).forEach((e) => {
        e.addEventListener("click", onFurniture);
    });
  }

    const displayFurnBuyer = (list, destination) => {
      const htmlString = list
          .map((element) => {
            if (element.favouritePhoto){
              return `
              <div data-id="${element.furniture.id}" class="item-card furniture">
                <div data-id="${element.furniture.id}" class="item-img-container">
                    <img data-id="${element.furniture.id}" src="${element.furniture.favouritePhoto}" alt="" class="item-img">
                    <h3 data-id="${element.id}" class="item-img-hover condensed onFurniture">Voir<br>article</h3>
                </div>
                <div data-id="${element.furniture.id}" class="item-name">${element.furniture.description}</div>
                <div data-id="${element.furniture.id}" class="item-price condensed">${element.furniture.offeredSellingPrice == 0 ? "N/A" : element.furniture.offeredSellingPrice}</div><div class="euro" >euro</div>
            </div>
          `;
            }else{
              return `
              <div data-id="${element.furniture.id}" class="item-card furniture">
                <div data-id="${element.furniture.id}" class="item-img-container">
                    <img data-id="${element.furniture.id}" src="../assets/furniture_sketch.jpg" alt="" class="item-img">
                    <h3 data-id="${element.furniture.id}" class="item-img-hover condensed onFurniture">Voir<br>article</h3>
                </div>
                <div data-id="${element.furniture.id}" class="item-name">${element.furniture.description}</div>
                <div data-id="${element.furniture.id}" class="item-price condensed">${element.furniture.offeredSellingPrice == 0 ? "N/A" : element.furniture.offeredSellingPrice}</div><div class="euro" >euro</div>
              </div>
              `;
            }
              
          })
          .join('');
      destination.innerHTML = htmlString;
      let listDesFurnitures = document.getElementsByClassName("onFurniture");
      Array.from(listDesFurnitures).forEach((e) => {
          e.addEventListener("click", onFurniture);
      });
    } 

const onMyTransactions = (e) => {
  e.preventDefault();
  RedirectUrl("/transactions");
}

const onMyVisits = (e) => {
  e.preventDefault();
  RedirectUrl("/visitsForClient");
}

const onFurniture = (e) => {
  let id = e.srcElement.dataset.id;
  FurniturePage(id);
};



  export default TransactionsPage;