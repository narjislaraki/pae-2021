import callAPI from "../utils/api";
import { RedirectUrl } from "./Router";
import { currentUser, getUserSessionData } from "../utils/session.js";
import PrintError from "./PrintError";
import waitingSpinner from "./WaitingSpinner.js";

const API_BASE_URL = "/api/users/";
let page = document.querySelector("#page");
let salesAsBuyer = []; 
let salesAsSeller = [];

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
              <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
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
              <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#accordionExample">
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
    waitingSpinner();
    page.innerHTML = title;

    let myTransactions = document.getElementById("myTransactions");
    myTransactions.addEventListener("click", onMyTransactions);

    let myVisits = document.getElementById("myVisits");
    myVisits.addEventListener("click", onMyVisits);
    
    let soldFurnAcc = document.querySelector("#soldFurnAcc");
    let boughtFurnAcc = document.querySelector("#boughtFurnAcc");
    let searchSold = document.querySelector("#searchBar-sold");
    let searchBought = document.querySelector("#searchBar-bought");

    searchSold.addEventListener('keyup', (e) => {
      console.log("Meubles vendus: " + e.target.value);
      const searchValue = e.target.value.toLowerCase();
  
      const filteredFurnitures = salesAsBuyer.filter(furniture => {
          return furniture.description.toLowerCase().includes(searchValue) || furniture.type.toLowerCase().includes(searchValue);
      });
      displayFurn(filteredFurnitures, soldFurnAcc);
  });
    searchBought.addEventListener('keyup', (e) => {
      console.log("Meubles achetés: " + e.target.value);
      const searchValue = e.target.value.toLowerCase();

      const filteredFurnitures = salesAsSeller.filter(furniture => {
          return furniture.description.toLowerCase().includes(searchValue) || furniture.type.toLowerCase().includes(searchValue);
      });
      displayFurn(filteredFurnitures, soldFurnAcc);
  });
    
    console.log(currentUser.id);
    
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

    } catch (err) {
        console.error("TransactionsPage::get sales", err);
        PrintError(err);
    }

    
    console.log(salesAsBuyer);
    console.log(salesAsBuyer[0]);
    console.log(salesAsSeller);
    console.log(soldFurnAcc);


    //salesAsBuyer = salesAsBuyer.filter(e => e.idBuyer == currentUser.id);
    
    displayFurn(salesAsBuyer, soldFurnAcc);

    /*
      salesAsBuyer.map((element) => {
        soldFurnAcc.innerHTML +=
          `
          <div data-id="${element.furniture.id}" class="item-card furniture">
              <div data-id="${element.furniture.id}" class="item-img-container">
                  <img data-id="${element.furniture.id}" src="${element.furniture.favouritePhoto}" alt="" class="item-img">
                  <h3 data-id="${element.id}" class="item-img-hover condensed">Voir<br>article</h3>
              </div>
              <div data-id="${element.furniture.id}" class="item-name">${element.furniture.description}</div>
              <div data-id="${element.furniture.id}" class="item-price condensed">${element.furniture.offeredSellingPrice == 0 ? "N/A" : element.furniture.offeredSellingPrice}</div><div class="currency" style="font-size: 18px;">euro</div>
          </div>
      `;
      });
      */

      displayFurn(salesAsSeller, boughtFurnAcc);

      /*
      
      salesAsSeller = salesAsSeller.filter(e => e.furniture.sellerId == currentUser.id);
      salesAsSeller.map((element) => {
          boughtFurnAcc.innerHTML +=
            `
            <div data-id="${element.furniture.id}" class="item-card furniture">
                <div data-id="${element.furniture.id}" class="item-img-container">
                    <img data-id="${element.furniture.id}" src="${element.furniture.favoritePhoto}" alt="" class="item-img">
                    <h3 data-id="${element.id}" class="item-img-hover condensed">Voir<br>article</h3>
                </div>
                <div data-id="${element.furniture.id}" class="item-name">${element.furniture.description}</div>
                <div data-id="${element.furniture.id}" class="item-price condensed">${element.furniture.offeredSellingPrice == 0 ? "N/A" : element.furniture.offeredSellingPrice}</div><div class="currency" style="font-size: 18px;">euro</div>
            </div>
        `;
    
        });
      */ 
  
      /*
    //close the div
    page.innerHTML += `</div>`;
    let list = document.getElementsByClassName("furniture");
    Array.from(list).forEach((e) => {
      e.addEventListener("click", onFurniture);
    });
  
    console.log(furnitures)
    */
  };

  const displayFurn = (list, destination) => {
    const htmlString = list
        .map((element) => {
            return `
            <div data-id="${element.furniture.id}" class="item-card furniture">
              <div data-id="${element.furniture.id}" class="item-img-container">
                  <img data-id="${element.furniture.id}" src="${element.furniture.favouritePhoto}" alt="" class="item-img">
                  <h3 data-id="${element.id}" class="item-img-hover condensed">Voir<br>article</h3>
              </div>
              <div data-id="${element.furniture.id}" class="item-name">${element.furniture.description}</div>
              <div data-id="${element.furniture.id}" class="item-price condensed">${element.furniture.offeredSellingPrice == 0 ? "N/A" : element.furniture.offeredSellingPrice}</div><div class="currency" style="font-size: 18px;">euro</div>
          </div>
        `;
        })
        .join('');
    destination.innerHTML = htmlString;
};

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

  export default TransactionsPage;