import { getUserSessionData, currentUser } from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
import PrintMessage from "./PrintMessage.js";
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

let typeElem;
let descElem;
let priceElem;
let type;
let desc;
let price;

let page = document.querySelector("#page");
async function FurniturePage(id) {

    userData = getUserSessionData();
    /****** Furniture ******/
    if (currentUser) {
        try {
            furniture = await callAPI(
                API_BASE_URL + id,
                "GET",
                userData.token,
                undefined);
        } catch (err) {
            console.error("FurniturePage::get furniture", err);
            PrintError(err);
        }
    }
    else {
        try {
            furniture = await callAPI(
                API_BASE_URL + "public/" + id,
                "GET",
                undefined,
                undefined);
        } catch (err) {
            console.error("FurniturePage::get furniture", err);
            PrintError(err);
        }
    }

    /****** nbOfDays ******/

    if (currentUser) {
        let idFurniture = furniture.id;
        try {
            nbOfDay = await callAPI(
                API_BASE_URL + idFurniture + "/getSumOfOptionDays",
                "GET",
                userData.token,
                undefined,
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

    page.innerHTML = `
        <div id="furniture-container">
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
                    <div id="editImage"></div>
                    <div class="condensed" id="furniture-description">
                        ${furniture.description}
                    </div>
                    <div class="furniture-price-inline">
                        <div id="furniture-price">${furniture.offeredSellingPrice == 0 ? "N/A" : furniture.offeredSellingPrice}</div>
                        <div class="currency">euro</div>
                        <br>
                        <div id="sellingDiv"></div>
                    </div>
                    
                </div>
        `;
    if (currentUser != null && (currentUser.role == "CLIENT" || currentUser.role == "ANTIQUAIRE")) {
        if (furniture.condition == "SOUS_OPTION") {

            if (option.idUser != currentUser.id) {
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
                page.innerHTML += `
                <div>
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
        let editIcon = document.createElement("img");
        editIcon.width = 40;
        editIcon.height = 40;
        editIcon.alt = "Edit icon";
        editIcon.src = "../assets/edit_icon.png";
        editIcon.id = "editIcon";
        document.getElementById("editImage").appendChild(editIcon);
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
                    <button id="buttonVendu" class="dropdown-item disabled" type="button">Vendu</button>
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
                    <button id="buttonVendu" class="dropdown-item disabled" type="button">Vendu</button>
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
                    <button id="buttonVendu" class="dropdown-item disabled" type="button">Vendu</button>
                </div>
            </div>
        `;
        } else if (furniture.condition == "EN_VENTE") {
            let divSelling = document.getElementById("sellingDiv");
            let clients;
            try {
                clients = await callAPI(
                    "/api/users/validatedList",
                    "GET",
                    userData.token,
                    undefined);
            } catch (err) {
                console.error("FurniturePage::get listClients", err);
                PrintError(err);
            }

            divSelling.innerHTML = `<button name="trigger_popup_fricc" class="btn btn-outline-dark" id="sell" type="button">      Vendre      </button>`
            document.getElementById("popups").innerHTML =`
            <div class="hover_bkgr_fricc" id="popupSell" >
                <span class="helper"></span>
                <div>
                    <div class="popupCloseButton" id="closeBtnPop">
                        &times;
                    </div>
                    <h2>Vendre</h2>
                    <div>
                        <span class="titre">Prix indiqué: </span> ${furniture.offeredSellingPrice}<br>
                        <div id="sellingPrice" style="display: none;" contenteditable="true"> ${furniture.offeredSellingPrice} </div>
                        <h4>Client: </h4>
                        <datalist id="clients-list">
                        </datalist>
                        <input id="input-client" list="clients-list">
                        <h4>Vente anonyme ? <input type="checkbox" id="unknownBuyerSell"> </h4><br>
                        <div class="container">
                            <div class="row">
                                <button class="btn btn-outline-success col-6 confirmSellBtn" id="confirmSellBtn" " type="submit">Confirmer</button>
                                <button class="btn btn-outline-danger col-6 cancelSellBtn" id="cancelSellBtn" type="submit">Annuler</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>`;

            let dataListClient = document.getElementById("clients-list");
            clients.map((element) => {
                dataListClient.innerHTML +=
                    `<option data-role="${element.role}" data-userId="${element.id}" value="${element.username}">${element.role}</option>`;
            })

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
                    <button id="buttonVendu" class="dropdown-item disabled" type="button">Vendu</button>
                </div>
            </div>
        `;
        } else if (furniture.condition == "SOUS_OPTION") {
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
                    <button id="buttonVendu" class="dropdown-item disabled" type="button">Vendu</button>
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
                    <button id="buttonVendu" class="dropdown-item disabled" type="button">Vendu</button>
                </div>
            </div>
        `;
        } else if (furniture.condition == "VENDU") {
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
                    <button id="buttonVendu" class="dropdown-item" type="button">Vendu</button>
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
        document.getElementById("editIcon").addEventListener("click", onEdit);
        try {
            let cancelOptionBtn = document.getElementById("cancelOptionBtn");
            cancelOptionBtn.addEventListener("click", onCancelOption);
        } catch (err) {

        }
        if (furniture.condition == "EN_VENTE") {
            let sellingBtn = document.getElementById("sell");
            let btnPopup = document.getElementById("closeBtnPop");
            let inputClient = document.getElementById("input-client");
            let cancelSellBtn = document.getElementById("cancelSellBtn");
            let confirmSellBtn = document.getElementById("confirmSellBtn");
            let sellingAnonCheck = document.getElementById("unknownBuyerSell");
            sellingBtn.addEventListener("click", onClickSell);
            inputClient.addEventListener("input", onClientSelection);
            btnPopup.addEventListener("click", onCloseSell);
            cancelSellBtn.addEventListener("click", onCloseSell);
            confirmSellBtn.addEventListener("click", onSell);
            sellingAnonCheck.addEventListener("change", onCheckBtnAnon);
        }
    }
}

const onEdit = () => {
    typeElem = document.getElementById("furniture-type");
    descElem = document.getElementById("furniture-description");
    priceElem = document.getElementById("furniture-price");
    type = typeElem.innerText; // TODO getType
    desc = descElem.innerText;
    price = priceElem.innerText;

    typeElem.contentEditable = "true";
    descElem.contentEditable = "true";
    priceElem.contentEditable = "true";
    typeElem.style.background = "lightgrey";
    descElem.style.background = "lightgrey";
    priceElem.style.background = "lightgrey";

    let buttons = document.createElement("div");
    buttons.innerHTML = `<button class="btn btn-outline-success col-6 confirmEditBtn" id="confirmEditBtn" " type="submit">Confirmer</button>
                         <button class="btn btn-outline-danger col-6 cancelEditBtn" id="cancelEditBtn" type="submit">Annuler</button>`
    buttons.className = "row";
    buttons.id = "furniture-edit-buttons"
    document.getElementById("furniture-container").appendChild(buttons);

    document.getElementById("confirmEditBtn").addEventListener("click", onConfirmEditButton);
    document.getElementById("cancelEditBtn").addEventListener("click", onCancelEditButton);
}

const onCancelEditButton = () => {
    typeElem.style.background = "none";
    descElem.style.background = "none";
    priceElem.style.background = "none";

    typeElem.contentEditable = "false";
    descElem.contentEditable = "false";
    priceElem.contentEditable = "false";

    typeElem.innerText = type;
    descElem.innerText = desc;
    priceElem.innerText = price;

    document.getElementById("furniture-container").removeChild(document.getElementById("furniture-edit-buttons"))
}

const onConfirmEditButton = () => {
    if (typeElem.innerText === type && descElem.innerText === desc && priceElem.innerText === price) {
        onCancelEditButton();
        return;
    }

    //TODO POST sur meuble pour modifications
    PrintMessage("Les modifications on été effectuées avec succès")
}

const onCheckBtnAnon = () => {
    let btnAnon = document.getElementById("input-client");

    btnAnon.disabled = !btnAnon.disabled;
}

const onClickSell = () => {
    document.getElementById("popupSell").style.display = "block";
}

const onCloseSell = () => {
    document.getElementById("popupSell").style.display = "none";
}

const onSell = async () => {
    let inputClient = document.getElementById("input-client").value;
    let data = document.querySelector("#clients-list option[value='" + inputClient + "']");

    let sale = {
        idFurniture: furniture.id,
        sellingPrice: furniture.offeredSellingPrice
    }
    // si c'est pas disabled, et qu'il y a autre chose dans le truc de client -> KO
    // sinon si c'est disabled, on gère client anon
    //sinon client

    if (document.getElementById("input-client").disabled == false && !data) {
        let err = {
            message: "L'utilisateur est invalide"
        }
        PrintError(err);
        return;
    }
    else if (document.getElementById("input-client").disabled == false) {
        if (data.dataset.role == "ANTIQUAIRE") {
            sale.sellingPrice = parseFloat(document.getElementById("sellingPrice").textContent);
        }
        sale.idBuyer = parseInt(data.dataset.userid);
    }

    console.log(sale.sellingPrice)
    if (isNaN(sale.sellingPrice)) {
        let err = {
            message: "Le prix est invalide"
        }
        PrintError(err);
        return;
    }

    try {
        const registeredSale = await callAPI(
            API_BASE_URL + "sale",
            "POST",
            userData.token,
            sale
        );
    } catch (err) {
        console.error("FurniturePage::onSell", err);
        PrintError(err);
    }
    await FurniturePage(furniture.id);
    PrintMessage("L'opération s'est déroulée avec succès");
}



const onClientSelection = () => {
    let inputClient = document.getElementById("input-client").value;
    if (!document.querySelector("#clients-list option[value='" + inputClient + "']")) return;
    let roleClient = document.querySelector("#clients-list option[value='" + inputClient + "']").dataset.role;
    let divPrice = document.getElementById("sellingPrice");

    if (roleClient == "ANTIQUAIRE") {
        divPrice.style.display = "block";
    }
    else {
        divPrice.style.display = "none";
    }
}

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
    if (price == "") {
        let error = {
            message: "Veuillez d'abord entrer un prix de vente",
        }
        console.error(error);
        PrintError(error);
        return;
    }
    try {
        await callAPI(
            API_BASE_URL + id + '/offeredForSale/',
            "POST",
            userData.token,
            {
                furniturePrice: price
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
    let id = furniture.id;
    let reason = document.getElementById("cancelOption").value;
    if (reason == "") {
        let error = {
            message: "Veuillez d'abord entrer une raison d'annulation",
        }
        console.error(error);
        PrintError(error);
        return;
    }
    try {
        await callAPI(
            API_BASE_URL + id + "/cancelOption",
            "POST",
            userData.token,
            {
                cancelReason: reason
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
                duration: optionTerm
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