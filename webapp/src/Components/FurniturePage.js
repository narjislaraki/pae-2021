import { getUserSessionData, currentUser } from "../utils/session.js";
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
let nbOfDay;
let option;
let menuDeroulant = '';
let page = document.querySelector("#page");
async function FurniturePage(id) {
    console.log(currentUser);

    userData = getUserSessionData();
    /****** Furniture ******/
    try {
        furniture = await callAPI(
            API_BASE_URL + id,
            "GET",
            undefined,
            undefined);
    } catch (err) {
        console.error("FurniturePage::get furniture", err);
        PrintError(err);
    }

    /****** nbOfDays ******/
    let idFurniture = furniture.id;
    if (currentUser) {
        let idUser = currentUser.id;
        try {
            nbOfDay = await callAPI(
                API_BASE_URL + idFurniture  + "/getSumOfOptionDays",
                "GET",
                userData.token,
                {userId : idUser},
            );
        } catch (err) {
            console.error("FurniturePage::onNbOfDay", err);
            PrintError(err);
        }

        /****** Option ******/
        try {
            option = await callAPI(
                API_BASE_URL + idFurniture + "/getOption",
                "GET",
                userData.token,
                undefined,
            );
        } catch (err) {
            //ugly and terrible idea but it works!
            if (err != "SyntaxError: Unexpected end of JSON input") {
                console.error("FurniturePage::onGetOption", err);
                PrintError(err);
            }
        }
    }

    console.log(furniture);
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
                        <div id="furniture-price">${furniture.offeredSellingPrice == 0 ? "N/A" : furniture.offeredSellingPrice}</div>
                        <div class="currency">euro</div>
                    </div>
                </div>
        `;
    if (currentUser != null && (currentUser.role == "CLIENT" || currentUser.role == "ANTIQUAIRE")) {
        if (furniture.condition == "SOUS_OPTION") {
            console.log(option);
            console.log("pouet")
            console.log(currentUser)
            if (option.idUser != currentUser.id) {
                console.log("je passe ici")
                page.innerHTML += `<div class="option-days condensed small-caps">Ce meuble est sous option, repassez plus tard</div>`;
            } else {
                page.innerHTML += `
                <div class="option-days condensed small-caps">Vous avez déjà réservé ${nbOfDay} jours</div>
                <div class="option-days-below">
                <p>Raison de l'annulation</p>
                <input type="text" id="cancelOption">
                <button class="btn-dark" id="cancelOptionBtn">Annuler l'option</button>
                </div>
                `;
                let cancelOptionBtn = document.getElementById("cancelOptionBtn");
                cancelOptionBtn.addEventListener("click", onCancelOption);
            }

        } else if (furniture.condition == "EN_VENTE") {
            if (nbOfDay >= 5) {
                page.innerHTML += `<div class="option-days condensed small-caps">Vous avez atteint la limite d'option pour cet objet</div>`;
            } else {
                if (nbOfDay > 0) {
                    page.innerHTML += `
                    <div class="option-days condensed small-caps">Vous avez déjà réservé ${nbOfDay} jours</div>`;
                }
                page.innerHTML += `<div>
                <p>Durée de l'option</p>
                <div class="plus-minus">
                            <button class="btn minus-btn disabled" type="button">-</button>
                            <input type="text" id="optionTerm" value="1" readonly="readonly">
                            <button class="btn plus-btn" type="button">+</button>
                        </div>

                <button class="btn-dark" id="introduceOptionBtn">Introduire une option</button>
                </div>`;
                //option counter
                document.querySelector(".minus-btn").setAttribute("disabled", "disabled");
                document.querySelector(".plus-btn").addEventListener("click", incrementCounter);
                document.querySelector(".minus-btn").addEventListener("click", decrementCounter);

                let introduceOptionBtn = document.getElementById("introduceOptionBtn");
                introduceOptionBtn.addEventListener("click", onIntroduceOption);
            }
        }
    } else if (currentUser != null && currentUser.role == "ADMIN") {
        if (furniture.condition == "ACHETE") {
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
        } else if (furniture.condition == "EN_RESTAURATION") {
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
        } else if (furniture.condition == "DEPOSE_EN_MAGASIN") {
            menuDeroulant = `
            <div class="price-inline" >
                        <input type="number"  min="0" id="price" required/>Entrez un prix de vente</div>
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    ${furniture.condition}
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                    <button id="buttonEnRestauration" class="dropdown-item disabled" type="button">En restauration</button>
                    <button id="buttonMagasin" class="dropdown-item disabled" type="button">Déposé en magasin</button>
                    <button id="buttonEnVente" class="dropdown-item" type="button">En vente</button>
                    <button id="buttonRetire" class="dropdown-item" type="button">Retiré de la vente</button>
                </div>
            </div>
        `;
        } else if (furniture.condition == "EN_VENTE") {
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
        } else if (furniture.condition == "SOUS_OPTION") {
            console.log(option);
            console.log("pouet")
            console.log(currentUser)
            page.innerHTML += `
                <div class="option-days-below">
                <p>Raison de l'annulation</p>
                <input type="text" id="cancelOption">
                <button class="btn-dark" id="cancelOptionBtn">Annuler l'option</button>
                </div>
                `;
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
        } else if (furniture.condition == "RETIRE" || furniture.condition == "REFUSE") {
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
        try {
            let cancelOptionBtn = document.getElementById("cancelOptionBtn");
            cancelOptionBtn.addEventListener("click", onCancelOption);
        } catch (err) {

        }
    }
};




