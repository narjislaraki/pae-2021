import { getUserSessionData} from "../utils/session.js";
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



async function FurniturePage (id) {  
    let page = document.querySelector("#page");
    let furniture;
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
                    <button class="btn btn-light option-counter">- O +</button>
                </div>
                <div class="option-buttons">
                    <div class="btn btn-success condensed small-caps">Introduire une option</div>
                    <div class="option-days condensed small-caps">Vous avez deja reserve x jour(s)</div>
                </div>
                <div class="options-info">Attention,  vous ne pouvez cumuler que 5 jours d’option au total sur un meuble </div>
            </div>
    `;
    
    const user = getUserSessionData();
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

export { FurniturePage };