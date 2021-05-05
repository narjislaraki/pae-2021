import callAPI from "../utils/api";
import { RedirectUrl } from "./Router";
import { currentUser, getUserSessionData } from "../utils/session.js";
import PrintError from "./PrintError";
import waitingSpinner from "./WaitingSpinner.js";
import { FurniturePage } from "./FurniturePage.js";

const API_BASE_URL = "/api/furnitures/slider";

const VENDU = `<h4><span class="badge bg-danger small-caps">VENDU</span></h4></div></div>`;
const SOUS_OPTION = `<h4><span class="badge bg-warning small-caps">SOUS OPTION</span></h4></div></div>`;
const EN_VENTE = `<h4><span class="badge bg-success small-caps">EN VENTE</span></h4></div></div>`;

let idType;
let title;
let typesOfFurnitures;

let homepageTitle = `
<div class="homepageTitle">
  <div class="all-furn-title small-caps" id="title">Accueil</div>
  <div class="dropdown">
    <button class="btn btn-secondary dropdown-toggle" type="buttonType" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      Choisir un type de meuble
    </button>
    <div class="dropdown-menu" id="type" aria-labelledby="dropdownMenuButton"></div>
  </div>
</div>
`;


const HomePage = async () => {  
  let page = document.querySelector("#page");
  waitingSpinner(page);
  let furnitures;
  try {
    furnitures = await callAPI(
      API_BASE_URL,
      "GET",
      undefined,
      undefined);

    } catch (err) {
    console.error("Homepage::get list", err);
    PrintError(err);
  }
  if(idType != null && idType != "all"){
    furnitures = furnitures.filter(e => e.typeId == idType);
  }
  try{
    typesOfFurnitures = await callAPI(
      "/api/furnitures/typeOfFurnitureList",
      "GET",
      undefined,
      undefined,);
  } catch(err){
    console.error("HomePage::get list of types of furnitures", err);
    PrintError(err);
  }

  if(furnitures.length >= 1){
    let carousel1 = `
    <div class="container-carousel">
            <div id="carouselHomepage" class="carousel slide condensed" data-bs-ride="carousel">
                <div class="carousel-indicators">
                  <button type="button" data-bs-target="#carouselHomepage" data-bs-slide-to="0" class="active" aria-current="true"></button>
  `;
  for (let index = 1; index < furnitures.length; index++) {
    carousel1 += `<button type="button" data-bs-target="#carouselHomepage" data-bs-slide-to="${index}"></button>`;
  }
  //close indicator div
  carousel1 += `
  </div>
                <div class="carousel-inner">
  `;
  let carousel2 = `
  <div class="carousel-item active">
                    <img src="${furnitures[0].favouritePhoto}" class="d-block w-100" alt="...">
                    <div class="carousel-caption d-none d-md-block">
                    
                      <p>${furnitures[0].description}</p>
  `;
  if(furnitures[0].condition == "EN_VENTE"){
    carousel2 += EN_VENTE;
  }
  else if(furnitures[0].condition == "VENDU"){
    carousel2 += VENDU;
  }
  else if(furnitures[0].condition == "SOUS_OPTION"){
    carousel2 += SOUS_OPTION;
  }
  for (let index = 1; index < furnitures.length; index++) {
    carousel2 += `
    <div class="carousel-item">
                    <img src="${furnitures[index].favouritePhoto}" class="d-block w-100" alt="...">
                    <div class="carousel-caption d-none d-md-block">
                      
                      <p>${furnitures[index].description}</p>
    `;
    if(furnitures[index].condition == "EN_VENTE"){
      carousel2 += EN_VENTE;
    }
    else if(furnitures[index].condition == "VENDU"){
      carousel2 += VENDU;
    }
    else if(furnitures[index].condition == "SOUS_OPTION"){
      carousel2 += SOUS_OPTION;
    }
  }

  carousel1 += carousel2;
  carousel1 += `
  </div>
                <button class="carousel-control-prev" type="button" data-bs-target="#carouselHomepage" data-bs-slide="prev">
                  <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                  <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#carouselHomepage" data-bs-slide="next">
                  <span class="carousel-control-next-icon" aria-hidden="true"></span>
                  <span class="visually-hidden">Next</span>
                </button>
              </div>

        </div>
  `;
  page.innerHTML = homepageTitle + carousel1;
  let menuDeroulant = document.getElementById("type");
  let title = document.getElementById("title");
  menuDeroulant.innerHTML += `<a id="all" class="dropdown-item typeOfFurniture" href="#">Voir tous les meubles</a>`;
  let typesOfFurnituresList = document.getElementsByClassName("typeOfFurniture");

  for (let i = 0; i < typesOfFurnitures.length; i++){
    menuDeroulant.innerHTML += `<a id="type${typesOfFurnitures[i].id}" data-id="${typesOfFurnitures[i].id}" class="dropdown-item typeOfFurniture" href="#">${typesOfFurnitures[i].label}</a>`;
  }

  
  for (let index = 0; index < typesOfFurnituresList.length; index++) {
    Array.from(typesOfFurnituresList)[index].addEventListener("click", ()=>{
      idType=typesOfFurnituresList[index].getAttribute("data-id");
      title.innerHTML = "Accueil : "+ typesOfFurnituresList[index].innerText;
    });
  }
 

if(currentUser){
  let list = document.getElementsByClassName("carousel-item");
  for (let index = 0; index < Array.from(list).length; index++) {
    Array.from(list)[index].addEventListener("click", ()=>{
      FurniturePage(furnitures[index].id);
    });
  }
}
}

};


export default HomePage;