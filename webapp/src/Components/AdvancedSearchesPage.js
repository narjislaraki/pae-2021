import callAPI from "../utils/api";
import { RedirectUrl } from "./Router";
import { getUserSessionData } from "../utils/session.js";
import { FurniturePage } from "./FurniturePage.js";
import PrintError from "./PrintError";
import { convertDateTimeToStringDate, convertDateTimeToStringTime } from "../utils/tools.js";
import waitingSpinner from "./WaitingSpinner.js";

const API_BASE_URL = "/api/searches/";

let page = document.querySelector("#page");
let userData;
let clientMode = true;
let goSpin = true;
let typeList = [];
let menu, advancedSearchesBar, advancedSearchesPageClient, advancedSearchesPageFurniture;
let furnList = [];
let saleList = [];
let clientList = [];

let AdvancedSearchesPage = async () => {
    if(goSpin)
        waitingSpinner();
    userData = getUserSessionData();
    typeList = await callAPI(
        "/api/furnitures/typeOfFurnitureList",
        "GET",
        undefined,
        undefined
    )

    clientList = await callAPI(
        "/api/users/validatedList",
        "GET",
        userData.token,
        undefined,
    );

    menu = `
    <div class="menuAdmin">
    <div class="condensed small-caps" id="visits">Visites en attente</div>
    <div class="condensed small-caps" id="visitsToBeProcessed">Visites à traiter</div>
    <div class="condensed small-caps menuAdminOn" id="advancedSearches">Recherches avancées</div>
    <div class="condensed small-caps" id="confirmRegister">Confirmation des inscriptions</div>
    </div>
    `;

    advancedSearchesBar = `
<div class="searchBar-advanced">
        
            <label class="switch-client-furn switch-client">
                <input type="radio" name="client-furn" value="client">
                <span class="radio-client-furn">
                    <i class="bi bi-people" id="icon-client" ></i>
                </span>
            </label>
            <label class="switch-client-furn switch-furn">
                <input type="radio" name="client-furn" value="furn">
                <span class="radio-client-furn">
                    <i class="bi bi-tag" id="icon-furn" ></i>
                </span>
            </label>
        
        <button class="btn btn-dark condensed small-caps" type="submit" id="search">Rechercher</button>
`

    advancedSearchesPageClient = `
        <p>Recherche:</p>
        <datalist id="names-list">
        </datalist>
        <input id="input-name" class="form-control" list="names-list" placeholder ="Nom">
        <datalist id="cities-list">
        </datalist>
        <input id="input-city" class="form-control" list="cities-list" placeholder ="Ville">
        <datalist id="postCode-list">
        </datalist>
        <input id="input-postCode" class="form-control" list="postCode-list" placeholder ="Code Postal">
    </div>
    <div id="searchspinner"></div>
    `;

    advancedSearchesPageFurniture = `
        <p>Recherche:</p>
        <datalist id="names-list">
        </datalist>
        <input id="input-name" class="form-control" list="names-list" placeholder ="Prénom">
        <datalist id="types-list">
        </datalist>
        <input id="input-type" class="form-control" list="types-list" placeholder="Type">
        <div class="searchMontant">
            <p>Montant de vente</p>
        <input type="text" class="form-control" id="montantMin" placeholder="Min.">
        <input type="text" class="form-control" id="montantMax" placeholder="Max.">
        </div>
       
    </div>
    <div id="searchspinner"></div>
    `;


    if (clientMode) {
        page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageClient;
        initializeDSClient();
    }

    else {
        page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageFurniture;
        initializeDSFur();
    }

    btns();
    addEL();

    return;
};

function initializeDSClient() {

    let dataListNames = document.getElementById("names-list");
    clientList.map((client) => {
        dataListNames.innerHTML +=
            `<option data-name="${client.lastName}" data-userId="${client.id}" value="${client.lastName}"></option>`;
    })

    let dataListCities = document.getElementById("cities-list");
    clientList.map((client) => {
        dataListCities.innerHTML +=
            `<option data-city="${client.address.city}" data-userId="${client.id}" value="${client.address.city}"></option>`;
    })

    let dataListPostCode = document.getElementById("postCode-list");
    clientList.map((client) => {
        dataListPostCode.innerHTML +=
            `<option data-postCode="${client.address.postCode}" data-userId="${client.id}" value="${client.address.postCode}"></option>`;
    })

}

