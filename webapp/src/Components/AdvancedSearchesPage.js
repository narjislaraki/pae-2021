import callAPI from "../utils/api";
import { RedirectUrl } from "./Router";
import { getUserSessionData } from "../utils/session.js";
import PrintError from "./PrintError";

const API_BASE_URL = "/api/searches/";

let page = document.querySelector("#page");
let userData = getUserSessionData();
let clientMode = true;
let typeList;
let menu, advancedSearchesBar, advancedSearchesPageClient,  advancedSearchesPageFurniture;
let furnList = [];
let saleList = [];
let clientList = [];

let AdvancedSearchesPage = async () =>{
    if (!typeList) {
        typeList = await callAPI(
            "/api/furnitures/typeOfFurnitureList",
            "GET",
            undefined,
            undefined
        )  
    }

    menu = `
    <div class="menuAdmin">
        <div id="visits" class="condensed small-caps ">Visites</div>
        <div id="advancedSearches" class="condensed small-caps menuAdminOn">Recherche avancées</div>
        <div id="confirmRegister" class="condensed small-caps">Confirmation des inscriptions</div>
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
        <input type="text" class="form-control" id="searchUsername" placeholder="Nom">
        <input type="text" class="form-control" id="searchUserCity" placeholder="Ville">
        <input type="text" class="form-control" id="searchUserPostCode" placeholder="Code Postal">
    </div>
    `;

    advancedSearchesPageFurniture = `
        <p>Recherche:</p>
        <input type="text" class="form-control" id="searchClientName" placeholder="Nom du client">
        <datalist id="types-list">
        </datalist>
        <input id="input-type" list="types-list">
        <div class="searchMontant">
            <p>Montant</p>
        <input type="text" class="form-control" id="montantMin" placeholder="Min.">
        <input type="text" class="form-control" id="montantMax" placeholder="Max.">
        </div>
    </div>
    `;


    if(clientMode){
        page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageClient;
    }

    else {
        page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageFurniture;
        let dataListTypes = document.getElementById("types-list");
        typeList.map((element) => {
            dataListTypes.innerHTML +=
            `<option data-label="${element.label}" data-typeId="${element.id}" value="${element.label}">${element.label}</option>`;
        })
    }

   btns();
   addEL();

   return;
};

let btns = () => {
    document.body.addEventListener('change', function(e){
        let target = e.target;
        switch(target.value){
            case 'client':
                page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageClient;
                clientMode = true;
                break;
            case 'furn':
                page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageFurniture;
                let dataListTypes = document.getElementById("types-list");
                typeList.map((element) => {
                    dataListTypes.innerHTML +=
                    `<option data-label="${element.label}" data-typeId="${element.id}" value="${element.label}">${element.label}</option>`;
                })
                clientMode = false;
                break;
            default:
                break;
        }
        addEL();
    });
}

