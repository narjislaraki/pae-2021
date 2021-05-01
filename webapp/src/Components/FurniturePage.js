import {getUserSessionData, currentUser} from "../utils/session.js";
import {RedirectUrl} from "./Router.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
import PrintMessage from "./PrintMessage.js";
import waitingSpinner from "./WaitingSpinner.js";

const API_BASE_URL = "api/furnitures/";

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

let furnitureTypes;

let furniturePhotos;
let nbPhoto;

let editionMode = false;
let edition = {
    description: "",
    idType: -1,
    offeredSellingPrice: -1,
    favouritePhotoId: -1,
    photosToAdd: [],
    photosToDelete: [],
    photosToDisplay: [],
    photosToHide: [],
}

let page = document.querySelector("#page");

async function FurniturePage(id) {
    if (!id){
        RedirectUrl("/");
        let err = {
            message: "Id invalide",
        }
        PrintError(err);
        return;
    }
    if (!currentUser) {
        RedirectUrl("/");
        let err = {
            message: "Vous n'êtes pas autorisé",
        }
        PrintError(err);
        return;
    }
    waitingSpinner();
    nbPhoto = 0; // must be initialized every time!!
    userData = getUserSessionData();
    try {
        furniturePhotos = await callAPI(
            API_BASE_URL + id + "/photos",
            "GET",
            userData.token,
            undefined);
    } catch (err) {
        console.error("FurniturePage::get furniture", err);
        PrintError(err);
    }
    /****** Furniture ******/

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

    /****** nbOfDays ******/

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
        if (err !== "SyntaxError: Unexpected end of JSON input") {
            console.error("FurniturePage::onGetOption", err);
            PrintError(err);
        }
    }

    page.innerHTML = `
                        <div id="furniture-container">
                            <div id="furniture-pictures">
                                <div id="furniture-small-images">
                                </div>
                            </div>
                            <div class="condensed small-caps" id="furniture-type">
                                ${furniture.type}
                            </div>
                            <div id="editImage"></div>
                            <div class="condensed" id="furniture-description">
                                ${furniture.description}
                            </div>
                            <div class="furniture-price-inline">
                                <div id="price-div">
                                    <div id="furniture-price">${furniture.offeredSellingPrice === 0 ? "N/A" : furniture.offeredSellingPrice}</div>
                                    <div class="currency">euro</div>
                                </div>
                                <br>
                                <div id="sellingDiv"></div>  
                            </div>
                        </div>
                        `;

    let smallImages = document.getElementById("furniture-small-images");
    furniturePhotos.map((element) => {
        if (nbPhoto === 0) {
            let image = document.createElement("img");
            image.src = element.photo;
            image.alt = "Grande image";
            image.id = "big-img";
            image.class = "main-image"
            image.dataset.id = nbPhoto;
            image.dataset.photoid = element.id
            document.getElementById("furniture-pictures").appendChild(image);
        }
        if (element.isAClientPhoto && currentUser.role === "ADMIN")
            smallImages.innerHTML += `<img data-id="${nbPhoto}" data-photoid=${element.id} id="small-img${nbPhoto++}" src="${element.photo}" alt="Petite image" style="border: red; border-style: dotted">`;
        else if (element.isVisible && currentUser.role !== "ADMIN")
            smallImages.innerHTML += `<img data-id="${nbPhoto}" data-photoid=${element.id} id="small-img${nbPhoto++}" src="${element.photo}" alt="Petite image">`;
    })
    if (currentUser.role === "CLIENT" || currentUser.role === "ANTIQUAIRE") {
        if (furniture.condition === "SOUS_OPTION") {

            if (option.idUser !== currentUser.id) {
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

        } else if (furniture.condition === "EN_VENTE") {
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
    } else if (currentUser.role === "ADMIN") {
        let editIcon = document.createElement("img");
        editIcon.width = 40;
        editIcon.height = 40;
        editIcon.alt = "Edit icon";
        editIcon.src = "../assets/edit_icon.png";
        editIcon.id = "editIcon";
        document.getElementById("editImage").appendChild(editIcon);

        document.getElementById("price-div").innerHTML += `<div id="furniture-price-paid">${furniture.purchasePrice === 0 ? "N/A" : furniture.purchasePrice}</div>
                        <div class="currency-paid">euro</div>`

        if (furniture.condition === "ACHETE") {
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
        } else if (furniture.condition === "EN_RESTAURATION") {
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
        } else if (furniture.condition === "DEPOSE_EN_MAGASIN") {
            menuDeroulant = `
                                <div id="price-inline">
                                            <input type="number"  min="0" id="price" required/>
                                            Entrez un prix de vente
                                </div>
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
        } else if (furniture.condition === "EN_VENTE") {
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
            document.getElementById("popups").innerHTML = `
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
        } else if (furniture.condition === "SOUS_OPTION") {
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
        } else if (furniture.condition === "RETIRE" || furniture.condition === "REFUSE") {
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
        } else if (furniture.condition === "VENDU") {
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
        for (let i = 0; i < nbPhoto; i++) {
            document.getElementById("small-img" + i).addEventListener("click", onSmallImg)
        }
        try {
            let cancelOptionBtn = document.getElementById("cancelOptionBtn");
            cancelOptionBtn.addEventListener("click", onCancelOption);
        } catch (err) {

        }
        if (furniture.condition === "EN_VENTE") {
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


const onEdit = async () => {
    editionMode = true;
    if (furniture.condition === "DEPOSE_EN_MAGASIN")
        document.getElementById("price-inline").style.display = "none";
    if (furniture.condition === "EN_VENTE")
        document.getElementById("sellingDiv").style.display = "none";
    document.getElementById("editIcon").style.display = "none"
    document.getElementById("dropdownMenu2").style.display = "none"

    /**** Furniture ****/
    typeElem = document.getElementById("furniture-type");
    descElem = document.getElementById("furniture-description");

    if (!furnitureTypes) {
        furnitureTypes = await callAPI(
            API_BASE_URL + "typeOfFurnitureList",
            "GET",
            undefined,
            undefined
        );
    }

    type = typeElem.innerText;
    desc = descElem.innerText;

    typeElem.innerHTML = `
                        <label for="furniture-types">Type de meuble:</label>
                        <select class="form-select" id="furniture-types"></select>`
    let furnitureTypesElem = document.getElementById("furniture-types");
    furnitureTypes.map((e) => {
        if (e.label === furniture.type)
            furnitureTypesElem.innerHTML += `<option value="${e.id}" selected>${e.label}</option>`
        else
            furnitureTypesElem.innerHTML += `<option value="${e.id}">${e.label}</option>`;
    });

    descElem.contentEditable = "true";
    typeElem.style.background = "lightgrey";
    descElem.style.background = "lightgrey";


    if (furniture.condition === "EN_VENTE") {
        priceElem = document.getElementById("furniture-price");
        price = priceElem.innerText;
        priceElem.contentEditable = "true";
        priceElem.style.background = "lightgrey";
    }

    let buttons = document.createElement("div");
    buttons.innerHTML = `<button class="btn btn-outline-success col-6 confirmEditBtn" id="confirmEditBtn" " type="submit">Confirmer</button>
                         <button class="btn btn-outline-danger col-6 cancelEditBtn" id="cancelEditBtn" type="submit">Annuler</button>`
    buttons.className = "row";
    buttons.id = "furniture-edit-buttons"
    document.getElementById("furniture-container").appendChild(buttons);

    document.getElementById("confirmEditBtn").addEventListener("click", onConfirmEditButton);
    document.getElementById("cancelEditBtn").addEventListener("click", onCancelEditButton);

    /**** Photos ****/

    let div = document.createElement("div");
    let bigImg = document.getElementById("big-img");
    let edit_btns = `<div id="edit-icons">`;
    if (furniture.favouritePhotoId == bigImg.dataset.photoid)
        edit_btns += `<img src="../assets/star_full.png" alt="Favoris" id="star-image">`
    else
        edit_btns += `<img src="../assets/star_empty.png" alt="Non avoris" id="star-image">`
    edit_btns += `<br><br>`
    if (furniturePhotos[0].isVisible)
        edit_btns += `<img src="../assets/eye_open.png" alt="Visible" id="eye-image">`
    else
        edit_btns += `<img src="../assets/eye_close.png" alt="Non visible" id="eye-image">`
    edit_btns += `<br><br>`
    edit_btns += `<img src="../assets/red_cross.png" alt="Suppression" id="delete-image"></div>`
    div.innerHTML = edit_btns;
    div.id = "img-edit"
    document.getElementById("furniture-pictures").appendChild(div);

    let plus = document.createElement("img");
    plus.id = "small-img-plus";
    plus.alt = "Ajout d'image"
    plus.src = "../assets/plus.png"
    document.getElementById("furniture-small-images").append(plus);

    let imgInput = document.createElement("input");
    imgInput.type = "file";
    imgInput.multiple = true;
    imgInput.hidden = true;
    imgInput.id = "imgInput"
    document.getElementById("furniture-small-images").append(imgInput);

    document.getElementById("small-img-plus").addEventListener("click", onPlusImage);
    document.getElementById("eye-image").addEventListener("click", onEyeImg);
    document.getElementById("delete-image").addEventListener("click", onDeleteImg);
    document.getElementById("star-image").addEventListener("click", onStarImg, true);
}

const onEyeImg = () => {
    let bigImg = document.getElementById("big-img");
    let clickedId = bigImg.dataset.photoid;
    let actualFavourite = edition.favouritePhotoId == -1 ? furniture.favouritePhotoId : edition.favouritePhotoId;

    if (actualFavourite == clickedId) {
        let err = {
            message: "Impossible de rendre non visible une image favorite",
        }
        PrintError(err);
    } else {
        let isOpen = document.getElementById("eye-image").src.includes("eye_open.png");
        let indexD = edition.photosToDisplay.indexOf(clickedId);
        let indexH = edition.photosToHide.indexOf(clickedId);
        if (isOpen) {
            if (indexD !== -1)
                edition.photosToDisplay.splice(indexD, 1);
            edition.photosToHide.push(clickedId);
            document.getElementById("eye-image").src = "../assets/eye_close.png"
        } else {
            if (indexH !== -1)
                edition.photosToHide.splice(indexH, 1);
            edition.photosToDisplay.push(clickedId)
            document.getElementById("eye-image").src = "../assets/eye_open.png"
        }
    }
}

const onDeleteImg = () => {
    let bigImg = document.getElementById("big-img");
    let clickedId = bigImg.dataset.photoid;
    let actualFavourite = edition.favouritePhotoId == -1 ? furniture.favouritePhotoId : edition.favouritePhotoId;

    if (actualFavourite == clickedId) {
        let err = {
            message: "Impossible de supprimer une image favorite",
        }
        PrintError(err);
    } else {
        let index = edition.photosToDelete.indexOf(clickedId)
        if (index === -1) {
            edition.photosToDelete.push(clickedId);
            bigImg.src = "../assets/red_cross.png";
        } else {
            edition.photosToDelete.splice(index, 1)
            bigImg.src = furniturePhotos[bigImg.dataset.id].photo
        }
    }
}

const onStarImg = () => {
    let bigImg = document.getElementById("big-img");
    let clickedId = bigImg.dataset.photoid;
    let actualFavourite = edition.favouritePhotoId == -1 ? furniture.favouritePhotoId : edition.favouritePhotoId;

    if (actualFavourite == clickedId) {
        let err = {
            message: "Action impossible, veuillez choisir une nouvelle image favorite au préalable",
        }
        PrintError(err);
    } else if (document.getElementById("eye-image").src.includes("eye_close.png")) {
        let err = {
            message: "Action impossible, l'image n'est pas visible",
        }
        PrintError(err);
    } else {
        let star = document.getElementById("star-image");
        star.src = "../assets/star_full.png";
        if (clickedId == furniture.favouritePhotoId)
            edition.favouritePhotoId = -1;
        else
            edition.favouritePhotoId = clickedId;
    }
}

const onPlusImage = () => {
    document.getElementById("imgInput").click();
}


const onCancelEditButton = () => {
    typeElem.style.background = "none";
    descElem.style.background = "none";

    descElem.contentEditable = "false";

    typeElem.innerText = type;
    descElem.innerText = desc;

    if (furniture.condition === "EN_VENTE") {
        priceElem.style.background = "none";
        priceElem.contentEditable = "false";
        priceElem.innerText = price;
    }
    removeEditElements();
    stopEdition();
}


const onConfirmEditButton = async () => {
    /***** Data management *****/
    let index = document.getElementById("furniture-types").value - 1;
    typeElem.innerText = furnitureTypes[index].label;

    let newType = furnitureTypes[index].id;

    edition.idType = newType === furniture.type ? -1 : newType;
    edition.description = descElem.innerText === furniture.description ? "" : descElem.innerText;
    if (furniture.condition === "EN_VENTE" && priceElem.innerText !== price) {
        edition.offeredSellingPrice = priceElem.innerText;
    }
    let files = document.getElementById("imgInput").files;
    if (files.length > 0) {
        edition.photosToAdd = await encodeFiles(files);
    }

    /***** Display management *****/
    document.getElementById("editIcon").style.display = "inline"
    typeElem.style.background = "none";
    descElem.style.background = "none";
    descElem.contentEditable = "false";

    if (furniture.condition === "EN_VENTE") {
        priceElem.style.background = "none";
        priceElem.contentEditable = "false";
    }

    removeEditElements();
    try {
        nbOfDay = await callAPI(
            API_BASE_URL + furniture.id + "/edit",
            "POST",
            userData.token,
            edition,
        );
    } catch (err) {
        console.error("FurniturePage::onNbOfDay", err);
        PrintError(err);
    }
    stopEdition();
    FurniturePage(furniture.id);
}

function stopEdition() {
    editionMode = false;
    edition = {
        description: "",
        idType: -1,
        offeredSellingPrice: -1,
        favouritePhotoId: -1,
        photosToAdd: [],
        photosToDelete: [],
        photosToDisplay: [],
        photosToHide: [],
    }
    if (furniture.condition === "DEPOSE_EN_MAGASIN")
        document.getElementById("price-inline").style.display = "block";
    if (furniture.condition === "EN_VENTE")
        document.getElementById("sellingDiv").style.display = "block";
    document.getElementById("editIcon").style.display = "inline"
    document.getElementById("dropdownMenu2").style.display = "block"
}

function removeEditElements() {
    document.getElementById("furniture-container").removeChild(document.getElementById("furniture-edit-buttons"));
    document.getElementById("furniture-pictures").removeChild(document.getElementById("img-edit"));
    document.getElementById("furniture-small-images").removeChild(document.getElementById("small-img-plus"));
    document.getElementById("furniture-small-images").removeChild(document.getElementById("imgInput"));
}


const onSmallImg = (e) => {
    let bigImg = document.getElementById("big-img");
    let index = e.target.dataset.id
    let actualFavourite = edition.favouritePhotoId == -1 ? furniture.favouritePhotoId : edition.favouritePhotoId;
    let visible = (furniturePhotos[index].isVisible == true && !edition.photosToHide.find(e => e == furniturePhotos[index].id)) || edition.photosToDisplay.find(e => e == furniturePhotos[index].id);
    bigImg.src = edition.photosToDelete.indexOf(furniturePhotos[index].id.toString()) === -1 ? furniturePhotos[index].photo : "../assets/red_cross.png";
    bigImg.dataset.photoid = furniturePhotos[index].id;
    bigImg.dataset.id = index;

    if (editionMode) {
        let star = document.getElementById("star-image")
        let eye = document.getElementById("eye-image")
        if (actualFavourite == furniturePhotos[index].id)
            star.src = "../assets/star_full.png"
        else
            star.src = "../assets/star_empty.png"
        if (visible)
            eye.src = "../assets/eye_open.png"
        else
            eye.src = "../assets/eye_close.png"
        star.removeEventListener("click", onStarImg, true);
        star.addEventListener("click", onStarImg, true);
    }
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

    if (document.getElementById("input-client").disabled === false && !data) {
        let err = {
            message: "L'utilisateur est invalide"
        }
        PrintError(err);
        return;
    } else if (document.getElementById("input-client").disabled === false) {
        if (data.dataset.role === "ANTIQUAIRE") {
            sale.sellingPrice = parseFloat(document.getElementById("sellingPrice").textContent);
        }
        sale.idBuyer = parseInt(data.dataset.userid);
    }

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
    onCloseSell();
    await FurniturePage(furniture.id);
    PrintMessage("L'opération s'est déroulée avec succès");
}


const onClientSelection = () => {
    let inputClient = document.getElementById("input-client").value;
    if (!document.querySelector("#clients-list option[value='" + inputClient + "']")) return;
    let roleClient = document.querySelector("#clients-list option[value='" + inputClient + "']").dataset.role;
    let divPrice = document.getElementById("sellingPrice");

    if (roleClient === "ANTIQUAIRE") {
        divPrice.style.display = "block";
    } else {
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
    await FurniturePage(id);
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
    await FurniturePage(id);
};

const onOfferedForSale = async () => {
    let id = furniture.id;
    let price = document.getElementById("price").value;
    if (price === "") {
        let error = {
            message: "Veuillez d'abord entrer un prix de vente",
        }
        console.error(error);
        PrintError(error);
        return;
    }
    if (!furniture.favouritePhotoId) {
        let error = {
            message: "Veuillez d'abord choisir une photo favorite",
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
    await FurniturePage(id);
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
    await FurniturePage(id);
};

const onCancelOption = async () => {
    let id = furniture.id;
    let reason = document.getElementById("cancelOption").value;
    if (reason === "") {
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
    await FurniturePage(id);
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
    await FurniturePage(id_furniture);
};

let valueCount;

const incrementCounter = () => {
    valueCount = document.getElementById("optionTerm").value;
    valueCount++;
    document.getElementById("optionTerm").value = valueCount;
    if (valueCount > 1) {
        document.querySelector(".minus-btn").removeAttribute("disabled");
        document.querySelector(".minus-btn").classList.remove("disabled");
    }
    if (valueCount === 5) {
        document.querySelector(".plus-btn").setAttribute("disabled", "disabled");
    }
}

const decrementCounter = () => {
    valueCount = document.getElementById("optionTerm").value;
    valueCount--;
    document.getElementById("optionTerm").value = valueCount;
    if (valueCount === 1) {
        document.querySelector(".minus-btn").setAttribute("disabled", "disabled");
    }
    if (valueCount < 5) {
        document.querySelector(".plus-btn").removeAttribute("disabled");
        document.querySelector(".plus-btn").classList.remove("disabled");
    }
}

function encodeFile(file) {
    return new Promise((resolve, reject) => {
        var fileReader = new FileReader();
        fileReader.onload = function (fileLoadedEvent) {
            let base64 = fileLoadedEvent.target.result;
            resolve(base64);
        }
        fileReader.readAsDataURL(file)
    });
}

async function encodeFiles(files) {
    const returnedFiles = [];
    if (files.length > 0) {
        for (let i = 0; i < files.length; i++) {
            let photo = await encodeFile(files[i])
            returnedFiles[i] = {
                photo: photo,
            }
        }
    }
    return returnedFiles;
}

export {FurniturePage};