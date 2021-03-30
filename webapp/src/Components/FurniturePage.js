import { getUserSessionData, currentUser} from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
const API_BASE_URL = "api/furnitures/";

let smallImg1 = document.getElementById("small-img1");
let smallImg2 = document.getElementById("small-img2");
let smallImg3 = document.getElementById("small-img3");
let smallImg4 = document.getElementById("small-img4");
let smallImg5 = document.getElementById("small-img5");

let furniture;
let userData;

async function FurniturePage (id) {  
    let page = document.querySelector("#page");
    userData = getUserSessionData();
    let type;
    try{
        furniture = await callAPI(
          API_BASE_URL + "furniture/" + id,
          "GET",
          undefined,
          undefined);
    }catch(err){
        console.error("FurniturePage::get furniture", err);
        PrintError(err);
    }
    if (currentUser == null){
        page.innerHTML = `
        <div class="furniture-container">
                    <div class="furniture-pictures">
                        <div class="furniture-small-images">
                            <img id="small-img1" src="" alt="">
                            <img id="small-img2" src="" alt="">
                            <img id="small-img3" src="" alt="">
                            <img id="small-img4" src="" alt="">
                            <img id="small-img5" src="" alt="">
                        </div>
                        <img src="" alt="" id="big-img" class="main-image">
                    </div>
                    <div class="condensed small-caps" id="furniture-type">
                        ${furniture.type}
                    </div>
                    <div class="condensed" id="furniture-description">
                        ${furniture.description}
                    </div>
                    <div class="furniture-price-inline">
                        <div id="furniture-price">${furniture.offeredSellingPrice}</div>
                        <div class="currency">euro</div>
                    </div>
                </div>
        `;
    }else if (currentUser.role == "CLIENT"){
        page.innerHTML = `
        <div class="furniture-container">
                    <div class="furniture-pictures">
                        <div class="furniture-small-images">
                            <img id="small-img1" src="" alt="">
                            <img id="small-img2" src="" alt="">
                            <img id="small-img3" src="" alt="">
                            <img id="small-img4" src="" alt="">
                            <img id="small-img5" src="" alt="">
                        </div>
                        <img src="" alt="" id="big-img" class="main-image">
                    </div>
                    <div class="condensed small-caps" id="furniture-type">
                        ${furniture.type}
                    </div>
                    <div class="condensed" id="furniture-description">
                        ${furniture.description}
                    </div>
                    <div class="furniture-price-inline">
                        <div id="furniture-price">${furniture.offeredSellingPrice}</div>
                        <div class="currency">euro</div>
                    </div>
        </div>
        
                <div class="furniture-options">
                    <div class="option-duration condensed">
                        <div class="option-duration-text small-caps">Durée de l’option</div>
                        <div class="plus-minus">
                            <button class="btn minus-btn disabled" type="button">-</button>
                            <input type="text" id="quantity" value="1">
                            <button class="btn plus-btn" type="button">+</button>
                        </div>
                    </div>
                    <div class="option-buttons">
                        <div class="btn btn-success condensed small-caps">Introduire une option</div>
                        <div class="option-days condensed small-caps">Vous avez deja reserve x jour(s)</div>
                    </div>
                    <div class="options-info">Attention,  vous ne pouvez cumuler que 5 jours d’option au total sur un meuble </div>
                </div>
        `;
        //option counter
        document.querySelector(".minus-btn").setAttribute("disabled", "disabled");
        document.querySelector(".plus-btn").addEventListener("click", incrementCounter());
        document.querySelector(".minus-btn").addEventListener("click", decrementCounter());


    }else if (currentUser.role == "ADMIN"){
        page.innerHTML = `
        <div class="furniture-container">
                    <div class="furniture-pictures">
                        <div class="furniture-small-images">
                            <img id="small-img1" src="" alt="">
                            <img id="small-img2" src="" alt="">
                            <img id="small-img3" src="" alt="">
                            <img id="small-img4" src="" alt="">
                            <img id="small-img5" src="" alt="">
                        </div>
                        <img src="" alt="" id="big-img" class="main-image">
                    </div>
                    <div class="condensed small-caps" id="furniture-type">
                        ${furniture.type}
                    </div>
                    <div class="condensed" id="furniture-description">
                        ${furniture.description}
                    </div>`;
                    
        if(furniture.condition != "DEPOSE_EN_MAGASIN"){
            page.innerHTML += `<div class="furniture-price-inline" >
                        <div id="furniture-price">${furniture.offeredSellingPrice}</div>
                        <div class="currency">euro</div>
                    </div>
                </div>
            `;    
        }else{
            page.innerHTML += `<div class="furniture-price-inline" >
                        <input type="number"  min="0" id="furniture-price" required>Entrez un prix de vente</div>
                        <div class="currency">euro</div>
                    </div>
                </div>
            `;    
        }
                    
    }

    let menuDeroulant = '';
    console.log(furniture.condition);
    if (furniture.condition == "ACHETE"){
        menuDeroulant = `
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    ${furniture.condition}
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                    <button id="buttonEnRestauration" class="dropdown-item" type="button">En restauration</button>
                    <button id="buttonMagasin" class="dropdown-item" type="button">Déposé en magasin</button>
                    <button id="buttonEnVente" class="dropdown-item disabled" type="button">En vente</button>
                    <button id="buttonRetire" class="dropdown-item disabled" type="button">Retiré de la vente</button>
                </div>
            </div>
        `;
    }else if (furniture.condition == "EN_RESTAURATION"){
        menuDeroulant = `
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    ${furniture.condition}
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                    <button id="buttonEnRestauration" class="dropdown-item disabled" type="button">En restauration</button>
                    <button id="buttonMagasin" class="dropdown-item" type="button">Déposé en magasin</button>
                    <button id="buttonEnVente" class="dropdown-item disabled" type="button">En vente</button>
                    <button id="buttonRetire" class="dropdown-item disabled" type="button">Retiré de la vente</button>
                </div>
            </div>
        `;
    }else if (furniture.condition == "DEPOSE_EN_MAGASIN"){
        menuDeroulant = `
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    ${furniture.condition}
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                    <button id="buttonEnRestauration" class="dropdown-item disabled" type="button">En restauration</button>
                    <button id="buttonMagasin" class="dropdown-item disabled" type="button">Déposé en magasin</button>
                    <button id="buttonEnVente" class="dropdown-item" type="button">En vente</button>
                    <button id="buttonRetire" class="dropdown-item disabled" type="button">Retiré de la vente</button>
                </div>
            </div>
        `;
    }else if (furniture.condition == "EN_VENTE"){
        menuDeroulant = `
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    ${furniture.condition}
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                    <button id="buttonEnRestauration" class="dropdown-item disabled" type="button">En restauration</button>
                    <button id="buttonMagasin" class="dropdown-item disabled" type="button">Déposé en magasin</button>
                    <button id="buttonEnVente" class="dropdown-item disabled" type="button">En vente</button>
                    <button id="buttonRetire" class="dropdown-item" type="button">Retiré de la vente</button>
                </div>
            </div>
        `;
    }else if (furniture.condition == "RETIRE"){
        menuDeroulant = `
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    ${furniture.condition}
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                    <button id="buttonEnRestauration" class="dropdown-item disabled" type="button">En restauration</button>
                    <button id="buttonMagasin" class="dropdown-item disabled" type="button">Déposé en magasin</button>
                    <button id="buttonEnVente" class="dropdown-item disabled" type="button">En vente</button>
                    <button id="buttonRetire" class="dropdown-item disabled" type="button">Retiré de la vente</button>
                </div>
            </div>
        `;
    }
    page.innerHTML += menuDeroulant;
    let buttonEnRestauration = document.getElementById("buttonEnRestauration");
    let buttonMagasin = document.getElementById("buttonMagasin");
    let buttonEnVente = document.getElementById("buttonEnVente");
    let buttonRetire = document.getElementById("buttonRetire");
    buttonEnRestauration.addEventListener("click", onWorkShop);
    buttonMagasin.addEventListener("click", onDropOfStore);
    buttonEnVente.addEventListener("click", onOfferedForSale);
    buttonRetire.addEventListener("click", onWithdrawSale)
        
    
    
};