function addEL () {
    let search = document.getElementById("search");
    search.addEventListener("click", onSearch);

    let visits = document.getElementById("visits");
    visits.addEventListener("click", onVisits);

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

const onSearch = async (e) => {
    e.preventDefault();
    if (clientMode){
        let city = document.getElementById("searchUserCity").value;
        let name = document.getElementById("searchUsername").value;
        let postcode = document.getElementById("searchUserPostCode").value;
        AdvancedSearchesPage();
        try{
            clientList = await callAPI(
                "/api/users/validatedList",
                "GET",
                userData.token,
                undefined,
            );
            if (name){
                clientList = clientList.filter(e => e.lastName.toLowerCase().startsWith(name.toLowerCase()));
            } if (city) {
                clientList = clientList.filter(e => e.address.city.toLowerCase().startsWith(city.toLowerCase()));
            } if (postcode) {
                clientList = clientList.filter(e => e.address.postCode.toLowerCase().startsWith(postcode.toLowerCase()));
            }

            
            onShowClientList(clientList);
            
            btns();
            addEL();
        } catch(err){
            if (err == "Error: Admin only") {
                err.message = "Seuls les administrateurs peuvent accéder à cette page !";
            }
            console.error("AdvancedSearchesPage::onSearchClients", err);
            PrintError(err);
        }
    }
    else {
        let username = document.getElementById("searchClientName").value;
        let inputType = document.getElementById("input-type").value;
        let type = document.querySelector("#types-list option[value='" + inputType + "']");
        let minAmount = document.getElementById("montantMin").value;
        let maxAmount = document.getElementById("montantMax").value;
        AdvancedSearchesPage();
        try{
            furnList = await callAPI(
                "/api/furnitures",
                "GET",
                userData.token,
                undefined,
            );
            if (username){
                furnList = furnList.filter(e => e.username.toLowerCase().startsWith(username.toLowerCase()));
            } if (type != null) {
    
                furnList = furnList.filter(e => e.type != null && e.type.toLowerCase() == type.dataset.label.toLowerCase());
            } 
            if (maxAmount) {
                furnList = furnList.filter(e => e.offeredSellingPrice <= maxAmount);
            } if (minAmount) {
                furnList = furnList.filter(e => e.offeredSellingPrice >= minAmount);
            }

            
            onShowFurnitureList(furnList);
            btns();
            addEL();
        } catch(err){
            if (err == "Error: Admin only") {
                err.message = "Seuls les administrateurs peuvent accéder à cette page !";
            }
            console.error("AdvancedSearchesPage::onSearchClients", err);
            PrintError(err);
        }
    }
}

async function SearchFurn (nameClient, type, maxAmount, minAmount) {

}

const onShowFurnitureList = async (data) => {
    try{
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
    } catch(err){
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
                <div>Date de l'emport: ${furniture.pickUpDate == null ? "N/A" : furniture.pickUpDate}</div>
                <div>Date dépot: ${furniture.depositDate== null ? "N/A" : furniture.depositDate}</div>
            </div>
        </div>
        <div class="furnInfo">
            <div class="furnInfo-cat">
                <p class="small-caps">Acheté à:</p>
                <div>${furniture.seller == null? "N/A" : clientList.filter(e=>e.id == furniture.seller)[0].username}</div>
            </div>
            <div class="furnInfo-cat">
            <p class="small-caps">Vendu à:</p>
                <div>${saleList.filter(s=>s.idFurniture == furniture.id).length == 0 ? "N/A" : clientList.filter(c=>c.id == saleList.filter(s=>s.idFurniture == furniture.id)[0].idBuyer)[0].username}</div>
            </div>
            <div class="furnInfo-cat">
                <p class="small-caps">Photo préférée :</p>
                <img data-id ="${nbPhoto}" id="small-img${nbPhoto++}" src="${furniture.favouritePhoto}" alt="Petite image"  width = 60px
                height= 60px>
            </div>
        </div>
    </div>`).join("");

    page.innerHTML += furnitureList;    

    page.innerHTML+= `
    <div class="white-space"></div>
    `
    return page;
}
  
const onShowClientList = async (data) =>  {
    let clientList =
      `<div class="clientHandles">
      <div id="pseudoHandle" class="condensed">PSEUDO</div>
      <div id="namesHandle" class="condensed">NOMS</div>
      <div id="emailHandle" class="condensed">EMAIL</div>
      <div id="moreInfoHandle" class="condensed">INFORMATIONS SUPPL.</div>
  </div>
    
      `;
      console.log(data);
        try{
            furnList = await callAPI(
                "/api/furnitures",
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
        } catch(err){
            if (err == "Error: Admin only") {
                err.message = "Seuls les administrateurs peuvent accéder à cette page !";
            }
            console.error("AdvancedSearchesPage::onSearchClients", err);
            PrintError(err);
        }


        console.log(furnList);
        console.log(saleList);
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
            <div class="asci-signUpDate">Inscrit depuis: ${user.registrationDate}</div>
            <div class="asci-role">Role: ${user.role} </div>
            <div class="asci-amountBought">Nbr achats: ${saleList.filter(e=>e.idBuyer == user.id).length}</div>
            <div class="asci-amountSold">Nbr ventes: ${furnList.filter(e=>e.seller == user.id).length}</div>
        </div>
    </div>
        <div class="furnInfo">
          <div class="advancedSearchClientItem-boughtFurn">
            <p>MEUBLES ACHETÉS</p>
            <img src="" alt="" class="asci-boughtFurn">
            <img src="" alt="" class="asci-boughtFurn">
            <img src="" alt="" class="asci-boughtFurn">
          </div>
         <div class="advancedSearchClientItem-soldFurn">
            <p>MEUBLES VENDUS</p>
            <img src="" alt="" class="asci-soldFurn">
         </div>
        </div>
        </div> `)
                              
      .join("");

    page.innerHTML += clientList;    

    page.innerHTML+= `
    <div class="white-space"></div>
    `
    return page;
  }

function onSwitch(switchElement, clientDiv, furnDiv, menuDiv){
    console.log("dans onSwitch")
    if(switchElement.checked){
        page.innerHTML = menuDiv + clientDiv;
    }
    else{
        page.innerHTML = menuDiv + furnDiv;
    }
}

export default AdvancedSearchesPage;