const onWorkShop = async () => {
    let id = furniture.id;
    try {
        await callAPI(
            API_BASE_URL + id + '/workShop',
            "POST",
            userData.token,
            undefined,
        );
    } catch (err) {
        console.error("FurniturePage::onWorkShop", err);
        PrintError(err);
    }
    FurniturePage(id);
};


const onDropOfStore = async () => {
    let id = furniture.id;
    console.log(userData.token);
    try {
        await callAPI(
            API_BASE_URL + id + '/dropOfStore',
            "POST",
            userData.token,
            undefined,
        );
    } catch (err) {
        console.error("FurniturePage::dropOfStore", err);
        PrintError(err);
    }
    FurniturePage(id);
};

const onOfferedForSale = async () => {
    let id = furniture.id;
    let price = document.getElementById("price").value;
    console.log(price);
    if (price == "") {
        let error = {
            message: "Veuillez d'abord entrer un prix de vente",
        }
        PrintError(error);
        return;
    }
    try {
        await callAPI(
            API_BASE_URL + id + '/offeredForSale/',
            "POST",
            userData.token,
            {
                furniturePrice : price
            },
        );
    } catch (err) {
        console.error("FurniturePage::offeredForSale", err);
        PrintError(err);
    }
    FurniturePage(id);
};


const onWithdrawSale = async () => {
    let id = furniture.id;
    console.log(userData.token);
    try {
        await callAPI(
            API_BASE_URL + id + '/withdrawSale',
            "POST",
            userData.token,
            undefined,
        );
    } catch (err) {
        console.error("FurniturePage::onWithdrawSale", err);
        PrintError(err);
    }
    FurniturePage(id);
};

const onCancelOption = async () => {
    console.log("ceci n'est pas un test")
    let id = furniture.id;
    let reason = document.getElementById("cancelOption").value;
    if (reason == "") {
        let error = {
            message: "Veuillez d'abord entrer une raison d'annulation",
        }
        PrintError(error);
        return;
    }
    try {
        await callAPI(
            API_BASE_URL + id + "/cancelOption",
            "POST",
            userData.token,
            {
                cancelReason : reason
            },
        );
    } catch (err) {
        console.error("FurniturePage::onWithdrawSale", err);
        PrintError(err);
    }
    FurniturePage(id);
};

const onIntroduceOption = async () => {
    let id_furniture = furniture.id;
    let id_user = currentUser.id;
    let optionTerm = document.getElementById("optionTerm").value;
    try {
        await callAPI(
            API_BASE_URL + "/" + id_furniture + "/" + id_user + "/introduceOption",
            "POST",
            userData.token,
            {
                duration : optionTerm
            },
        );
    } catch (err) {
        console.error("FurniturePage::onWithdrawSale", err);
        PrintError(err);
    }
    FurniturePage(id_furniture);
};



/*function gallerySlides(smallImg) {
    let bigImg = document.getElementById("big-img");
    bigImg.src = smallImg.src;
}*/

/*smallImg1.addEventListener("mouseover", () => { gallerySlides(smallImg1); });
smallImg2.addEventListener("mouseover", () => { gallerySlides(smallImg2); });
smallImg3.addEventListener("mouseover", () => { gallerySlides(smallImg3); });
smallImg4.addEventListener("mouseover", () => { gallerySlides(smallImg4); });
smallImg5.addEventListener("mouseover", () => { gallerySlides(smallImg5); });*/

let valueCount;

const incrementCounter = () => {
    console.log("increment")
    valueCount = document.getElementById("optionTerm").value;
    valueCount++;
    document.getElementById("optionTerm").value = valueCount;
    if (valueCount > 1) {
        document.querySelector(".minus-btn").removeAttribute("disabled");
        document.querySelector(".minus-btn").classList.remove("disabled");
    }
    if (valueCount == 5) {
        document.querySelector(".plus-btn").setAttribute("disabled", "disabled");
    }
}

const decrementCounter = () => {
    console.log("decrement")
    valueCount = document.getElementById("optionTerm").value;
    valueCount--;
    document.getElementById("optionTerm").value = valueCount;
    if (valueCount == 1) {
        document.querySelector(".minus-btn").setAttribute("disabled", "disabled");
    }
    if (valueCount < 5) {
        document.querySelector(".plus-btn").removeAttribute("disabled");
        document.querySelector(".plus-btn").classList.remove("disabled");
    }
}


export { FurniturePage };