function initializeDSFur() {
    let dataListNames = document.getElementById("names-list");
    clientList.map((client) => {
        dataListNames.innerHTML +=
            `<option data-name="${client.firstName}" data-userId="${client.id}" value="${client.firstName}"></option>`;
    })

    let dataListTypes = document.getElementById("types-list");
    typeList.map((element) => {
        dataListTypes.innerHTML +=
            `<option data-label="${element.label}" data-typeId="${element.id}" value="${element.label}">${element.label}</option>`;
    })
}
let btns = () => {
    document.body.addEventListener('change', function (e) {
        let target = e.target;
        switch (target.value) {
            case 'client':
                page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageClient;
                clientMode = true;
                initializeDSClient();
                break;
            case 'furn':
                page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageFurniture;
                clientMode = false;
                initializeDSFur();
                break;
            default:
                break;
        }
        addEL();
    });
}

function addEL() {
    let search = document.getElementById("search");
    search.addEventListener("click", onSearch);

    let visits = document.getElementById("visits");
    visits.addEventListener("click", onVisits);

    let visitsATraiter = document.getElementById("visitsToBeProcessed");
    visitsATraiter.addEventListener("click", onVisitsToBeProcessed);

    let advancedSearches = document.getElementById("advancedSearches");
    advancedSearches.addEventListener("click", onAdvancedSearches);

    let confirmRegister = document.getElementById("confirmRegister");
    confirmRegister.addEventListener("click", onConfirmRegister);
}

const onVisits = (e) => {
    e.preventDefault();
    RedirectUrl("/visits");
};

const onAdvancedSearches = (e) => {
    e.preventDefault();
    AdvancedSearchesPage();
};

const onConfirmRegister = (e) => {
    e.preventDefault();
    RedirectUrl("/confirmRegistration");
};

const onVisitsToBeProcessed = (e) => {
    e.preventDefault();
    RedirectUrl("/visitsToBeProcessed");
};

const onSearch = async (e) => {
    e.preventDefault();
    goSpin = false;
    if (clientMode) {
        let inputCity = document.getElementById("input-city").value;
        let city = document.querySelector("#cities-list option[value='" + inputCity + "']");
        let inputName = document.getElementById("input-name").value;
        let name = document.querySelector("#names-list option[value='" + inputName + "']");
        let inputPostCode = document.getElementById("input-postCode").value;
        let postcode = document.querySelector("#postCode-list option[value='" + inputPostCode + "']");
        await AdvancedSearchesPage();
        waitingSpinner(document.getElementById("searchspinner"))
        try {
            clientList = await callAPI(
                "/api/users/validatedList",
                "GET",
                userData.token,
                undefined,
            );

            if (name) {
                clientList = clientList.filter(e => e.lastName.toLowerCase() == name.dataset.name.toLowerCase());
            } else if (inputName) {
                clientList = clientList.filter(e => e.lastName.toLowerCase().startsWith(inputName.toLowerCase()));

            } if (city) {
                clientList = clientList.filter(e => e.address.city.toLowerCase() == city.dataset.city.toLowerCase());
            } else if (inputCity) {
                clientList = clientList.filter(e => e.address.city.toLowerCase().startsWith(inputCity.toLowerCase()));

            } if (postcode) {
                clientList = clientList.filter(e => e.address.postCode.toLowerCase() == postcode.dataset.postcode.toLowerCase());
            } else if (inputPostCode) {
                clientList = clientList.filter(e => e.address.postCode.toLowerCase().startsWith(inputPostCode.toLowerCase()));
            }


            await onShowClientList(clientList);
            btns();
            addEL();
            let list = document.getElementsByClassName("furnituresClient");
            Array.from(list).map((e, i, a) => {
                e.addEventListener("click", onFurniture);
            });

        } catch (err) {
            if (err == "Error: Admin only") {
                err.message = "Seuls les administrateurs peuvent accéder à cette page !";
            }
            console.error("AdvancedSearchesPage::onSearchClients", err);
            PrintError(err);
        }
    }
    else {
        let inputName = document.getElementById("input-name").value;
        let name = document.querySelector("#names-list option[value='" + inputName + "']");
        let inputType = document.getElementById("input-type").value;
        let type = document.querySelector("#types-list option[value='" + inputType + "']");
        let minAmount = document.getElementById("montantMin").value;
        let maxAmount = document.getElementById("montantMax").value;
        await AdvancedSearchesPage();
        waitingSpinner(document.getElementById("searchspinner"))
        try {
            furnList = await callAPI(
                "/api/furnitures/research",
                "GET",
                userData.token,
                undefined,
            );

            saleList = await callAPI(
                "/api/sales",
                "GET",
                userData.token,
                undefined
            )

            if (name) {
                let clients = clientList.filter(c => c.firstName.toLowerCase() == name.dataset.name.toLowerCase());
                let clientIds = clients.map(c => c.id);
                let salesWithClient = saleList.filter(s => clientIds.includes(s.idBuyer));
                let idFurOfSales = salesWithClient.map(s => s.idFurniture);
                furnList = furnList.filter(f => clientIds.includes(f.sellerId) || idFurOfSales.includes(f.id));

            } else if (inputName) {
                let clients = clientList.filter(c => c.firstName.toLowerCase().startsWith(inputName.toLowerCase()));
                let clientIds = clients.map(c => c.id);
                let salesWithClient = saleList.filter(s => clientIds.includes(s.idBuyer));
                let idFurOfSales = salesWithClient.map(s => s.idFurniture);
                furnList = furnList.filter(f => clientIds.includes(f.sellerId) || idFurOfSales.includes(f.id));
            }
            if (type) {
                furnList = furnList.filter(f => f.type != null && f.type.toLowerCase() == type.dataset.label.toLowerCase());
            }
            else if (inputType) {
                furnList = furnList.filter(f => f.type != null && f.type.toLowerCase().startsWith(inputType.toLowerCase()));
            }
            if (maxAmount) {
                furnList = furnList.filter(e => e.offeredSellingPrice <= maxAmount);
            } if (minAmount) {
                furnList = furnList.filter(e => e.offeredSellingPrice >= minAmount);
            }


            await onShowFurnitureList(furnList);
            btns();
            addEL();
        } catch (err) {
            if (err == "Error: Admin only") {
                err.message = "Seuls les administrateurs peuvent accéder à cette page !";
            }
            console.error("AdvancedSearchesPage::onSearchClients", err);
            PrintError(err);
        }
    }
}