const onWorkShop = async() =>{
    let id = furniture.id;
    console.log(userData.token);
    try{
      await callAPI(
        API_BASE_URL + "furniture/" + id + '/workShop',
        "PATCH",
        userData.token,
        undefined,
      );
    }catch(err){
      console.error("FurniturePage::onWorkShop", err);
      PrintError(err);
    }
    FurniturePage(id);
  };


const onDropOfStore = async() =>{
    let id = furniture.id;
    console.log(userData.token);
    try{
      await callAPI(
        API_BASE_URL + "furniture/" + id + '/dropOfStore',
        "PATCH",
        userData.token,
        undefined,
      );
    }catch(err){
      console.error("FurniturePage::dropOfStore", err);
      PrintError(err);
    }
    FurniturePage(id);
  };

const onOfferedForSale = async() =>{
    let id = furniture.id;
    let price = document.getElementById("furniture-price").value;
    
    try{
      await callAPI(
        API_BASE_URL + "furniture/" + id + '/offeredForSale/' + price,
        "PATCH",
        userData.token,
        undefined,
      );
    }catch(err){
      console.error("FurniturePage::offeredForSale", err);
      PrintError(err);
    }
    FurniturePage(id);
  };


const onWithdrawSale = async() =>{
  let id = furniture.id;
  console.log(userData.token);
  try{
    await callAPI(
      API_BASE_URL + "furniture/" + id + '/withdrawSale',
      "PATCH",
      userData.token,
      undefined,
    );
  }catch(err){
    console.error("FurniturePage::onWithdrawSale", err);
    PrintError(err);
  }
  FurniturePage(id);
};



function gallerySlides(smallImg){
    let bigImg = document.getElementById("big-img");
    bigImg.src = smallImg.src;
}

/*smallImg1.addEventListener("mouseover", () => { gallerySlides(smallImg1); });
smallImg2.addEventListener("mouseover", () => { gallerySlides(smallImg2); });
smallImg3.addEventListener("mouseover", () => { gallerySlides(smallImg3); });
smallImg4.addEventListener("mouseover", () => { gallerySlides(smallImg4); });
smallImg5.addEventListener("mouseover", () => { gallerySlides(smallImg5); });*/

let valueCount;

function incrementCounter(){
  console.log("increment")
  valueCount = document.getElementById("quantity").value;
  valueCount++;
  document.getElementById("quantity").value = valueCount;
  if(valueCount > 1){
      document.querySelector(".minus-btn").removeAttribute("disabled");
      document.querySelector(".minus-btn").classList.remove("disabled");
  }
  if(valueCount == 5){
      document.querySelector(".plus-btn").setAttribute("disabled", "disabled");
  }
}

function decrementCounter(){
  console.log("decrement")
  valueCount = document.getElementById("quantity").value;
  valueCount--;
  document.getElementById("quantity").value = valueCount;
  if(valueCount == 1){
      document.querySelector(".minus-btn").setAttribute("disabled", "disabled");
  }
  if(valueCount < 5){
      document.querySelector(".plus-btn").removeAttribute("disabled");
      document.querySelector(".plus-btn").classList.remove("disabled");
  }
}

export { FurniturePage };