const onShowFurnitureList = async (data) => {
    try {
        clientList = await callAPI(
            "/api/users/validatedList",
            "GET",
            userData.token,
            undefined,
        );

        saleList = await callAPI(
            "/api/sales",
            "GET",
            userData.token,
            undefined
        )
    } catch (err) {
        if (err == "Error: Admin only") {
            err.message = "Seuls les administrateurs peuvent accéder à cette page !";
        }
        console.error("AdvancedSearchesPage::onSearchClients", err);
        PrintError(err);
    }
    let furnitureList = `
    <div class="clientHandles">
        <div id="pseudoHandle" class="condensed">TYPE</div>
        <div id="namesHandle" class="condensed">DESCRIPTION</div>
        <div id="moreInfoHandle" class="condensed">INFORMATIONS SUPPL.</div>
    </div>
    `;

    let nbPhoto = 1;
    furnitureList += data.map((furniture) =>
        `<div class="advancedSearchClientItem-container">
        <div class="advancedSearchClientItem condensed">
            <div class="advancedSearchClientItem_pseudo">${furniture.type == null ? "N/A" : furniture.type}</div>
            <div class="advancedSearchFurntItem_description">${furniture.description}</div>
            <div class="advancedSearchFurnItem_moreInfo1">
                <div>Statut: ${furniture.condition}</div>
                <div>Prix d'achat: ${furniture.purchasePrice == null ? "N/A" : furniture.purchasePrice}</div>
                <div>Prix de vente: ${furniture.offeredSellingPrice == null ? "N/A" : furniture.offeredSellingPrice}</div>
            </div>
            <div class="advancedSearchFurnItem_moreInfo2">
                <div>Date de l'emport: ${furniture.pickUpDate == null ? "N/A" : convertDateTimeToStringDate(furniture.pickUpDate) + " à " + convertDateTimeToStringTime(furniture.pickUpDate)}</div>
                <div>Date dépot: ${furniture.depositDate == null ? "N/A" : furniture.depositDate}</div>
            </div>
        </div>
        <div class="furnInfo">
            <div class="furnInfo-cat">
                <p class="small-caps">Acheté à:</p>
                <div>${furniture.seller == null ? "N/A" : furniture.seller.username}</div>
            </div>
            <div class="furnInfo-cat">
            <p class="small-caps">Vendu à:</p>
                <div>${saleList.filter(s => s.idFurniture == furniture.id).length == 0 ? "N/A" : (clientList.filter(c=>c.id == saleList.filter(s => s.idFurniture == furniture.id)[0].idBuyer).length == 0 ? "Vente anonyme" : clientList.filter(c=>c.id == saleList.filter(s => s.idFurniture == furniture.id)[0].idBuyer)[0].username)}</div>
            </div>
            <div class="furnInfo-cat">
                <p  class="small-caps">Photos du meuble:</p>
                <div id = "photosMeuble${furniture.id}"></div>
            </div>
            <div class="furnInfo-cat">
                <p  class="small-caps">Photo préférée:</p>
                <img data-id ="${nbPhoto}" id="small-img${nbPhoto++}" src="${furniture.favouritePhoto}" alt="Petite image"  width = 60px
                height= 60px>
            </div>
        </div>
    </div>`).join("");
    document.getElementById("searchspinner").innerHTML = ``;
    page.innerHTML += furnitureList;

    let furniturePhotos;
    for (let i = 0; i < data.length; i++) {
        let photosMeubles = document.getElementById("photosMeuble" + data[i].id);
        let photosAAjouter = "<br>";
        furniturePhotos = await callAPI(
            "/api/furnitures/" + data[i].id + "/photos",
            "GET",
            userData.token,
            undefined
        );

        furniturePhotos.map((p) => {
            photosAAjouter += `<img data-id ="${nbPhoto}" id="small-img${nbPhoto++}" src="${p.photo}" alt="Petite image"  width = 60px
            height= 60px>`
        })
        photosMeubles.innerHTML = photosAAjouter;
    }
    page.innerHTML += `
    <div class="white-space"></div>
    `
    return page;
}

const onShowClientList = async (data) => {
   

    let clientList =
        `<div class="clientHandles">
      <div id="pseudoHandle" class="condensed">PSEUDO</div>
      <div id="namesHandle" class="condensed">NOMS</div>
      <div id="emailHandle" class="condensed">EMAIL</div>
      <div id="moreInfoHandle" class="condensed">INFORMATIONS SUPPL.</div>
  </div>
    
      `;
    try {
        furnList = await callAPI(
            "/api/furnitures/research",
            "GET",
            userData.token,
            undefined,
        );

        saleList = await callAPI(
            "/api/sales",
            "GET",
            userData.token,
            undefined
        )
    } catch (err) {
        if (err == "Error: Admin only") {
            err.message = "Seuls les administrateurs peuvent accéder à cette page !";
        }
        console.error("AdvancedSearchesPage::onSearchClients", err);
        PrintError(err);
    }

    clientList += data
        .map((user) =>
            `<div class="advancedSearchClientItem-container">
            <div class="advancedSearchClientItem condensed">
                <div class="advancedSearchClientItem_pseudo">${user.username}</div>
                <div class="advancedSearchClientItem_fullName">
                    <div class="asci-name">${user.firstName}</div>
                    <div class="asci-surname">${user.lastName}</div>
                </div>
                <div class="advancedSearchClientItem_email">${user.email}</div>
                <div class="advancedSearchClientItem_moreInfo">
                    <div class="asci-signUpDate">Inscrit depuis: ${convertDateTimeToStringDate(user.registrationDate)}</div>
                    <div class="asci-role">Role: ${user.role} </div>
                    <div class="asci-amountBought">Nbr achats: ${saleList.filter(e => e.idBuyer == user.id).length}</div>
                    <div class="asci-amountSold">Nbr ventes: ${furnList.filter(e => e.sellerId == user.id).length}</div>
                </div>
            </div>
            <div class="furnInfo">
            <div class="advancedSearchClientItem-soldFurn">
                <p class="condensed">MEUBLES VENDUS : </p>
                <div id= "meublesVendus${user.id}" ></div>
            </div>
            <div class="advancedSearchClientItem-boughtFurn">
                <p class="condensed">MEUBLES ACHETÉS :</p>
                <div id= "meublesAchetes${user.id}" ></div>
            </div>
        </div>
        </div> `)

        .join("");
    document.getElementById("searchspinner").innerHTML = ``;
    page.innerHTML += clientList;
    for (let i = 0; i < data.length; i++) {
        let meublesVendusHTML = document.getElementById("meublesVendus" + data[i].id);
        let meublesAchetesHTML = document.getElementById("meublesAchetes" + data[i].id);
        let mAchetesList = saleList.filter(s => s.idBuyer == data[i].id);
        let mVendusList = furnList.filter(f => f.sellerId == data[i].id);
        let meublesVAjouter = "";
        let meublesAAjouter = "";

        
        mAchetesList.map((m) => {
            let meuble = furnList.find(e => e.id == m.id);
            meublesAAjouter += `<br><div data-id="${meuble.id}" class="furnituresClient">${meuble.description}</div>`
        })

        mVendusList.map((m) => {
            meublesVAjouter += `<br><div data-id="${m.id}" class="furnituresClient">${m.description}</div>`
        })
        

        meublesVendusHTML.innerHTML = meublesVAjouter;
        meublesAchetesHTML.innerHTML = meublesAAjouter;


    }
    page.innerHTML += `
    <div class="white-space"></div>
    `;

    return page;
}

const onFurniture = (e) => {
    let id = e.target.dataset.id;
    FurniturePage(id);
};


export default AdvancedSearchesPage;